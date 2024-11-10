# 2. Recipe rewrite

Date: 2024-11-03
Last update: 2024-11-08

**DO NOT rely on any APIs introduced until we finish the work completely!**

## Status

Phase 1: Work in progress

## Context

Slimefun currently lacks a robust recipe system. Multiblock crafting
does one thing, while electric machines do another, even though some
of them craft the same items.

Slimefun also lacks certain features that vanilla minecraft has, like true
shaped and shapeless recipes, tagged inputs, and the ability to edit recipes
without any code.

## Goals

The goal of this rewrite is to introduce an improved recipe system to
Slimefun, focusing on

- Ease of use: The API should be clean and the system intuitive for
  developers to use
- Extensibility: Addons should be able to create and use their own types
  of recipes with this system.
- Customizability: Server owners should be able to customize any and all
  Slimefun recipes
- Performance: Should be on par or better than the current system.

The new recipe system should also be completely backwards compatible.

## API Additions

### 5 main recipe classes

All recipes are now `Recipe` objects. It is an association between
inputs (see `RecipeInput`) and outputs (see `RecipeOutput`), along with other metadata
for how the recipe should be crafted -- recipe type, energy cost, base crafting duration, etc.

`RecipeInput`s are a list of `RecipeInputItem`s plus a `MatchProcedure` -- how the inputs of
the recipe should be matched to items in a multiblock/machine when crafting. The base ones are:

- Shaped/Shapeless: Exactly the same as vanilla
- Subset: How the current smeltery, etc. craft
- Shaped-flippable: The recipe can be flipped on the Y-axis
- Shaped-rotatable: The recipe can be rotated (currently only 45deg, 3x3)

`RecipeInputItem`s describe a single slot of a recipe and determines what
items match it. There can be a single item that matches (see `RecipeInputSlimefunItem`,
`RecipeInputItemStack`), or a list (tag) of items all of which can be used
in that slot (see `RecipeInputGroup`, `RecipeInputTag`).

`RecipeOutput`s are just a list of `RecipeOutputItem`s, all of which are crafted by the recipe.

An `RecipeOutputItem`s controls how an output is generated when the recipe is
crafted. It can be a single item (see `RecipeOutputItemStack`, `RecipeOutputSlimefunItem`),
or a group of items each with a certain weight of being output (see `RecipeOutputGroup`).

#### Examples (pseudocode)

Here are the inputs and outputs of the recipe for a vanilla torch

```txt
RecipeInput (
    {
        EMPTY, EMPTY, EMPTY
        EMPTY, RecipeInputGroup(COAL, CHARCOAL), EMPTY,
        EMPTY, RecipeInputItemStack(STICK), EMPTY
    },
    SHAPED
)
RecipeOutput (
    RecipeOutputItemStack(4 x TORCH)
)
```

Here are the inputs and outputs of a gold pan

```txt
RecipeInput (
    { RecipeOutputItemStack(GRAVEL) },
    SUBSET
)
RecipeOutput (
    RecipeOutputGroup(
        40 RecipeOutputItemStack(FLINT)
        5  RecipeOutputItemStack(IRON_NUGGET)
        20 RecipeOutputItemStack(CLAY_BALL)
        35 RecipeOutputSlimefunItem(SIFTED_ORE)
    )
)
```

This would remove the need to use ItemSettings to determine the gold pan weights

### RecipeService

This is the public interface for the recipe system, there are methods here to add,
load, save, and search recipes. It also stores a map of `MatchProcedures` and
`RecipeType` by key for conversions from a string

### JSON Serialization

All recipes are able to be serialized to and deserialized
from JSON. The schemas are shown below.

Here, `key` is the string representation of a namespaced key

`Recipe`

```txt
{
    "input"?: RecipeInput
    "output"?: RecipeOutput
    "type": key | key[]
    "energy"?: int
    "crafting-time"?: int
    "permission-node"?: string | string[]
}
```

The recipe deserializer technically needs a `__filename` field, but it is
inserted when the file is read, so it isn't (and shouldn't) be in the schema

`RecipeInput`

```txt
{
    "items": string | string[]
    "key": {
        [key: string]: RecipeInputItem
    }
    "match"?: key
}
```

`RecipeOutput`

```txt
{
    "items": RecipeOutputItem[]
}
```

`RecipeInputItem`*

```txt
{
    "id": key
    "amount"?: int
    "durability"?: int
} | {
    "tag": key
    "amount"?: int
    "durability"?: int
} | {
    "group": RecipeInputItem[]
}
```

`RecipeOutputItem`*

```txt
{
    "id": key
    "amount"?: int
} | {
    "group": RecipeInputItem[]
    "weights"?: int[]
}
```

*In addition to those schemas, items can be in short form:

- Single items: `<namespace>:<id>|<amount>`
- Tags: `#<namespace>:<id>|<amount>`

## Extensibility

The 5 main recipe classes are all polymorphic, and subclasses can be used in their
stead, and should not affect the recipe system (as long as the right methods are
overriden, see javadocs)

### Custom serialization/deserialization

The default deserializers recognize subclasses with custom deserializers by
the presence of a `class` field in the json, which should be the key of a
custom deserializer registered with Slimefun's `RecipeService`.
For custom serializers, override the `serialize` method on the subclass,
and ensure they also add the `class` field

## Recipe Lifecycle

### Stage 1a

When Slimefun is enabled, all recipes in the resources folder will be
moved to `plugins/Slimefun/recipes/` (unless a file with its name already exists).

Addons should do the same. (We recommend saving to
`plugins/Slimefun/recipes/<your-addon-name>/` but it's not required).

### Stage 1b

Also on enable, recipes defined in code should be registered. These two steps
can be done in no particular order.

### Stage 2

On the first server tick, all recipes in the `plugins/Slimefun/recipes` folder
are read and added to the `RecipeService`, removing all recipes with the
same filename. This is why recipes should ideally be *defined* in JSON,
to prevent unnecessary work.

When loading JSON recipes, we also need to be able to tell the difference between
a server owner changing a recipe, and a developer changing a recipe. To do this,
we use a system called Recipe Overrides; it allows for updates to recipes from
developers while also preserving custom recipes by server owners

- Slimefun/addons should tell the recipe service it will apply a recipe
  override on enable, **before** any JSON recipes are copied from the resources
  folder
- The recipe service checks all recipe overrides that have already run
  (in the file `plugins/Slimefun/recipe-overrides`) and if it never received
  that override before, it deletes the old files and all recipes inside them.
  Then all recipes are loaded as before.

### Stage 3

While the server is running, recipes can be modified in code, saved to disk, or
re-loaded from disk. New recipes can also be added, however not to any existing
file (unless forced, which is not recommended)

### Stage 4

On server shutdown (or `/sf recipe save`), **all** recipes are saved to disk.
This means any changes made while the server is running will be overwritten.
Server owners should run `/sf recipe reload <file-name?>` to load new recipes
dynamically from disk.

## Phases

Each phase should be a separate PR

- Phase 1 - Add the new API
- Phase 2 - Migrate Slimefun items/multiblocks/machines toward the new API
- Phase 3 - Update the Slimefun Guide to use the new API

The entire process should be seamless for the end users, and
backwards compatible with addons that haven't yet migrated
