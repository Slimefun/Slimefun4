# Update Procedure

Date: 2024-01-15
Last updated: 2024-01-15

## Goal

This SOP will go over updating Slimefun to the newest Minecraft version, most of this will only apply to major versions, but we have also seen minor versions break things. So please read through the whole SOP and make sure you do everything applicable.

## Updating

### Updating Bukkit/Spigot

The first step is just updating Spigot in the pom.xml. This should only be done in 2 cases:
* There's a new major version (well, MC major - 1.19 -> 1.20 is a major)
* There was a change within MC or Bukkit/Spigot that broke the API

To update the Spigot version, you will need to go to the `pom.xml` and find the `spigot.version` property, this will be within the `properties` property. Simply make this the MC version (e.g. `1.20` or in the case of minor `1.20.4`).

Once updated, **make sure to run a build** to check for compilation failures with `mvn clean package -DskipTests=true`. We will go over the tests next.

### Updating tests

The next step is making sure our tests are still working correctly as is. This can be done by running `mvn test` and verifying that all tests pass correctly without any failures or errors.

If there are any failures you will need to investigate these, it's best to run them one at a time, so you don't have the potential for cross-test contamination. If you find any issues with the tests, please fix them and make sure to add a comment to the PR explaining why the test was changed.

If you need any help fixing tests feel free to join the [Discord](https://discord.gg/slimefun).

Once all the tests are passed, check to see if there's a new version of [MockBukkit](https://github.com/MockBukkit/MockBukkit), this is the framework handling the Bukkit side of our tests. There very well may not be a new version, they usually lag updates a bit. If not, that's perfectly ok, just make sure to note it on the PR.

### Testing in game

The final and most important step is testing this in game. While I'd love for our tests to be perfect, they are not (especially if MockBukkit hasn't had an update yet). We need to ensure that everything is working in-game before we can ship a new version release.

To do this, you will need to build the plugin with `mvn clean package` and then copy the jar from `target/` to your server's `plugins/` folder. Once you've done this, start the server. You will want to test various things but the things we always want covered are:
* Commands, verify running a few commands work
  * `/sf versions`
  * `/sf cheat`
  * `/sf search`
* Items, verify you can use a few items (you can grab these from `/sf cheat`)
  * Wind staff
  * One of the talismans
  * One of the backpacks
* Blocks, verify you can place, break and ensure they all work
  * Ancient altar
  * Ore washer
  * Coal generator

It is important to verify heads are still working (part of the energy network and the coal generator). If head skins are not loading, consider it as a bug: try figuring out what the issue is, and ask in the [Discord](https://discord.gg/slimefun) if you are not sure what the cause may be.

Also make sure to verify that there are no errors in the console, any errors here should be investigated and fixed.

If you find any issues, please fix them and make sure to add a comment to the PR explaining why the fix was needed.

> **Note**
> An issue here usually means that we need to update Dough. If this is the case, please open a PR to Dough and then update the Dough version in the `pom.xml` to the new version. Once you've done this, make sure to run a build to verify everything is working correctly.

### Final steps

Once you've verified everything is working, you can go ahead and open the PR. We will get to this as soon as we can :)

While the PR is open, make sure to verify the E2E tests are passing, and you should also verify the output of these. If the E2E tests look good then finally we will update these.

#### Updating E2E tests

**This is only needed in a major version**

In the `e2e-testing.yml` file you will need to update the matrix strategy, please add the latest version of the old major (e.g. if 1.21 came out, add 1.20.x where x is the latest released version). If MC is requiring a new Java version make sure that is updated too in the `latest` version.

Once updated, push and re-verify that the E2E tests are still passing.
