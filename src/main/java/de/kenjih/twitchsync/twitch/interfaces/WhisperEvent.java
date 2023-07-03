package de.kenjih.twitchsync.twitch.interfaces;

import com.github.twitch4j.common.events.domain.EventUser;
import de.kenjih.twitchsync.twitch.TwitchBot;

public interface WhisperEvent {

    void onWhisper(EventUser user, String message, TwitchBot bot);

}
