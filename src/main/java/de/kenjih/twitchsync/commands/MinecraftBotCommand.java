package de.kenjih.twitchsync.commands;

import de.kenjih.twitchsync.discord.DiscordBot;
import de.kenjih.twitchsync.utils.Values;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.TabExecutor;

public class MinecraftBotCommand implements TabExecutor {
    @Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] strings) {
        return null;
    }
/*
    private static final String SYNTAX = Constants.SYNTAX + "/bot (start | stop | info | reload)";

   public MinecraftBotComman(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }



    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length > 1) {
            sender.sendMessage(Constants.toBaseComponent(SYNTAX));
            return;
        }

        // info
        if (args.length == 0 || args[0].equalsIgnoreCase("info")) {

            // collect information
            String status = DiscordBot.getInstance().getStatus();
            String token =  "§agesetzt";
            String guildId = "§e" + Values.Discord_GuildID;

            // send information
            sender.sendMessage(Values.Prefix + "§7Status: " + status);
            sender.sendMessage(Values.Prefix + "§7Token: " + token);
            sender.sendMessage(Values.Prefix + "§7GuildId: " + guildId);

            return;
        }

        // start
        if (args[0].equalsIgnoreCase("start")) {
            // not shutdown
            if (!DiscordBot.getInstance().isOffline()) {
                sender.sendMessage(Constants.toBaseComponent(Constants.PREFIX + "§7Der Bot ist momentan nicht §4offline"));
                return;
            }

            // start bot
            DiscordBot.getInstance().start();

            // send message
            sender.sendMessage(Constants.toBaseComponent(Constants.PREFIX + "§7Der Bot wird §agestartet"));
            return;
        }

        // stop
        if (args[0].equalsIgnoreCase("stop")) {
            // already shutdown
            if (DiscordBot.getInstance().isShutdown()) {
                sender.sendMessage(Constants.toBaseComponent(Constants.PREFIX + "§7Der Bot ist bereits §4gestoppt"));
                return;
            }

            // stop bot
            DiscordBot.getInstance().shutdown();

            // send message
            sender.sendMessage(Constants.toBaseComponent(Constants.PREFIX + "§7Der Bot wird §cgestoppt"));
            return;
        }

        // reload
        if (args[0].equalsIgnoreCase("reload")) {

            // load config
            TwitchSync.getFilesUtil().load();

            // send message
            sender.sendMessage(Constants.toBaseComponent(Constants.PREFIX + "§7Die Konfigurationsdatei wurde §eneu geladen"));
            return;
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> tocomplete = new ArrayList<>();
        List<String> complete = new ArrayList<>();

        if (args.length == 1) {
            tocomplete.addAll(Arrays.asList("stop", "start", "info", "reload"));
        }

        for (String tc : tocomplete) {
            if (tc.toLowerCase().startsWith(args[args.length - 1].toLowerCase())) {
                complete.add(tc);
            }
        }
        return complete;
    }

 */
}
