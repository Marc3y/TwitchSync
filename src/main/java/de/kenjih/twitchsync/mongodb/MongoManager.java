package de.kenjih.twitchsync.mongodb;

import com.github.twitch4j.pubsub.ITwitchPubSub;
import com.mongodb.client.model.Updates;
import de.kenjih.twitchsync.TwitchSync;
import de.kenjih.twitchsync.utils.objects.By;
import de.kenjih.twitchsync.utils.objects.SyncUser;
import de.kenjih.twitchsync.utils.objects.TokenSetting;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MongoManager {

    //TWITCHID, DISCORDID, MINECRAFTUUID

    private static MongoDB mongoDB = TwitchSync.getMongoDB();

    public void set(SyncUser user){
        if(getUser(user.getTwitchId(), By.TWITCHID) == null){
            Document document = new Document();
            document.append("twitchid", user.getTwitchId());
            document.append("discordid", user.getDiscordId());
            document.append("minecraftuuid", user.getUuid().toString());
            mongoDB.getCollection("data").set(document);
            return;
        }
        Bson updates = Updates.combine(
                Updates.set("twitchid", user.getTwitchId()),
                Updates.set("discordid", user.getDiscordId()),
                Updates.set("minecraftuuid", user.getUuid().toString())
        );
        Document search = mongoDB.getCollection("data").find("minecraftuuid", user.getUuid().toString());
        mongoDB.getCollection("data").update(search, updates);
    }

    public List<TokenSetting> getAllTokens(){
        List<TokenSetting> list = new ArrayList<>();
        if(mongoDB.getCollectionDocument("tokens").find().first() == null) return list;
        for(Document doc : mongoDB.getCollectionDocument("tokens").find()){
            list.add(new TokenSetting(doc.getString("id"), doc.getString("accesstoken"), doc.getString("refreshtoken")));
        }
        return list;
    }

    public void unlink(SyncUser user){
        if(getUser(user.getTwitchId(), By.TWITCHID) == null) return;
        Document doc = new Document("twitchid", user.getTwitchId());
        mongoDB.getCollection("data").getDocument().deleteOne(doc);
    }

    public SyncUser getUser(String value, By by){
        Document doc = mongoDB.getCollection("data").find(by.toString().toLowerCase(), value);
        if(doc == null) return null;
        return new SyncUser(doc.getString("twitchid"), doc.getString("discordid"), UUID.fromString(doc.getString("minecraftuuid")));
    }

    public TokenSetting getToken(String id){
        Document doc = mongoDB.getCollection("tokens").find("id", id);
        if(doc == null) return null;
        return new TokenSetting(doc.getString("id"), doc.getString("accesstoken"), doc.getString("refreshtoken"));
    }

    public void setAccessToken(TokenSetting setting){
        if(getToken(setting.getId()) == null){
            Document document = new Document();
            document.append("id", setting.getId());
            document.append("accesstoken", setting.getAccessToken());
            document.append("refreshtoken", setting.getRefreshToken());
            mongoDB.getCollection("tokens").set(document);
            return;
        }
        Bson updates = Updates.combine(
                Updates.set("id", setting.getId()),
                Updates.set("accesstoken", setting.getAccessToken()),
                Updates.set("refreshtoken", setting.getRefreshToken())
        );
        Document search = mongoDB.getCollection("tokens").find("id", setting.getId());
        mongoDB.getCollection("tokens").update(search, updates);
    }

}
