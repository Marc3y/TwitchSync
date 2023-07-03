package de.kenjih.twitchsync.twitch.events;

import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.common.events.user.PrivateMessageEvent;
import de.kenjih.twitchsync.TwitchSync;
import de.kenjih.twitchsync.twitch.interfaces.WhisperEvent;

public class WhisperEventHandler {

    public WhisperEventHandler(SimpleEventHandler eventHandler){
        eventHandler.onEvent(PrivateMessageEvent.class, event -> onWhisper(event));
    }

    public void onWhisper(PrivateMessageEvent e){
        for(WhisperEvent c : TwitchSync.getInstance().getTwitchBot().getWhisperClasses()){
            c.onWhisper(e.getUser(), e.getMessage(), TwitchSync.getInstance().getTwitchBot());
        }
    }

}
