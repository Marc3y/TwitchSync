package de.kenjih.twitchsync.discord.ticket;


import de.kenjih.twitchsync.TwitchSync;
import de.kenjih.twitchsync.discord.DiscordBot;
import de.kenjih.twitchsync.utils.Values;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.channel.concrete.TextChannelManager;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public abstract class Ticket extends ListenerAdapter {

    private final UUID ticketId;
    private final User user;

    private long createdAt;
    private TextChannel textChannel;

    public Ticket(User user) {
        this.ticketId = UUID.randomUUID();
        this.user = user;
    }

    public abstract void init();

    public boolean create() {
        // Add to ticket list
        TicketManager.getInstance().tickets.add(this);

        this.createdAt = System.currentTimeMillis();
        return true;
    }

    public void destroy() {
        // Remove from ticket list
        TicketManager.getInstance().tickets.remove(this);

        // Destroy TicketMessages of this ticket
        for (TicketMessage ticketMessage : new ArrayList<>(TicketManager.getInstance().ticketMessages)) {

            // is for this ticket
            if (ticketMessage.getTicket() == null || ticketMessage.getTicket().getTicketId().equals(ticketId)) {
                ticketMessage.destroy();
            }
        }
    }

    public void setPermissions() {
        // Bot offline
        if (DiscordBot.getInstance().isOffline()) {
            return;
        }

        // Get guild
        Guild guild = DiscordBot.getInstance().getJda().getGuildById(Values.Discord_GuildID);
        if (guild == null) {
            return;
        }

        // TextChannel created?
        if (textChannel == null) {
            return;
        }

        // Get channel manager
        TextChannelManager channelManager = textChannel.getManager();

        // remove permissions for @everyone
        channelManager.putRolePermissionOverride(guild.getPublicRole().getIdLong(), null, Arrays.asList(Permission.values())).queueAfter(1, TimeUnit.SECONDS);

        // give permissions to ticket user
        channelManager.putMemberPermissionOverride(user.getIdLong(), getDefaultAllowPermission(), getDefaultDenyPermission()).queueAfter(1, TimeUnit.SECONDS);
        
        // give permissions to bot
        channelManager.putMemberPermissionOverride(DiscordBot.getInstance().getJda().getSelfUser().getIdLong(), Arrays.asList(Permission.values()), null).queueAfter(1, TimeUnit.SECONDS);

    }

    public void closeTicket() {
        // Create embedBuilder
        EmbedBuilder closeBuilder = new EmbedBuilder();

        // Set timestamp
        closeBuilder.setTimestamp(Instant.now());

        // Set color
        closeBuilder.setColor(Values.Discord_Embed_Color);

        // Set title
        closeBuilder.setTitle("Abgeschlossen");

        // Set description
        long closeTime = (long) (Math.ceil(System.currentTimeMillis() / 1000d) + 10);
        closeBuilder.setDescription("Das Ticket wird <t:" + closeTime + ":R> geschlossen");

        // Create close ticketMessage
        TicketMessage closeTicketMessage = new TicketMessage(this, null, closeBuilder);
        closeTicketMessage.create();
        closeTicketMessage.sendLater(300, TimeUnit.MILLISECONDS);

        // Close ticket
        this.destroy();

        // Delete channel
        TwitchSync.runLater(this::deleteChannel, (int) (closeTime * 1000 - System.currentTimeMillis()), TimeUnit.MILLISECONDS);
    }

    public TextChannel createChannel(String channelName) {
        // Get bot-JDA
        JDA jda = DiscordBot.getInstance().getJda();
        if (jda == null) {
            return null;
        }

        // Get guild
        Guild guild = jda.getGuildById(Values.Discord_GuildID);
        if (guild == null) {
            return null;
        }

        // Get category
        Category category = guild.getCategoryById(Values.Discord_CategoryID);
        if (category == null) {
            return null;
        }

        // Get member
        Member member = guild.getMemberById(user.getIdLong());
        if (member == null) {
            return null;
        }

        // Create textChannel
        this.textChannel = category.createTextChannel(channelName).complete();

        // Set Permissions
        setPermissions();

        return this.textChannel;
    }

    public void deleteChannel() {
        if (textChannel == null) {
            return;
        }

        textChannel.delete().queue();
    }

    private List<Permission> getDefaultAllowPermission() {
        List<Permission> permissions = new ArrayList<>();
        permissions.add(Permission.VIEW_CHANNEL);
        permissions.add(Permission.MESSAGE_SEND);
        permissions.add(Permission.MESSAGE_HISTORY);
        permissions.add(Permission.USE_APPLICATION_COMMANDS);

        return permissions;
    }

    private List<Permission> getDefaultDenyPermission() {
        return Stream.of(Permission.values()).filter(p -> !getDefaultAllowPermission().contains(p)).collect(Collectors.toList());
    }

    // Getter

    public UUID getTicketId() {
        return ticketId;
    }

    public User getUser() {
        return user;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public TextChannel getTextChannel() {
        return textChannel;
    }
}
