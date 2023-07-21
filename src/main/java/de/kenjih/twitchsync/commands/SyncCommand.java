package de.kenjih.twitchsync.commands;

import de.kenjih.twitchsync.TwitchSync;
import de.kenjih.twitchsync.discord.DiscordBot;
import de.kenjih.twitchsync.discord.ticket.SyncTicket;
import de.kenjih.twitchsync.discord.ticket.TicketManager;
import de.kenjih.twitchsync.utils.Values;
import de.kenjih.twitchsync.utils.objects.By;
import de.kenjih.twitchsync.utils.objects.SyncUser;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.List;

public class SyncCommand extends Command implements TabExecutor {

    public SyncCommand() {
        super("sync");
    }

    @Override
    public void execute(CommandSender s, String[] args) {

        if(!(s instanceof ProxiedPlayer)) return;
        ProxiedPlayer p = (ProxiedPlayer) s;
        SyncUser user = TwitchSync.getMongoManager().getUser(p.getUniqueId().toString(), By.MINECRAFTUUID);
        if(args.length == 0) {
            for(String a : getConnectionInfoMessage(user)){
                p.sendMessage(a);
            }
        } else if(args.length == 1){
            if(!args[0].equalsIgnoreCase("unlink")) {
                if (DiscordBot.getInstance().isOffline()) {
                    p.sendMessage(Values.Prefix + "§7Es ist ein §cFehler §7aufgetreten >> §cDiscord-Bot offline");
                    return;
                }
                String discordname = args[0];
                TicketManager.getInstance().createSyncTicket(p.getUniqueId(), p.getName(), DiscordBot.jda.getUsersByName(discordname, true).get(0));
                p.sendMessage(Values.Prefix + " §7Es wurde ein Ticket auf dem Kenjih-Discord erstellt. Bitte gehe auf dieses und gebe deinen Twitch-Namen ein und klicke daraufhin auf akzeptieren.");
            } else {
                if(user == null){
                    p.sendMessage(Values.Prefix + " §cDu bist nicht gelinked.");
                    return;
                }
                p.sendMessage(Values.Prefix + " §7Gebe §e/sync unlink confirm §7ein um dich zu unlinken.");
            }
        } else if(args.length == 2){
            if(args[0].equalsIgnoreCase("unlink") && args[1].equalsIgnoreCase("confirm")){
                if(user == null){
                    p.sendMessage(Values.Prefix + " §cDu bist nicht gelinked.");
                    return;
                }
                TwitchSync.getMongoManager().unlink(user);
                p.sendMessage(Values.Prefix + " §7Du hast dich erfolgreich §cunlinked§7.");
            }
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender s, String[] args) {
        List<String> list = new ArrayList<>();
        if(!(s instanceof ProxiedPlayer)) return list;
        ProxiedPlayer p = (ProxiedPlayer) s;
        String input = "";
        if(args.length == 1){
            list.add("unlink");
            list.add("<Discord-Name>");
        }
        return list;
    }

    public List<String> getConnectionInfoMessage(SyncUser user){
        List<String> list = new ArrayList<>();
        if(user == null){
            list.add(Values.Prefix + " §7Discord: §cNicht verbunden");
            list.add(Values.Prefix + " §7Twitch: §cNicht verbunden");
        } else {
            if(DiscordBot.jda.getGuildById(Values.Discord_GuildID).getMemberById(user.getDiscordId()).getEffectiveName() != null) {
                list.add(Values.Prefix + " §7Discord: §b" + DiscordBot.jda.getGuildById(Values.Discord_GuildID).getMemberById(user.getDiscordId()).getUser().getName());
            } else list.add(Values.Prefix + " §7Discord: §cKein Member vom Kenjih-Discord");
            list.add(Values.Prefix + " §7Twitch: §b" + TwitchSync.getInstance().getTwitchBot().getUserNameByID(user.getTwitchId()));
        }
        return list;
    }
}
