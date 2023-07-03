package de.kenjih.twitchsync.discord.config;



import de.kenjih.twitchsync.TwitchSync;
import de.kenjih.twitchsync.utils.Values;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RegisteredConfig {

    public List<Registered> registereds = new ArrayList<>();





    public void acceptRegistered(User user, UUID playerId, String playerName, Message twitchName) {

        //boolean created = createRegistered(user, playerId, playerName);
       final String username = twitchName.getContentRaw();

        // get player
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playerId);
        System.out.println("name:"  + username);

        TwitchSync.getInstance().getTwitchBot().check(user.getId(), playerId, username);
        //Send Message
        if (player != null && player.isConnected()) {
            //if (created) {
                //No Error - Registered successfully
                //  updateRegisteredRole(player);
                //  updatePermRole(player);

                //Send Message
                player.sendMessage(Values.Prefix + "§7Dein Minecraft Account wurde mit §e" + user.getAsTag() + " §averknüpft");
        //    } else {
                //Error - Registered failed
                //Send Message
            //    player.sendMessage(Values.Prefix + "§7Bei der Verknüpfung mit Discord ist ein §cFehler §7aufgetreten");
           // }
        }
    }

    public void denyRegistered(User user, UUID playerId, String playerName) {

        // get player
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playerId);

        //Send Message
        if (player != null && player.isConnected()) {
            player.sendMessage(Values.Prefix + "§7Dein Ticket zur Verknüpfung mit §e" + user.getAsTag() + " §7wurde §cabgelehnt");
        }
    }

    /*public boolean createRegistered(User user, UUID playerId, String playerName) {
        if (getRegistered(playerId) != null) {
            return false;
        }

        new Registered(playerName, playerId, user.getName(), user.getIdLong()).create();
        return true;

    }
       */



   /* public Registered getRegistered(UUID playerId) {
        for (Registered registered : registereds) {
            if (registered.getMinecraftUUID().equals(playerId)) {
                return registered;
            }
        }
        return null;
    }

    */


    public Registered getRegistered(long userId) {
        for (Registered registered : registereds) {
            if (registered.getDiscordID() == userId) {
                return registered;
            }
        }
        return null;
    }

    @Deprecated
    public Registered getRegistered(String name) {
        for (Registered registered : registereds) {
            if (registered.getMinecraftName().equalsIgnoreCase(name)) {
                return registered;
            }
        }
        return null;
    }

}
