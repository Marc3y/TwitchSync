package de.kenjih.twitchsync.utils;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Config {

    private Configuration configuration;
    private File file;
    private File folder;

    public Config(String name){
        this.folder = new File(ProxyServer.getInstance().getPluginsFolder() + "/TwitchSync");
        if(!this.folder.exists()){
            this.folder.mkdirs();
        }
        this.file = new File(ProxyServer.getInstance().getPluginsFolder() + "/TwitchSync/" + name + ".yml");
        try {
            if(!this.file.exists()){
                this.file.createNewFile();
            }
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
            configuration.set("TwitchSync", "Codet by Marcey & Chilliger");
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Configuration getConfig() {
        return configuration;
    }

    public void saveConfig(){
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
