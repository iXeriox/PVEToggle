package com.ixeriox.PVEToggle;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

public class Events implements Listener {

    private final PVEToggle plugin;

    public Events(PVEToggle plugin) {
        this.plugin = plugin;
    }

    public boolean register() {
        // Just to check if the plugin is correctly registered.
        return true;
    }

    @EventHandler
    public void onEntityTarget(org.bukkit.event.entity.EntityTargetEvent e) {
        Entity ent = e.getEntity();
        Entity tar = e.getTarget();

        if (!(ent instanceof Player) && !(tar instanceof Player)) {
            return; // Skip if neither entity is a player.
        }

        boolean isPlayer = ent instanceof Player;
        boolean targetIsPlayer = tar instanceof Player;
        boolean checkWorld = isPlayer ? plugin.checkWorld(ent.getWorld().getName()) : plugin.checkWorld(tar.getWorld().getName());
        boolean isEnabled = isPlayer ? plugin.checkEnabled(ent.getName()) : plugin.checkEnabled(tar.getName());

        if (isPlayer && targetIsPlayer && plugin.check_status("plugin.IncludePVP")) {
            e.setCancelled(true); // Block PvP if enabled.
            return;
        }

        if (isPlayer && !targetIsPlayer || !isPlayer && targetIsPlayer) {
            e.setCancelled(true); // Block damage if the target is not the same type (player/mob).
            return;
        }

        if (checkWorld && isEnabled) {
            e.setTarget(null); // Remove the target if both checks pass.
            e.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityTargetLiving(EntityTargetLivingEntityEvent evt) {
        if (evt.getTarget() instanceof Player && plugin.checkEnabled(evt.getTarget().getName())
                && plugin.checkWorld(evt.getTarget().getWorld().getName())) {
            evt.setCancelled(true);
            evt.setTarget(null);
        }

        if (evt.getEntity() instanceof Mob && plugin.checkEnabled(evt.getEntity().getName())
                && plugin.checkWorld(evt.getEntity().getWorld().getName())) {
            Mob mob = (Mob) evt.getEntity();
            if (mob.getTarget() instanceof Player) {
                mob.setTarget(null);
            }
        }
    }

    @EventHandler
    public void onEntityTargetLivingEntity(org.bukkit.event.entity.EntityTargetLivingEntityEvent e) {
        if (e.getEntity() == null || e.getTarget() == null) return;

        boolean isPlayer = e.getEntity() instanceof Player;
        boolean targetIsPlayer = e.getTarget() instanceof Player;
        boolean allowed = true;

        // Handle PVP mode if enabled
        if (isPlayer && targetIsPlayer && plugin.check_status("plugin.IncludePVP")) {
            allowed = false; // Block PvP between players.
        }

        // Block player interaction if their status is 'on'
        if (isPlayer && plugin.check("plugin.users." + e.getEntity().getName() + ".status").equalsIgnoreCase("on")) {
            allowed = false;
        }

        if (!isPlayer && targetIsPlayer && plugin.check("plugin.users." + e.getTarget().getName() + ".status").equalsIgnoreCase("on")) {
            allowed = false;
        }

        // Cancel event if not allowed
        if (!allowed) {
            e.setTarget(null);
            e.setCancelled(true);
        }

        if (plugin.isDebugMode()) {
            plugin.write("Entity(" + e.getEntity().getName() + ") isPlayer? " + isPlayer + " Damager(" + e.getTarget().getName() + ") isPlayer? " + targetIsPlayer + " :: Allowed? " + allowed, "onEntityTargetLiving");
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        Entity dmgr = e.getDamager();
        Entity ent = e.getEntity();

        boolean isEnabled = false;
        boolean checkWorld = false;
        boolean allowed = true;
        boolean dmgrIsPlayer = dmgr instanceof Player;
        boolean entIsPlayer = ent instanceof Player;

        if (dmgrIsPlayer) {
            isEnabled = plugin.checkEnabled(dmgr.getName());
            checkWorld = plugin.checkWorld(dmgr.getWorld().getName());
        }

        if (entIsPlayer) {
            isEnabled = plugin.checkEnabled(ent.getName());
            checkWorld = plugin.checkWorld(ent.getWorld().getName());
        }

        if (checkWorld || !isEnabled) {
            return; // No need to process if not enabled or world check fails
        }

        // Handle PVP and mob damage scenarios
        if (entIsPlayer && dmgrIsPlayer && plugin.check_status("includePVP")) {
            allowed = false; // Block PvP between players
        }

        if ((entIsPlayer && !dmgrIsPlayer) || (!entIsPlayer && dmgrIsPlayer)) {
            if (dmgrIsPlayer && plugin.check_status("plugin.stopmobdmg")) {
                allowed = false;
                if (plugin.check_status("plugin.shownohitmessagetoplayersonmobhit")) {
                    dmgr.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.check("plugin.nohitmessage")));
                }
            }
            if (!dmgrIsPlayer) {
                allowed = false; // Prevent mob damage
            }
        }

        if (!allowed) {
            e.setCancelled(true); // Cancel event if not allowed
        }
    }
}
