package io.github.thebusybiscuit.slimefun4.core.services.localization;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.inventory.ItemStack;

/**
 * This enum holds info about a {@link Language} that is embedded in our resources folder.
 * Every enum constant holds the key of that {@link Language} as well as a texture hash
 * for the {@link ItemStack} to display.
 * 
 * @author TheBusyBiscuit
 * 
 * @see Language
 *
 */
public enum LanguagePreset {

    ENGLISH("en", "a1701f21835a898b20759fb30a583a38b994abf60d3912ab4ce9f2311e74f72"),
    GERMAN("de", "5e7899b4806858697e283f084d9173fe487886453774626b24bd8cfecc77b3f"),
    FRENCH("fr", "51269a067ee37e63635ca1e723b676f139dc2dbddff96bbfef99d8b35c996bc"),
    ITALIAN("it", "85ce89223fa42fe06ad65d8d44ca412ae899c831309d68924dfe0d142fdbeea4"),
    SPANISH("es", "32bd4521983309e0ad76c1ee29874287957ec3d96f8d889324da8c887e485ea8"),
    RUSSIAN("ru", "16eafef980d6117dabe8982ac4b4509887e2c4621f6a8fe5c9b735a83d775ad"),
    POLISH("pl", false, "921b2af8d2322282fce4a1aa4f257a52b68e27eb334f4a181fd976bae6d8eb"),
    UKRAINIAN("uk", "28b9f52e36aa5c7caaa1e7f26ea97e28f635e8eac9aef74cec97f465f5a6b51"),
    BELARUSIAN("be", false, "8c12eaf0d83e97e2bace652d0d23e74908ee766894361271f00c22ea82d25b02"),
    SWEDISH("sv", "e910904bff9c86f6ed47688e9429c26e8d9c5d5743bd3ebb8e6f5040be192998"),
    DUTCH("nl", false, "c23cf210edea396f2f5dfbced69848434f93404eefeabf54b23c073b090adf"),
    DANISH("da", false, "10c23055c392606f7e531daa2676ebe2e348988810c15f15dc5b3733998232"),
    FINNISH("fi", false, "59f2349729a7ec8d4b1478adfe5ca8af96479e983fbad238ccbd81409b4ed"),
    NORWEGIAN("no", false, "e0596e165ec3f389b59cfdda93dd6e363e97d9c6456e7c2e123973fa6c5fda"),
    CZECH("cs", "48152b7334d7ecf335e47a4f35defbd2eb6957fc7bfe94212642d62f46e61e"),
    ROMANIAN("ro", false, "dceb1708d5404ef326103e7b60559c9178f3dce729007ac9a0b498bdebe46107"),
    BULGARIAN("bg", "19039e1fd88c78d9d7adc5aad5ab16e356be13464934ed9e2b0cef2051c5b534"),
    PORTUGUESE_PORTUGAL("pt", false, "ebd51f4693af174e6fe1979233d23a40bb987398e3891665fafd2ba567b5a53a"),
    PORTUGUESE_BRAZIL("pt-BR", "9a46475d5dcc815f6c5f2859edbb10611f3e861c0eb14f088161b3c0ccb2b0d9"),
    HUNGARIAN("hu", "4a9c3c4b6c5031332dd2bfece5e31e999f8deff55474065cc86993d7bdcdbd0"),
    CROATIAN("hr", false, "b050c04ec8cabce71d7103f3e9ef4bb8819f9f365eb335a9139912bc07ed445"),
    LATVIAN("lv", false, "f62a4938b59447f996b5ed94101df07429d1ad34776d591ffc6fd75b79473c"),
    GREEK("el", false, "1514de6dd2b7682b1d3ebcd10291ae1f021e3012b5c8beffeb75b1819eb4259d"),
    SLOVAK("sk", "6c72a8c115a1fb669a25715c4d15f22136ac4c2452784e4894b3d56bc5b0b9"),
    VIETNAMESE("vi", "8a57b9d7dd04169478cfdb8d0b6fd0b8c82b6566bb28371ee9a7c7c1671ad0bb"),
    INDONESIAN("id", "5db2678ccaba7934412cb97ee16d416463a392574c5906352f18dea42895ee"),
    CHINESE_CHINA("zh-CN", "7f9bc035cdc80f1ab5e1198f29f3ad3fdd2b42d9a69aeb64de990681800b98dc"),
    CHINESE_TAIWAN("zh-TW", "702a4afb2e1e2e3a1894a8b74272f95cfa994ce53907f9ac140bd3c932f9f"),
    JAPANESE("ja", "d640ae466162a47d3ee33c4076df1cab96f11860f07edb1f0832c525a9e33323"),
    KOREAN("ko", "fc1be5f12f45e413eda56f3de94e08d90ede8e339c7b1e8f32797390e9a5f"),
    HEBREW("he", TextDirection.RIGHT_TO_LEFT, "1ba086a2cc7272cf5ba49c80248546c22e5ef1bab54120e8a8e5d9e75b6a"),
    ARABIC("ar", TextDirection.RIGHT_TO_LEFT, "a4be759a9cf7f0a19a7e8e62f23789ad1d21cebae38af9d9541676a3db001572"),
    TURKISH("tr", "9852b9aba3482348514c1034d0affe73545c9de679ae4647f99562b5e5f47d09"),
    PERSIAN("fa", false, "5cd9badf1972583b663b44b1e027255de8f275aa1e89defcf77782ba6fcc652"),
    SERBIAN("sr", false, "5b0483a4f0ddf4fbbc977b127b3d294d7a869f995366e3f50f6b05a70f522510"),
    AFRIKAANS("af", false, "961a1eacc10524d1f45f23b0e487bb2fc33948d9676b418b19a3da0b109d0e3c"),
    MALAY("ms", false, "754b9041dea6db6db44750f1385a743adf653767b4b8802cad4c585dd3f5be46"),
    THAI("th", "2a7916e4a852f7e6f3f3de19c7fb57686a37bce834bd54684a7dbef8d53fb"),
    MACEDONIAN("mk", "a0e0b0b5d87a855466980a101a757bcdb5f77d9f7287889f3efa998ee0472fc0"),
    TAGALOG("tl", "9306c0c1ce6a9c61bb42a572c49e6d0ed20e0e6b3d122cc64c339cbf78e9e937");

