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
    void testCompressInventoryRenamedItemsNotTreated() {
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

    @Test
    @DisplayName("compressInventory Chest is full")
    void testCompressInventoryChestIsFull() {
        Inventory inventory = server.createInventory(null, InventoryType.CHEST, "Inventory", 9);
        inventory.setItem(0, new ItemStack(Material.OAK_LOG, 64));
        inventory.setItem(1, new ItemStack(Material.OAK_LOG, 64));
        inventory.setItem(2, new ItemStack(Material.OAK_LOG, 42));
        inventory.setItem(3, new ItemStack(Material.STONE, 64));
        inventory.setItem(4, new ItemStack(Material.STONE, 64));
        inventory.setItem(5, new ItemStack(Material.STONE, 12));
        inventory.setItem(6, new ItemStack(Material.DIAMOND, 64));
        inventory.setItem(7, new ItemStack(Material.DIAMOND, 64));
        inventory.setItem(8, new ItemStack(Material.DIAMOND, 9));

        Inventory expected = server.createInventory(null, InventoryType.CHEST, "Expected", 9);
        expected.setContents(inventory.getContents());

        sortingListener.compressInventory(inventory);
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
    
    // Tests for groupItems

    @Test
    @DisplayName("groupItem empty list")
    void testgroupItemEmptyList() {
        List<ItemStack> src = new ArrayList<>();
        List<List<ItemStack>> actual = sortingListener.groupItems(src);
        Assertions.assertEquals(0, actual.size());
    }

    @Test
    @DisplayName("groupItem several stacks")
    void testgroupItemSeveralStacks() {
        List<ItemStack> src = new ArrayList<>();
        src.add(new ItemStack(Material.OAK_LOG, 52));
        src.add(new ItemStack(Material.REDSTONE, 40));
        src.add(new ItemStack(Material.STONE_BRICK_SLAB, 64));
        src.add(new ItemStack(Material.REDSTONE, 2));
        src.add(new ItemStack(Material.STONE_BRICK_SLAB, 28));
        src.add(new ItemStack(Material.STONE_BRICK_SLAB, 12));

        List<List<ItemStack>> actual = sortingListener.groupItems(src);

        List<List<ItemStack>> expected = new ArrayList<>();
        List<ItemStack> slab = new ArrayList<>();
        slab.add(new ItemStack(Material.STONE_BRICK_SLAB, 64));
        slab.add(new ItemStack(Material.STONE_BRICK_SLAB, 28));
        slab.add(new ItemStack(Material.STONE_BRICK_SLAB, 12));
        expected.add(slab);
        List<ItemStack> redstone = new ArrayList<>();
        redstone.add(new ItemStack(Material.REDSTONE, 40));
        redstone.add(new ItemStack(Material.REDSTONE, 2));
        expected.add(redstone);
        List<ItemStack> oak = new ArrayList<>();
        oak.add(new ItemStack(Material.OAK_LOG, 52));
        expected.add(oak);
        Assertions.assertEquals(expected, actual);
    }

    // Tests for smartSort

    @Test
    @DisplayName("smartSort More types than rows")
    void testSmartSortMoreTypesThanRows() {
        List<List<ItemStack>> itemLists = new ArrayList<>();
        List<ItemStack> oak = new ArrayList<>();
        oak.add(new ItemStack(Material.OAK_LOG, 64));
        oak.add(new ItemStack(Material.OAK_LOG, 64));
        oak.add(new ItemStack(Material.OAK_LOG, 64));
        oak.add(new ItemStack(Material.OAK_LOG, 52));
        itemLists.add(oak);
        List<ItemStack> redstone = new ArrayList<>();
        redstone.add(new ItemStack(Material.REDSTONE, 64));
        redstone.add(new ItemStack(Material.REDSTONE, 64));
        redstone.add(new ItemStack(Material.REDSTONE, 64));
        redstone.add(new ItemStack(Material.REDSTONE, 52));
        itemLists.add(redstone);
        List<ItemStack> stone = new ArrayList<>();
        stone.add(new ItemStack(Material.STONE, 64));
        stone.add(new ItemStack(Material.STONE, 64));
        stone.add(new ItemStack(Material.STONE, 64));
        stone.add(new ItemStack(Material.STONE, 52));
        itemLists.add(stone);
        List<ItemStack> sand = new ArrayList<>();
        sand.add(new ItemStack(Material.SAND, 64));
        sand.add(new ItemStack(Material.SAND, 64));
        sand.add(new ItemStack(Material.SAND, 64));
        sand.add(new ItemStack(Material.SAND, 52));
        itemLists.add(sand);
        List<ItemStack> honey_bottle = new ArrayList<>();
        honey_bottle.add(new ItemStack(Material.HONEY_BOTTLE, 16));
        honey_bottle.add(new ItemStack(Material.HONEY_BOTTLE, 16));
        honey_bottle.add(new ItemStack(Material.HONEY_BOTTLE, 16));
        honey_bottle.add(new ItemStack(Material.HONEY_BOTTLE, 10));
        itemLists.add(honey_bottle);
        List<ItemStack> piston = new ArrayList<>();
        piston.add(new ItemStack(Material.PISTON, 64));
        piston.add(new ItemStack(Material.PISTON, 64));
        piston.add(new ItemStack(Material.PISTON, 64));
        piston.add(new ItemStack(Material.PISTON, 52));
        itemLists.add(piston);
        List<ItemStack> lever = new ArrayList<>();
        lever.add(new ItemStack(Material.LEVER, 64));
        lever.add(new ItemStack(Material.LEVER, 64));
        lever.add(new ItemStack(Material.LEVER, 52));
        itemLists.add(lever);
        List<ItemStack> actual = sortingListener.smartSort(itemLists, 27);

        List<ItemStack> expected = new ArrayList<>();
        expected.add(new ItemStack(Material.OAK_LOG, 64));
        expected.add(new ItemStack(Material.OAK_LOG, 64));
        expected.add(new ItemStack(Material.OAK_LOG, 64));
        expected.add(new ItemStack(Material.OAK_LOG, 52));
        expected.add(new ItemStack(Material.REDSTONE, 64));
        expected.add(new ItemStack(Material.REDSTONE, 64));
        expected.add(new ItemStack(Material.REDSTONE, 64));
        expected.add(new ItemStack(Material.REDSTONE, 52));
        expected.add(new ItemStack(Material.LEVER, 64));

        expected.add(new ItemStack(Material.STONE, 64));
        expected.add(new ItemStack(Material.STONE, 64));
        expected.add(new ItemStack(Material.STONE, 64));
        expected.add(new ItemStack(Material.STONE, 52));
        expected.add(new ItemStack(Material.SAND, 64));
        expected.add(new ItemStack(Material.SAND, 64));
        expected.add(new ItemStack(Material.SAND, 64));
        expected.add(new ItemStack(Material.SAND, 52));
        expected.add(new ItemStack(Material.LEVER, 64));

        expected.add(new ItemStack(Material.HONEY_BOTTLE, 16));
        expected.add(new ItemStack(Material.HONEY_BOTTLE, 16));
        expected.add(new ItemStack(Material.HONEY_BOTTLE, 16));
        expected.add(new ItemStack(Material.HONEY_BOTTLE, 10));
        expected.add(new ItemStack(Material.PISTON, 64));
        expected.add(new ItemStack(Material.PISTON, 64));
        expected.add(new ItemStack(Material.PISTON, 64));
        expected.add(new ItemStack(Material.PISTON, 52));
        expected.add(new ItemStack(Material.LEVER, 52));

        Assertions.assertEquals(expected, actual);
    }


    @Test
    @DisplayName("smartSort giant lists")
    void testSmartSortGiantLists() {
        List<List<ItemStack>> itemLists = new ArrayList<>();
        
        List<ItemStack> oak = new ArrayList<>();
        for(int i = 0; i < 12; i++) oak.add(new ItemStack(Material.OAK_LOG, 64));
        oak.add(new ItemStack(Material.OAK_LOG, 8));
        itemLists.add(oak);

        List<ItemStack> redstone = new ArrayList<>();
        for(int i = 0; i < 9; i++) redstone.add(new ItemStack(Material.REDSTONE, 64));
        redstone.add(new ItemStack(Material.REDSTONE, 64));
        redstone.add(new ItemStack(Material.REDSTONE, 64));
        redstone.add(new ItemStack(Material.REDSTONE, 24));
        itemLists.add(redstone);


        List<ItemStack> stone = new ArrayList<>();
        stone.add(new ItemStack(Material.STONE, 64));
        stone.add(new ItemStack(Material.STONE, 64));
        stone.add(new ItemStack(Material.STONE, 64));
        stone.add(new ItemStack(Material.STONE, 52));
        itemLists.add(stone);
        List<ItemStack> sand = new ArrayList<>();
        sand.add(new ItemStack(Material.SAND, 64));
        sand.add(new ItemStack(Material.SAND, 64));
        sand.add(new ItemStack(Material.SAND, 64));
        sand.add(new ItemStack(Material.SAND, 52));
        itemLists.add(sand);
        List<ItemStack> honey_bottle = new ArrayList<>();
        honey_bottle.add(new ItemStack(Material.HONEY_BOTTLE, 16));
        honey_bottle.add(new ItemStack(Material.HONEY_BOTTLE, 16));
        honey_bottle.add(new ItemStack(Material.HONEY_BOTTLE, 16));
        honey_bottle.add(new ItemStack(Material.HONEY_BOTTLE, 10));
        itemLists.add(honey_bottle);
        List<ItemStack> piston = new ArrayList<>();
        piston.add(new ItemStack(Material.PISTON, 64));
        piston.add(new ItemStack(Material.PISTON, 64));
        piston.add(new ItemStack(Material.PISTON, 64));
        piston.add(new ItemStack(Material.PISTON, 52));
        itemLists.add(piston);
        List<ItemStack> lever = new ArrayList<>();
        lever.add(new ItemStack(Material.LEVER, 64));
        lever.add(new ItemStack(Material.LEVER, 64));
        lever.add(new ItemStack(Material.LEVER, 52));
        itemLists.add(lever);
        List<ItemStack> actual = sortingListener.smartSort(itemLists, 27);

        List<ItemStack> expected = new ArrayList<>();
        expected.add(new ItemStack(Material.OAK_LOG, 64));
        expected.add(new ItemStack(Material.OAK_LOG, 64));
        expected.add(new ItemStack(Material.OAK_LOG, 64));
        expected.add(new ItemStack(Material.OAK_LOG, 52));
        expected.add(new ItemStack(Material.REDSTONE, 64));
        expected.add(new ItemStack(Material.REDSTONE, 64));
        expected.add(new ItemStack(Material.REDSTONE, 64));
        expected.add(new ItemStack(Material.REDSTONE, 52));
        expected.add(new ItemStack(Material.LEVER, 64));

        expected.add(new ItemStack(Material.STONE, 64));
        expected.add(new ItemStack(Material.STONE, 64));
        expected.add(new ItemStack(Material.STONE, 64));
        expected.add(new ItemStack(Material.STONE, 52));
        expected.add(new ItemStack(Material.SAND, 64));
        expected.add(new ItemStack(Material.SAND, 64));
        expected.add(new ItemStack(Material.SAND, 64));
        expected.add(new ItemStack(Material.SAND, 52));
        expected.add(new ItemStack(Material.LEVER, 64));

        expected.add(new ItemStack(Material.HONEY_BOTTLE, 16));
        expected.add(new ItemStack(Material.HONEY_BOTTLE, 16));
        expected.add(new ItemStack(Material.HONEY_BOTTLE, 16));
        expected.add(new ItemStack(Material.HONEY_BOTTLE, 10));
        expected.add(new ItemStack(Material.PISTON, 64));
        expected.add(new ItemStack(Material.PISTON, 64));
        expected.add(new ItemStack(Material.PISTON, 64));
        expected.add(new ItemStack(Material.PISTON, 52));
        expected.add(new ItemStack(Material.LEVER, 52));

        Assertions.assertEquals(expected, actual);
    }

}
