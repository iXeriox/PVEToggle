package com.ixeriox;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.UUID;

public final class PVEToggleV2 extends JavaPlugin {

    public static PVEToggleV2 instance;
    public static ArrayList<UUID> toggledPlayers = new ArrayList<>();
    public static Events events;
    public static Commands commands;


    public static PVEToggleV2 getInstance(){
        return instance;
    }

    public void write(String msg, String label){
        getLogger().info("[" + label + "] -> " + msg);
    }

    public String getConfigurationValue(String path){
        return getConfig().getString(path);
    }
    
    public boolean hasUpdate(){
        try {
            java.net.URL url = new java.net.URL("https://api.spigotmc.org/legacy/update.php?resource=67738");
            java.net.HttpURLConnection connection = (java.net.HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
    
            try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(connection.getInputStream()))) {
                String latestVersion = reader.readLine();
                return !getDescription().getVersion().equalsIgnoreCase(latestVersion);
            }
        } catch (Exception e) {
            write("Failed to check for updates: " + e.getMessage(), "UpdateChecker");
            return false;
        }
    }
    


    @Override
    public void onEnable() {
        write("Plugin enabled", "Event");
        write("Version " + getDescription().getVersion(), "Event");
        if (hasUpdate()) {
            getLogger().warning("[Updator] -> A new version of PVEToggle is available!");
        }
        write("Loading config..", "Event");
        if (!getDataFolder().exists()) {
            saveDefaultConfig();
            write("Config not found, created new one, please edit accordingly.", "Event");
        }
        instance = this;
        events = new Events(this);
        commands = new Commands(this);
        getCommand("pve").setExecutor(commands);
        getServer().getPluginManager().registerEvents(events, this);

    }

    @Override
    public void onDisable() {
        write("Plugin disabled", "Event");
    }
    public static boolean isToggled(Player player){
        return toggledPlayers.contains(player.getUniqueId());
    }

    public boolean isInCombat(Player player){
        return events != null && events.isInCombat(player);
    }

    public boolean togglePlayer(Player player){
        if (isInCombat(player)){
            return false;
        } else {
            if(toggledPlayers.contains(player.getUniqueId())){
                toggledPlayers.remove(player.getUniqueId());
            } else {
                toggledPlayers.add(player.getUniqueId());
            }
            return true;
        }
    }
}
