package com.ixeriox.PVEToggle;

import org.bukkit.plugin.java.*;
import java.io.File;

public class PVEToggle extends JavaPlugin {

    public static PVEToggle plugin;

    // Optimised createConfig method
    public void createConfig(int i) {
        File configFile = new File(this.getDataFolder(), "config.yml");

        switch (i) {
            case 0:
                if (!configFile.exists()) {
                    System.out.println("[PVE-Toggle] Config doesn't exist.. Creating..");
                    this.saveDefaultConfig();
                }
                break;
            case 1:
                if (!configFile.exists()) {
                    System.out.println("[PVE-Toggle][ERROR] - Config still doesn't exist.. There's an issue.. Contact Leo..");
                }
                break;
            default:
                System.out.println("[PVE-Toggle][ERROR] - Error: Undefined call '" + i + "' at createConfig(int)");
                break;
        }
    }

    // Optimised checkWorld method
    public boolean checkWorld(String world) {
        String worldPath = "PVEToggle.worlds." + world;
        return this.check_status(worldPath + ".disabled");
    }

    public void write(String message, String label) {
        System.out.println("[PVE-Toggle][" + label + "] " + message);
    }

    @Override
    public void onEnable() {
        System.out.println("[PVE-Toggle] - Enabled!");
        System.out.println("[PVE-Toggle] - Checking config..");
        createConfig(0);
        applyUpdates();
        System.out.println("[PVE-Toggle] - Debug mode: " + isDebugMode());
        System.out.println("[PVE-Toggle] - Registering events..");

        getServer().getPluginManager().registerEvents(new Events(this), this);
        write("Registered successfully!", "Events");
        this.getCommand("pve").setExecutor(new Commands(this));
    }

    public boolean isDebugMode() {
        return this.getConfig().getBoolean("PVEToggle.debug-mode");
    }

    // Optimised applyUpdates method
    public void applyUpdates() {
        System.out.println("[PVE-Toggle] Checking for potential configuration updates within versions!");
        int updatesApplied = 0;

        // List of config paths and their default values
        String[][] updates = {
                {"PVEToggle.stopmobdmg", "false"},
                {"PVEToggle.npc_attack_message", "false"},
                {"PVEToggle.no_perms", "&4You do not have the required permissions!"},
                {"PVEToggle.getNullMsg", "null"},
                {"PVEToggle.debug-mode", "false"},
                {"PVEToggle.permission_foradmin", "PVEToggle.admin"},
                {"PVEToggle.admin_togglemsg", "&B{user}'s status was successfully toggled: {status}"},
                {"PVEToggle.admin_notfoundmsg", "[PVE-Toggle] {user} not found!"},
                {"PVEToggle.RequirementMsg", "[PVE-Toggle] Unfortunately; You didn't meet the argument requirements. You need: {requirement} || You entered: {current}"},
                {"PVEToggle.allowDmgMessage", "[PVE-Toggle][CONFIG] Mob Damage has been: {value}"},
                {"PVEToggle.customStatusOn", "on"},
                {"PVEToggle.customStatusOff", "off"},
                {"PVEToggle.userStatusMsg", "[PVE-Toggle] {user}'s PVE status is currently set to {status}"},
                {"PVEToggle.admin_toggled", "[PVE-Toggle] Your PVE status has been force toggled {status}"},
                {"PVEToggle.nohitmessage", "&4[PVE-Toggle] Mob Damage whilst your PVE status is on has been disabled!"},
                {"PVEToggle.shownohitmessagetoplayersonmobhit", "true"}
        };

        // Apply updates for missing or outdated configuration keys
        for (String[] update : updates) {
            String key = update[0];
            String defaultValue = update[1];
            if (this.getConfig().getString(key) == null) {
                updatesApplied++;
                System.out.println("[PVE-Toggle] Applying patch to " + key);
                this.getConfig().set(key, defaultValue);
                System.out.println("[PVE-Toggle] Done!");
            }
        }

        // Report the result of the updates
        if (updatesApplied > 0) {
            System.out.println("[PVE-Toggle] Finished! There were " + updatesApplied + " updates! Edit your config for best experience!");
        } else {
            System.out.println("[PVE-Toggle] Finished! There were 0 updates! Awesome!");
        }

        this.saveConfig();
    }

    // Optimised checkEnabled method (String comparison fix)
    public boolean checkEnabled(String name) {
        String status = this.getConfig().getString("PVEToggle.users." + name + ".status");
        return "on".equals(status);
    }

    // Optimised check method
    public String check(String key) {
        String result = this.getConfig().getString(key, "off");
        if (isDebugMode()) {
            write("check called for '" + key + "' returned: " + result, "check()");
        }
        return result;
    }

    // Optimised check_status method
    public boolean check_status(String key) {
        boolean value = Boolean.parseBoolean(this.getConfig().getString(key, "false"));
        if (isDebugMode()) {
            write("Boolean check called for '" + key + "' returned: " + value, "check_status()");
        }
        return value;
    }
}
