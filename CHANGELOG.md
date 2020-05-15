<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of contents**

- [Release Candidate 12 (TBD)](#release-candidate-12-tbd)
- [Release Candidate 11 (25 Apr 2020)](#release-candidate-11-25-apr-2020)
- [Release Candidate 10 (28 Mar 2020)](#release-candidate-10-28-mar-2020)
- [Release Candidate 9 (07 Mar 2020)](#release-candidate-9-07-mar-2020)
- [Release Candidate 8 (06 Mar 2020)](#release-candidate-8-06-mar-2020)
- [Release Candidate 7 (06 Mar 2020)](#release-candidate-7-06-mar-2020)
- [Release Candidate 6 (16 Feb 2020)](#release-candidate-6-16-feb-2020)
- [Release Candidate 5 (09 Feb 2020)](#release-candidate-5-09-feb-2020)
- [Release Candidate 4 (06 Jan 2020)](#release-candidate-4-06-jan-2020)
- [Release Candidate 3 (21 Nov 2019)](#release-candidate-3-21-nov-2019)
- [Release Candidate 2 (29 Sep 2019)](#release-candidate-2-29-sep-2019)
- [Release Candidate 1 (26 Sep 2019)](#release-candidate-1-26-sep-2019)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

## Release Candidate 12 (TBD)

#### Additions
* Added Ukrainian translations
* Added /sf backpack to restore lost backpacks
* Added automated Unit Tests

#### Changes
* Little performance improvements
* Bandages, Rags and Splints will no longer be consumed if your health is full and you are not on fire
* Player Profiles (researches and stuff) are now loaded completely asynchronously

#### Fixes
* Fixed #1824
* Fixed #1833
* Fixed #1834
* Fixed #1843
* Fixed #1873
* Fixed Electric Smeltery not prioritisting recipes
* Fixed #1851
* Fixed #1891
* Fixed #1893
* Fixed #1897
* Fixed #1908
* Fixed #1903

## Release Candidate 11 (25 Apr 2020)

#### Additions
* Added GEOResourceGenerationEvent
* Added AncientAltarCraftEvent
* Added SlimefunGuide-Options API
* Added ItemSettings API
* Added 1.13 backwards compatibility
* Added "Magma Cream to Magma Blocks" recipe to the Electric Press
* Added "Magma Blocks to Sulfate" recipe
* You can now search for items from within the book variant of the Guide
* GEO Scans now support endlessly many different resources
* Added Output Chest support to the Composter

#### Changes
* Replaced GuideHandlers with FlexCategories
* Removed support for old EmeraldEnchants versions
* Updated the book variant of the guide to use the newer API
* Removed internal /sf elevator command
* Split whitelist.yml up into individual /world-settings/worldname.yml files
* Performance improvements
* Slimefun Guide runs much faster now and can better deal with many Categories and items
* Lots of API improvements
* Faulty addons are now identified more easily and will no longer break Slimefun's main content this quickly
* You can no longer /sf give yourself a Multiblock
* Addons have no longer access to Slimefuns default categories
* Updated seasonal Categories to have better icons
* Even more performance improvements
* Changed Ignition Chamber Recipe
* GEO Miner is now 2 seconds faster
* All Generators will now stop consuming fuel if no energy is needed
* /sf teleporter will now open your own Teleporter Menu if you specify no Player
* Added counter-measures against Players who design Cargo networks in a way that intentionally lags out servers
* API requests to Mojang are now spread across a longer time period to prevent rate-limits

#### Fixes
* Fixed error message when clicking empty slots in the Slimefun Guide
* Fixed #1779
* Fixed localized messages not showing in the book guide
* Fixed empty categories showing up when items inside were hidden
* Fixed ghost pages showing up when too many categories were disabled
* Fixed debug fish not showing the correct chunk timings
* Fixed heads with missing permissions placing down
* Fixed unpermitted items still showing up in the guide if researches are disabled
* Fixed unpermitted items in the book guide triggering the search function
* Fixed #1803
* Fixed #1806
* Fixed #1807
* Fixed Coolers accepting non-Juice items
* Fixed #1813
* Fixed #1814
* Fixed GEO Scanner being unable to deal with more than 28 different resources
* Fixed #893
* Fixed #1798
* Fixed #1490
* Fixed GPS Emergency Transmitters not working

## Release Candidate 10 (28 Mar 2020)
https://thebusybiscuit.github.io/builds/TheBusyBiscuit/Slimefun4/stable/#10

#### Additions
* Added some new charts to bStats
* Added a new language: Turkish
* Multiblocks that use Fences or Trap doors now accept all wood types
* Added Makeshift Smeltery
* Added Tree Growth Accelerator
* Added "Glass to Glass Panes" recipe to the Electric Press
* Added "Snowballs to Snow blocks" recipe to the Electric Press
* Added "Snow blocks to Ice" recipe to the Freezer
* You can now use Cooked Salmon in an Auto Drier to craft Fish Jerky
* The Lumber Axe can now strip logs too
* The Slimefun Guide can now remember what page of a Category or Minecraft Item you were on

#### Changes
* Removed some deprecated parts of the API
* Internal clean up and further documentation
* Changed Automatic Ignition Chamber to be a Dropper
* Teleporters are now significantly faster
* Item permissions have been moved to a seperate permissions.yml file
* Salt now only requires 2 blocks of Sand
* Fireworks from researching no longer damages entities
* Very slight performance improvements for Cargo networks
* 4K-carat gold ingots can now be used in a workbench by default (overridden by Items.yml)
* The project license is now included in every build
* Moved EmeraldsEnchants integration from EmeraldEnchants to Slimefun

#### Fixes
* Fixed some languages showing numbers larger than 100%
* Fixed #1570
* Fixed #1686
* Fixed #1648
* Fixed #1397
* Fixed #1706
* Fixed #1710
* Fixed #1711
* Fixed Slimefun Guide showing shaped recipes incorrectly
* Fixed #1719
* Fixed death waypoints not having the correct texture
* Fixed Androids having no texture when moving
* Fixed Androids not taking fuel from interfaces
* Fixed #1721
* Fixed #1619
* Fixed #1768

## Release Candidate 9 (07 Mar 2020)
https://thebusybiscuit.github.io/builds/TheBusyBiscuit/Slimefun4/stable/#9

#### Fixes
* Fixed Solar Generators not working

## Release Candidate 8 (06 Mar 2020)
https://thebusybiscuit.github.io/builds/TheBusyBiscuit/Slimefun4/stable/#8

#### Fixes
* Fixed bStats Metrics not sending properly

## Release Candidate 7 (06 Mar 2020)
https://thebusybiscuit.github.io/builds/TheBusyBiscuit/Slimefun4/stable/#7

#### Additions
* Added translations for Recipe Types
* Added Rainbow Concrete
* Added Rainbow Glazed Terracotta
* Added more internal documentation

#### Changes
* Researches now use their namespaced keys in the Researches.yml
* A lot of API changes

#### Fixes
* Fixed #1553
* Fixed #1513
* Fixed #1557
* Fixed #1558
* Fixed a translation not showing properly
* Fixed #1577
* Fixed #1597
* Fixed disabled Slimefun Addons not showing under /sf versions
* Fixed #1613

## Release Candidate 6 (16 Feb 2020)
https://thebusybiscuit.github.io/builds/TheBusyBiscuit/Slimefun4/stable/#6

#### Additions
* Added a new language: Japanese
* Added a new language: Swedish
* Added a new language: Czech
* Added a new language: Portuguese (Brazil)
* Added a new language: Arabic

#### Changes
* /sf research now uses namespaced keys instead of ids

#### Fixes
* Fixed #1515
* Fixed back-button in guide-settings not working via commands
* Fixed #1516
* Fixed magician talisman not being able to enchant books

## Release Candidate 5 (09 Feb 2020)
https://thebusybiscuit.github.io/builds/TheBusyBiscuit/Slimefun4/stable/#5

#### Additions
* Aded preset messages.yml files
* Added user-configurable localization
* Added many more options to the messages.yml
* Added custom model data support for Languages
* Added Grind Stone Recipes for Prismarine
* Added String to the Bio Reactor
* Added a config setting to limit how many Nodes a Network can have
* Added support for Furnaces and Brewing Stands to Cargo Networks
* Added Organic Food/Fertilizer for Dried Kelp
* Added many more strings to the messages.yml
* Added ability to translate messages for Players
* Added the ability to translate Researches
* Added StatusEffect API
* Added translatibility to categories
* Added translatibility to geo-resources

#### Changes
* Removed Solar Array
* A lot of internal cleanup
* Performance improvements for GEO Miner and Oil Pump
* General performance improvements
* Changed Startup console message
* Changed GEO-Resources API

#### Fixes
* Fixed #1355
* Fixed Localization mistakes
* Fixed #1366
* Fixed GitHub cache
* Fixed #1364
* Fixed Bio Reactor not accepting melons
* Fixed Cargo Networks particles being broken
* Fixed #1379
* Fixed #1212
* Fixed #114
* Fixed #1385
* Fixed #1390
* Fixed #1394
* Fixed #1313
* Fixed #1396
* Fixed Backpacks being placeable
* Fixed wrong file encoding for translations
* Fixed Minecraft recipes not showing correctly
* Fixed #1428
* Fixed #1435
* Fixed #1438
* Fixed Multi Tool functioning as unlimited Shears
* Fixed #1383
* Fixed Android Script Component textures

## Release Candidate 4 (06 Jan 2020)
https://thebusybiscuit.github.io/builds/TheBusyBiscuit/Slimefun4/stable/#4

#### Additions
* Added 1.15 support (1.14 and 1.15 are both supported)
* Added custom model support to Slimefun Guide and some Recipe Types
* Added Nether Gold Pan
* Added Iron Nuggets to Gold Pan drops
* Added CS-CoreLib version to the guide info
* Added AndroidMineEvent
* Added Electric Press
* Added Soulbound Trident
* Added "Andesite, Granite and Diorite to Gravel" recipes to the Grinder
* Added "nuggets to ingots and ingots to blocks" recipes to the Electric Press
* Added Salt to the GEO - Miner
* Added Magnesium Salt
* Added Magnesium-powered Generator
* Added "Gravel to Sand" recipe to the Grinder
* Added config option for circuit board drops
* Added player option to toggle research fireworks in the guide settings
* Added Kelp Cookies
* Added support for multiple recipes on vanilla items
* Added a "Craft last" button to the Automated Crafting Chamber
* Added more ore-doubling Recipes to the Ore Crusher
* Added Addons to the guide settings

#### Changes
* Revamped Guide Settings menu
* Changed some Category icons
* Changed Grappling Hook recipe
* Searching the guide now shows the Category of the item
* Contributors now also show their minecraft username (if possible)
* Changed teleporter sounds
* Electric Gold Pan now also supports Nether Gold Pan drops
* More performance improvements
* Improved Cargo performance
* Removed Nether Drill
* Tweaked Enhanced Furnace Recipes
* Changed tooltips for Radiation
* Oil Pump now shows its "Bucket -> Oil" recipe

#### Fixes
* Fixed Research Titles
* Fixed #1264
* Fixed #1261
* Fixed #1266
* Fixed #1272
* Fixed #1273
* Fixed christmas items
* Fixed Multi Tools
* Fixed credits not showing all contributors
* Fixed exception when viewing the second page of the credits
* Fixed #1269
* Fixed #1276
* Fixed GEO-Miner dupes
* Fixed Output Chest not working
* Fixed #1281
* Fixed #1280
* Fixed a lot of issues with Crucibles
* Fixed Grind Stone dupes
* Fixed #1316
* Fixed performance issues with Oil Pumps
* Fixed #1318
* Fixed #1298
* Fixed #1325
* Fixed #1295
* Fixed MultiBlocks not accepting different fence types
* Fixed #1337
* Fixed Applie Pie ID mismatch
* Fixed #1344
* Fixed #1349
* Fixed #1332
* Fixed #1356 and maybe other concurrency issues
* Fixed Ore Crusher's missing recipes
* Fixed #1354

## Release Candidate 3 (21 Nov 2019)
https://thebusybiscuit.github.io/builds/TheBusyBiscuit/Slimefun4/stable/#3

#### Additions
* Smeltery now shows some recipes in the guide
* Added Sweet Berry Juice
* MultiBlocks that require fences will now accept all types of wooden fences
* Added craftable Totems of Undying
* Added back some wiki-pages to the guide
* Added support for all new minecraft recipes to correctly display in the guide
* Added support for custom model data for items
* Added Output Chest support to the Table Saw
* Added Output Chest support to the Automated Panning Machine
* Added Jerky recipes to the Auto-Drier
* Added AutoDisenchantEvent
* Added "Flint to Cobblestone" Recipe to the Compressor


#### Changes
* Changed Ignition Chamber Recipe
* /sf cheat no longer allows you to spawn in MultiBlocks
* Removed Heavy Armor
* Massive performance improvements with a new item-id system
* Huge performance improvements with skippable tickers
* Changed Elytra Scale Recipe
* Revamped Reactor Access Port
* Performance improvements for multi tools
* Performance improvements for armor
* Performance improvements for the Slimefun Guide

#### Fixes
* Fixed Stone Chunk -> Cobblestone Recipe not working
* Fixed #1145
* Fixed #1157
* Fixed #1180
* Fixed Backpacks not working
* Fixed /sf cheat not showing locked categories
* Fixed #1200
* Fixed #1196
* Fixed #1153
* Fixed some food items
* Fixed multi tools not working
* Fixed #1202
* Fixed #1211
* Fixed #1219
* Fixed #1226
* Fixed #1224
* Fixed repair-cost getting wiped after disenchanting
* Fixed GPS transmitters transmitting wrong locations
* Fixed Ancient Altar allowing you to craft locked items

## Release Candidate 2 (29 Sep 2019)
https://thebusybiscuit.github.io/builds/TheBusyBiscuit/Slimefun4/stable/#2

#### Additions
* Added GEO - Miner
* Added more bStats Charts

#### Changes
* Reworked MultiBlocks
* Removed the Saw Mill

### Fixes
* Fixed Basic Machines not showing all recipes
* Fixed #1129
* Fixed #1130
* Fixed Auto-Updater for stable builds

## Release Candidate 1 (26 Sep 2019)
https://thebusybiscuit.github.io/builds/TheBusyBiscuit/Slimefun4/stable/#1

* First "stable" release since over a year. Stable builds will NOT receive support for bug reports since they are technically outdated. 
