package io.github.thebusybiscuit.slimefun4.implementation.items.electric.gadgets;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import io.github.bakedlibs.dough.common.ChatColors;
import io.github.bakedlibs.dough.data.persistent.PersistentDataAPI;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.Rechargeable;
import io.github.thebusybiscuit.slimefun4.core.handlers.EntityInteractHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.ToolUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.utils.compatibility.VersionedEntityType;

/**
 * The {@link MultiTool} is an electric device which can mimic
 * the behaviour of any other {@link SlimefunItem}.
 *
 * @author TheBusyBiscuit
 */
public class MultiTool extends SlimefunItem implements Rechargeable {

    private static final float COST = 0.3F;

    private final List<MultiToolMode> modes = new ArrayList<>();
    private final float capacity;

    private static final NamespacedKey key = new NamespacedKey(Slimefun.instance(), "multitool_mode");
    private static final String LORE_PREFIX = ChatColors.color("&8\u21E8 &7Mode: ");
    private static final Pattern REGEX = Pattern.compile(ChatColors.color("(&c&o)?" + LORE_PREFIX) + "(.+)");

    @ParametersAreNonnullByDefault
    public MultiTool(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, float capacity, String... items) {
        super(itemGroup, item, recipeType, recipe);

        for (int i = 0; i < items.length; i++) {
            modes.add(new MultiToolMode(this, i, items[i]));
        }

        this.capacity = capacity;
    }

    @Override
    public float getMaxItemCharge(ItemStack item) {
        return capacity;
    }

    private int nextIndex(int i) {
        int index = i;

        do {
            index++;

            if (index >= modes.size()) {
                index = 0;
            }
        } while (index != i && !modes.get(index).isEnabled());

        return index;
    }

    @Nonnull
    protected ItemUseHandler getItemUseHandler() {
        return e -> {
            Player p = e.getPlayer();
            ItemStack item = e.getItem();
            ItemMeta meta = item.getItemMeta();
            e.cancel();

            int index = PersistentDataAPI.getInt(meta, key, 0);
            SlimefunItem sfItem = modes.get(index).getItem();

            if (!p.isSneaking()) {
                if (sfItem != null && removeItemCharge(item, COST)) {
                    sfItem.callItemHandler(ItemUseHandler.class, handler -> handler.onRightClick(e));
                }
            } else {
                index = nextIndex(index);

                SlimefunItem selectedItem = modes.get(index).getItem();
                String itemName = selectedItem != null ? selectedItem.getItemName() : "Unknown";
                Slimefun.getLocalization().sendMessage(p, "messages.multi-tool.mode-change", true, msg -> msg.replace("%device%", "Multi Tool").replace("%mode%", ChatColor.stripColor(itemName)));

                PersistentDataAPI.setInt(meta, key, index);

                List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();

                boolean regexMatchFound = false;
                for (int i = 0; i < lore.size(); i++) {
                    String line = lore.get(i);

                    if (REGEX.matcher(line).matches()) {
                        lore.set(i, LORE_PREFIX + ChatColor.stripColor(itemName));
                        regexMatchFound = true;
                        break;
                    }
                }

                if (!regexMatchFound) {
                    lore.add(2, LORE_PREFIX + ChatColor.stripColor(itemName));
                }

                meta.setLore(lore);
                item.setItemMeta(meta);
            }
        };
    }

    @Nonnull
    private ToolUseHandler getToolUseHandler() {
        return (e, tool, fortune, drops) -> {
            // Multi Tools cannot be used as shears
            Slimefun.getLocalization().sendMessage(e.getPlayer(), "messages.multi-tool.not-shears");
            e.setCancelled(true);
        };
    }

    @Nonnull
    private EntityInteractHandler getEntityInteractionHandler() {
        return (e, item, offhand) -> {
            // Fixes #2217 - Prevent them from being used to shear entities
            EntityType type = e.getRightClicked().getType();
            if (type == VersionedEntityType.MOOSHROOM
                || type == VersionedEntityType.SNOW_GOLEM
                || type == EntityType.SHEEP
            ) {
                Slimefun.getLocalization().sendMessage(e.getPlayer(), "messages.multi-tool.not-shears");
                e.setCancelled(true);
            }
        };
    }

    @Override
    public void preRegister() {
        super.preRegister();

        addItemHandler(getItemUseHandler());
        addItemHandler(getToolUseHandler());
        addItemHandler(getEntityInteractionHandler());
    }

}
