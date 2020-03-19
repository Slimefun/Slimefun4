package io.github.thebusybiscuit.slimefun4.core.guide;

import java.util.Deque;
import java.util.LinkedList;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

/**
 * {@link GuideHistory} represents the browsing history of a {@link Player} through the
 * {@link SlimefunGuide}.
 * 
 * @author TheBusyBiscuit
 * 
 * @see SlimefunGuide
 * @see PlayerProfile
 *
 */
public class GuideHistory {

    private final PlayerProfile profile;
    private final Deque<GuideEntry<?>> queue = new LinkedList<>();

    public GuideHistory(PlayerProfile profile) {
        this.profile = profile;
    }

    public void clear() {
        queue.clear();
    }

    public void add(Category category, int page) {
        refresh(category, page);
    }

    public void add(ItemStack item, int page) {
        refresh(item, page);
    }

    public void add(SlimefunItem item) {
        queue.add(new GuideEntry<>(item, 0));
    }

    public void add(String searchTerm) {
        queue.add(new GuideEntry<>(searchTerm, 0));
    }

    private <T> void refresh(T object, int page) {
        GuideEntry<?> lastEntry = getLastEntry(false);

        if (lastEntry != null && lastEntry.getIndexedObject() == object) {
            lastEntry.setPage(page);
        }
        else {
            queue.add(new GuideEntry<>(object, page));
        }
    }

    public int size() {
        return queue.size();
    }

    /**
     * Retrieves the last page in the {@link SlimefunGuide} that was visited by a {@link Player}.
     * Optionally also rewinds the history back to that entry.
     * 
     * @param remove
     *            Whether to remove the current entry so it moves back to the entry returned.
     * @return The last Guide Entry that was saved to the given Players guide history.
     */
    private GuideEntry<?> getLastEntry(boolean remove) {
        if (remove && !queue.isEmpty()) {
            queue.removeLast();
        }

        return queue.isEmpty() ? null : queue.getLast();
    }

    public void openLastEntry(SlimefunGuideImplementation guide, boolean survival) {
        GuideEntry<?> entry = getLastEntry(false);
        open(guide, entry, survival);
    }

    public void goBack(SlimefunGuideImplementation guide, boolean survival) {
        GuideEntry<?> entry = getLastEntry(true);
        open(guide, entry, survival);
    }

    private <T> void open(SlimefunGuideImplementation guide, GuideEntry<T> entry, boolean survival) {
        if (entry == null) {
            guide.openMainMenu(profile, survival, 1);
        }
        else if (entry.getIndexedObject() instanceof Category) {
            guide.openCategory(profile, (Category) entry.getIndexedObject(), survival, entry.getPage());
        }
        else if (entry.getIndexedObject() instanceof SlimefunItem) {
            guide.displayItem(profile, (SlimefunItem) entry.getIndexedObject(), false);
        }
        else if (entry.getIndexedObject() instanceof String) {
            guide.openSearch(profile, (String) entry.getIndexedObject(), survival, false);
        }
        else if (entry.getIndexedObject() instanceof ItemStack) {
            guide.displayItem(profile, (ItemStack) entry.getIndexedObject(), entry.getPage(), false);
        }
    }

}
