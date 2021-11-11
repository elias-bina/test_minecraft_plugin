package org.ensimag.myplugin.chestsorting;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SortByNameTest {

    private SortByName sort = new SortByName();

    @Test
    @DisplayName("Same item")
    void testSameItem() {
        ItemStack a = new ItemStack(Material.OAK_LOG);
        ItemStack b = new ItemStack(Material.OAK_LOG);
        Assertions.assertEquals(0, sort.compare(a, b));
    }

    @Test
    @DisplayName("Same item different size")
    void testSameItemDifferentSize() {
        ItemStack a = new ItemStack(Material.OAK_LOG, 5);
        ItemStack b = new ItemStack(Material.OAK_LOG);
        Assertions.assertEquals(0, sort.compare(a, b));
    }

    @Test
    @DisplayName("First before second")
    void testFirstBeforeSecond() {
        ItemStack a = new ItemStack(Material.OAK_LOG);
        ItemStack b = new ItemStack(Material.OBSIDIAN);
        Assertions.assertTrue(sort.compare(a, b) < 0);
    }

    @Test
    @DisplayName("Second before first")
    void testSecondBeforeFirst() {
        ItemStack a = new ItemStack(Material.OBSIDIAN);
        ItemStack b = new ItemStack(Material.OAK_LOG);
        Assertions.assertTrue(sort.compare(a, b) > 0);
    }

    @Test
    @DisplayName("First is null")
    void testFirstIsNull() {
        ItemStack b = new ItemStack(Material.OAK_LOG);
        Assertions.assertTrue(sort.compare(null, b) > 0);
    }

    @Test
    @DisplayName("Second is null")
    void testSecondIsNull() {
        ItemStack a = new ItemStack(Material.OAK_LOG);
        Assertions.assertTrue(sort.compare(a, null) < 0);
    }

    @Test
    @DisplayName("Both are null")
    void testBothAreNull() {
        Assertions.assertEquals(0, sort.compare(null, null));
    }
}
