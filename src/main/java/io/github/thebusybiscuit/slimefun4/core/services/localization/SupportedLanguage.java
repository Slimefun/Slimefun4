package io.github.thebusybiscuit.slimefun4.core.services.localization;

public enum SupportedLanguage {

    ENGLISH("en", "a1701f21835a898b20759fb30a583a38b994abf60d3912ab4ce9f2311e74f72");

    private final String id;
    private final String textureHash;

    SupportedLanguage(String id, String textureHash) {
        this.id = id;
        this.textureHash = textureHash;
    }

    public String getId() {
        return id;
    }

    public String getTexture() {
        return textureHash;
    }
}
