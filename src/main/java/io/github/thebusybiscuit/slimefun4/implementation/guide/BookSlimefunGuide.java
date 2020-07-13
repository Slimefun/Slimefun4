package io.github.thebusybiscuit.slimefun4.implementation.guide;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.chat.ChatColors;
import io.github.thebusybiscuit.cscorelib2.chat.ChatInput;
import io.github.thebusybiscuit.cscorelib2.chat.json.ChatComponent;
import io.github.thebusybiscuit.cscorelib2.chat.json.ClickEvent;
import io.github.thebusybiscuit.cscorelib2.chat.json.CustomBookInterface;
import io.github.thebusybiscuit.cscorelib2.chat.json.HoverEvent;
import io.github.thebusybiscuit.cscorelib2.inventory.ItemUtils;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.core.categories.FlexCategory;
import io.github.thebusybiscuit.slimefun4.core.categories.LockedCategory;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuide;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuideImplementation;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuideLayout;
import io.github.thebusybiscuit.slimefun4.core.researching.Research;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.utils.ChatUtils;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.Slimefun;

public class BookSlimefunGuide implements SlimefunGuideImplementation {

    private final NamespacedKey guideSearch = new NamespacedKey(SlimefunPlugin.instance(), "search");

    @Override
    public SlimefunGuideLayout getLayout() {
        return SlimefunGuideLayout.BOOK;
    }

    @Override
    public boolean isSurvivalMode() {
        return true;
    }

    @Override
    public ItemStack getItem() {
        return new CustomItem(new ItemStack(Material.ENCHANTED_BOOK), "&aSlimefun Guide &7(Book GUI)", "", "&eRight Click &8\u21E8 &7Browse Items", "&eShift + Right Click &8\u21E8 &7Open Settings / Credits");
    }

    private void openBook(Player p, PlayerProfile profile, List<ChatComponent> lines, boolean backButton) {
        CustomBookInterface book = new CustomBookInterface(SlimefunPlugin.instance());
        book.setTitle(SlimefunPlugin.getLocalization().getMessage(p, "guide.title.main"));

        for (int i = 0; i < lines.size(); i = i + 10) {
            ChatComponent page = new ChatComponent("");
            ChatComponent header = new ChatComponent(ChatColors.color("&b&l- " + SlimefunPlugin.getLocalization().getMessage(p, "guide.title.main") + " -\n\n"));
            header.setHoverEvent(new HoverEvent(ChestMenuUtils.getSearchButton(p)));

            header.setClickEvent(new ClickEvent(guideSearch, player -> Slimefun.runSync(() -> {
                SlimefunPlugin.getLocalization().sendMessage(player, "guide.search.message");
                ChatInput.waitForPlayer(SlimefunPlugin.instance(), player, msg -> SlimefunGuide.openSearch(profile, msg, true, true));
            }, 1)));

            page.append(header);

            for (int j = i; j < lines.size() && j < i + 10; j++) {
                page.append(lines.get(j));
            }

            page.append(new ChatComponent("\n"));

            if (backButton) {
                ChatComponent button = new ChatComponent(ChatColor.DARK_BLUE + "\u21E6 " + SlimefunPlugin.getLocalization().getMessage(p, "guide.back.title"));
                button.setHoverEvent(new HoverEvent(ChatColor.DARK_BLUE + "\u21E6 " + SlimefunPlugin.getLocalization().getMessage(p, "guide.back.title"), "", ChatColor.GRAY + SlimefunPlugin.getLocalization().getMessage(p, "guide.back.guide")));
                button.setClickEvent(new ClickEvent(new NamespacedKey(SlimefunPlugin.instance(), "slimefun_guide"), pl -> openMainMenu(profile, 1)));
                page.append(button);
            }

            book.addPage(page);
        }

        book.open(p);
    }

    @Override
    public void openMainMenu(PlayerProfile profile, int page) {
        Player p = profile.getPlayer();

        if (p == null) {
            return;
        }

        List<ChatComponent> lines = new LinkedList<>();
        int tier = 0;

        for (Category category : SlimefunPlugin.getRegistry().getCategories()) {
            if (!category.isHidden(p) && (!(category instanceof FlexCategory) || ((FlexCategory) category).isVisible(p, profile, getLayout()))) {
                if (tier < category.getTier()) {
                    tier = category.getTier();

                    if (tier > 1) {
                        for (int i = 0; i < 10 && lines.size() % 10 != 0; i++) {
                            lines.add(new ChatComponent("\n"));
                        }
                    }

                    lines.add(new ChatComponent(ChatColor.DARK_GRAY + "\u21E8" + ChatColor.DARK_BLUE + " Tier " + tier + "\n"));
                }

                if (category instanceof LockedCategory && !((LockedCategory) category).hasUnlocked(p, profile)) {
                    List<String> lore = new LinkedList<>();
                    lore.add(ChatColor.DARK_RED + SlimefunPlugin.getLocalization().getMessage(p, "guide.locked") + " " + ChatColor.GRAY + "- " + ChatColor.RESET + category.getItem(p).getItemMeta().getDisplayName());
                    lore.add("");

                    for (String line : SlimefunPlugin.getLocalization().getMessages(p, "guide.locked-category")) {
                        lore.add(ChatColor.RESET + line);
                    }

                    lore.add("");

                    for (Category parent : ((LockedCategory) category).getParents()) {
                        lore.add(parent.getItem(p).getItemMeta().getDisplayName());
                    }

                    ChatComponent chatComponent = new ChatComponent(ChatUtils.crop(ChatColor.RED, ItemUtils.getItemName(category.getItem(p))) + "\n");
                    chatComponent.setHoverEvent(new HoverEvent(lore));
                    lines.add(chatComponent);
                }
                else {
                    ChatComponent chatComponent = new ChatComponent(ChatUtils.crop(ChatColor.DARK_GREEN, ItemUtils.getItemName(category.getItem(p))) + "\n");
                    chatComponent.setHoverEvent(new HoverEvent(ItemUtils.getItemName(category.getItem(p)), "", ChatColor.GRAY + "\u21E8 " + ChatColor.GREEN + SlimefunPlugin.getLocalization().getMessage(p, "guide.tooltips.open-category")));
                    chatComponent.setClickEvent(new ClickEvent(category.getKey(), pl -> openCategory(profile, category, 1)));
                    lines.add(chatComponent);
                }
            }
        }

        openBook(p, profile, lines, false);
    }

