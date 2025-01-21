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
    


    @Override
    public void onEnable() {
        write("Plugin enabled", "Event");
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
