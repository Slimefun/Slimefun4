package io.github.thebusybiscuit.slimefun4.core.services.localization;

public enum SupportedLanguage {

    ENGLISH("en", "a1701f21835a898b20759fb30a583a38b994abf60d3912ab4ce9f2311e74f72"),
    GERMAN("de", "5e7899b4806858697e283f084d9173fe487886453774626b24bd8cfecc77b3f"),
    FRENCH("fr", "51269a067ee37e63635ca1e723b676f139dc2dbddff96bbfef99d8b35c996bc"),
    ITALIAN("it", "85ce89223fa42fe06ad65d8d44ca412ae899c831309d68924dfe0d142fdbeea4"),
    SPANISH("es", "32bd4521983309e0ad76c1ee29874287957ec3d96f8d889324da8c887e485ea8"),
    POLISH("pl", "921b2af8d2322282fce4a1aa4f257a52b68e27eb334f4a181fd976bae6d8eb"),
    SWEDISH("sv", "e910904bff9c86f6ed47688e9429c26e8d9c5d5743bd3ebb8e6f5040be192998"),
    DUTCH("nl", "c23cf210edea396f2f5dfbced69848434f93404eefeabf54b23c073b090adf");

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
