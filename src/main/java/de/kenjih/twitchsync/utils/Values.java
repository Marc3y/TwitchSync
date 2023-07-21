package de.kenjih.twitchsync.utils;

import de.kenjih.twitchsync.TwitchSync;

import java.awt.*;

public class Values {
    //Discord
    public static String Discord_GuildID = "445308722168987648";
    public static String Discord_BotToken = "MTEyNTQ4NTc0MzIzODgwNzU2Mg.GmlX2E.gy1lkbfTBHtaPor3TJUEjCAQZmMLBAA216Egkk";
    public static String Discord_CategoryID = "1125786444678565948";
    public static Color Discord_Embed_Color = new Color(0x00F3FF);

    //Twitch

    public static String Twitch_ClientId = "96rzm7grnrncg7uwpctt6mcznjseyb";
    public static String Twitch_ClientSecret = "h7fcgx3lrtpjmk54migcj2cjavlsao";
    public static String Twitch_RedirectUri = "http://localhost";
    private static String Twitch_AccessToken = "vrq3seny93y4g1enesgp5nkgf15jq1";
    private static String Twitch_RefreshToken = "rqtivwksdu14f82f5nvi87v2irkxbh0nskxe7792x2rclg64l0";

    public static String KenjihClientId = "oz9e2w4jy11dlqwfb3dpts0h2c0iih";
    public static String KenjihClientSecret = "9jnq7dgsqac1p3mrplaoirld418ow7";

    public static String getTwitch_AccessToken(){
        return TwitchSync.getMongoManager().getToken("KenjihBot").getAccessToken();
    }

    //Minecraft
    public static String Prefix = "§d§lSync §r§8>>§r";


}