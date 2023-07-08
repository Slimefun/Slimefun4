package io.github.thebusybiscuit.slimefun4.implementation.tasks.armor;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.bakedlibs.dough.common.ChatColors;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.core.attributes.ProtectionType;
import io.github.thebusybiscuit.slimefun4.core.attributes.RadiationSymptom;
import io.github.thebusybiscuit.slimefun4.core.attributes.Radioactive;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.items.RadioactiveItem;
import io.github.thebusybiscuit.slimefun4.utils.RadiationUtils;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;

/**
 * The {@link RadiationTask} handles radioactivity for
 * {@link Radioactive} items.
 *
 * @author Semisol
 */
public class RadiationTask extends AbstractArmorTask {

    private final RadiationSymptom[] symptoms = RadiationSymptom.values();

    @Override
    @ParametersAreNonnullByDefault
    protected void onPlayerTick(Player p, PlayerProfile profile) {
        int exposureTotal = 0;

        if (!profile.hasFullProtectionAgainst(ProtectionType.RADIATION) && p.getGameMode() != GameMode.CREATIVE && p.getGameMode() != GameMode.SPECTATOR) {
            for (ItemStack item : p.getInventory()) {
                if (item == null || item.getType().isAir()) {
                    continue;
                }
                SlimefunItem sfItem = SlimefunItem.getByItem(item);
                if (sfItem instanceof RadioactiveItem radioactiveItem) {
                    exposureTotal += item.getAmount() * radioactiveItem.getRadioactivity().getExposureModifier();
                }
            }
            int exposureLevelBefore = RadiationUtils.getExposure(p);

            if (exposureTotal > 0) {
                if (exposureLevelBefore == 0) {
                    Slimefun.getLocalization().sendMessage(p, "messages.radiation");
                }

                RadiationUtils.addExposure(p, exposureTotal);
            } else if (exposureLevelBefore > 0) {
                RadiationUtils.removeExposure(p, 1);
            }

            int exposureLevelAfter = RadiationUtils.getExposure(p);

            Slimefun.runSync(() ->  {
                for (RadiationSymptom symptom : symptoms) {
                    if (symptom.shouldApply(exposureLevelAfter)) {
                        symptom.apply(p);
                    }
                }
            });

            if (exposureLevelAfter > 0 || exposureLevelBefore > 0) {
                String msg = Slimefun.getLocalization().getMessage(p, "actionbar.radiation").replace("%level%", "" + exposureLevelAfter);
                BaseComponent[] components = new ComponentBuilder().append(ChatColors.color(msg)).create();
                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, components);
            }
        }
    }
}
