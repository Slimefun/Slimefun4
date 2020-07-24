package io.github.thebusybiscuit.slimefun4.implementation.items.tools;

import java.text.DecimalFormat;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.github.thebusybiscuit.slimefun4.core.attributes.NotPlaceable;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * The {@link TapeMeasure} is used to measure the distance between two {@link Block Blocks}.
 * 
 * @author TheBusyBiscuit
 *
 */
public class TapeMeasure extends SimpleSlimefunItem<ItemUseHandler> implements NotPlaceable {

    private final NamespacedKey key = new NamespacedKey(SlimefunPlugin.instance(), "anchor");
    private final DecimalFormat format = new DecimalFormat("##.###");

    public TapeMeasure(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    public ItemUseHandler getItemHandler() {
        return e -> {
            e.cancel();

            if (e.getClickedBlock().isPresent()) {
                Block block = e.getClickedBlock().get();

                if (e.getPlayer().isSneaking()) {
                    setAnchor(e.getPlayer(), e.getItem(), block);
                }
                else {
                    measure(e.getPlayer(), e.getItem(), block);
                }
            }
        };
    }

    private void setAnchor(Player p, ItemStack item, Block block) {
        ItemMeta meta = item.getItemMeta();

        JsonObject json = new JsonObject();
        json.addProperty("world", block.getWorld().getUID().toString());
        json.addProperty("x", block.getX());
        json.addProperty("y", block.getY());
        json.addProperty("z", block.getZ());

        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, json.toString());

        String anchor = block.getX() + " | " + block.getY() + " | " + block.getZ();
        SlimefunPlugin.getLocalization().sendMessage(p, "messages.tape-measure.anchor-set", msg -> msg.replace("%anchor%", anchor));

        item.setItemMeta(meta);

    }

    private Optional<Location> getAnchor(Player p, ItemStack item) {
        ItemMeta meta = item.getItemMeta();

        String data = meta.getPersistentDataContainer().get(key, PersistentDataType.STRING);

        if (data != null) {
            JsonObject json = new JsonParser().parse(data).getAsJsonObject();

            UUID uuid = UUID.fromString(json.get("world").getAsString());

            if (p.getWorld().getUID().equals(uuid)) {
                int x = json.get("x").getAsInt();
                int y = json.get("y").getAsInt();
                int z = json.get("z").getAsInt();
                Location loc = new Location(p.getWorld(), x, y, z);
                return Optional.of(loc);
            }
            else {
                SlimefunPlugin.getLocalization().sendMessage(p, "messages.tape-measure.wrong-world");
                return Optional.empty();
            }
        }
        else {
            SlimefunPlugin.getLocalization().sendMessage(p, "messages.tape-measure.no-anchor");
            return Optional.empty();
        }
    }

    private void measure(Player p, ItemStack item, Block block) {
        Optional<Location> anchor = getAnchor(p, item);

        if (anchor.isPresent()) {
            Location loc = anchor.get();
            double distance = loc.distance(block.getLocation());
            SlimefunPlugin.getLocalization().sendMessage(p, "messages.tape-measure.distance", msg -> msg.replace("%distance%", format.format(distance)));
        }
    }

}
