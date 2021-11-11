package org.ensimag.myplugin.chestsorting;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.block.state.ChestMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import be.seeseemelk.mockbukkit.inventory.ChestInventoryMock;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.ensimag.myplugin.PluginMain;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Objects;

public class ChestSortingListenerTest {

    private ServerMock server;
    private PluginMain plugin;

    private PlayerMock player;
    private SortingCommand sortingCommand;
    private ChestSortingListener sortingListener = ChestSortingListener.getInstance();

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

    @Test
    @DisplayName("OnInventoryClick player not in the map")
    void testOnInventoryClickPlayerNotInTheMap() {
        Inventory inventory = new ChestInventoryMock(null, 27);
        inventory.setItem(6, new ItemStack(Material.OAK_LOG, 5));
        InventoryOpenEvent event = new InventoryOpenEvent(Objects.requireNonNull(player.openInventory(inventory)));
        System.out.println(Arrays.toString(inventory.getContents()));
    }
}