    private final String id;
    private final boolean releaseReady;
    private final String textureHash;
    private final TextDirection textDirection;

    @ParametersAreNonnullByDefault
    LanguagePreset(String id, boolean releaseReady, TextDirection direction, String textureHash) {
        this.id = id;
        this.releaseReady = releaseReady;
        this.textureHash = textureHash;
        this.textDirection = direction;
    }

    @ParametersAreNonnullByDefault
    LanguagePreset(String id, boolean releaseReady, String textureHash) {
        this(id, releaseReady, TextDirection.LEFT_TO_RIGHT, textureHash);
    }

    @ParametersAreNonnullByDefault
    LanguagePreset(String id, TextDirection direction, String textureHash) {
        this(id, true, direction, textureHash);
    }

    @ParametersAreNonnullByDefault
    LanguagePreset(String id, String textureHash) {
        this(id, true, textureHash);
    }

    /**
     * This returns the id of this {@link Language}.
     * 
     * @return The language code
     */
    public @Nonnull String getLanguageCode() {
        return id;
    }

    /**
     * This returns whether this {@link LanguagePreset} is "release-ready".
     * A release-ready {@link Language} will be available in RC builds of Slimefun.
     * 
     * @return Whether this {@link Language} is "release-ready"
     */
    boolean isReadyForRelease() {
        return releaseReady;
    }

    /**
     * This returns the texture hash for this language.
     * This will be the flag of the corresponding country.
     * (Not accurate I know, but better than having all languages
     * look the same by using the same items)
     * 
     * @return The texture hash of this language
     */
    public @Nonnull String getTexture() {
        return textureHash;
    }

    /**
     * This returns the direction of text for
     * this language.
     * 
     * @return The direction of text for this language
     */
    public @Nonnull TextDirection getTextDirection() {
        return textDirection;
    }
}
