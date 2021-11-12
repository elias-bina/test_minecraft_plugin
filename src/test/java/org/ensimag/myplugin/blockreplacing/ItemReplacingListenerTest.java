package org.ensimag.myplugin.blockreplacing;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.ensimag.myplugin.PluginMain;
import org.junit.jupiter.api.*;

public class ItemReplacingListenerTest {

    private PluginMain plugin;

    private PlayerMock player;
    private ServerMock server;

    private ItemReplacingListener itemReplacingListener;

    @BeforeEach
    public void setUp() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(PluginMain.class);

        player = server.addPlayer("Dummy");
        itemReplacingListener = ItemReplacingListener.getInstance();
    }

    @AfterEach
    public void tearDown() {
        MockBukkit.unmock();
        ItemReplacingListener.deleteInstance();
    }

    @Test
    @DisplayName("Placed a block with remaining stack (empty inventory)")
    void testRemainingItems() {
        final int MIN = 2, MAX = 64;

        final int itemNumber = MIN + (int)(Math.random() * ((MAX - MIN) + 1));

        PlayerInventory inv = player.getInventory();
        inv.clear();

        
        ItemStack handItem = new ItemStack(Material.BEDROCK, itemNumber);
        inv.setItemInMainHand(handItem);

        Block block = server.getWorld("world").getBlockAt(0, 0, 0);
        Block blockZ1 = server.getWorld("world").getBlockAt(0, 0, 1);
        block.setType(Material.BEDROCK);
        blockZ1.setType(Material.BEDROCK);
        BlockPlaceEvent event = new BlockPlaceEvent(block, 
                                                    block.getState(), 
                                                    blockZ1, 
                                                    handItem, 
                                                    player, true, EquipmentSlot.HAND);
        itemReplacingListener.onBlockPlaced(event);

        Assertions.assertFalse(inv.isEmpty());
        Assertions.assertEquals(Material.BEDROCK, inv.getItemInMainHand().getType());
        Assertions.assertEquals(itemNumber, inv.getItemInMainHand().getAmount());
        
    }

    @Test
    @DisplayName("Placed a block replaced (Only replaced block in inventory)")
    void testReplaced() {
        final int MIN = 2, MAX = 64;

        final int itemNumber = MIN + (int)(Math.random() * ((MAX - MIN) + 1));

        PlayerInventory inv = player.getInventory();
        inv.clear();

        ItemStack replacingItem = new ItemStack(Material.BEDROCK, itemNumber);
        ItemStack handItem = new ItemStack(Material.BEDROCK, 1);

        // The item that will replace the hand item will be before him
        inv.setHeldItemSlot(1);
        inv.setItemInMainHand(handItem);
        inv.setItem(0,replacingItem);

        Block block = server.getWorld("world").getBlockAt(0, 0, 0);
        Block blockZ1 = server.getWorld("world").getBlockAt(0, 0, 1);
        block.setType(Material.BEDROCK);
        blockZ1.setType(Material.BEDROCK);
        BlockPlaceEvent event = new BlockPlaceEvent(block, 
                                                    block.getState(), 
                                                    blockZ1, 
                                                    handItem, 
                                                    player, true, EquipmentSlot.HAND);
        itemReplacingListener.onBlockPlaced(event);

        Assertions.assertFalse(inv.isEmpty());
        Assertions.assertEquals(Material.BEDROCK, inv.getItemInMainHand().getType());
        Assertions.assertEquals(itemNumber, inv.getItemInMainHand().getAmount());
        Assertions.assertNull(inv.getItem(0));
        
    }

    @Test
    @DisplayName("Placed a block replaced (Only replaced block in inventory but after hand)")
    void testReplacedByLaterInventorySlot() {
        final int MIN = 2, MAX = 64;

        final int itemNumber = MIN + (int)(Math.random() * ((MAX - MIN) + 1));

        PlayerInventory inv = player.getInventory();
        inv.clear();

        ItemStack replacingItem = new ItemStack(Material.BEDROCK, itemNumber);
        ItemStack handItem = new ItemStack(Material.BEDROCK, 1);

        // The item that will replace the hand item will be before him
        inv.setHeldItemSlot(0);
        inv.setItemInMainHand(handItem);
        inv.setItem(1,replacingItem);

        Block block = server.getWorld("world").getBlockAt(0, 0, 0);
        Block blockZ1 = server.getWorld("world").getBlockAt(0, 0, 1);
        block.setType(Material.BEDROCK);
        blockZ1.setType(Material.BEDROCK);
        BlockPlaceEvent event = new BlockPlaceEvent(block, 
                                                    block.getState(), 
                                                    blockZ1, 
                                                    handItem, 
                                                    player, true, EquipmentSlot.HAND);
        itemReplacingListener.onBlockPlaced(event);

        Assertions.assertFalse(inv.isEmpty());
        Assertions.assertEquals(Material.BEDROCK, inv.getItemInMainHand().getType());
        Assertions.assertEquals(itemNumber, inv.getItemInMainHand().getAmount());
        Assertions.assertNull(inv.getItem(1));
        
    }

    @Test
    @DisplayName("Placed last block of stack but only something else in inventory")
    void testNoReplacement() {
        final int MIN = 2, MAX = 64;

        final int itemNumber = MIN + (int)(Math.random() * ((MAX - MIN) + 1));

        PlayerInventory inv = player.getInventory();
        inv.clear();

        ItemStack replacingItem = new ItemStack(Material.ACACIA_LOG, itemNumber);
        ItemStack handItem = new ItemStack(Material.BEDROCK, 1);

        // The item that will replace the hand item will be before him
        inv.setHeldItemSlot(0);
        inv.setItemInMainHand(handItem);
        inv.setItem(1,replacingItem);

        Block block = server.getWorld("world").getBlockAt(0, 0, 0);
        Block blockZ1 = server.getWorld("world").getBlockAt(0, 0, 1);
        block.setType(Material.BEDROCK);
        blockZ1.setType(Material.BEDROCK);
        BlockPlaceEvent event = new BlockPlaceEvent(block, 
                                                    block.getState(), 
                                                    blockZ1, 
                                                    handItem, 
                                                    player, true, EquipmentSlot.HAND);
        itemReplacingListener.onBlockPlaced(event);

        Assertions.assertFalse(inv.isEmpty());
        Assertions.assertEquals(Material.AIR, inv.getItemInMainHand().getType());
        Assertions.assertEquals(1, inv.getItemInMainHand().getAmount());
        Assertions.assertEquals(Material.ACACIA_LOG, inv.getItem(1).getType());
        Assertions.assertEquals(itemNumber, inv.getItem(1).getAmount());
        
    }


    // TODO : Main vs Off hand
}
