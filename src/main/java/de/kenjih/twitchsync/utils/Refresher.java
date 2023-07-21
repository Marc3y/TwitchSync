package de.kenjih.twitchsync.utils;

import de.kenjih.twitchsync.TwitchSync;
import de.kenjih.twitchsync.utils.objects.TokenSetting;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.concurrent.TimeUnit;

public class Refresher {

    public static void refresh(){

    }

    public static void timer(){
        TwitchSync.getInstance().getProxy().getScheduler().schedule(TwitchSync.getInstance(), new Runnable() {
            @Override
            public void run() {
                for(ProxiedPlayer current : TwitchSync.getInstance().getProxy().getPlayers()){
                    current.sendMessage("§7Die Twitch-Server werden reconnected. Dies kann eventuell zu kurzen Lags führen.");
                }
                for(TokenSetting token : TwitchSync.getMongoManager().getAllTokens()) {
                    try {
                        token.refresh(Values.Twitch_ClientId, Values.Twitch_ClientSecret, Values.Twitch_RedirectUri);
                        TwitchSync.getMongoManager().setAccessToken(token);
                    } catch (Exception ignored){}
                    try {
                        token.refresh(Values.KenjihClientId, Values.KenjihClientSecret, "http://localhost/");
                        TwitchSync.getMongoManager().setAccessToken(token);
                    } catch (Exception ignored){}
                }
            }
        }, 0, 59, TimeUnit.MINUTES);
    }

}
