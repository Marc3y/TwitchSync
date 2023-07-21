package de.kenjih.twitchsync;

import de.kenjih.twitchsync.commands.BotCommand;
import de.kenjih.twitchsync.commands.SyncCommand;
import de.kenjih.twitchsync.discord.DiscordBot;
import de.kenjih.twitchsync.discord.config.RegisteredConfig;
import de.kenjih.twitchsync.mongodb.MongoDB;
import de.kenjih.twitchsync.mongodb.MongoManager;
import de.kenjih.twitchsync.twitch.TwitchBot;
import de.kenjih.twitchsync.utils.Config;
import de.kenjih.twitchsync.utils.Refresher;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.util.concurrent.TimeUnit;

public final class TwitchSync extends Plugin {

    private static TwitchSync instance;
    private static Config config;
    private TwitchBot twitchBot;
    private static MongoDB mongoDB;
    private static MongoManager mongoManager;
    private static RegisteredConfig registeredConfig;

    @Override
    public void onEnable() {
        instance = this;
        config = new Config("config");
        mongoDB = new MongoDB("mongodb://TwitchSync:zV5YxxxJ0MuGM6Ie3f321pgtyvuvNkck7qWQ1iibhfDNtAcUiT@94.250.204.44:27017/?authMechanism=SCRAM-SHA-256&authSource=TwitchSync", "TwitchSync");
        mongoDB.openConnection();
        mongoManager = new MongoManager();
        registeredConfig = new RegisteredConfig();
        twitchBot = new TwitchBot();
        Refresher.timer();
        DiscordBot.getInstance().start();
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new SyncCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new BotCommand("bot", "command.bot"));
    }

    @Override
    public void onDisable() {
    }

    public static TwitchSync getInstance() {
        return instance;
    }

    public static Config getConfig() {
        return config;
    }

    public TwitchBot getTwitchBot() {
        return twitchBot;
    }

    public static ScheduledTask runAsync(Runnable runnable) {
        return ProxyServer.getInstance().getScheduler().runAsync(instance, runnable);
    }

    public static ScheduledTask runLater(Runnable runnable, int delay, TimeUnit timeUnit) {
        return ProxyServer.getInstance().getScheduler().schedule(instance, runnable, delay, timeUnit);
    }

    public static ScheduledTask runRepeat(Runnable runnable, int delay, int period, TimeUnit timeUnit) {
        return ProxyServer.getInstance().getScheduler().schedule(instance, runnable, delay, period, timeUnit);
    }

    public static RegisteredConfig getRegisteredConfig() {
        return registeredConfig;
    }

    public static void setRegisteredConfig(RegisteredConfig registeredConfig) {
        TwitchSync.registeredConfig = registeredConfig;
    }

    public static MongoDB getMongoDB() {
        return mongoDB;
    }

    public static MongoManager getMongoManager() {
        return mongoManager;
    }
}
