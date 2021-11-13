package org.ensimag.myplugin.chestsorting;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import be.seeseemelk.mockbukkit.inventory.meta.ItemMetaMock;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.units.qual.A;
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
        for(int i = 0 ; i < 27 - 6 ; i++) src.add(null);

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
        final int MIN = 1, MAX = 64;

        List<List<ItemStack>> itemLists = new ArrayList<>();

        final int oakNb = MIN + (int)(Math.random() * ((MAX - MIN) + 1));
        itemLists.add(createItemStackList(Material.OAK_LOG, 4, 64, oakNb));

        final int redstoneNb = MIN + (int)(Math.random() * ((MAX - MIN) + 1));
        itemLists.add(createItemStackList(Material.REDSTONE, 4, 64, redstoneNb));

        final int stoneNb = MIN + (int)(Math.random() * ((MAX - MIN) + 1));
        itemLists.add(createItemStackList(Material.STONE, 4, 64, stoneNb));

        final int sandNb = MIN + (int)(Math.random() * ((MAX - MIN) + 1));
        itemLists.add(createItemStackList(Material.SAND, 4, 64, sandNb));

        final int honeyNb = MIN + (int)(Math.random() * ((16 - MIN) + 1));
        itemLists.add(createItemStackList(Material.HONEY_BOTTLE, 4, 16, honeyNb));

        final int pistonNb = MIN + (int)(Math.random() * ((MAX - MIN) + 1));
        itemLists.add(createItemStackList(Material.PISTON, 4, 64, pistonNb));

        final int leverNb = MIN + (int)(Math.random() * ((MAX - MIN) + 1));
        itemLists.add(createItemStackList(Material.LEVER, 3, 64, leverNb));

        List<ItemStack> actual = sortingListener.smartSort(itemLists, 27);

        List<ItemStack> expected = new ArrayList<>();

        // First line
        addItemsStack(expected, Material.OAK_LOG, 4, 64, oakNb);
        addItemsStack(expected, Material.REDSTONE, 4, 64, redstoneNb);
        addItemsStack(expected, Material.LEVER, 1, 64);

        // Second line
        addItemsStack(expected, Material.STONE, 4, 64, stoneNb);
        addItemsStack(expected, Material.SAND, 4, 64, sandNb);
        addItemsStack(expected, Material.LEVER, 1, 64);

        // Third line
        addItemsStack(expected, Material.HONEY_BOTTLE, 4, 16, honeyNb);
        addItemsStack(expected, Material.PISTON, 4, 64, pistonNb);
        addItemsStack(expected, Material.LEVER, 1, 64, leverNb);

        Assertions.assertEquals(expected, actual);
    }


    @Test
    @DisplayName("smartSort giant lists")
    void testSmartSortGiantLists() {
        
        final int MIN = 1, MAX = 64;

        List<List<ItemStack>> itemLists = new ArrayList<>();

        
        final int oakNb = MIN + (int)(Math.random() * ((MAX - MIN) + 1));
        itemLists.add(createItemStackList(Material.OAK_LOG, 15, 64, oakNb));

        final int redstoneNb = MIN + (int)(Math.random() * ((MAX - MIN) + 1));
        itemLists.add(createItemStackList(Material.REDSTONE, 13, 64, redstoneNb));

        final int stoneNb = MIN + (int)(Math.random() * ((MAX - MIN) + 1));
        itemLists.add(createItemStackList(Material.STONE, 12, 64, stoneNb));

        final int sandNb = MIN + (int)(Math.random() * ((MAX - MIN) + 1));
        itemLists.add(createItemStackList(Material.SAND, 10, 64, sandNb));        

        final int honeyBottleNb = MIN + (int)(Math.random() * ((16 - MIN) + 1));
        itemLists.add(createItemStackList(Material.HONEY_BOTTLE, 4, 16, honeyBottleNb));


        List<ItemStack> actual = sortingListener.smartSort(itemLists, 54);

        List<ItemStack> expected = new ArrayList<>();

        // First two lines
        addItemsStack(expected, Material.OAK_LOG, 15, 64, oakNb);
        addItemsStack(expected, Material.SAND, 3, 64);

        // Lines 3 and 4 
        addItemsStack(expected, Material.REDSTONE, 13, 64, redstoneNb);
        addItemsStack(expected, Material.HONEY_BOTTLE, 4, 16, honeyBottleNb);
        addItemsStack(expected, Material.SAND, 1, 64);

        // Last Two lines
        
        addItemsStack(expected, Material.STONE, 12, 64, stoneNb);
        addItemsStack(expected, Material.SAND, 6, 64, sandNb);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Sort giant lists")
    void testSortGiantLists() {
        
        final int MIN = 1, MAX = 64;

        List<ItemStack> itemsList = new ArrayList<>();
        
        final int oakNb = MIN + (int)(Math.random() * ((MAX - MIN) + 1));
        addItemsStack(itemsList, Material.OAK_LOG, 15, 64, oakNb);

        final int redstoneNb = MIN + (int)(Math.random() * ((MAX - MIN) + 1));
        addItemsStack(itemsList, Material.REDSTONE, 13, 64, redstoneNb);

        final int stoneNb = MIN + (int)(Math.random() * ((MAX - MIN) + 1));
        addItemsStack(itemsList, Material.STONE, 12, 64, stoneNb);

        final int sandNb = MIN + (int)(Math.random() * ((MAX - MIN) + 1));
        addItemsStack(itemsList, Material.SAND, 10, 64, sandNb);        

        final int honeyBottleNb = MIN + (int)(Math.random() * ((MAX - MIN) + 1));
        addItemsStack(itemsList, Material.HONEY_BOTTLE, 4, 16, honeyBottleNb);

        Inventory inv = server.createInventory(null, InventoryType.CHEST, "Expected", 54);

        inv.clear();
        ItemStack[] i = itemsList.toArray(ItemStack[]::new);
    
        inv.setContents(i);

        sortingListener.sortInventory(inv, false); // WTF null ??????? Only when use built-in sort but sort by name function seems to work

        List<ItemStack> actual =  Arrays.asList(inv.getContents());
        List<ItemStack> expected = new ArrayList<>();

        // First two lines
        addItemsStack(expected, Material.OAK_LOG, 15, 64, oakNb);
        addItemsStack(expected, Material.SAND, 3, 64);

        // Lines 3 and 4 
        addItemsStack(expected, Material.REDSTONE, 13, 64, redstoneNb);
        addItemsStack(expected, Material.HONEY_BOTTLE, 4, 16, honeyBottleNb);
        addItemsStack(expected, Material.SAND, 1, 64);

        // Last Two lines
        
        addItemsStack(expected, Material.STONE, 12, 64, stoneNb);
        addItemsStack(expected, Material.SAND, 6, 64, sandNb);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Smart sort giant lists (from listener)")
    void testSmartSortGiantListsFromListener() {
        
        final int MIN = 1, MAX = 64;

        List<ItemStack> itemsList = new ArrayList<>();
        
        final int oakNb = MIN + (int)(Math.random() * ((MAX - MIN) + 1));
        addItemsStack(itemsList, Material.OAK_LOG, 15, 64, oakNb);

        final int redstoneNb = MIN + (int)(Math.random() * ((MAX - MIN) + 1));
        addItemsStack(itemsList, Material.REDSTONE, 13, 64, redstoneNb);

        final int stoneNb = MIN + (int)(Math.random() * ((MAX - MIN) + 1));
        addItemsStack(itemsList, Material.STONE, 12, 64, stoneNb);

        final int sandNb = MIN + (int)(Math.random() * ((MAX - MIN) + 1));
        addItemsStack(itemsList, Material.SAND, 10, 64, sandNb);        

        final int honeyBottleNb = MIN + (int)(Math.random() * ((16 - MIN) + 1));
        addItemsStack(itemsList, Material.HONEY_BOTTLE, 4, 16, honeyBottleNb);

        Inventory inv = server.createInventory(null, InventoryType.CHEST, "Expected", 54);
        inv.clear();
        ItemStack[] i = itemsList.toArray(ItemStack[]::new);
    
        inv.setContents(i);

        sortingListener.sortInventory(inv, true);

        List<ItemStack> actual =  Arrays.asList(inv.getContents());
        List<ItemStack> expected = new ArrayList<>();

        // First two lines
        addItemsStack(expected, Material.OAK_LOG, 15, 64, oakNb);
        addItemsStack(expected, Material.SAND, 3, 64);

        // Lines 3 and 4 
        addItemsStack(expected, Material.REDSTONE, 13, 64, redstoneNb);
        addItemsStack(expected, Material.HONEY_BOTTLE, 4, 16, honeyBottleNb);
        addItemsStack(expected, Material.SAND, 1, 64);

        // Last Two lines
        
        addItemsStack(expected, Material.STONE, 12, 64, stoneNb);
        addItemsStack(expected, Material.SAND, 6, 64, sandNb);

        Assertions.assertEquals(expected, actual);
    }


    @Test
    @DisplayName("Smart sort giant lists (from listener)")
    void testSortTotal() {
        
        final int MIN = 1, MAX = 64;

        List<ItemStack> itemsList = new ArrayList<>();
        
        final int oakNb = MIN + (int)(Math.random() * ((MAX - MIN) + 1));
        addItemsStack(itemsList, Material.OAK_LOG, 15, 64, oakNb);

        final int redstoneNb = MIN + (int)(Math.random() * ((MAX - MIN) + 1));
        addItemsStack(itemsList, Material.REDSTONE, 13, 64, redstoneNb);

        final int stoneNb = MIN + (int)(Math.random() * ((MAX - MIN) + 1));
        addItemsStack(itemsList, Material.STONE, 12, 64, stoneNb);

        final int sandNb = MIN + (int)(Math.random() * ((MAX - MIN) + 1));
        addItemsStack(itemsList, Material.SAND, 10, 64, sandNb);        

        final int honeyBottleNb = MIN + (int)(Math.random() * ((MAX - MIN) + 1));
        addItemsStack(itemsList, Material.HONEY_BOTTLE, 4, 16, honeyBottleNb);

        Inventory inv = server.createInventory(null, InventoryType.CHEST, "Expected", 54);
        inv.clear();
        ItemStack[] i = itemsList.toArray(ItemStack[]::new);
    
        inv.setContents(i);

        sortingCommand.onCommand(player, Objects.requireNonNull(plugin.getCommand("sortchest")),"sortchest", new String[]{"true"});

        InventoryView transaction = player.openInventory(inv);
        InventoryOpenEvent event = new InventoryOpenEvent(transaction);
        sortingListener.onInventoryClick(event); // This seems not to run the sort algorithm even if the inventory is a chest one, 
                                                 // its size is > 5 and the player creating making the event has it's sort variable set to true

        List<ItemStack> actual =  Arrays.asList(inv.getContents());
        List<ItemStack> expected = new ArrayList<>();

        // First two lines
        addItemsStack(expected, Material.OAK_LOG, 15, 64, oakNb);
        addItemsStack(expected, Material.SAND, 3, 64);

        // Lines 3 and 4 
        addItemsStack(expected, Material.REDSTONE, 13, 64, redstoneNb);
        addItemsStack(expected, Material.HONEY_BOTTLE, 4, 16, honeyBottleNb);
        addItemsStack(expected, Material.SAND, 1, 64);

        // Last Two lines
        
        addItemsStack(expected, Material.STONE, 12, 64, stoneNb);
        addItemsStack(expected, Material.SAND, 6, 64, sandNb);

        Assertions.assertEquals(expected, actual);
    }





    List<ItemStack> createItemStackList(Material type, int StackNumber, int stackMax){
        return createItemStackList(type, StackNumber, stackMax, 64);
    }

    List<ItemStack> createItemStackList(Material type, int StackNumber, int stackMax, int lastStackAmount){
        List<ItemStack> list = new ArrayList<>();
        addItemsStack(list, type, StackNumber, stackMax, lastStackAmount);
        return list;
    }

    void addItemsStack(List<ItemStack> list, Material type, int StackNumber, int stackMax){
        addItemsStack(list, type, StackNumber, stackMax, 64);
    }

    void addItemsStack(List<ItemStack> list, Material type, int StackNumber, int stackMax, int lastStackAmount){
        for(int i = 0; i < StackNumber - 1; i++) list.add(new ItemStack(type, stackMax));
        list.add(new ItemStack(type, lastStackAmount));
    }

}
