package de.kenjih.twitchsync.discord.config;

import de.kenjih.twitchsync.TwitchSync;
import de.kenjih.twitchsync.discord.ticket.SyncTicket;

import java.util.UUID;

public class Registered {

    private String minecraftName;
    private final UUID minecraftUUID;
    private String discordName;
    private final long discordID;
    // private final List<String> rolesToGive;

    public Registered(String minecraftName, UUID minecraftUUID, String discordName, long discordID) {
        this.minecraftName = minecraftName;
        this.minecraftUUID = minecraftUUID;
        this.discordName = discordName;
        this.discordID = discordID;
        // this.rolesToGive = new ArrayList<>();
    }

    public void create() {
        TwitchSync.getRegisteredConfig().registereds.add(this);
    }

    public void destroy() {
        TwitchSync.getRegisteredConfig().registereds.remove(this);
    }

    public String getMinecraftName() {
        return minecraftName;
    }

    public void setMinecraftName(String minecraftName) {
        this.minecraftName = minecraftName;
    }

    public UUID getMinecraftUUID() {
        return minecraftUUID;
    }

    public String getDiscordName() {
        return discordName;
    }

    public void setDiscordName(String discordName) {
        this.discordName = discordName;
    }

    public long getDiscordID() {
        return discordID;
    }
}
