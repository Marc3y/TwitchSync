package de.kenjih.twitchsync.discord.ticket;

import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class TicketButton {

    private final Button button;
    private final ButtonPress buttonPress;

    public TicketButton(Button button, ButtonPress onPressed) {
        this.button = button;
        this.buttonPress = onPressed;
    }

    public Button getButton() {
        return button;
    }

    public ButtonPress getButtonPress() {
        return buttonPress;
    }
}
