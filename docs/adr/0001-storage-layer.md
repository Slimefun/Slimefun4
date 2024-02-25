# 1. Storage layer

Date: 2023-11-15
Last update: 2023-12-27

**DO NOT rely on any APIs introduced until we finish the work completely!**

## Status

Work in progress

## Context

Slimefun has been around for a very long time and due to that, the way we
wrote persistence of data has also been around for a very long time.
While Slimefun has grown, the storage layer has never been adapted.
This means that even all these years later, it's using the same old saving/loading.
This isn't necessarily always bad, however, as Slimefun has grown both in terms of content
and the servers using it - we've seen some issues.

Today, files are saved as YAML files (sometimes with just a JSON object per line),
which is good for a config format but not good for a data store. It can create very large files
that can get corrupted, the way we've been saving data often means loading it all at once as well
rather than lazy-loading and generally isn't very performant.

For a long time we've been talking about rewriting our data storage in multiple forms
(you may have seen this referenced for "BlockStorage rewrite" or "SQL for PlayerProfiles", etc.).
Now is the time we start to do this, this will be a very large change and will not be done quickly or rushed.

This ADR talks about the future of our data persistence. 

## Decision

We want to create a new storage layer abstraction and implementations
which will be backwards-compatible but open up new ways of storing data
within Slimefun. The end end goal is we can quickly and easily support
new storage backends (such as binary storage, SQL, etc.) for things like
[PlayerProfile](https://github.com/Slimefun/Slimefun4/blob/bbfb9734b9f549d7e82291eff041f9b666a61b63/src/main/java/io/github/thebusybiscuit/slimefun4/api/player/PlayerProfile.java), [BlockStorage](https://github.com/Slimefun/Slimefun4/blob/bbfb9734b9f549d7e82291eff041f9b666a61b63/src/main/java/me/mrCookieSlime/Slimefun/api/BlockStorage.java), etc.

We also want to be generally more efficient in the way we save and load data.
Today, we load way more than is required.
We can improve memory usage by only loading what we need, when we need it.

We will do this incrementally and at first, in an experimental context.
In that regard, we should aim to minimise the blast radius and lift as much
as possible.

### Quick changes overview

* New abstraction over storage to easily support multiple backends.
* Work towards moving away from the legacy YAML based storage.
* Lazy load and save data to more efficiently handle the data life cycle.

### Implementation details

There is a new interface called [`Storage`](TBD) which is what all storage
backends will implement.
This will have methods for loading and saving things like
[`PlayerProfile`](https://github.com/Slimefun/Slimefun4/blob/bbfb9734b9f549d7e82291eff041f9b666a61b63/src/main/java/io/github/thebusybiscuit/slimefun4/api/player/PlayerProfile.java) and [`BlockStorage`](https://github.com/Slimefun/Slimefun4/blob/bbfb9734b9f549d7e82291eff041f9b666a61b63/src/main/java/me/mrCookieSlime/Slimefun/api/BlockStorage.java).

Then, backends will implement these
(e.g. [`LegacyStorageBackend`](TBD) (today's YAML situation))
in order to support these functions.
Not all storage backends are required support each data type.
e.g. SQL may not support [`BlockStorage`](https://github.com/Slimefun/Slimefun4/blob/bbfb9734b9f549d7e82291eff041f9b666a61b63/src/main/java/me/mrCookieSlime/Slimefun/api/BlockStorage.java).


## Addons

The goal is that Addons will be able to use and implement new storage backends
if they wish and also be extended so they can load/save things as they wish.

The first few iterations will not focus on Addon support. We want to ensure
this new storage layer will work and supports what we need it to today.

This ADR will be updated when we get to supporting Addons properly.

## Considerations

This will be a big change therefore we will be doing it as incrementally as
possible.
Changes will be tested while in the PR stage and merged into the Dev releases when possible.
We may do an experimental release if required.

Phases do not (and very likely will not) be done within a single PR. They will also not have any timeframe attached to them.

The current plan looks like this:

* Phase 1 - Implement legacy data backend for [`PlayerProfile`](https://github.com/Slimefun/Slimefun4/blob/bbfb9734b9f549d7e82291eff041f9b666a61b63/src/main/java/io/github/thebusybiscuit/slimefun4/api/player/PlayerProfile.java).
  * We want to load player data using the new storage layer with the current
    data system.
  * We'll want to monitor for any possible issues and generally refine 
    how this system should look
* Phase 2 - Implement new experimental binary backend for [`PlayerProfile`](https://github.com/Slimefun/Slimefun4/blob/bbfb9734b9f549d7e82291eff041f9b666a61b63/src/main/java/io/github/thebusybiscuit/slimefun4/api/player/PlayerProfile.java).
  * Create a new backend for binary storage
  * Implement in an experimental capacity and allow users to opt-in
    * Provide a warning that this is **experimental** and there will be bugs.
  * Implement new metric for storage backend being used
* Phase 3 - Mark the new backend as stable for [`PlayerProfile`](https://github.com/Slimefun/Slimefun4/blob/bbfb9734b9f549d7e82291eff041f9b666a61b63/src/main/java/io/github/thebusybiscuit/slimefun4/api/player/PlayerProfile.java).
  * Mark it as stable and remove the warnings once we're sure things are
    working correctly
  * Create a migration path for users currently using "legacy".
  * Enable by default for new servers
* Phase 4 - Move [`BlockStorage`](https://github.com/Slimefun/Slimefun4/blob/bbfb9734b9f549d7e82291eff041f9b666a61b63/src/main/java/me/mrCookieSlime/Slimefun/api/BlockStorage.java) to new storage layer.
  * The big one! We're gonna tackle adding this to BlockStorage.
    This will probably be a large change and we'll want to be as 
    careful as possible here.
  * Implement `legacy` and `binary` as experimental storage backends
    for BlockStorage and allow users to opt-in
    * Provide a warning that this is **experimental** and there will be bugs.
* Phase 5 - Mark the new storage layer as stable for [`BlockStorage`](https://github.com/Slimefun/Slimefun4/blob/bbfb9734b9f549d7e82291eff041f9b666a61b63/src/main/java/me/mrCookieSlime/Slimefun/api/BlockStorage.java).
  * Mark it as stable and remove the warnings once we're sure things are
    working correctly
  * Ensure migration path works here too.
  * Enable by default for new servers
* Phase 6 - Finish up and move anything else we want over
  * Move over any other data stores we have to the new layer
  * We should probably still do experimental -> stable but it should have
    less of a lead time.

## State of work

* Phase 1: In progress
  * https://github.com/Slimefun/Slimefun4/pull/4065
* Phase 2: Not started
* Phase 3: Not started
* Phase 4: Not started
* Phase 5: Not started
* Phase 6: Not started
