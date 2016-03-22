package com.me.tft_02.dynamictextures;

import com.me.tft_02.dynamictextures.listeners.PlayerListener;
import com.me.tft_02.dynamictextures.runnables.RegionTimerTask;
import com.me.tft_02.dynamictextures.util.Misc;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.World;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Logger;

import javax.inject.Inject;

@Plugin(id = "dynamictextures", name = "Dynamic Textures")
public class DynamicTextures {
    public static DynamicTextures p;
    public static File dynamictextures;
    public CommentedConfigurationNode config;

    @Inject @DefaultConfig(sharedRoot = false)
    private Path configPath;

    @Inject public Logger logger;

    private HoconConfigurationLoader loader;

    public boolean worldGuardEnabled = false;

    // Update Check
    public boolean updateAvailable;

    /**
     * Run things on enable.
     */
    @Listener
    public void onPreIni(GamePreInitializationEvent event) {
        p = this;

        this.initConfig();
        this.registerEvents();
        this.setupFilePaths();
        this.setupCommands();

        if (worldGuardEnabled) {
            //Region check timer (Runs every five seconds)
            Task.builder().delayTicks(0).intervalTicks(5 * 20).execute(new RegionTimerTask()).submit(this);
        }

        checkForUpdates();
        setupMetrics();
    }

    public void reloadConfig() {
        try {
            this.config = this.loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public CommentedConfigurationNode getConfig() {
        return this.config;
    }

    private void setupCommands() {
        CommandSpec reload = CommandSpec.builder()
                .permission("dynamictextures.commands.reload")
                .executor((src, args) -> {
                    DynamicTextures.p.reloadConfig();
                    src.sendMessage(Text.of(TextColors.GREEN, "Configuration reloaded."));

                    if (src instanceof Player) {
                        Misc.loadResourcePack((Player) src);
                    }

                    return CommandResult.success();
                }).build();

        CommandSpec refresh = CommandSpec.builder()
                .permission("dynamictextures.commands.refreshall")
                .executor((src, args) -> {
                    src.sendMessage(Text.of(TextColors.GREEN, "Refreshing textures for all players."));

                    for (Player player : Sponge.getServer().getOnlinePlayers()) {
                        Misc.loadResourcePack(player);
                        player.sendMessage(Text.of(TextColors.GREEN, "Refreshing textures..."));
                    }

                    return CommandResult.success();
                }).build();

        CommandSpec main = CommandSpec.builder()
                .child(reload, "reload")
                .child(refresh, "refreshall")
                .build();

        Sponge.getCommandManager().register(this, main, "dynamictextures");

    }

    private void initConfig() {

        this.loader = HoconConfigurationLoader.builder().setDefaultOptions(ConfigurationOptions.defaults().setShouldCopyDefaults(true)).setPath(this.configPath).build();

        try {
            this.config = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.config.getNode("General.Stats_Tracking").getBoolean(true);
        this.config.getNode("General.Update_Check").getBoolean(true);
        for (World world : Sponge.getServer().getWorlds()) {
            this.config.getNode("Worlds." + world.getName().toLowerCase()).getString("http://url_to_the_resource_pack_here");
        }
        this.config.getNode("Permissions.custom_perm_name").getString("http://url_to_the_resource_pack_here");
        this.config.getNode("WorldGuard_Regions.region_name").getString("http://url_to_the_resource_pack_here");
        saveConfig();
    }

    private void saveConfig() {
        try {
            this.loader.save(this.config);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Registers all event listeners
     */
    private void registerEvents() {
        // Register events
        Sponge.getEventManager().registerListeners(this, new PlayerListener());
    }

    /*private void setupWorldGuard() {
        if (getServer().getPluginManager().isPluginEnabled("WorldGuard")) {
            worldGuardEnabled = true;
            getLogger().info("WorldGuard found!");
        }
    }

    public WorldGuardPlugin getWorldGuard() {
        Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");

        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            return null;
        }

        return (WorldGuardPlugin) plugin;
    }*/

    public boolean getWorldGuardEnabled() {
        return worldGuardEnabled;
    }

    /**
     * Setup the various storage file paths
     */
    private void setupFilePaths() {
        //dynamictextures = getFile();
    }

    private void checkForUpdates() {
        if (!this.config.getNode("General.Update_Check").getBoolean()) {
            return;
        }

        /*Updater updater = new Updater(this, 48782, dynamictextures, UpdateType.NO_DOWNLOAD, false);

        if (updater.getResult() != UpdateResult.UPDATE_AVAILABLE) {
            this.updateAvailable = false;
            return;
        }

        this.updateAvailable = true;
        this.logger.info("DynamicTextures is outdated!");
        this.logger.info("http://dev.bukkit.org/server-mods/worldtextures/");*/
    }

    private void setupMetrics() {
        if (!config.getNode("General.Stats_Tracking").getBoolean()) {
            return;
        }

        //Metrics metrics = new Metrics(this);
        //metrics.start();
    }
}
