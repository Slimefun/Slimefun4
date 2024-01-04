# 2. Recipe Rewrite

Date: 2024-01-02
Last update: 2023-01-04

## Status

WIP

## Context and Definitions

Unlike vanilla Minecraft, the recipe system of Slimefun is very basic and
rigid. The most notable issues include:

- It is impossible for an item to have more than one crafting recipe in
  certain workstations (Enhanced Crafting Table, Magic Workbench, etc).
- Recipes cannot be shifted. For example, Copper Wire can only be crafted
  in the middle row, and not in the top nor bottom.
- Items with more than one recipe (e.g. Sulfate) can have only one
  displayed when its entry is viewed in the guide.
  
### Managing Recipes

Another less noticeable issue is all the different sources of recipes.
Some recipes come from `SlimefunItems`, others are defined by the workstations
themselves.

Other machines have completely unique processes that cannot be written
down succinctly in terms of input and output items (Auto Anvil, for example
takes a Duct Tape and a damaged item, and returns a less damaged item;
Gold Pans take Gravel/Soul Sand and spit out randomly determined items).

This rewrite will aim to tackle all such recipes.

### Machines

We cannot attempt to redesign recipes without mentioning the machines they are
crafted in. So in this ADR, a machine will be some mechanic where a recipe's
input is converted into its output. This includes both manual workstations and
automatic machines.

## Solution

### Categorizing Recipes

So far, all we have is a lump of recipes and a lump of machines which can
craft some of those recipes. Thankfully, most machines have no overlap with
each other, and we can simplify this mess a little with categories. Many
of the `RecipeType`s are already categories, but there will be many more
automatic machines who don't have a `RecipeType` that need a category too.

#### Smeltery and Improvised Smeltery

There is a small hiccup with these two machines, in that the Improvised
Smeltery can only craft a subset of the Smeltery recipes. To fix this, we
have a 'Dust Smelting' category and an 'Ingot Mixing' Category. The Improvised
Smeltery will only be able to craft recipes in the 'Dust Smelting' category,
but the Smeltery will be able to craft both.

### Recipe Structures

The last defining feature of a recipe is its structure (or lack thereof) of
its input items. The four structures a recipe can have are:

- **Identical**: The items in the input grid/zone/area must be **exactly**
  as defined in the recipe, in the **exact** same spot as defined in the
  recipe.
- **Shaped**: The items in the input grid/zone/area must be **exactly** as
  defined in the recipe, but only the relative positions of the items to
  each other must be the same as in the recipe. i.e., the input items can
  be **shifted**
- **Shapeless**: The items in the input grid/zone/area must be **exactly** as
  defined in the recipe, but their order/position does not matter
- **Subset**: The recipe's inputs must be a **subset** (not necessarily proper)
  of the items in the input grid/zone/area, and the order/position does not
  matter. The majority of recipes in Slimefun are Subset recipes (Smeltery,
  Ore Grinder, Compressor, etc...)

Currently, there are no Shaped or Shapeless recipes in Slimefun, however most
Identical recipes should be Shaped/Shapeless instead (Copper Wire -> Shaped,
Monster Jerky -> Shapeless, etc...)

#### Ancient Altar

This is a rather unique case, since the inputs can be rotated, but we only
need to check each of the up to 8 rotations against the recipe.

### Searching and Matching Recipes

The problem we have now is that given an ordered set of input items, how
do we know what it should craft? Searching through the recipes will be done
with a simple linear search, but matching a recipe is a bit more involved

#### Identical

Identical recipes are the easiest, but also the least common. To match them,
we simply iterate over each index (left-right, top-down) and match the two
items in that index.

#### Shaped

Similar to Identical, but with start by finding the first non-empty element
in each list of items before continuing.

One small caveat is that if two non-empty items match, their row-difference
cannot be different from that of the first non-empty elements. This makes
sure that (1) and (2) match, but (3) doesn't match either

```txt
+-+-+    +-+-+    +-+-+
|a|b|    | | |    | |a|
+-+-+    +-+-+    +-+-+
| | |    |a|b|    |b| |
+-+-+    +-+-+    +-+-+
 (1)      (2)      (3)
```

#### Shapeless and Subset

For both Shapeless and Subset recipes, we look for an injective map from the
recipe inputs to the given inputs, where a recipe input item is mapped to
an given input item if the latter can be used as the former in the recipe
being matched. For Shapeless recipes, we additionally require that the size
of the recipe inputs is the same as the size of the actual inputs.

#### Optimization

To optimize this process a little bit, whenever a given set of input items
can craft more than one output, we hash the inputs and put it + the recipe
into an LRU cache so that next craft, we can instantly retrieve the recipe.

### The Guide Display

If available, an item's guide page will be paginated, with each recipe
taking up a page. Inputs that can be multiple items will cycle through
them, similar to the guide page for vanilla items.

The recipes displayed in the bottom two rows will only show recipes
that have one input.

## Implementation

### The `Recipe` class

This is a pretty straightforward data class that contains a recipes input,
output, and structure.

Items in a `Recipe`'s input are `RecipeComponent`s rather than plain
`ItemStack`s, which allows for 'tagged' components (e.g. all types of Wood
Log/Coloured Wool, etc...). This also allows both vanilla and Slimefun copper
to be used as one item

### `SlimefunRecipeService`

This is essentially a multimap of `RecipeCategory`s to `Recipe`s, along with
an LRU cache

### Registering Recipes

The preferred constructors for `SlimefunItem`s have switched from using
`RecipeType` to `RecipeCategory`, but the recipes themselves have not been
changed, so little migration is necessary for items with only 1 recipe.

For Slimefun items with more than one recipe, call
`SlimefunItem.addRecipe(recipe)` before registering the item.

For recipes that craft vanilla items, call
`RecipeCategory.registerRecipe(recipe)`

### Searching Recipes

Machines can implement the `RecipeCrafter` interface, which comes with
searching utilities already. Otherwise, call `Slimefun.searchRecipes()`.
This performs a linear seach as described in the 'Searching and Matching
Recipes' section

## Deprecation and removal of old recipe system

The old recipe system will be deprecated once the new recipe system is
complete, and will be removed entirely at a later date.

## Progress

- New recipe system / API: WIP
- Migration of crafting machines to new API: Not Started
- Guide recipe pagination / item cycling: Not Started