package io.github.starwishsama.extra.items;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.core.attributes.NotPlaceable;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemUseHandler;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import org.apache.commons.lang.Validate;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;
import java.util.logging.Level;

public class OverClockModule extends SimpleSlimefunItem<ItemUseHandler> implements NotPlaceable {
    private final JsonParser parser = new JsonParser();
    private final String PATH_NAME = "overclock_modules";

    public OverClockModule(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    public ItemUseHandler getItemHandler() {
        return e -> {
            Player p = e.getPlayer();
            Optional<Block> block = e.getClickedBlock();

            if (block.isPresent()) {
                Block b = block.get();

                if (ChargableBlock.isChargable(b)) {
                    e.cancel();

                    // 潜行时拿出模块, 反之则装载模块
                    if (p.isSneaking()) {
                        int amount = getModuleAmount(BlockStorage.getBlockInfoAsJson(b));
                        if (amount > 0) {
                            b.getWorld().dropItemNaturally(b.getLocation(), new CustomItem(SlimefunItems.OVERCLOCK_MODULE.clone(), amount));
                        } else {
                            SlimefunPlugin.getLocal().sendMessage(p, "message.no-module", true);
                        }
                    } else {
                        if (addModule(b)) {
                            ItemStack item = p.getInventory().getItemInMainHand();
                            if (SlimefunUtils.isItemSimilar(item, SlimefunItems.OVERCLOCK_MODULE, true)) {
                                p.getInventory().getItemInMainHand().setAmount(item.getAmount() - 1);
                            }
                            SlimefunPlugin.getLocal().sendMessage(p, "message.module-added", true);
                            return;
                        }

                        SlimefunPlugin.getLocal().sendMessage(p, "message.module-added-failed", true);
                    }
                }
            }
        };
    }

    private boolean addModule(Block b) {
        int machineHas = getModuleAmount(BlockStorage.getBlockInfoAsJson(b));
        if (machineHas == 0) {
            BlockStorage.addBlockInfo(b, PATH_NAME, "1");
            return true;
        } else if (machineHas > 0) {
            BlockStorage.addBlockInfo(b, PATH_NAME, String.valueOf(machineHas + 1));
            return true;
        }

        return false;
    }

    private int getModuleAmount(String json) {
        Validate.notNull(json, "Machine's json must not be null!");
        try {
            JsonElement element = parser.parse(json);
            if (element.isJsonObject()) {
                JsonObject object = element.getAsJsonObject();
                return object.get(PATH_NAME).getAsInt();
            }
        } catch (Exception e) {
            Slimefun.getLogger().log(Level.SEVERE, "无法解析机器方块信息", e);
            return 0;
        }

        return 0;
    }

}
