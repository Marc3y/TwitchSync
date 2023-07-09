package de.kenjih.twitchsync.discord.ticket;

import de.kenjih.twitchsync.TwitchSync;
import de.kenjih.twitchsync.discord.DiscordBot;
import de.kenjih.twitchsync.utils.Values;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageEditData;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class SyncTicket extends Ticket {

    private final UUID playerId;
    private final String playerName;

    public SyncTicket(User user, UUID playerId, String playerName) {
        super(user);
        this.playerId = playerId;
        this.playerName = playerName;
    }

    @Override
    public void init() {

        // Create embedBuilder
        EmbedBuilder builder = new EmbedBuilder();

        // Set timestamp
        builder.setTimestamp(Instant.now());

        // Set color
        builder.setColor(Values.Discord_Embed_Color);

        // Set title
        builder.setTitle("Heißt du " + playerName + " in Minecraft?");

        // Set description
        builder.setDescription("**" + playerName + "** " + "versucht gerade eine Verbindung zu Discord/Twitch herzustellen. Wenn das du bist, gebe deinen Twitch-Namen ein und klicke auf akzeptieren.");

        // Create Buttons
        Button confirm = Button.success("confirm", "Bestätigen");
        Button deny = Button.danger("deny", "Ablehnen");


        // Create TicketButton
        TicketButton confirmButton = new TicketButton(confirm, (ticketMessage, interaction) -> {
            // is user?
            if (interaction.getUser().getIdLong() != getUser().getIdLong()) {
                interaction.reply("Du darfst das nicht!").setEphemeral(true).queue();
                return;
            }

            // send thinking
            interaction.deferReply().queue();

            // disable Buttons
            List<Button> buttons = new ArrayList<>();
            for (Button button : interaction.getMessage().getButtons()) {
                buttons.add(button.asDisabled());
            }
            interaction.getMessage().editMessage(MessageEditData.fromMessage(interaction.getMessage())).setActionRow(buttons).queue();

            // destroy old ticket message
            ticketMessage.destroy();

            // Create embedBuilder
            EmbedBuilder confirmBuilder = new EmbedBuilder();

            // Set timestamp
            confirmBuilder.setTimestamp(Instant.now());

            // Set color
            confirmBuilder.setColor(Values.Discord_Embed_Color);

            // Set title
            confirmBuilder.setTitle("Der Twitch-Name wurde abgeschickt.");

            // Set description
            confirmBuilder.setDescription("Um Twitch nun zu verifizieren musst du einmal in den Chat von Kenjih den Command \"!confirm\" schreiben.");

            // Create ticketMessage
            TicketMessage confirmTicketMessage = new TicketMessage(this, null, confirmBuilder);
            confirmTicketMessage.create();
            confirmTicketMessage.reply(interaction.getHook());

            List<Message> history =  DiscordBot.jda.getGuildById(Values.Discord_GuildID).getTextChannelById(this.getTextChannel().getId()).getHistory().retrievePast(2).complete();

            if (history.isEmpty()){
                System.out.println("lol");
                interaction.reply("Du musst noch deine namen angeben").setEphemeral(true).queue();
                return;
                }

            // Accept ticket (register user)
            for (Message message : history){
                if (message.equals("")){
                    System.out.println("lol");
                    interaction.reply("Du musst noch deine namen angeben").setEphemeral(true).queue();
                    return;
                }

                TwitchSync.getRegisteredConfig().acceptRegistered(getUser(), getPlayerId(), getPlayerName(), message);
            }
            //TwitchSync.getRegisteredConfig().acceptRegistered(getUser(), getPlayerId(), getPlayerName());

            // close ticket
            closeTicket();
        });



        TicketButton denyButton = new TicketButton(deny, (ticketMessage, interaction) -> {
            // is user?
            if (interaction.getUser().getIdLong() != getUser().getIdLong()) {
                interaction.reply("Du darfst das nicht!").setEphemeral(true).queue();
                return;
            }

            // send thinking
            interaction.deferReply().queue();

            // disable Buttons
            List<Button> buttons = new ArrayList<>();
            for (Button button : interaction.getMessage().getButtons()) {
                buttons.add(button.asDisabled());
            }
            interaction.getMessage().editMessage(MessageEditData.fromMessage(interaction.getMessage())).setActionRow(buttons).queue();

            // destroy old ticket message
            ticketMessage.destroy();

            // Create embedBuilder
            EmbedBuilder denyBuilder = new EmbedBuilder();

            // Set timestamp
            denyBuilder.setTimestamp(Instant.now());

            // Set color
            denyBuilder.setColor(Values.Discord_Embed_Color);

            // Set title
            denyBuilder.setTitle("Ticket abgelehnt");

            // Set description
            denyBuilder.setDescription("Du hast das Ticket abgelehnt!");

            // Deny ticket
            TwitchSync.getRegisteredConfig().denyRegistered(getUser(), getPlayerId(), getPlayerName());

            // Create ticketMessage
            TicketMessage denyTicketMessage = new TicketMessage(this, null, denyBuilder);
            denyTicketMessage.create();
            denyTicketMessage.reply(interaction.getHook());

            // close ticket
            closeTicket();
        });

        // Create ticketMessage
        TicketMessage ticketMessage = new TicketMessage(this, "<@" + getUser().getIdLong() + ">", builder, confirmButton, denyButton);
        ticketMessage.create();
        ticketMessage.send();
    }

    public TextChannel createChannel() {
        return super.createChannel("📧Sync-ticket");
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public String getPlayerName() {
        return playerName;
    }
}
