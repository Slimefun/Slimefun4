package io.github.thebusybiscuit.slimefun5.core.guide.options;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.cryptomorin.xseries.XMaterial;

import io.github.bakedlibs.dough.chat.ChatInput;
import io.github.bakedlibs.dough.config.Config;
import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun5.core.services.LocalizationService;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.utils.ChestMenuUtils;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.MaterialCompat;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;

/**
 * An admin-only, chest-GUI editor for the whole server config. Sections are folders you click into;
 * booleans toggle, numbers/strings open a chat prompt, lists are shown read-only.
 *
 * <p>Gated by {@code config-editor.enabled} (default true) and OP / {@link #PERMISSION}. The
 * {@code config-editor} section itself is hidden from the editor, so the feature can only be turned on
 * or off by editing {@code config.yml} directly - never from in-game.
 *
 * <p>Saving uses the standard config writer, which does not preserve inline comments.
 *
 * @author Slimefun5
 */
public final class ConfigEditorMenu {

    public static final String PERMISSION = "slimefun.config-editor";
    private static final String HIDDEN_ROOT = "config-editor";
    private static final int PAGE_SIZE = 45;
    private static final int BACK_SLOT = 45;
    private static final int PREV_SLOT = 48;
    private static final int INFO_SLOT = 49;
    private static final int NEXT_SLOT = 50;
    private static final int[] BORDER = { 45, 46, 47, 48, 49, 50, 51, 52, 53 };

    private ConfigEditorMenu() {}

    /** Whether the in-game config editor is enabled (config, default true when the key is absent). */
    public static boolean isEnabled() {
        Config cfg = Slimefun.getCfg();
        return !cfg.contains("config-editor.enabled") || cfg.getBoolean("config-editor.enabled");
    }

    public static boolean canUse(@Nonnull Player p) {
        return p.isOp() || p.hasPermission(PERMISSION);
    }

    public static void open(@Nonnull Player p) {
        open(p, "", 0);
    }

    @ParametersAreNonnullByDefault
    public static void open(Player p, String path, int page) {
        LocalizationService locale = Slimefun.getLocalization();

        if (!isEnabled()) {
            locale.sendMessage(p, "guide.config.disabled", true);
            return;
        }
        if (!canUse(p)) {
            locale.sendMessage(p, "messages.no-permission", true);
            return;
        }

        FileConfiguration cfg = Slimefun.getCfg().getConfiguration();
        ConfigurationSection section = path.isEmpty() ? cfg : cfg.getConfigurationSection(path);
        if (section == null) {
            open(p, "", 0);
            return;
        }

        List<String> keys = new ArrayList<>();
        for (String key : section.getKeys(false)) {
            String full = path.isEmpty() ? key : path + "." + key;
            if (full.equals(HIDDEN_ROOT) || full.startsWith(HIDDEN_ROOT + ".")) {
                continue; // the disable switch can never be changed from in-game
            }
            keys.add(key);
        }

        String title = locale.getMessage(p, "guide.config.title") + (path.isEmpty() ? "" : " /" + path);
        ChestMenu menu = new ChestMenu(title);
        menu.setEmptySlotsClickable(false);
        ChestMenuUtils.drawBackground(menu, BORDER);

        menu.addItem(BACK_SLOT, CustomItemStack.create(MaterialCompat.stack(XMaterial.ENCHANTED_BOOK),
            "&7" + locale.getMessage(p, "guide.config.back")));
        menu.addMenuClickHandler(BACK_SLOT, (pl, slot, item, action) -> {
            if (path.isEmpty()) {
                pl.closeInventory();
            } else {
                open(pl, parent(path), 0);
            }
            return false;
        });

        int pages = Math.max(1, (int) Math.ceil(keys.size() / (double) PAGE_SIZE));
        int start = page * PAGE_SIZE;
        for (int i = start, shown = 0; i < keys.size() && shown < PAGE_SIZE; i++, shown++) {
            String key = keys.get(i);
            String full = path.isEmpty() ? key : path + "." + key;
            renderEntry(menu, shown, p, cfg, path, page, key, full);
        }

        menu.addItem(INFO_SLOT, CustomItemStack.create(MaterialCompat.stack(XMaterial.PAPER),
            "&e" + locale.getMessage(p, "guide.config.page").replace("%page%", String.valueOf(page + 1)).replace("%pages%", String.valueOf(pages))));
        menu.addMenuClickHandler(INFO_SLOT, ChestMenuUtils.getEmptyClickHandler());

        if (page > 0) {
            menu.addItem(PREV_SLOT, ChestMenuUtils.getPreviousButton(p, page + 1, pages));
            menu.addMenuClickHandler(PREV_SLOT, (pl, slot, item, action) -> {
                open(pl, path, page - 1);
                return false;
            });
        }
        if (page < pages - 1) {
            menu.addItem(NEXT_SLOT, ChestMenuUtils.getNextButton(p, page + 1, pages));
            menu.addMenuClickHandler(NEXT_SLOT, (pl, slot, item, action) -> {
                open(pl, path, page + 1);
                return false;
            });
        }

        menu.open(p);
    }

