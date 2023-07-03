package de.kenjih.twitchsync.discord.ticket;

import net.dv8tion.jda.api.interactions.components.buttons.ButtonInteraction;

public interface ButtonPress {
    public void onPressed(TicketMessage ticketMessage, ButtonInteraction interaction);
}
