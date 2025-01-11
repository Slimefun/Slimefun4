default: help

.PHONY: help
help:
	@echo "  * gen-biomes version=<version> - Generate biomes for the given version"

.PHONY: gen-biomes
gen-biomes:
	@echo "Generating biomes for $(version)"
	@curl "https://raw.githubusercontent.com/MockBukkit/MockBukkit/refs/heads/v$(version)/src/main/resources/keyed/worldgen/biome.json" -s \
		| jq '[ .values[].key]' \
		> "src/test/resources/biomes/$(version).x.json"
