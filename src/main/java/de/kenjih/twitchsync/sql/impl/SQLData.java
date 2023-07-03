package de.kenjih.twitchsync.sql.impl;

import de.kenjih.twitchsync.sql.MySQL;
import de.kenjih.twitchsync.utils.objects.By;
import de.kenjih.twitchsync.utils.objects.SyncUser;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SQLData {

    //TWITCHID, DISCORDID, MINECRAFTUUID

    public void createTable(){
        try {
            PreparedStatement ps = MySQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS userdata (TWITCHID VARCHAR(500), DISCORDID TEXT, MINECRAFTUUID TEXT, PRIMARY KEY(TWITCHID))");
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void unlink(SyncUser user){
        try {
            PreparedStatement ps = MySQL.getConnection().prepareStatement("DELETE FROM userdata WHERE TWITCHID = ?");
            ps.setString(1, user.getTwitchId());
            ps.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public boolean isUserExists(SyncUser user){
        try {
            PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT UUID FROM userdata WHERE TWITCHID = ?");
            ps.setString(1, user.getTwitchId());
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public SyncUser set(SyncUser user){
        if(user == null) return null;
        if(isUserExists(user)){
            try {
                PreparedStatement ps = MySQL.getConnection().prepareStatement("UPDATE userdata SET TWITCHID = ? WHERE DISCORDID = ?");
                ps.setString(1, user.getTwitchId());
                ps.setString(2, user.getDiscordId());
                ps.executeUpdate();

                ps = MySQL.getConnection().prepareStatement("UPDATE userdata SET DISCORDID = ? WHERE TWITCHID = ?");
                ps.setString(1, user.getDiscordId());
                ps.setString(2, user.getTwitchId());
                ps.executeUpdate();

                ps = MySQL.getConnection().prepareStatement("UPDATE userdata SET MINECRAFTUUID = ? WHERE TWITCHID = ?");
                ps.setString(1, user.getUuid().toString());
                ps.setString(2, user.getTwitchId());
                ps.executeUpdate();

            } catch (SQLException e){
                e.printStackTrace();
            }
        } else {
            try {
                PreparedStatement ps = MySQL.getConnection().prepareStatement("INSERT INTO userdata (TWITCHID, DISCORDID, MINECRAFTUUID) VALUES (?,?,?)");
                ps.setString(1, user.getTwitchId());
                ps.setString(2, user.getDiscordId());
                ps.setString(3, user.getUuid().toString());
                ps.executeUpdate();
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
        return user;
    }

    public SyncUser getUser(String value, By by){
        try {
            PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT * FROM userdata WHERE " + by.toString() + " = ?");
            ps.setString(1, value);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                return new SyncUser(rs.getString("TWITCHID"), rs.getString("DISCORDID"), UUID.fromString(rs.getString("MINECRAFTUUID")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

}
