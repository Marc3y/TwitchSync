package de.kenjih.twitchsync.discord.ticket;


import de.kenjih.twitchsync.discord.config.Registered;
import de.kenjih.twitchsync.utils.Values;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageEditData;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class UnSyncTicket  extends Ticket{

    private Registered registered;

    public UnSyncTicket(User user, Registered registered) {
        super(user);
        this.registered = registered;
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
        builder.setTitle("Willst du die Synchronisierung mit " + registered.getMinecraftName() + " aufheben?");

        // Set description
        builder.setDescription("Ein Moderator hat ein Ticket zur Aufhebung der Synchronisierung dieses Discord-Accounts mit **" + registered.getMinecraftName() + "** erstellt. Klicke auf Bestätigen, wenn du deinen Discord-Account Ent-Synchronisieren willst. Klicke auf Ablehnen, wenn nicht!");

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
            confirmBuilder.setTitle("Ticket bestätigt");

            // Set description
            confirmBuilder.setDescription("Du hast das Ticket bestätigt und deinen Discord-Account Ent-Synchronisiert!");

            // Create ticketMessage
            TicketMessage confirmTicketMessage = new TicketMessage(this, null, confirmBuilder);
            confirmTicketMessage.create();
            confirmTicketMessage.reply(interaction.getHook());

            // Accept ticket (unregister user)
            // TwitchSync.getRegisteredConfig().unregister(getUser());

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
        return super.createChannel("📧Unsync-ticket");
    }

    public Registered getRegistered() {
        return registered;
    }
}
