package de.kenjih.twitchsync.discord.ticket;


import de.kenjih.twitchsync.TwitchSync;
import de.kenjih.twitchsync.discord.DiscordBot;
import de.kenjih.twitchsync.discord.config.Registered;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class TicketManager {

    private static TicketManager instance;

    public ScheduledTask autoDestroyTickets = null;

    public List<Ticket> tickets = new ArrayList<>();
    public List<TicketMessage> ticketMessages = new ArrayList<>();

    public TicketManager() {
        instance = this;
    }

    public void startDestroyTask() {
        if (autoDestroyTickets != null) {
            return;
        }

        autoDestroyTickets = TwitchSync.runRepeat(() -> {
            for (Ticket ticket : new ArrayList<>(tickets)) {
                if (ticket.getCreatedAt() < System.currentTimeMillis() - 5 * 60 * 1000) {
                    ticket.destroy();
                    ticket.deleteChannel();
                }
            }
        }, 10, 10, TimeUnit.SECONDS);
    }

    public void stopDestroyTask() {
        if (autoDestroyTickets == null) {
            return;
        }

        autoDestroyTickets.cancel();
        autoDestroyTickets = null;
    }

    public boolean createSyncTicket(UUID playerId, String playerName, User user) {
        // offline
        if (DiscordBot.getInstance().isOffline()) {
            return false;
        }

        // create ticket
        SyncTicket syncTicket = new SyncTicket(user, playerId, playerName);
        TextChannel channel = syncTicket.createChannel();
        if (channel == null) {
            return false;
        }

        // save ticket
        syncTicket.create();

        // init
        syncTicket.init();
        return true;
    }

    public boolean createUnSyncTicket(User user) {
        // offline
        if (DiscordBot.getInstance().isOffline()) {
            return false;
        }

        // not registered
        Registered registered = TwitchSync.getRegisteredConfig().getRegistered(user.getIdLong());
        if(registered == null) {
            return false;
        }

        // create ticket
        UnSyncTicket unSyncTicket = new UnSyncTicket(user, registered);
        TextChannel channel = unSyncTicket.createChannel();
        if (channel == null) {
            return false;
        }

        // save ticket
        unSyncTicket.create();

        // init
        unSyncTicket.init();
        return true;
    }
/*
    public SyncTicket getSyncTicket(UUID uuid) {
        for (Ticket ticket : tickets) {
            if (!(ticket instanceof SyncTicket syncTicket)) {
                continue;
            }

            if (syncTicket.getPlayerId().equals(uuid)) {
                return syncTicket;
            }
        }

        return null;
    }


    public SyncTicket getSyncTicket(long userId) {
        for (Ticket ticket : tickets) {
            if (!(ticket instanceof SyncTicket syncTicket)) {
                continue;
            }

            if (syncTicket.getUser().getIdLong() == userId) {
                return syncTicket;
            }
        }

        return null;
    }

    public UnSyncTicket getUnSyncTicket(long userId) {
        for (Ticket ticket : tickets) {
            if (!(ticket instanceof UnSyncTicket unSyncTicket)) {
                continue;
            }

            if (unSyncTicket.getUser().getIdLong() == userId) {
                return unSyncTicket;
            }
        }

        return null;
    }

 */

    public static TicketManager getInstance() {
        if (instance == null) {
            return new TicketManager();
        }
        return instance;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public List<TicketMessage> getTicketMessages() {
        return ticketMessages;
    }
}
