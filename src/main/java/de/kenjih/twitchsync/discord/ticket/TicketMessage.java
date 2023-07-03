package de.kenjih.twitchsync.discord.ticket;

import de.kenjih.twitchsync.TwitchSync;
import de.kenjih.twitchsync.discord.DiscordBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
import net.dv8tion.jda.api.requests.restaction.WebhookMessageCreateAction;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class TicketMessage extends ListenerAdapter {

    private final Ticket ticket;
    private final String message;
    private final EmbedBuilder embed;
    private final List<TicketButton> ticketButtons;

    private long messageId;

    public TicketMessage(Ticket ticket, String message, EmbedBuilder embed, TicketButton... ticketButtons) {
        this.ticket = ticket;
        this.message = message;
        this.embed = embed;
        this.ticketButtons = List.of(ticketButtons);
    }

    public void create() {
        // Get bot-JDA
        JDA jda = DiscordBot.getInstance().getJda();
        if (jda == null) {
            return;
        }

        // Add to list
        TicketManager.getInstance().ticketMessages.add(this);

        // Register listener
        jda.addEventListener(this);
    }

    public void destroy() {
        // Remove from list
        TicketManager.getInstance().ticketMessages.remove(this);

        // Get bot-JDA
        JDA jda = DiscordBot.getInstance().getJda();
        if (jda == null) {
            return;
        }

        // Unregister listener
        jda.removeEventListener(this);
    }

    public void send() {
        sendLater(0, TimeUnit.SECONDS);
    }

    public void sendLater(int delay, TimeUnit timeUnit) {

        // Has textChannel
        if (ticket == null || ticket.getTextChannel() == null) {
            return;
        }

        // Has a message
        if (message == null && embed == null) {
            return;
        }

        // Create message
        MessageCreateBuilder messageBuilder = new MessageCreateBuilder();
        if (message != null) {
            messageBuilder.setContent(message);
        }

        if (embed != null) {
            messageBuilder.setEmbeds(embed.build());
        }

        // Send message
        MessageCreateAction messageAction = ticket.getTextChannel().sendMessage(messageBuilder.build());

        // Add buttons
        List<Button> buttons = new ArrayList<>();
        for (TicketButton button : ticketButtons) {
            buttons.add(button.getButton());
        }

        // queue send
        TwitchSync.runLater(() -> {
            if (buttons.isEmpty()) {
                messageAction.queue(m -> {
                    this.messageId = m.getIdLong();
                });
            } else {
                messageAction.setActionRow(buttons).queue(m -> {
                    this.messageId = m.getIdLong();
                });
            }
        }, delay, timeUnit);
    }

    public void reply(InteractionHook hook) {
        hook.setEphemeral(false);

        // Create message
        MessageCreateBuilder messageBuilder = new MessageCreateBuilder();
        if (message != null) {
            messageBuilder.setContent(message);
        }

        if (embed != null) {
            messageBuilder.setEmbeds(embed.build());
        }

        // Send message
        WebhookMessageCreateAction<Message> messageAction = hook.sendMessage(messageBuilder.build());

        // Get Buttons
        List<Button> buttons = new ArrayList<>();
        for (TicketButton button : ticketButtons) {
            buttons.add(button.getButton());
        }

        // queue send
        if (buttons.isEmpty()) {
            messageAction.queue(m -> {
                this.messageId = m.getIdLong();
            });
        } else {
            messageAction.addActionRow(buttons).queue(m -> {
                this.messageId = m.getIdLong();
            });
        }
    }

    // Event

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        // get message
        if (event.getMessage().getIdLong() != this.messageId) {
            return;
        }

       /* // send waiting
        event.deferReply().queue();*/


        // get button
        Button button = event.getButton();
        String buttonId = button.getId();

        // has action
        for (TicketButton ticketButton : this.ticketButtons) {
            if (ticketButton.getButton() == null) {
                continue;
            }

            if (Objects.deepEquals(ticketButton.getButton().getId(), buttonId)) {
                ticketButton.getButtonPress().onPressed(this, event.getInteraction());
                break;
            }
        }
    }

    // Getter
    public Ticket getTicket() {
        return ticket;
    }

    public String getMessage() {
        return message;
    }

    public EmbedBuilder getEmbed() {
        return embed;
    }

    public List<TicketButton> getTicketButtons() {
        return ticketButtons;
    }
}

