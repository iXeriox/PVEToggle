package com.ixeriox.pvetoggle;

import org.bukkit.plugin.java.*;
import java.io.File;

import org.bukkit.entity.Player;

public class PVEToggle extends JavaPlugin {

	public static PVEToggle plugin;
	public void createConfig(int i) {
		switch( i ) {
		case 0:	
			File f = new File(this.getDataFolder(), "/");
			if(!f.exists()){
				System.out.println("[PVE-Toggle] Config doesn't exist.. Creating..");
				this.saveDefaultConfig();
			}
		break;
		case 1:
			File f1 = new File(this.getDataFolder(), "/");
			if(!f1.exists()){
				System.out.println("[PVE-Toggle][ERROR] - Config still doesn't exist.. There's an issue.. Contact Leo..");
			}
		break;
		default:
			System.out.println("[PVE-Toggle][ERROR] - Error: Undefined call '"+i+"' at createConfig(int)");
		break;
		}
	}
	   public PVEToggle getInstance() {
	        return this;
	    }
	public boolean allow(Player player, String type) {
		switch( type ) {
		default:
			if(this.isDebugMode()) {
				System.out.println("[PVE-Toggle] Uncaught type: "+type+"; Player: "+player.getName() + " - World: "+player.getWorld().getName());
			}
			return false;
		}
	}


