package de.kenjih.twitchsync.utils;

import de.kenjih.twitchsync.TwitchSync;

import java.awt.*;

public class Values {
    //Discord
    public static String Discord_GuildID = "1037069989804515469";
    public static String Discord_BotToken = "MTEyNTQ4NTc0MzIzODgwNzU2Mg.GmlX2E.gy1lkbfTBHtaPor3TJUEjCAQZmMLBAA216Egkk";
    public static String Discord_CategoryID = "1037069990291058709";
    public static Color Discord_Embed_Color = new Color(0x00F3FF);

    //Twitch

    public static String Twitch_ClientId = "96rzm7grnrncg7uwpctt6mcznjseyb";
    public static String Twitch_ClientSecret = "h7fcgx3lrtpjmk54migcj2cjavlsao";
    public static String Twitch_RedirectUri = "http://localhost";
    private static String Twitch_AccessToken = "thj7lbigngegufypxppulrgra60rq2";

    public static String getTwitch_AccessToken(){
        Config config = TwitchSync.getConfig();
        return config.getConfig().contains("Twitch_AccessToken") ? config.getConfig().getString("Twitch_AccessToken") : Twitch_AccessToken;
    }

    //Minecraft
    public static String Prefix = "§d§lSync §r§8>>§r";


}