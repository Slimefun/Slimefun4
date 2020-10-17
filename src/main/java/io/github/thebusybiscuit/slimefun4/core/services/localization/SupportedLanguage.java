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
enum SupportedLanguage {

    ENGLISH("en", true, "a1701f21835a898b20759fb30a583a38b994abf60d3912ab4ce9f2311e74f72"),
    GERMAN("de", true, "5e7899b4806858697e283f084d9173fe487886453774626b24bd8cfecc77b3f"),
    FRENCH("fr", true, "51269a067ee37e63635ca1e723b676f139dc2dbddff96bbfef99d8b35c996bc"),
    ITALIAN("it", true, "85ce89223fa42fe06ad65d8d44ca412ae899c831309d68924dfe0d142fdbeea4"),
    SPANISH("es", true, "32bd4521983309e0ad76c1ee29874287957ec3d96f8d889324da8c887e485ea8"),
    RUSSIAN("ru", true, "16eafef980d6117dabe8982ac4b4509887e2c4621f6a8fe5c9b735a83d775ad"),
    POLISH("pl", false, "921b2af8d2322282fce4a1aa4f257a52b68e27eb334f4a181fd976bae6d8eb"),
    UKRAINIAN("uk", true, "28b9f52e36aa5c7caaa1e7f26ea97e28f635e8eac9aef74cec97f465f5a6b51"),
    BELARUSIAN("be", false, "8c12eaf0d83e97e2bace652d0d23e74908ee766894361271f00c22ea82d25b02"),
    SWEDISH("sv", true, "e910904bff9c86f6ed47688e9429c26e8d9c5d5743bd3ebb8e6f5040be192998"),
    DUTCH("nl", false, "c23cf210edea396f2f5dfbced69848434f93404eefeabf54b23c073b090adf"),
    DANISH("da", false, "10c23055c392606f7e531daa2676ebe2e348988810c15f15dc5b3733998232"),
    FINNISH("fi", false, "59f2349729a7ec8d4b1478adfe5ca8af96479e983fbad238ccbd81409b4ed"),
    NORWEGIAN("no", false, "e0596e165ec3f389b59cfdda93dd6e363e97d9c6456e7c2e123973fa6c5fda"),
    CZECH("cs", true, "48152b7334d7ecf335e47a4f35defbd2eb6957fc7bfe94212642d62f46e61e"),
    ROMANIAN("ro", false, "dceb1708d5404ef326103e7b60559c9178f3dce729007ac9a0b498bdebe46107"),
    BULGARIAN("bg", false, "19039e1fd88c78d9d7adc5aad5ab16e356be13464934ed9e2b0cef2051c5b534"),
    PORTUGUESE_PORTUGAL("pt", false, "ebd51f4693af174e6fe1979233d23a40bb987398e3891665fafd2ba567b5a53a"),
    PORTUGUESE_BRAZIL("pt-BR", true, "9a46475d5dcc815f6c5f2859edbb10611f3e861c0eb14f088161b3c0ccb2b0d9"),
    HUNGARIAN("hu", true, "4a9c3c4b6c5031332dd2bfece5e31e999f8deff55474065cc86993d7bdcdbd0"),
    CROATIAN("hr", false, "b050c04ec8cabce71d7103f3e9ef4bb8819f9f365eb335a9139912bc07ed445"),
    LATVIAN("lv", false, "f62a4938b59447f996b5ed94101df07429d1ad34776d591ffc6fd75b79473c"),
    GREEK("el", false, "1514de6dd2b7682b1d3ebcd10291ae1f021e3012b5c8beffeb75b1819eb4259d"),
    SLOVAK("sk", true, "6c72a8c115a1fb669a25715c4d15f22136ac4c2452784e4894b3d56bc5b0b9"),
    VIETNAMESE("vi", true, "8a57b9d7dd04169478cfdb8d0b6fd0b8c82b6566bb28371ee9a7c7c1671ad0bb"),
    INDONESIAN("id", true, "5db2678ccaba7934412cb97ee16d416463a392574c5906352f18dea42895ee"),
    CHINESE_CHINA("zh-CN", true, "7f9bc035cdc80f1ab5e1198f29f3ad3fdd2b42d9a69aeb64de990681800b98dc"),
    CHINESE_TAIWAN("zh-TW", true, "702a4afb2e1e2e3a1894a8b74272f95cfa994ce53907f9ac140bd3c932f9f"),
    JAPANESE("ja", true, "d640ae466162a47d3ee33c4076df1cab96f11860f07edb1f0832c525a9e33323"),
    KOREAN("ko", true, "fc1be5f12f45e413eda56f3de94e08d90ede8e339c7b1e8f32797390e9a5f"),
    HEBREW("he", false, "1ba086a2cc7272cf5ba49c80248546c22e5ef1bab54120e8a8e5d9e75b6a"),
    ARABIC("ar", true, "a4be759a9cf7f0a19a7e8e62f23789ad1d21cebae38af9d9541676a3db001572"),
    TURKISH("tr", true, "9852b9aba3482348514c1034d0affe73545c9de679ae4647f99562b5e5f47d09"),
    PERSIAN("fa", false, "5cd9badf1972583b663b44b1e027255de8f275aa1e89defcf77782ba6fcc652"),
    SERBIA("sr", false, "5b0483a4f0ddf4fbbc977b127b3d294d7a869f995366e3f50f6b05a70f522510"),
    AFRIKAANS("af", false, "961a1eacc10524d1f45f23b0e487bb2fc33948d9676b418b19a3da0b109d0e3c"),
    MALAY("ms", false, "754b9041dea6db6db44750f1385a743adf653767b4b8802cad4c585dd3f5be46"),
    THAI("th", true, "2a7916e4a852f7e6f3f3de19c7fb57686a37bce834bd54684a7dbef8d53fb"),
    MACEDONIAN("mk", false, "a0e0b0b5d87a855466980a101a757bcdb5f77d9f7287889f3efa998ee0472fc0"),
    TAGALOG("tl", true, "9306c0c1ce6a9c61bb42a572c49e6d0ed20e0e6b3d122cc64c339cbf78e9e937");

    public static final SupportedLanguage[] valuesCache = values();

    private final String id;
    private final boolean releaseReady;
    private final String textureHash;

    @ParametersAreNonnullByDefault
    SupportedLanguage(String id, boolean releaseReady, String textureHash) {
        this.id = id;
        this.releaseReady = releaseReady;
        this.textureHash = textureHash;
    }

    @Nonnull
    public String getLanguageId() {
        return id;
    }

    public boolean isReadyForRelease() {
        return releaseReady;
    }

    @Nonnull
    public String getTexture() {
        return textureHash;
    }
}