	public void write(String string, String string2) {
		System.out.println("[PVE-Toggle]["+string2+"] "+string);
	}
    public void onEnable() {
    	System.out.println("[PVE-Toggle] - Enabled!");
    	System.out.println("[PVE-Toggle] - Checking config..");
    	createConfig(0);
    	applyUpdates();
    	System.out.println("[PVE-Toggle] - Debug mode: "+isDebugMode());
    	System.out.println("[PVE-Toggle] - Registering events..");

    	getServer().getPluginManager().registerEvents(new Events(this), this);
		write("Registered successfully!","Events");
    	this.getCommand("pve").setExecutor(new Commands(this));

    }
    public boolean isDebugMode() {
    		return this.getConfig().getBoolean("PVEToggle.debug-mode");
    }
    public void applyUpdates() {
    	//StopMobDmg update -- #02-06-19
    	System.out.println("[PVE-Toggle] Checking for potential configuration updates within versions!");
    	int x = 0;
    	if(this.getConfig().getString("PVEToggle.stopmobdmg") == null) {
    		x = x+1;
    		System.out.println("[PVE-Toggle] Applying 'stopmobdmg' patch to config..");
    		this.getConfig().set("PVEToggle.stopmobdmg", false);
    		this.getConfig().set("PVEToggle.npc_attack_message", false);
    		System.out.println("[PVE-Toggle] Done!");
    	}
    	if(this.getConfig().getString("PVEToggle.no_perms") == null){
    		x = x+1;
    		System.out.println("[PVE-Toggle] 'no_perms' does not exist..creating!");
    		this.getConfig().set("PVEToggle.no_perms", "&4You do not have the required permissions!");
    	}
    	if(this.getConfig().getString("PVEToggle.getNullMsg") == null) {

    		x = x+1;
    		System.out.println("[PVE-Toggle] Applying 'getNullMsg' patch to config.yml");
    		this.getConfig().set("PVEToggle.getNullMsg", "null");
    	}
    	if(this.getConfig().getString("PVEToggle.debug-mode") == null) {

    		x = x+1;
    		System.out.println("[PVE-Toggle] Applying 'debug-mode' patch to config.yml");
    		this.getConfig().set("PVEToggle.debug-mode", false);
    		System.out.println("[PVE-Toggle] The update was successful - change 'debug-mode: false' to 'debug-mode: true' to enable more debug messages!");
    	}
    	if(this.getConfig().getString("PVEToggle.permission_foradmin") == null) {

    		x = x+1;
    		System.out.println("[PVE-Toggle] Applying new permission 'foradmin' patch to config.yml");
    		this.getConfig().set("PVEToggle.permission_foradmin", "PVEToggle.admin");
    		System.out.println("[PVE-Toggle] Done!");
    	}
    	if(this.getConfig().getString("PVEToggle.admin_togglemsg") == null) {

    		x = x+1;
    		System.out.println("[PVE-Toggle] Applying 'admin_togglemsg' patch to config.yml");
    		this.getConfig().set("PVEToggle.admin_togglemsg","&B{user}'s status was successfully toggled: {status}");
    		System.out.println("[PVE-Toggle] Done!");
    		
    	}
    	if(this.getConfig().getString("PVEToggle.admin_notfoundmsg") == null) {

    		x = x+1;
    		System.out.println("[PVE-Toggle] Applying 'admin_notfoundmsg' patch to config.yml");
    		this.getConfig().set("PVEToggle.admin_notfoundmsg","[PVE-Toggle] {user} not found!");
    		System.out.println("[PVE-Toggle] Done!");
    	}
    	if(this.getConfig().getString("PVEToggle.RequirementMsg") == null) {

    		x = x+1;
    		System.out.println("[PVE-Toggle] Applying 'RequirementMsg' patch to config.yml");
    		this.getConfig().set("PVEToggle.RequirementMsg","[PVE-Toggle]  Unfortunately; You didn't meet the argument requirements. You need: {requirement} || You entered: {current}");
    		System.out.println("[PVE-Toggle] Done!");
    		
    	}
    	if(this.getConfig().getString("PVEToggle.allowDmgMessage") == null) {

    		x = x+1;
    		System.out.println("[PVE-Toggle] Applying 'allowDmgMessage' patch to config.yml");
    		this.getConfig().set("PVEToggle.allowDmgMessage","[PVE-Toggle][CONFIG] Mob Damage has been: {value}");
    		System.out.println("[PVE-Toggle] Done!");
    		
    	}
    	if(this.getConfig().getString("PVEToggle.customStatusOn") == null) {
    		x = x+1;
    		System.out.println("[PVE-Toggle] Applying 'customStatusOn' patch to config.yml");
    		this.getConfig().set("PVEToggle.customStatusOn","on");
    		System.out.println("[PVE-Toggle] Done!");
    	}
    	if(this.getConfig().getString("PVEToggle.customStatusOff") == null) {
    		x = x+1;
    		System.out.println("[PVE-Toggle] Applying 'customStatusOff' patch to config.yml");
    		this.getConfig().set("PVEToggle.customStatusOff","off");
    		System.out.println("[PVE-Toggle] Done!");
    	}
    	if(this.getConfig().getString("PVEToggle.userStatusMsg") == null) {
    		x = x+1;
    		System.out.println("[PVE-Toggle] Applying 'userStatusMsg' patch to config.yml");
    		this.getConfig().set("PVEToggle.userStatusMsg","[PVE-Toggle] {user}'s PVE status is currently set to {status}");
    		System.out.println("[PVE-Toggle] Done!");
    		
    	}
    	if(this.getConfig().getString("PVEToggle.admin_toggled") == null) {

    		x = x+1;
    		System.out.println("[PVE-Toggle] Applying 'admin_toggled' patch to config.yml");
    		this.getConfig().set("PVEToggle.admin_toggled", "[PVE-Toggle] Your PVE status has been force toggled {status}");
    		System.out.println("[PVE-Toggle] Done! ");
    	}
    	// 17/06/19
    	if(this.getConfig().getString("PVEToggle.nohitmessage") == null) {

    		x = x+1;
    		System.out.println("[PVE-Toggle] Applying 'nohitmessage' patch to config.yml");
    		this.getConfig().set("PVEToggle.nohitmessage", "&4[PVE-Toggle] Mob Damage whilst your PVE status is on has been disabled!");
    		System.out.println("[PVE-Toggle] Done! ");
    	}
    	if(this.getConfig().getString("PVEToggle.shownohitmessagetoplayersonmobhit") == null) {
    		x = x+1;
    		System.out.println("[PVE-Toggle] Applying 'shownohitmessagetoplayersonmobhit' patch to config.yml");
    		this.getConfig().set("PVEToggle.shownohitmessagetoplayersonmobhit", true);
    		System.out.println("[PVE-Toggle] Done! ");
    	}
    	if(x != 0) {
    	if(x == 1) {
    		System.out.println("[PVE-Toggle] Finished! There was 1 update!");
    	} else {
    	System.out.println("[PVE-Toggle] Finished! There were "+x+" updates! Edit your config for best experience!");
    	}
    	} else {

    		System.out.println("[PVE-Toggle] Finished! There were 0 updates! Awesome!");
    	}
    	this.saveConfig();
    }
    public String check(String string) {
    	String returnstring = "";
    	if(this.getConfig().getString(string) == null) {
    		returnstring = "off";
    	} else {
    		returnstring = this.getConfig().getString(string);
    	}
    	if(isDebugMode()) {
    		write("check called for '"+string+"' returned: "+ returnstring, "check()");
    	}
    	return returnstring;
    }
	public boolean check_status(String string) {
    	if(isDebugMode()) {
    		write("Boolean check called for '"+string+"' returned: "+ Boolean.parseBoolean(this.getConfig().getString(string)), "check_status()");
    	}
		return Boolean.parseBoolean(this.getConfig().getString(string));
	}

}