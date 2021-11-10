package org.ensimag.myplugin.chestsorting;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import org.ensimag.myplugin.PluginMain;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class ChestSortingListenerTest {
    private PluginMain plugin;

    private PlayerMock player;
    private SortingCommand sortingCommand;

    @BeforeEach
    public void setUp() {
        ServerMock server = MockBukkit.mock();
        plugin = MockBukkit.load(PluginMain.class);

        player = server.addPlayer("Dummy");
        sortingCommand = SortingCommand.getInstance();
    }

    @AfterEach
    public void tearDown() {
        SortingCommand.deleteInstance();
        MockBukkit.unmock();
    }
}
