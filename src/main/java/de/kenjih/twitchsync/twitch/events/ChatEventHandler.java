package de.kenjih.twitchsync.twitch.events;

import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.github.twitch4j.common.events.user.PrivateMessageEvent;
import de.kenjih.twitchsync.TwitchSync;
import de.kenjih.twitchsync.discord.DiscordBot;
import de.kenjih.twitchsync.twitch.events.impl.ConfirmCheck;
import de.kenjih.twitchsync.twitch.interfaces.WhisperEvent;
import de.kenjih.twitchsync.utils.Values;
import de.kenjih.twitchsync.utils.objects.SyncUser;

public class ChatEventHandler {

    public ChatEventHandler(SimpleEventHandler eventHandler){
        eventHandler.onEvent(ChannelMessageEvent.class, event -> onChat(event));



    }

    public void onChat(ChannelMessageEvent e){
        if(!e.getChannel().getId().equalsIgnoreCase("168334067")) return;
        if(!e.getMessage().trim().equalsIgnoreCase("!confirm")) return;
        SyncUser user = ConfirmCheck.getFromTwitchId(e.getUser().getId());
        ConfirmCheck.toConfirm.remove(user);
        user.setTwitchId(e.getUser().getId());
        TwitchSync.getInstance().getSQLData().set(user);
        e.getTwitchChat().sendMessage("Kenjih", "@" + e.getUser().getName() + ", die Verknüpfung zu Discord wurde erfolgreich herrgestellt (Dc: " + DiscordBot.jda.getGuildById(Values.Discord_GuildID).getMemberById(user.getDiscordId()).getEffectiveName() + ")");
    }

}