    @Override
    public void openCategory(PlayerProfile profile, Category category, int page) {
        Player p = profile.getPlayer();

        if (p == null) {
            return;
        }

        if (category instanceof FlexCategory) {
            ((FlexCategory) category).open(p, profile, getLayout());
        }
        else if (category.getItems().size() < 250) {
            profile.getGuideHistory().add(category, page);

            List<ChatComponent> items = new LinkedList<>();

            for (SlimefunItem item : category.getItems()) {
                if (Slimefun.hasPermission(p, item, false)) {
                    if (Slimefun.isEnabled(p, item, false)) {
                        appendSlimefunItem(category, page, p, profile, item, items);
                    }
                }
                else {
                    ChatComponent component = new ChatComponent(ChatUtils.crop(ChatColor.DARK_RED, ItemUtils.getItemName(item.getItem())) + "\n");

                    List<String> lore = new ArrayList<>();
                    lore.add(ChatColor.DARK_RED + ChatColor.stripColor(ItemUtils.getItemName(item.getItem())));
                    lore.add("");

                    for (String line : SlimefunPlugin.getPermissionsService().getLore(item)) {
                        lore.add(ChatColors.color(line));
                    }

                    component.setHoverEvent(new HoverEvent(lore));
                    items.add(component);
                }
            }

            openBook(p, profile, items, true);
        }
        else {
            p.sendMessage(ChatColor.RED + "That Category is too big to open :/");
        }
    }

    private void appendSlimefunItem(Category category, int page, Player p, PlayerProfile profile, SlimefunItem item, List<ChatComponent> items) {
        NamespacedKey key = new NamespacedKey(SlimefunPlugin.instance(), item.getID().toLowerCase(Locale.ROOT));

        if (!Slimefun.hasUnlocked(p, item, false) && item.getResearch() != null) {
            Research research = item.getResearch();

            ChatComponent component = new ChatComponent(ChatUtils.crop(ChatColor.RED, item.getItemName()) + "\n");
            component.setHoverEvent(new HoverEvent(ChatColor.RESET + item.getItemName(), ChatColor.DARK_RED.toString() + ChatColor.BOLD + SlimefunPlugin.getLocalization().getMessage(p, "guide.locked"), "", ChatColor.GREEN + "> Click to unlock", "", ChatColor.GRAY + "Cost: " + ChatColor.AQUA.toString() + research.getCost() + " Level(s)"));
            component.setClickEvent(new ClickEvent(key, player -> Slimefun.runSync(() -> {
                if (!SlimefunPlugin.getRegistry().getCurrentlyResearchingPlayers().contains(p.getUniqueId())) {
                    if (research.canUnlock(p)) {
                        if (profile.hasUnlocked(research)) {
                            openCategory(profile, category, page);
                        }
                        else {
                            unlockItem(p, item, pl -> openCategory(profile, category, page));
                        }
                    }
                    else {
                        SlimefunPlugin.getLocalization().sendMessage(p, "messages.not-enough-xp", true);
                    }
                }
            })));

            items.add(component);
        }
        else {
            ChatComponent component = new ChatComponent(ChatUtils.crop(ChatColor.DARK_GREEN, item.getItemName()) + "\n");

            List<String> lore = new ArrayList<>();
            lore.add(item.getItemName());

            if (item.getItem().hasItemMeta() && item.getItem().getItemMeta().hasLore()) {
                lore.addAll(item.getItem().getItemMeta().getLore());
            }

            component.setHoverEvent(new HoverEvent(lore));
            component.setClickEvent(new ClickEvent(key, player -> Slimefun.runSync(() -> displayItem(profile, item, true))));
            items.add(component);
        }
    }

    @Override
    public void openSearch(PlayerProfile profile, String input, boolean addToHistory) {
        // We need to write a book implementation for this at some point
        SlimefunGuide.openSearch(profile, input, true, addToHistory);
    }

    @Override
    public void displayItem(PlayerProfile profile, ItemStack item, int index, boolean addToHistory) {
        SlimefunGuide.displayItem(profile, item, addToHistory);
    }

    @Override
    public void displayItem(PlayerProfile profile, SlimefunItem item, boolean addToHistory) {
        SlimefunGuide.displayItem(profile, item, addToHistory);
    }

}
