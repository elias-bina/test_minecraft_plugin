package org.ensimag.myplugin.chestsorting;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class SortByLenTest {

    private SortByLen sort = new SortByLen();

    @Test
    @DisplayName("Both lists are empty")
    void testBothListsAreEmpty() {
        List<ItemStack> a = new ArrayList<>();
        List<ItemStack> b = new ArrayList<>();
        Assertions.assertEquals(0, sort.compare(a, b));
    }

    @Test
    @DisplayName("First list is empty")
    void testFirstListIsEmpty() {
        List<ItemStack> a = new ArrayList<>();
        List<ItemStack> b = new ArrayList<>();
        b.add(new ItemStack(Material.OAK_LOG, 5));
        Assertions.assertTrue(sort.compare(a, b) > 0);
    }

    @Test
    @DisplayName("Second list is empty")
    void testSecondListIsEmpty() {
        List<ItemStack> a = new ArrayList<>();
        a.add(new ItemStack(Material.OAK_LOG, 5));
        List<ItemStack> b = new ArrayList<>();
        Assertions.assertTrue(sort.compare(a, b) < 0);
    }

    @Test
    @DisplayName("Same elements")
    void testSameElements() {
        List<ItemStack> a = new ArrayList<>();
        a.add(new ItemStack(Material.OAK_LOG, 5));
        a.add(new ItemStack(Material.PISTON, 36));
        List<ItemStack> b = new ArrayList<>();
        b.add(new ItemStack(Material.OAK_LOG, 5));
        b.add(new ItemStack(Material.PISTON, 36));
        Assertions.assertEquals(0, sort.compare(a, b));
    }

    @Test
    @DisplayName("More in the first list")
    void testMoreInTheFirstList() {
        List<ItemStack> a = new ArrayList<>();
        a.add(new ItemStack(Material.OAK_LOG, 5));
        a.add(new ItemStack(Material.PISTON, 36));
        a.add(new ItemStack(Material.OAK_LOG, 12));
        List<ItemStack> b = new ArrayList<>();
        b.add(new ItemStack(Material.OAK_LOG, 5));
        b.add(new ItemStack(Material.PISTON, 36));
        Assertions.assertTrue(sort.compare(a, b) < 0);
    }

    @Test
    @DisplayName("More in the second list")
    void testMoreInTheSecondList() {
        List<ItemStack> a = new ArrayList<>();
        a.add(new ItemStack(Material.OAK_LOG, 5));
        a.add(new ItemStack(Material.PISTON, 36));
        List<ItemStack> b = new ArrayList<>();
        b.add(new ItemStack(Material.OAK_LOG, 5));
        b.add(new ItemStack(Material.PISTON, 36));
        b.add(new ItemStack(Material.OAK_LOG, 12));
        Assertions.assertTrue(sort.compare(a, b) > 0);
    }

    @Test
    @DisplayName("First list is null")
    void testFirstListIsNull() {
        List<ItemStack> a = null;
        List<ItemStack> b = new ArrayList<>();
        b.add(new ItemStack(Material.OAK_LOG, 5));;
        Assertions.assertTrue(sort.compare(a, b) > 0);
    }

    @Test
    @DisplayName("Second list is null")
    void testSecondListIsNull() {
        List<ItemStack> a = new ArrayList<>();
        a.add(new ItemStack(Material.OAK_LOG, 5));
        List<ItemStack> b = null;
        Assertions.assertTrue(sort.compare(a, b) < 0);
    }

    @Test
    @DisplayName("Both lists are null")
    void testBothListsAreNull() {
        List<ItemStack> a = null;
        List<ItemStack> b = null;
        Assertions.assertEquals(0, sort.compare(a, b));
    }
}
