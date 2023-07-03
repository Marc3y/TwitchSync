package de.kenjih.twitchsync.twitch;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.TwitchClientPool;
import com.github.twitch4j.TwitchClientPoolBuilder;
import com.github.twitch4j.chat.util.TwitchChatLimitHelper;
import com.github.twitch4j.helix.domain.UserList;
import de.kenjih.twitchsync.discord.DiscordBot;
import de.kenjih.twitchsync.twitch.events.ChatEventHandler;
import de.kenjih.twitchsync.twitch.events.WhisperEventHandler;
import de.kenjih.twitchsync.twitch.events.impl.ConfirmCheck;
import de.kenjih.twitchsync.twitch.interfaces.WhisperEvent;
import de.kenjih.twitchsync.utils.Values;
import de.kenjih.twitchsync.utils.objects.SyncUser;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class TwitchBot {

    public TwitchClientPool twitchClient;
    private OAuth2Credential credential;

    private List<WhisperEvent> whisperClasses = new ArrayList<>();

    public TwitchBot(){
        this.credential = new OAuth2Credential("896068422", Values.getTwitch_AccessToken());
        this.twitchClient = TwitchClientPoolBuilder.builder()
                .withClientId(Values.Twitch_ClientId)
                .withClientSecret(Values.Twitch_ClientSecret)
                .withEnableHelix(true)
                .withChatRateLimit(TwitchChatLimitHelper.MOD_MESSAGE_LIMIT)
                .withChatAccount(credential)
                .withEnableChat(true)
                .withEnableGraphQL(true)
                .withEnableKraken(true)
                .withEnablePubSub(true)
                .withEnableTMI(true)
                .withEnablePubSubPool(true)
                .build();
        twitchClient.getChat().joinChannel("Kenjih");
        registerEvents();
    }

    public void check(String discordId, UUID mcId, String username){
        System.out.println("-- Sollte geprintet werden " + discordId + " " + mcId + " " + username);
        String userId = getUserIdByName(username);
        System.out.println("dings haha " + userId);
        ConfirmCheck.toConfirm.add(new SyncUser(userId, discordId, mcId));
        System.out.println("whatdahell");
        System.out.println("dings " + ConfirmCheck.toConfirm);
    }

    private void registerEvents(){
        SimpleEventHandler eventHandler = twitchClient.getEventManager().getEventHandler(SimpleEventHandler.class);
        WhisperEventHandler whisper = new WhisperEventHandler(eventHandler);
        ChatEventHandler chat = new ChatEventHandler(eventHandler);

        //Reigster Stuff

    }

    public List<WhisperEvent> getWhisperClasses() {
        return whisperClasses;
    }

    private void registerWhisper(Class<? extends WhisperEvent> clazz){
        try {
            WhisperEvent obj = clazz.getDeclaredConstructor().newInstance();
            whisperClasses.add(obj);
        } catch (InstantiationException | IllegalAccessException
                 | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public String getUserIdByName(String name){
        List<String> list = new ArrayList<>();
        if(name == null) return "Invalid";
        UserList resultList = twitchClient.getHelix().getUsers(null, null, Arrays.asList(name)).execute();
        resultList.getUsers().forEach(user -> {
            list.add(user.getId());
        });
        return !list.isEmpty() ? list.get(0) : "Invalid";
    }

    public String getUserNameByID(String id){
        if(id != null) {
            List<String> list = new ArrayList<>();
            UserList resultList = twitchClient.getHelix().getUsers(null, Arrays.asList(id), null).execute();
            resultList.getUsers().forEach(user -> {
                list.add(user.getDisplayName());
            });
            return !list.isEmpty() ? list.get(0) : "Invalid";
        }
        return "Invalid";
    }

}
