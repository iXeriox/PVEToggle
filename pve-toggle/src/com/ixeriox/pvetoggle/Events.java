package com.ixeriox.pvetoggle;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;


public class Events implements Listener {
	private static PVEToggle PVEToggle;
	public Events(PVEToggle PVEToggle) {
		Events.PVEToggle = PVEToggle; // Store the plugin in situations where you need it.
	}
	public boolean register() {
		//Just to check it's here!
		return true;
	}
	


	//Handle Events.
	@EventHandler
	public void EntityDamageByEntityEvent( EntityDamageByEntityEvent e ) {

		if(PVEToggle.check_status("PVEToggle.worlds."+Bukkit.getPlayer(e.getEntity().getName()).getWorld().getName()+".disabled") == false) {
			//Statement 
			boolean isPlayer = false;
			boolean DisPlayer = false;
			boolean allowed = true;
			
			if(e.getEntity() instanceof Player) {
				isPlayer = true;
			}
			if(e.getDamager() instanceof Player) {
				DisPlayer = true;
			}
			//Let's resolve PVP first!
			if(isPlayer && DisPlayer && PVEToggle.check_status("PVEToggle.IncludePVP") == true) {
				allowed = false; //Means PVP mode is enabled; We must block players from fighting!				
			}
			if(isPlayer && PVEToggle.check("PVEToggle.users."+e.getEntity().getName()+".status").equalsIgnoreCase("on")) {
				allowed = false;
			}
			if(DisPlayer && !isPlayer && PVEToggle.check_status("PVEToggle.stopmobdmg") == true) {
				if(PVEToggle.check_status("PVEToggle.shownohitmessagetoplayersonmobhit") == true) {
					if(PVEToggle.check("PVEToggle.nohitmessage").equalsIgnoreCase("null")) {
						e.getDamager().sendMessage(ChatColor.DARK_RED + "[PVE-Toggle] Mob Damage whilst your PVE status is on has been disabled!");
					} else {
					e.getDamager().sendMessage(ChatColor.translateAlternateColorCodes('&', PVEToggle.check("PVEToggle.nohitmessage")));
					}
				}
				allowed = false;
			}
			if(!allowed) {
				e.setCancelled(true);
			}
			if(PVEToggle.isDebugMode()) {
			PVEToggle.write("Entity("+e.getEntity().getName()+") > isPlayer? "+isPlayer+" Damager("+e.getDamager().getName()+") > isPlayer? "+DisPlayer+" :: Allowed? "+allowed,"EntityDamageByEntityEvent");
			}
		}
	}






}
