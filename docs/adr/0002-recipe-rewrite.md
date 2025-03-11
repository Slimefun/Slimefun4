# 2. Recipe rewrite

Date: 2024-11-03
Last update: 2025-03-11

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

`RecipeInput`s are a list of `RecipeInputItem`s plus a `MatchProcedure` -- how the inputs of the recipe should be matched to items in a multiblock/machine when
crafting. The ones provided by Slimefun are:

- Fixed: Each item must be in the exact same spot as the recipe.
- Shaped: The positions of each item relative to each other must remain the same,
  however the recipe as a whole can be shifted around the crafting grid
  - Example: Placing two planks on any two vertically adjacent slots crafts sticks
- Shapeless: The recipe can be crafted as long as all inputs -- and **only** those
  inputs -- are present.
  - Example: Flint and steel can be crafted with the flint and iron ingot anywhere
    on the crafting grid.
- Subset: The recipe can be crafted as long as all inputs are present
  - Example: Crafting anything in the Slimefun smeltery
- Shaped-flippable: Same as shaped, but the recipe can be flipped on the Y-axis
  - Example: In vanilla minecraft, axes and hoes can be crafted facing the left
    or right.
- Shaped-rotatable: Same as shaped, but the recipe can be rotated 45 degrees any
  number of times
  - Example (pseudocode): The inputs for an Ancient Rune \[Water\] in the ancient altar
  
    ```txt
    RecipeInput (
        {
            SALMON,       MAGIC_LUMP_2, WATER_BUCKET
            SAND,         BLANK_RUNE,   SAND,
            WATER_BUCKET, MAGIC_LUMP_2, COD
        },
        SHAPED_ROTATABLE_45_3X3
    )
    ```

    would match

    ```txt
    SALMON,       MAGIC_LUMP_2, WATER_BUCKET
    SAND,         BLANK_RUNE,   SAND,
    WATER_BUCKET, MAGIC_LUMP_2, COD
    ```

    ```txt
    SAND,         SALMON,     MAGIC_LUMP_2
    WATER_BUCKET, BLANK_RUNE, WATER_BUCKET,
    MAGIC_LUMP_2, COD,        SAND
    ```

    ```txt
    WATER_BUCKET, SAND,       SALMON
    MAGIC_LUMP_2, BLANK_RUNE, MAGIC_LUMP_2,
    COD,          SAND,       WATER_BUCKET
    ```

    etc.

`RecipeInputItem`s describe a single slot of a recipe and determines what
items match it. There can be a single item that matches (see `RecipeInputSlimefunItem`,
`RecipeInputItemStack`), or a list (tag) of items all of which can be used
in that slot (see `RecipeInputGroup`, `RecipeInputTag`).

`RecipeOutput`s are a list of `RecipeOutputItem`s, all of which are the result
of the recipe.

`RecipeOutputItem`s controls how an output is generated when the recipe is
crafted. It can be a single item (see `RecipeOutputItemStack`,
`RecipeOutputSlimefunItem`),
or it can draw a single item from a weighted set of items (see
`RecipeOutputGroup`). To output multiple items, specify multiple `RecipeOutputItem`s
in the `RecipeOutput`

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

This would remove the need to use `ItemSetting`s to determine the gold pan weights.

Here are the inputs and outputs of a freezer turning water into ice

```txt
RecipeInput (
    { RecipeOutputItemStack(WATER_BUCKET) },
    SUBSET
)
RecipeOutput (
    RecipeOutputItem(ICE),
    RecipeOutputItem(BUCKET)
)
```

### RecipeService

This is the public interface for the recipe system, there are methods here to register,
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
    "type": NamespacedKey | NamespacedKey[]
    "energy"?: int
    "craftingTime"?: int
    "permissionNode"?: string | string[]
}
```

The recipe deserializer technically needs a `__filename` field, but it is
inserted when the file is read, so it isn't (and shouldn't) be provided in
the JSON.

`RecipeInput`

```txt
{
    "items": string | string[]
    "key": {
        [key: string]: RecipeInputItem
    }
    "match"?: NamespacedKey
}
```

Example showing the `items`/`key` fields:

```
{
    "items": [
        "123",
        " 4 ",
        "   "
    ],
    "key": {
        "1": "slimefun:nickel_ingot",
        "2": "slimefun:magnet",
        "3": "slimefun:cobalt_ingot",
        "4": "slimefun:battery"
    },
    "match": "slimefun:shaped"
},
```

`items` can also be a single string when shape doesn't matter

`RecipeOutput`

```txt
{
    "items": RecipeOutputItem[]
}
```

`RecipeInputItem`*

```txt
{
    "id": NamespacedKey
    "amount"?: int
} | {
    "tag": NamespacedKey
    "amount"?: int
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
    "group": RecipeOutputItem[]
    "weights"?: int[]
}
```

*In addition to those schemas, items can be in short form:

- Single items: `<namespace>:<id>` or `<namespace>:<id>|<amount>`
  - Example: Two batteries `slimefun:battery|2`
- Tags: `#<namespace>:<id>` or `#<namespace>:<id>|<amount>`
  - Example: A single plank `#minecraft:planks`

These short forms are purely for convenience when manually writing recipes
and are not extensible.

#### Implementation Note

Slimefun does not have namespaced item ids at the time of writing, so any namespace
that is not `minecraft` will be meaningless, however to future-proof this system,
namespaces should still be used where specified

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

### Folder Structure

All JSON recipes are stored at `plugins/Slimefun/recipes`, which has subfolders
Slimefun and each addon. There is another subfolder at
`plugins/Slimefun/recipes/custom`, where recipes in it override their corresponding
recipes in the parent folder.

For example, a recipe at `plugins/Slimefun/recipes/custom/Slimefun/battery.json` will
override the recipe at `plugins/Slimefun/recipes/Slimefun/battery.json` but not one at `plugins/Slimefun/recipes/custom/SomeAddon/battery.json`.

The filename referenced in below sections refers to the path of the JSON file
relative to `plugins/Slimefun/recipes/` or `plugins/Slimefun/recipes/custom`,
whichever one it is in.

### When the Server Starts

### Stage 1a

When a new version of Slimefun is enabled, all recipes in the resources folder
will be moved to `plugins/Slimefun/recipes/Slimefun`, and the version number will
be saved there too.

Addons should do the same and save to `plugins/Slimefun/recipes/<your-addon-name>`.

### Stage 1b

Also on enable, recipes defined in code should be registered. These two steps
can be done in either order.

### Stage 2

On the first server tick, all recipes in `plugins/Slimefun/recipes` (but not `/custom`)
are read and added to the `RecipeService`, removing all recipes with the same
filename that were already in the service.

Next, the server reads and adds all recipes in `plugins/Slimefun/recipes/custom`
which again overrides previous recipes with the same filename. It keeps track
of which recipes are custom overrides so it can be saved to the correct location

### Stage 3

While the server is running, recipes can be modified in code, saved to disk, or
re-loaded from disk. New recipes can also be added.

### Stage 4

On server shutdown (or `/sf recipe save`), **all** recipes are saved to disk.
This means any changes made while the server was running will be overwritten.
Server owners should run `/sf recipe reload <file-name?>` to load new recipes
dynamically from disk.

## Phases

Each phase should be a separate PR

- Phase 1 - Add the new API
- Phase 2 - Migrate Slimefun items/multiblocks/machines toward the new API
- Phase 3 - Update the Slimefun Guide to use the new API

The entire process should be seamless for the end users, and
backwards compatible with addons that haven't yet migrated
