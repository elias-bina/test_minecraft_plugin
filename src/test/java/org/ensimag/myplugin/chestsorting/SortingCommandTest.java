package org.ensimag.myplugin.chestsorting;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import org.ensimag.myplugin.PluginMain;
import org.junit.jupiter.api.*;

import java.util.Objects;

public class SortingCommandTest {

    private ServerMock server;
    private PluginMain plugin;

    private PlayerMock player;
    private SortingCommand sortingCommand;

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

    @Test
    @DisplayName("No argument")
    void testNoArgument() {
        boolean response = sortingCommand.onCommand(player, Objects.requireNonNull(plugin.getCommand("sortchest")),
                "sortchest", new String[]{});
        Assertions.assertFalse(response);
    }

    @Test
    @DisplayName("Wrong argument")
    void testWrongArgument() {
        boolean response = sortingCommand.onCommand(player, Objects.requireNonNull(plugin.getCommand("sortchest")),
                "sortchest", new String[]{"foo"});
        Assertions.assertFalse(response);
    }

    @Test
    @DisplayName("Empty to false")
    void testEmptyToFalse() {
        Assertions.assertNull(sortingCommand.getChestSortingPerPlayer().get("Dummy"));

        boolean response = sortingCommand.onCommand(player, Objects.requireNonNull(plugin.getCommand("sortchest")),
                "sortchest", new String[]{"false"});
        Assertions.assertTrue(response);

        Assertions.assertFalse(sortingCommand.getChestSortingPerPlayer().get("Dummy"));
    }

    @Test
    @DisplayName("Empty to True")
    void testEmptyToTrue() {
        Assertions.assertNull(sortingCommand.getChestSortingPerPlayer().get("Dummy"));

        boolean response = sortingCommand.onCommand(player, Objects.requireNonNull(plugin.getCommand("sortchest")),
                "sortchest", new String[]{"true"});
        Assertions.assertTrue(response);

        Assertions.assertTrue(sortingCommand.getChestSortingPerPlayer().get("Dummy"));
    }

    @Test
    @DisplayName("False to True")
    void testFalseToTrue() {
        sortingCommand.getChestSortingPerPlayer().put("Dummy", false);

        boolean response = sortingCommand.onCommand(player, Objects.requireNonNull(plugin.getCommand("sortchest")),
                "sortchest", new String[]{"true"});
        Assertions.assertTrue(response);

        Assertions.assertTrue(sortingCommand.getChestSortingPerPlayer().get("Dummy"));
    }

    @Test
    @DisplayName("True to False")
    void testTrueToFalse() {
        sortingCommand.getChestSortingPerPlayer().put("Dummy", true);

        boolean response = sortingCommand.onCommand(player, Objects.requireNonNull(plugin.getCommand("sortchest")),
                "sortchest", new String[]{"false"});
        Assertions.assertTrue(response);

        Assertions.assertFalse(sortingCommand.getChestSortingPerPlayer().get("Dummy"));
    }

    @Test
    @DisplayName("True to True")
    void testTrueToTrue() {
        sortingCommand.getChestSortingPerPlayer().put("Dummy", true);

        boolean response = sortingCommand.onCommand(player, Objects.requireNonNull(plugin.getCommand("sortchest")),
                "sortchest", new String[]{"true"});
        Assertions.assertTrue(response);

        Assertions.assertTrue(sortingCommand.getChestSortingPerPlayer().get("Dummy"));
    }

    @Test
    @DisplayName("False to False")
    void testFalseToFalse() {
        sortingCommand.getChestSortingPerPlayer().put("Dummy", false);

        boolean response = sortingCommand.onCommand(player, Objects.requireNonNull(plugin.getCommand("sortchest")),
                "sortchest", new String[]{"false"});
        Assertions.assertTrue(response);

        Assertions.assertFalse(sortingCommand.getChestSortingPerPlayer().get("Dummy"));
    }

    @Test
    @DisplayName("Not a Player")
    void testNotAPlayer() {
        boolean response = sortingCommand.onCommand(server.getConsoleSender(), Objects.requireNonNull(plugin.getCommand("sortchest")),
                "sortchest", new String[]{"false"});
        Assertions.assertFalse(response);
    }
}
