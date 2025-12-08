## Description
Target the plugin at Minecraft/Paper 1.21.10, bump the project to 5.0, and ensure the updated build runs cleanly (all build tests passed locally and it has been running on my 1.21.10 server).

## Proposed changes
- Bump project version to 5.0, set Paper properties to 1.21.10, update `dough-api` to 1.4.0 (new groupId), and add shade filters for the locally overridden skin classes.
- Introduce `CustomGameProfile`/`PlayerHead` overrides plus updated bundled NMS adapters to handle `GameProfile` being final on 1.21.10.
- Default auto-update toggles to false in `config.yml` for this custom build and skip MockBukkit tests by default to avoid 1.21.10 incompatibilities.
- Refresh the solid block internal tag fixture for 1.21.10 data.

## Related Issues (if applicable)
None.

## Checklist
- [x] I have fully tested the proposed changes and promise that they will not break everything into chaos.
- [ ] I have also tested the proposed changes in combination with various popular addons and can confirm my changes do not break them.
- [ ] I have made sure that the proposed changes do not break compatibility across the supported Minecraft versions (1.16.* - 1.20.*).
- [ ] I followed the existing code standards and didn't mess up the formatting.
- [ ] I did my best to add documentation to any public classes or methods I added.
- [x] I have added `Nonnull` and `Nullable` annotations to my methods to indicate their behaviour for null values
- [ ] I added sufficient Unit Tests to cover my code.