    @ParametersAreNonnullByDefault
    private static void renderEntry(ChestMenu menu, int slot, Player p, FileConfiguration cfg, String path, int page, String key, String full) {
        LocalizationService locale = Slimefun.getLocalization();

        if (cfg.isConfigurationSection(full)) {
            menu.addItem(slot, CustomItemStack.create(MaterialCompat.stack(XMaterial.BOOKSHELF), "&b" + key,
                locale.getMessages(p, "guide.config.section-lore").toArray(new String[0])));
            menu.addMenuClickHandler(slot, (pl, s, item, action) -> {
                open(pl, full, 0);
                return false;
            });
        } else if (cfg.isBoolean(full)) {
            boolean value = cfg.getBoolean(full);
            menu.addItem(slot, CustomItemStack.create(MaterialCompat.stack(value ? XMaterial.LIME_DYE : XMaterial.GRAY_DYE), "&e" + key,
                lore(p, "guide.config.value-toggle", value ? "&atrue" : "&cfalse")));
            menu.addMenuClickHandler(slot, (pl, s, item, action) -> {
                setAndSave(full, !value);
                open(pl, path, page);
                return false;
            });
        } else if (cfg.isList(full)) {
            List<String> lore = new ArrayList<>(locale.getMessages(p, "guide.config.value-list"));
            for (Object entry : cfg.getList(full)) {
                lore.add("&8- &7" + entry);
            }
            menu.addItem(slot, CustomItemStack.create(MaterialCompat.stack(XMaterial.PAPER), "&e" + key, lore.toArray(new String[0])));
            menu.addMenuClickHandler(slot, ChestMenuUtils.getEmptyClickHandler());
        } else {
            menu.addItem(slot, CustomItemStack.create(MaterialCompat.stack(XMaterial.NAME_TAG), "&e" + key,
                lore(p, "guide.config.value-edit", "&f" + cfg.get(full))));
            menu.addMenuClickHandler(slot, (pl, s, item, action) -> {
                promptEdit(pl, path, page, full);
                return false;
            });
        }
    }

    @ParametersAreNonnullByDefault
    private static void promptEdit(Player p, String path, int page, String full) {
        LocalizationService locale = Slimefun.getLocalization();
        FileConfiguration cfg = Slimefun.getCfg().getConfiguration();
        p.closeInventory();
        locale.sendMessage(p, "guide.config.prompt", true);

        boolean expectInt = cfg.isInt(full);
        boolean expectLong = cfg.isLong(full);
        boolean expectDouble = cfg.isDouble(full);

        ChatInput.waitForPlayer(Slimefun.instance(), p, input -> {
            String trimmed = input.trim();
            if (!trimmed.equalsIgnoreCase("cancel")) {
                Object parsed = parse(trimmed, expectInt, expectLong, expectDouble);
                if (parsed == null) {
                    locale.sendMessage(p, "guide.config.invalid", true);
                } else {
                    setAndSave(full, parsed);
                    locale.sendMessage(p, "guide.config.saved", true);
                }
            }
            Slimefun.runSync(() -> open(p, path, page));
        });
    }

    private static Object parse(@Nonnull String input, boolean expectInt, boolean expectLong, boolean expectDouble) {
        try {
            if (expectInt) {
                return Integer.parseInt(input);
            }
            if (expectLong) {
                return Long.parseLong(input);
            }
            if (expectDouble) {
                return Double.parseDouble(input);
            }
        } catch (NumberFormatException e) {
            return null;
        }
        return input;
    }

    private static void setAndSave(@Nonnull String path, @Nonnull Object value) {
        Slimefun.getCfg().setValue(path, value);
        Slimefun.getCfg().save();
    }

    private static String parent(@Nonnull String path) {
        int dot = path.lastIndexOf('.');
        return dot < 0 ? "" : path.substring(0, dot);
    }

    @ParametersAreNonnullByDefault
    private static String[] lore(Player p, String key, String value) {
        List<String> lines = new ArrayList<>();
        for (String line : Slimefun.getLocalization().getMessages(p, key)) {
            lines.add(line.replace("%value%", value));
        }
        return lines.toArray(new String[0]);
    }
}
