# 2. Recipe rewrite

Date: 2024-11-03
Last update: 2024-11-03

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
- Performance: Should not blow up any servers

The new system should also be completely backwards compatible with the old.

## API Changes

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

#### Examples

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

## JSON Serialization

All recipes should be able to be serialized to and deserialized
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

The recipe deserializer also needs a `__filename` field, which is inserted when the file is read, so it doesn't (and shouldn't) be in the schema

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

*In addition to those schemata, items can be in short form:

- Single items: `<namespace>:<id>|<amount>`
- Tags: `#<namespace>:<id>|<amount>`

## Extensibility

The 5 main recipe classes are all polymorphic, and subclasses can be used in their
stead, and should not affect the recipe system (as long as the right methods are
override, see javadocs)

### Custom serialization/deserialization

The default deserializers recognize subclasses with custom deserializers by
the presence of a `class` field in the json, which is the key of a
custom deserializer registered with Slimefun's `RecipeService`.
For custom serializers, override the `serialize` method on the subclass,
and ensure they also add the `class` field

## Phases

Each phase should be a separate PR

- Phase 1 - Add the new API
- Phase 2 - Migrate Slimefun toward the new API

The entire process should be seamless for the end users, and
backwards compatible with addons that haven't yet migrated
