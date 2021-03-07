package com.ixeriox.pvetoggle;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;


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
	public void EntityTargetEvent(org.bukkit.event.entity.EntityTargetEvent e){
		Entity ent = e.getEntity();
		Entity tar = e.getTarget();
		boolean checkWorld = false;
		boolean allowed = true;
		boolean isEnabled = false;
		boolean ent_isPlayer = false;
		boolean tar_isPlayer = false;
		
		if(ent instanceof Player) {
			checkWorld = PVEToggle.checkWorld(ent.getWorld().getName());
			isEnabled = PVEToggle.checkEnabled(ent.getName());
			ent_isPlayer = true;
			
		}
		if(tar instanceof Player) {
			checkWorld = PVEToggle.checkWorld(tar.getWorld().getName());
			isEnabled = PVEToggle.checkEnabled(tar.getName());
			tar_isPlayer = true;
		}
		if(tar_isPlayer && ent_isPlayer && PVEToggle.check_status("PVEToggle.IncludePVP") == true) {
				allowed = false; //Means PVP mode is enabled; We must block players from fighting!	
		}
		if(ent_isPlayer && !tar_isPlayer) {
			allowed = false;
		}
		if(tar_isPlayer && !ent_isPlayer) {
			allowed = false;
		}
		if(!allowed && isEnabled && checkWorld) {
			e.setTarget(null);
			e.setCancelled(true);
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onEntityTarget(EntityTargetLivingEntityEvent evt) {
	    if (evt.getTarget() instanceof Player && PVEToggle.checkEnabled(evt.getTarget().getName()) && PVEToggle.checkWorld(evt.getTarget().getWorld().getName()) ) {
	        evt.setCancelled(true);
	        evt.setTarget(null);
	    }
	// Incase the mob already has a target...
	    if (evt.getEntity() instanceof Mob  && PVEToggle.checkEnabled(evt.getEntity().getName()) && PVEToggle.checkWorld(evt.getEntity().getWorld().getName())) {
	        Mob mob = (Mob) evt.getEntity();
	        if (mob.getTarget() instanceof Player) {
	            mob.setTarget(null);
	        }
	    }
	}
	
	@EventHandler
	public void onEntityTargetLiving(org.bukkit.event.entity.EntityTargetLivingEntityEvent e)
	{

	
		if(PVEToggle.check_status("PVEToggle.worlds") != false){
		if(PVEToggle.check_status("PVEToggle.worlds."+Bukkit.getPlayer(e.getEntity().getName()).getWorld().getName()) != false) {
		if(PVEToggle.check_status("PVEToggle.worlds."+Bukkit.getPlayer(e.getEntity().getName()).getWorld().getName()+".disabled") != false) {
			//This means this world has been switched off and cannot use PVEToggle!
			return;
		}
		} else {
			//Cannot find world in list! Ignore. This isn't fatal.
		}
		} else {
			//Worlds doesn't exist. Ignore this error.
		}
			//Statement 
			boolean isPlayer = false;
			boolean DisPlayer = false;
			boolean allowed = true;
			if(e.getEntity() == null || e.getTarget() == null) {
				return;
			}
			if(e.getEntity() instanceof Player) {
				isPlayer = true;
			}
			if(e.getTarget() instanceof Player) {
				DisPlayer = true;
			}
			//Let's resolve PVP first!
			if(isPlayer && DisPlayer && PVEToggle.check_status("PVEToggle.IncludePVP") == true) {
				allowed = false; //Means PVP mode is enabled; We must block players from fighting!				
			}
			if(isPlayer && PVEToggle.check("PVEToggle.users."+e.getEntity().getName()+".status").equalsIgnoreCase("on")) {
				allowed = false;
			}
			if(!isPlayer && DisPlayer && PVEToggle.check("PVEToggle.users."+e.getTarget().getName()+".status").equalsIgnoreCase("on")) {
				allowed = false;
			}
			if(!allowed) {
				e.setTarget(null);
				e.setCancelled(true);
			}
			if(PVEToggle.isDebugMode()) {
				if(e.getTarget() == null) {
					return;
				}
				if(e.getTarget() == null) {
					return;
				}
				
			PVEToggle.write("Entity("+e.getEntity().getName()+") > isPlayer? "+isPlayer+" Damager("+e.getTarget().getName()+") > isPlayer? "+DisPlayer+" :: Allowed? "+allowed,"onEntityTargetLiving");
		}
	}
	
	
	@EventHandler
	public void EntityDamageByEntityEvent( EntityDamageByEntityEvent e ) {
			Entity dmgr = e.getDamager();
			Entity ent = e.getEntity();
			boolean isEnabled = false;
			boolean checkWorld = false;
			boolean allowed = true; 
			boolean dmgr_isPlayer = false;
			boolean ent_isPlayer = false;
			
			if(dmgr instanceof Player) {
				dmgr_isPlayer = true;
				isEnabled = PVEToggle.checkEnabled(dmgr.getName());
				checkWorld = PVEToggle.checkWorld(dmgr.getWorld().getName());
			}
			if(ent instanceof Player) {
				ent_isPlayer = true;
				isEnabled = PVEToggle.checkEnabled(ent.getName());
				checkWorld = PVEToggle.checkWorld(ent.getWorld().getName());
			}
			
			if(checkWorld || !isEnabled ) {
				return;
			}
			
			if(ent_isPlayer && dmgr_isPlayer) {
				//PVP
				if(PVEToggle.check_status("includePVP")) {
					allowed = false;
				}
			}
			if(ent_isPlayer && !dmgr_isPlayer || dmgr_isPlayer && !ent_isPlayer) {
				//One of these is a mob.
				if(dmgr_isPlayer && PVEToggle.check_status("PVEToggle.stopmobdmg")) {
					allowed = false;
					if(PVEToggle.check_status("PVEToggle.shownohitmessagetoplayersonmobhit")) {
						dmgr.sendMessage(ChatColor.translateAlternateColorCodes('&', PVEToggle.check("PVEToggle.nohitmessage")));
					}
				}
				if(!dmgr_isPlayer) {
					allowed = false;
				}
			}
			if(!allowed) {
				e.setCancelled(true);
			}
	}






}
