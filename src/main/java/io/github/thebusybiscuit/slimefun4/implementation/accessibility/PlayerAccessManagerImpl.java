package io.github.thebusybiscuit.slimefun4.implementation.accessibility;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.api.accessibility.AccessData;
import io.github.thebusybiscuit.slimefun4.api.accessibility.AccessLevel;
import io.github.thebusybiscuit.slimefun4.api.accessibility.AccessManager;
import io.github.thebusybiscuit.slimefun4.api.accessibility.ConcreteAccessLevel;
import io.github.thebusybiscuit.slimefun4.core.attributes.TierAccessible;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Represents an data wrapper object, responsible for reading from any given {@link SlimefunItem} which is also
 * an instance of {@link TierAccessible}.
 *
 * @author md5sha256
 */
public class PlayerAccessManagerImpl implements AccessManager {

    public static final NamespacedKey DATA_KEY =
        new NamespacedKey(SlimefunPlugin.instance(), "abstract-access-manager-data");
    protected static final JsonParser SHARED_PARSER = new JsonParser();
    private final NamespacedKey namespace;
    private final Map<Class<?>, AccessData<?>> dataMap = new HashMap<>();

    public PlayerAccessManagerImpl(@Nonnull final NamespacedKey namespace) {
        this.namespace = namespace;
    }

    @Nonnull
    @Override
    public AccessLevel getLevel(@Nonnull final Player player) {
        return getLevel(player.getUniqueId());
    }

    @Nonnull
    @Override
    public AccessLevel getLevel(@Nonnull final UUID player) {
        return getAccessData(UUID.class).map(accessData -> accessData.getAccessLevel(player))
            .orElse(ConcreteAccessLevel.NONE);
    }

    @Nonnull
    @Override
    public <T> Optional<AccessData<T>> getAccessData(@Nonnull final Class<T> object) {
        final AccessData<?> raw = dataMap.get(object);
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
    public <T> AccessData<T> getOrRegisterAccessData(@Nonnull final Class<T> clazz,
                                                     @Nonnull final AccessData<T> def) {
        return getAccessData(clazz).orElseGet(() -> {
            dataMap.put(clazz, def);
            return def;
        });
    }

    @Override
    public boolean hasDataFor(@Nonnull final Object object) {
        if (object instanceof Player) {
            return getAccessData(Player.class).map(data -> data.hasDataFor((Player) object))
                .orElse(false);
        } else if (object instanceof UUID) {
            return getAccessData(UUID.class).map(data -> data.hasDataFor((UUID) object))
                .orElse(false);
        }
        final Optional<? extends AccessData> optional = getAccessData(object.getClass());
        return optional.map(accessData -> accessData.hasDataFor(object)).orElse(false);
    }

    @Override
    public boolean isEmpty() {
        return dataMap.isEmpty();
    }

    @Override
    public void reset() {
        this.dataMap.clear();
    }

    @Override
    public void load(@Nonnull SlimefunItemStack itemStack) {
        reset();
        if (!SlimefunPlugin.getMinecraftVersion().isAtLeast(MinecraftVersion.MINECRAFT_1_14)) {
            // This feature was introduced post 1.13. Cannot load newer data on an older version
            return;
        }
        final ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) {
            return;
        }
        final Optional<String> optional = SlimefunPlugin.getItemDataService().getItemData(meta);
        if (!optional.isPresent()) {
            return;
        }
        final PersistentDataContainer container =
            meta.getPersistentDataContainer().get(namespace, PersistentDataType.TAG_CONTAINER);
        if (container == null) {
            return;
        }
        final String json = container.get(DATA_KEY, PersistentDataType.STRING);
        if (json != null) {
            loadFromString(json);
        }
    }

    @Override
    public void saveTo(@Nonnull final SlimefunItemStack item) {
        if (!SlimefunPlugin.getMinecraftVersion().isAtLeast(MinecraftVersion.MINECRAFT_1_14)) {
            // This feature was introduced post 1.13. Cannot save.
            return;
        }
        final ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return;
        }
        final PersistentDataContainer base = meta.getPersistentDataContainer();
        final PersistentDataContainer container =
            base.getAdapterContext().newPersistentDataContainer();
        base.set(namespace, PersistentDataType.TAG_CONTAINER, container);
        container.set(DATA_KEY, PersistentDataType.STRING, saveToString());
        item.setItemMeta(meta);
    }

    @Override
    public void loadFromString(@Nonnull final String data) throws IllegalArgumentException {
        reset();
        final JsonElement element = SHARED_PARSER.parse(data);
        if (!element.isJsonObject()) {
            return;
        }
        final JsonObject jsonObject = element.getAsJsonObject();
        final String playerAccessDataName = PlayerAccessDataImpl.class.getCanonicalName();
        for (Map.Entry<String, JsonElement> entry : element.getAsJsonObject().entrySet()) {
            final String rawClass = entry.getKey();
            if (!rawClass.equalsIgnoreCase(playerAccessDataName)) {
                Slimefun.getLogger().log(Level.WARNING, "Unknown data class: " + rawClass);
                continue;
            }
            final PlayerAccessDataImpl accessData = new PlayerAccessDataImpl();
            final JsonElement raw = entry.getValue();
            final JsonPrimitive primitive;
            if (!raw.isJsonPrimitive() || (!(primitive = raw.getAsJsonPrimitive()).isString())) {
                Slimefun.getLogger().log(Level.WARNING,
                    "Invalid JSON data for access data class: " + entry.getKey()
                        + "! JsonElement is not a string");
                continue;
            }
            accessData.deserialize(primitive.getAsString());
            dataMap.put(Player.class, accessData);
        }
    }

    @Nonnull
    @Override
    public String saveToString() {
        final JsonObject jsonObject = new JsonObject();
        for (final AccessData<?> accessData : dataMap.values()) {
            jsonObject
                .addProperty(accessData.getType().getCanonicalName(), accessData.saveToString());
        }
        return jsonObject.toString();
    }
}
