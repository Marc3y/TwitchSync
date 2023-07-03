package de.kenjih.twitchsync.utils.objects;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

public class SyncUser {

    private String twitchId;
    private String discordId;
    private UUID uuid;

    public SyncUser(String twitchId, String discordId, UUID uuid){
        this.twitchId = twitchId;
        this.discordId = discordId;
        this.uuid = uuid;
    }

    public String getTwitchId() {
        return twitchId;
    }

    public String getDiscordId() {
        return discordId;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void setDiscordId(String discordId) {
        this.discordId = discordId;
    }

    public void setTwitchId(String twitchId) {
        this.twitchId = twitchId;
    }

    public ProxiedPlayer getPlayerIfOnline(){
        return ProxyServer.getInstance().getPlayer(getUuid());
    }
}
