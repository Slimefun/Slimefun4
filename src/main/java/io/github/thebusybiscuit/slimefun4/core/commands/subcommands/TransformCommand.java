package io.github.thebusybiscuit.slimefun4.core.commands.subcommands;

import io.github.thebusybiscuit.cscorelib2.chat.ChatColors;
import io.github.thebusybiscuit.slimefun4.core.commands.SlimefunCommand;
import io.github.thebusybiscuit.slimefun4.core.commands.SubCommand;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

class TransformCommand extends SubCommand {
    TransformCommand(SlimefunPlugin plugin, SlimefunCommand cmd) {
        super(plugin, cmd);
    }

    @Override
    public String getName() {
        return "transform";
    }

    @Override
    public boolean isHidden() {
        return false;
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            ItemStack item = p.getInventory().getItemInMainHand();
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                if (meta.getDisplayName().contains(ChatColors.color("&b已修复的刷怪笼"))) {
                    ItemStack transform = p.getInventory().getItemInMainHand().clone();
                    List<String> lore = meta.getLore();
                    if (lore != null) {
                        for (int i = 0; i < lore.size(); i++) {
                            if (lore.get(i).contains("类型")) {
                                lore.set(i, lore.get(i).replace("类型", "Type"));
                            }
                        }
                    }

                    meta.setLore(lore);
                    meta.setDisplayName(ChatColors.color("&bReinforced Spawner"));
                    transform.setItemMeta(meta);
                    p.getInventory().setItemInMainHand(null);
                    p.getInventory().setItemInMainHand(transform);
                } else if (meta.getDisplayName().contains(ChatColors.color("&bReinforced Spawner"))) {
                    ItemStack transform = SlimefunItems.REPAIRED_SPAWNER.clone();
                    ItemMeta im = item.getItemMeta().clone();
                    List<String> lore = im.getLore();

                    if (lore != null) {
                        for (int i = 0; i < lore.size(); i++) {
                            if (lore.get(i).contains("Type")) {
                                lore.set(i, lore.get(i).replace("Type", "类型"));
                            }
                        }
                    }

                    im.setLore(lore);
                    im.setDisplayName(ChatColors.color("&b已修复的刷怪笼"));
                    transform.setItemMeta(im);
                    p.getInventory().setItemInMainHand(null);
                    p.getInventory().setItemInMainHand(transform);
                } else {
                    SlimefunPlugin.getLocalization().sendMessage(sender, "messages.not-right-item", true);
                }
            }
        } else {
            SlimefunPlugin.getLocalization().sendMessage(sender, "messages.only-players", true);
        }
    }
}
