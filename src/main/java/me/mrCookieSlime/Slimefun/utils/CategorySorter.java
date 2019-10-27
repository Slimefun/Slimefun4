package me.mrCookieSlime.Slimefun.utils;

import java.util.Comparator;

import me.mrCookieSlime.Slimefun.Objects.Category;

public class CategorySorter implements Comparator<Category> {

	/**
	 * @since 4.0
	 */
	@Override
	public int compare(Category c1, Category c2) {
	    return Integer.compare(c1.getTier(), c2.getTier());
	}

}