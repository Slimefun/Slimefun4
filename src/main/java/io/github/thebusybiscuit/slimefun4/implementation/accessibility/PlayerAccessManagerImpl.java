package io.github.thebusybiscuit.slimefun4.implementation.accessibility;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.api.accessibility.AccessData;
import io.github.thebusybiscuit.slimefun4.api.accessibility.AccessLevel;
import io.github.thebusybiscuit.slimefun4.api.accessibility.AccessManager;
import io.github.thebusybiscuit.slimefun4.api.accessibility.ConcreteAccessLevel;
import io.github.thebusybiscuit.slimefun4.core.attributes.TieredAccessible;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Represents an data wrapper object, responsible for reading from any given {@link SlimefunItem} which is also
 * an instance of {@link TieredAccessible}. This data wrapper object is intended for {@link Player}s,
 *
 * @author md5sha256
 */
public class PlayerAccessManagerImpl implements AccessManager {

    static {
        SlimefunPlugin.getJsonDeserializationService().registerDeserialization(PlayerAccessManagerImpl.class, PlayerAccessManagerImpl::new);
    }

    public static final NamespacedKey DATA_KEY =
        new NamespacedKey(SlimefunPlugin.instance(), "player-access-manager-data");
    private final Map<Class<?>, AccessData<?>> dataMap = new HashMap<>();

    public PlayerAccessManagerImpl() { }

    public PlayerAccessManagerImpl(@Nonnull String json) {
        loadFromString(json);
    }

    public PlayerAccessManagerImpl(@Nonnull JsonElement element) {
        loadFromJsonElement(element);
    }

    @Nonnull
    @Override
    public AccessLevel getAccessLevel(@Nonnull Player player) {
        return getAccessLevel(player.getUniqueId());
    }

    @Nonnull
    @Override
    public AccessLevel getAccessLevel(@Nonnull UUID player) {
        return getAccessData(UUID.class).map(accessData -> accessData.getAccessLevel(player))
            .orElse(ConcreteAccessLevel.NONE);
    }

    @Nonnull
    @Override
    public <T> Optional<AccessData<T>> getAccessData(@Nonnull Class<T> object) {
        AccessData<?> raw = dataMap.get(object);
        if (raw != null) {
            return Optional.of(((AccessData<T>) raw));
        }
        for (Map.Entry<Class<?>, AccessData<?>> entry : dataMap.entrySet()) {
            if (object.isAssignableFrom(entry.getKey())) {
                return Optional.of((AccessData<T>) entry.getValue());
            }
        }
        return Optional.empty();
    }

    @Nonnull
    @Override
    public <T> AccessData<T> getOrRegisterAccessData(@Nonnull Class<T> clazz,
                                                     @Nonnull AccessData<T> def) {
        return getAccessData(clazz).orElseGet(() -> {
            dataMap.put(clazz, def);
            return def;
        });
    }

    @Override
    public <T> boolean hasDataFor(@Nonnull T object) {
        if (object instanceof Player) {
            return getAccessData(UUID.class).map(data -> data.hasDataFor((UUID) object))
                .orElse(false);
        } else if (object instanceof UUID) {
            return getAccessData(UUID.class).map(data -> data.hasDataFor((UUID) object))
                .orElse(false);
        }
        Optional<? extends AccessData> optional = getAccessData(object.getClass());
        return optional.map(accessData -> accessData.hasDataFor(object)).orElse(false);
    }

    @Override
    public boolean isEmpty() {
        return dataMap.isEmpty();
    }

    @Override
    public void clear() {
        this.dataMap.clear();
    }

