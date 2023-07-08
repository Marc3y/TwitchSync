package de.kenjih.twitchsync.sql.impl;

import de.kenjih.twitchsync.sql.MySQL;
import de.kenjih.twitchsync.utils.objects.By;
import de.kenjih.twitchsync.utils.objects.SyncUser;
import de.kenjih.twitchsync.utils.objects.TokenSetting;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SQLTokens {

    //ID, ACCESSTOKEN, REFRESHTOKEN

    public void createTable(){
        try {
            PreparedStatement ps = MySQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS tokens (ID VARCHAR(500), ACCESSTOKEN TEXT, REFRESHTOKEN TEXT, PRIMARY KEY(ID))");
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(String accesstoken){
        try {
            PreparedStatement ps = MySQL.getConnection().prepareStatement("DELETE FROM tokens WHERE ACCESSTOKEN = ?");
            ps.setString(1, accesstoken);
            ps.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public boolean isIdExists(String id){
        try {
            PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT ACCESSTOKEN FROM tokens WHERE ID = ?");
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public TokenSetting set(TokenSetting setting){
        if(isIdExists(setting.getId())){
            try {
                PreparedStatement ps = MySQL.getConnection().prepareStatement("UPDATE tokens SET ACCESSTOKEN = ? WHERE ID = ?");
                ps.setString(1, setting.getAccessToken());
                ps.setString(2, setting.getId());
                ps.executeUpdate();

                ps = MySQL.getConnection().prepareStatement("UPDATE tokens SET REFRESHTOKEN = ? WHERE ID = ?");
                ps.setString(1, setting.getRefreshToken());
                ps.setString(2, setting.getId());
                ps.executeUpdate();

                ps = MySQL.getConnection().prepareStatement("UPDATE tokens SET ID = ? WHERE ACCESSTOKEN = ?");
                ps.setString(1, setting.getId());
                ps.setString(2, setting.getAccessToken());
                ps.executeUpdate();

            } catch (SQLException e){
                e.printStackTrace();
            }
        } else {
            try {
                PreparedStatement ps = MySQL.getConnection().prepareStatement("INSERT INTO tokens (ID, ACCESSTOKEN, REFRESHTOKEN) VALUES (?,?,?)");
                ps.setString(1, setting.getId());
                ps.setString(2, setting.getAccessToken());
                ps.setString(3, setting.getRefreshToken());
                ps.executeUpdate();
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
        return setting;
    }

    public TokenSetting getTokenSettingById(String id){
        try {
            PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT * FROM tokens WHERE ID = ?");
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                return new TokenSetting(rs.getString("ID"), rs.getString("ACCESSTOKEN"), rs.getString("REFRESHTOKEN"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

}
