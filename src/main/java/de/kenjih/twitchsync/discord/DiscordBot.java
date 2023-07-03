package de.kenjih.twitchsync.discord;

import de.kenjih.twitchsync.utils.Values;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import java.util.Arrays;

public class DiscordBot{

    private static DiscordBot instance;
    public static JDA jda;
    
    public DiscordBot() {
        instance = this;
    }

    public void start(){
        try {
        JDABuilder builder = JDABuilder.createDefault(Values.Discord_BotToken);

        builder.setBulkDeleteSplittingEnabled(true);

        builder.setActivity(Activity.listening("Bot startet..."));

        builder.setStatus(OnlineStatus.IDLE);

        builder.enableIntents(Arrays.asList(GatewayIntent.values()));

        builder.disableCache(CacheFlag.ONLINE_STATUS, CacheFlag.ACTIVITY, CacheFlag.STICKER, CacheFlag.CLIENT_STATUS, CacheFlag.VOICE_STATE, CacheFlag.EMOJI);



        builder.setMemberCachePolicy(MemberCachePolicy.ALL);

        jda = builder.build();

         jda.awaitStatus(JDA.Status.CONNECTED).getPresence().setStatus(OnlineStatus.ONLINE);

         jda.awaitStatus(JDA.Status.CONNECTED).getPresence().setActivity(Activity.watching("Das Leben..."));
         clearTicketChannels();

            //jda.addEventListener(new );

        } catch (InterruptedException e) {
            throw new RuntimeException("Fehler beim Bot Starten:" + e);
        }
    }

    public static void clearTicketChannels(){
        Guild guild = jda.getGuildById(Values.Discord_GuildID);
        Category category = guild.getCategoryById(Values.Discord_CategoryID);
        for (GuildChannel channel : category.getTextChannels()) {
            channel.delete().queue();
        }
    }

    public void shutdown() {
        if (jda != null) {
          //  clearTicketChannels();
            jda.getPresence().setStatus(OnlineStatus.DO_NOT_DISTURB);
            jda.getPresence().setActivity(Activity.watching("Bot stoppt 🛑"));
            jda.shutdown();
        }
    }

    public String getStatus() {
        if(DiscordBot.getInstance().getJda() == null) {
            return "§4Offline";
        }
        String status = "";
        switch (DiscordBot.getInstance().getJda().getStatus()) {
            case SHUTTING_DOWN :
                status = "§cAm herunterfahren";
            break;
            case DISCONNECTED :
                status = "§cVerbindung verloren";
            break;
            case CONNECTED :
                status = "§aVerbunden";
            break;
            case INITIALIZING :
                status = "§eAm hochfahren";
            break;
            case INITIALIZED :
                status = "§eHochgefahren";
            break;
            case FAILED_TO_LOGIN :
                status = "§4Verbindungsfehler";
            break;
            default:
                status = "§4Offline";
                break;
            }

        return status;
    }
    public boolean isOffline() {
        if (getJda() == null) {
            return true;
        }else {
            return false;
        }
    }
    public boolean isShutdown() {
        if (getJda() == null) {
            return true;
        }

        JDA.Status status = getJda().getStatus();
        return status == JDA.Status.SHUTDOWN || status == JDA.Status.SHUTTING_DOWN;
    }

    public static DiscordBot getInstance() {
        if (instance == null) {
            return new DiscordBot();
        }
        return instance;
    }
    public JDA getJda() {
        return jda;
    }

}