    @Override
    public void load(@Nonnull NamespacedKey namespace, @Nonnull ItemStack itemStack) {
        clear();
        if (!SlimefunPlugin.getMinecraftVersion().isAtLeast(MinecraftVersion.MINECRAFT_1_14)) {
            // This feature was introduced post 1.13. Cannot load newer data on an older version
            return;
        }
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            load(namespace, meta);
        }

    }

    @Override
    public void load(@Nonnull NamespacedKey namespace, @Nonnull Block block) {
        clear();
        if (!SlimefunPlugin.getMinecraftVersion().isAtLeast(MinecraftVersion.MINECRAFT_1_14)) {
            // This feature was introduced post 1.13. Cannot load newer data on an older version
            return;
        }
        BlockState state = block.getState();
        if (state instanceof TileState) {
            load(namespace, ((TileState) state));
        }
    }

    @Override
    public void saveTo(@Nonnull NamespacedKey namespace, @Nonnull ItemStack item) {
        if (!SlimefunPlugin.getMinecraftVersion().isAtLeast(MinecraftVersion.MINECRAFT_1_14)) {
            // This feature was introduced post 1.13. Cannot save.
            return;
        }
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            saveTo(namespace, meta);
            item.setItemMeta(meta);
        }
    }

    @Override
    public void saveTo(@Nonnull NamespacedKey namespace, @Nonnull Block block) {
        if (!SlimefunPlugin.getMinecraftVersion().isAtLeast(MinecraftVersion.MINECRAFT_1_14)) {
            return;
        }
        BlockState state = block.getState();
        if (state instanceof TileState) {
            saveTo(namespace, ((TileState) state));
            state.update(true, false);
        }
    }

    @Override
    public void loadFromJsonElement(@Nonnull JsonElement element) throws IllegalArgumentException {
        clear();
        if (!element.isJsonObject()) {
            return;
        }
        for (Map.Entry<String, JsonElement> entry : element.getAsJsonObject().entrySet()) {
            String rawClass = entry.getKey();
            Class<? extends AccessData> clazz = resolveAccessDataClass(rawClass);
            if (clazz == null) {
                continue;
            }
            Optional<?> optional =
                SlimefunPlugin.getJsonDeserializationService().deserialize(clazz, entry.getValue());
            optional.ifPresent((o) -> {
                AccessData<?> accessData = (AccessData<?>) o;
                dataMap.put(accessData.getType(), accessData);
            });

        }
    }

    @Nonnull
    @Override
    public JsonElement saveToJsonElement() {
        JsonObject jsonObject = new JsonObject();
        for (AccessData<?> accessData : dataMap.values()) {
            jsonObject.add(accessData.getClass().getCanonicalName(), accessData.saveToJsonElement());
        }
        return jsonObject;
    }

    public void load(@Nonnull NamespacedKey namespace, @Nonnull PersistentDataHolder holder) {
        clear();
        String json = holder.getPersistentDataContainer().get(namespace, PersistentDataType.STRING);
        if (json != null) {
            loadFromString(json);
        }
    }

    public void saveTo(@Nonnull NamespacedKey namespace, @Nonnull PersistentDataHolder holder) {
        PersistentDataContainer base = holder.getPersistentDataContainer();
        PersistentDataContainer container = base.getAdapterContext().newPersistentDataContainer();
        base.set(namespace, PersistentDataType.TAG_CONTAINER, container);
        container.set(DATA_KEY, PersistentDataType.STRING, saveToString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        PlayerAccessManagerImpl that = (PlayerAccessManagerImpl) o;
        return dataMap.equals(that.dataMap);
    }

    @Override
    public int hashCode() {
        return dataMap.hashCode();
    }

    private static Class<? extends AccessData> resolveAccessDataClass(@Nonnull String rawClass) {
        Class<?> clazz;
        try {
            clazz = Class.forName(rawClass);
        } catch (ClassNotFoundException ex) {
            Slimefun.getLogger()
                .log(Level.WARNING, "Invalid AccessData! Unknown AccessData class: " + rawClass);
            return null;
        }
        if (!AccessData.class.isAssignableFrom(clazz)) {
            Slimefun.getLogger()
                .log(Level.WARNING, "Invalid AccessData! AccessData is not assignable from: " + rawClass);
            return null;
        }
        return clazz.asSubclass(AccessData.class);
    }
}
