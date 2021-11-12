package org.ensimag.myplugin.chestsorting;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.block.state.ChestMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import be.seeseemelk.mockbukkit.inventory.ChestInventoryMock;
import be.seeseemelk.mockbukkit.inventory.InventoryMock;
import be.seeseemelk.mockbukkit.inventory.meta.ItemMetaMock;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.ensimag.myplugin.PluginMain;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ChestSortingListenerTest {

    private ServerMock server;
    private PluginMain plugin;

    private PlayerMock player;
    private SortingCommand sortingCommand;
    private final ChestSortingListener sortingListener = ChestSortingListener.getInstance();

    @BeforeEach
    public void setUp() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(PluginMain.class);

        player = server.addPlayer("Dummy");
        sortingCommand = SortingCommand.getInstance();
    }

    @AfterEach
    public void tearDown() {
        SortingCommand.deleteInstance();
        MockBukkit.unmock();
    }


    // Tests for OnInventoryClick

    /*
    @Test
    @DisplayName("OnInventoryClick player not in the map")
    void testOnInventoryClickPlayerNotInTheMap() {
        Chest chest = new ChestMock(Material.OAK_LOG);  // Material does not seem to have any meaning
        Inventory inventory = chest.getInventory();
        inventory.setItem(6, new ItemStack(Material.OAK_LOG, 5));
        InventoryOpenEvent event = new InventoryOpenEvent(Objects.requireNonNull(player.openInventory(inventory)));
        System.out.println(Arrays.toString(inventory.getContents()));
        sortingListener.onInventoryClick(event);
        System.out.println(Arrays.toString(inventory.getContents()));
    }*/
    
    // Tests for compressInventory

    @Test
    @DisplayName("compressInventory Empty inventory")
    void testCompressInventoryEmptyInventory() {
        Inventory inventory = server.createInventory(null, InventoryType.CHEST, "Inventory", 27);
        sortingListener.compressInventory(inventory);
        Inventory expected = server.createInventory(null, InventoryType.CHEST, "Expected", 27);
        Assertions.assertEquals(Arrays.toString(expected.getContents()), Arrays.toString(inventory.getContents()));
    }

    @Test
    @DisplayName("compressInventory One stack of each")
    void testCompressInventoryOneStackOfEach() {
        Inventory inventory = server.createInventory(null, InventoryType.CHEST, "Inventory", 27);
        inventory.setItem(0, new ItemStack(Material.OAK_LOG, 16));
        inventory.setItem(1, new ItemStack(Material.REDSTONE, 7));
        inventory.setItem(2, new ItemStack(Material.STONE_BRICK_SLAB, 64));
        sortingListener.compressInventory(inventory);
        Inventory expected = server.createInventory(null, InventoryType.CHEST, "Expected", 27);
        expected.setItem(0, new ItemStack(Material.OAK_LOG, 16));
        expected.setItem(1, new ItemStack(Material.REDSTONE, 7));
        expected.setItem(2, new ItemStack(Material.STONE_BRICK_SLAB, 64));
        Assertions.assertEquals(Arrays.toString(expected.getContents()), Arrays.toString(inventory.getContents()));
    }

    @Test
    @DisplayName("compressInventory Several stacks of each")
    void testCompressInventorySeveralStacksOfEach() {
        Inventory inventory = server.createInventory(null, InventoryType.CHEST, "Inventory", 27);
        inventory.setItem(0, new ItemStack(Material.OAK_LOG, 16));
        inventory.setItem(1, new ItemStack(Material.OAK_LOG, 20));
        inventory.setItem(2, new ItemStack(Material.REDSTONE, 7));
        inventory.setItem(3, new ItemStack(Material.REDSTONE, 50));
        inventory.setItem(4, new ItemStack(Material.REDSTONE, 3));
        inventory.setItem(5, new ItemStack(Material.STONE_BRICK_SLAB, 63));
        inventory.setItem(6, new ItemStack(Material.STONE_BRICK_SLAB, 1));
        sortingListener.compressInventory(inventory);
        Inventory expected = server.createInventory(null, InventoryType.CHEST, "Expected", 27);
        expected.setItem(0, new ItemStack(Material.OAK_LOG, 36));
        expected.setItem(1, new ItemStack(Material.REDSTONE, 60));
        expected.setItem(2, new ItemStack(Material.STONE_BRICK_SLAB, 64));
        Assertions.assertEquals(Arrays.toString(expected.getContents()), Arrays.toString(inventory.getContents()));
    }

    @Test
    @DisplayName("compressInventory More than one stack")
    void testCompressInventoryMoreThanOneStack() {
        Inventory inventory = server.createInventory(null, InventoryType.CHEST, "Inventory", 27);
        inventory.setItem(0, new ItemStack(Material.HONEY_BOTTLE, 10));
        inventory.setItem(1, new ItemStack(Material.HONEY_BOTTLE, 10));
        inventory.setItem(2, new ItemStack(Material.REDSTONE, 27));
        inventory.setItem(3, new ItemStack(Material.REDSTONE, 50));
        inventory.setItem(4, new ItemStack(Material.REDSTONE, 63));
        inventory.setItem(5, new ItemStack(Material.STONE_BRICK_SLAB, 63));
        inventory.setItem(6, new ItemStack(Material.STONE_BRICK_SLAB, 1));
        inventory.setItem(7, new ItemStack(Material.STONE_BRICK_SLAB, 1));
        sortingListener.compressInventory(inventory);
        Inventory expected = server.createInventory(null, InventoryType.CHEST, "Expected", 27);
        expected.setItem(0, new ItemStack(Material.HONEY_BOTTLE, 16));
        expected.setItem(1, new ItemStack(Material.HONEY_BOTTLE, 4));
        expected.setItem(2, new ItemStack(Material.REDSTONE, 64));
        expected.setItem(3, new ItemStack(Material.REDSTONE, 64));
        expected.setItem(4, new ItemStack(Material.REDSTONE, 12));
        expected.setItem(5, new ItemStack(Material.STONE_BRICK_SLAB, 64));
        expected.setItem(6, new ItemStack(Material.STONE_BRICK_SLAB, 1));
        Assertions.assertEquals(Arrays.toString(expected.getContents()), Arrays.toString(inventory.getContents()));
    }

    @Test
    @DisplayName("compressInventory Renamed items not treated")
    void testcompressInventoryRenamedItemsNotTreated() {
        Inventory inventory = server.createInventory(null, InventoryType.CHEST, "Inventory", 27);
        inventory.setItem(0, new ItemStack(Material.OAK_LOG, 16));
        inventory.setItem(1, new ItemStack(Material.REDSTONE, 7));
        ItemStack redstone = new ItemStack(Material.REDSTONE, 63);
        ItemMeta itemMeta = new ItemMetaMock();
        itemMeta.setDisplayName("customName");
        redstone.setItemMeta(itemMeta);
        ItemStack redstone2 = new ItemStack(Material.REDSTONE, 1);
        redstone2.setItemMeta(itemMeta);
        inventory.setItem(2, redstone);
        inventory.setItem(3, redstone2);
        inventory.setItem(4, new ItemStack(Material.STONE_BRICK_SLAB, 64));
        sortingListener.compressInventory(inventory);
        inventory.getContents()[1].setItemMeta(null);
        inventory.getContents()[2].setItemMeta(null);
        Inventory expected = server.createInventory(null, InventoryType.CHEST, "Expected", 27);
        expected.setItem(0, new ItemStack(Material.OAK_LOG, 16));
        expected.setItem(1, new ItemStack(Material.REDSTONE, 63));
        expected.setItem(2, new ItemStack(Material.REDSTONE, 1));
        expected.setItem(3, new ItemStack(Material.REDSTONE, 7));
        expected.setItem(4, new ItemStack(Material.STONE_BRICK_SLAB, 64));
        Assertions.assertEquals(Arrays.toString(expected.getContents()), Arrays.toString(inventory.getContents()));
    }

    // Tests for moveFirstItemStack

    @Test
    @DisplayName("moveFirstItemStack")
    void testmoveFirstItemStack() {
        List<ItemStack> src = new ArrayList<>();
        List<ItemStack> dest = new ArrayList<>();

        ItemStack first = new ItemStack(Material.REDSTONE, 63);
        src.add(first);
        sortingListener.moveFirstItemStack(src, dest);
        Assertions.assertEquals(first, dest.get(0));
        Assertions.assertEquals(0, src.size());
    }

}
