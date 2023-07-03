package de.kenjih.twitchsync.twitch.events.impl;

import com.github.twitch4j.common.events.domain.EventUser;
import de.kenjih.twitchsync.TwitchSync;
import de.kenjih.twitchsync.twitch.TwitchBot;
import de.kenjih.twitchsync.twitch.interfaces.WhisperEvent;
import de.kenjih.twitchsync.utils.Values;
import de.kenjih.twitchsync.utils.objects.SyncUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class  ConfirmCheck {

    public static List<SyncUser> toConfirm = new ArrayList<>();

    public static SyncUser getFromTwitchId(String twitchId){
        for(SyncUser user : toConfirm){
            if(user.getTwitchId().equalsIgnoreCase(twitchId)) return user;
        }
        return null;
    }
}
