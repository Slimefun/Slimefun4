# 1. Storage layer

Date: 2023-11-15

**DO NOT rely on any APIs introduced until we finish the work completely!**

## Status

Proposed

## Context

Slimefun has been around for a very long time and due to that, the way we
wrote persistence of data has also been around for a very long time.
While Slimefun has grown, the storage layer has never been adapted.
This means that even all these years later, it's using the same old saving/loading.
This isn't necessarily always bad, however, the way Slimefun has
saved content until now is.

Today, files are saved as YAML files, which is good for a config format
but terrible for a data store. It has created very large files
that can get corrupted, aren't easy to manage and generally just 
take up a lot of space.

This ADR talks about the future of our data persistence. 

## Decision

We want to create a new storage layer abstraction and implementations
which will be backwards-compatible but open up new ways of storing data
within Slimefun. The end end goal is we can quickly and easily support
new storage backends (such as binary storage, SQL, etc.) for things like
[PlayerProfile](), [BlockStorage](), etc.

We also want to be generally more efficient in the way we save and load data.
Today, we HAVE to load way more than is required.
We can improve memory usage by only loading what we need, when we need it.

We will do this incrementally and at first, in an experimental context.
In that regard, we should aim to minimise the blast radius and lift as much
as possible.

### Quick changes overview

* New abstraction over storage to easily support multiple backends.
* Work towards moving away from the legacy YAML based storage.
* Lazy load and save data to more efficiently handle the data life cycle.

### Implementation details

There is a new interface called [`Storage`]() which is what all storage
backends will implement.
This will have methods for loading and saving things like
[`PlayerProfile`]() and [`BlockStorage`]().

Then, backends will implement these
(e.g. [`LegacyStorageBackend`]() (today's YAML situation))
in order to support these functions.
Not all storage backends are required support each data type.
e.g. SQL may not support [`BlockStorage`]().


## Addons

The goal is that Addons will be able to use implement new storage backends
if they wish and also be extended so they can load/save things as they wish.

The first few iterations will not focus on Addon support. We want to ensure
this new storage layer will work and supports what we need it to today.

This ADR will be updated when we get to supporting Addons properly.

## Considerations

This will be a big change therefore we will be doing it as incrementally as
possible.

The current plan looks like this:

* Phase 1 - Implement legacy data backend for [`PlayerProfile`]().
  * We want to load player data using the new storage layer with the current
    data system.
  * We'll want to monitor for any possible issues and generally refine 
    how this system should look
* Phase 2 - Implement new experimental binary backend for [`PlayerProfile`]().
  * Create a new backend for binary storage
  * Implement in an experimental capacity and allow users to opt-in
    * Provide a warning that this is **experimental** and there will be bugs.
  * Implement new metric for storage backend being used
* Phase 3 - Mark the new backend as stable for [`PlayerProfile`]().
  * Mark it as stable and remove the warnings once we're sure things are
    working correctly
  * Create a migration path for users currently using "legacy".
  * **MAYBE** enable by default for new servers?
* Phase 4 - Move [`BlockStorage`]() to new storage layer.
  * The big one! We're gonna tackle adding this to BlockStorage.
    This will probably be a large change and we'll want to be as 
    careful as possible here.
  * Implement `legacy` and `binary` as experimental storage backends
    for BlockStorage and allow users to opt-in
    * Provide a warning that this is **experimental** and there will be bugs.
* Phase 5 - Mark the new storage layer as stable for [`BlockStorage`]().
  * Mark it as stable and remove the warnings once we're sure things are
    working correctly
  * Ensure migration path works here too.
  * **MAYBE** enable by default for new servers?
* Phase 6 - Finish up and move anything else we want over
  * Move over any other data stores we have to the new layer
  * We should probably still do experimental -> stable but it should have
    less of a lead time.

## State of work

* Phase 1: In progress
