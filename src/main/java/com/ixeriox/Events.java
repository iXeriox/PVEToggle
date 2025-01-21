package com.ixeriox;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

import java.util.HashMap;
import java.util.UUID;

import static com.ixeriox.PVEToggleV2.instance;

public class Events implements Listener {

    private final HashMap<UUID, Long> combatTracker = new HashMap<>();
    private final boolean debugMode;

    public Events(PVEToggleV2 instance) {
        instance.write("Events registered", "Event");
        instance.getServer().getPluginManager().registerEvents(this, instance);
        this.debugMode = Boolean.parseBoolean(instance.getConfigurationValue("debug-mode.enabled"));
    }

    // Check if a player is under attack or has recently been in combat
    public boolean isInCombat(Player player) {
        if (!combatTracker.containsKey(player.getUniqueId())) {
            return false;
        }
        long lastAttackTime = combatTracker.get(player.getUniqueId());
        return (System.currentTimeMillis() - lastAttackTime) < 10000; // 10 seconds in milliseconds
    }

    // Mark a player as under attack
    private void setInCombat(Player player) {
        combatTracker.put(player.getUniqueId(), System.currentTimeMillis());
    }

    @EventHandler
    public void onEntityTargetEvent(EntityTargetEvent event) {
        if (event.getTarget() instanceof Player player && !(event.getEntity() instanceof Player) && PVEToggleV2.isToggled(player)) {
            if (debugMode)
                instance.write("Player " + player.getName() + " is in PVE mode; block", "onEntityTargetEvent");
            event.setCancelled(true);
            event.setTarget(null);
        }
    }

    @EventHandler
    public void onEntityTargetLiving(EntityTargetLivingEntityEvent event) {
        Entity entity = event.getTarget();
        if (entity instanceof Player player && PVEToggleV2.isToggled(player)) {
            if (debugMode)
                instance.write("Player " + player.getName() + " is in PVE mode; block", "EntityTargetLivingEvent");
            event.setCancelled(true);
            event.setTarget(null);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Entity dmgr = event.getDamager();
        Entity ent = event.getEntity();

        // If a player damages another player, or is damaged
        boolean dmgrIsPlayer = dmgr instanceof Player;
        boolean entIsPlayer = ent instanceof Player;

        if (dmgrIsPlayer && PVEToggleV2.isToggled((Player) dmgr) || entIsPlayer && PVEToggleV2.isToggled((Player) ent)) {
            Player player = (Player) (dmgrIsPlayer ? dmgr : ent);
            if (debugMode)
                instance.write("Player " + player.getName() + " is in PVE mode; block", "EntityDamageByEntityEvent");
            event.setCancelled(true);
        }

        // Track combat if the target or damager is a player
        if (ent instanceof Player) {
            setInCombat((Player) ent);
        }
        if (dmgr instanceof Player) {
            setInCombat((Player) dmgr);
        }
    }

    // Clean up combat tracker when players leave
    public void clearCombatTracker(Player player) {
        combatTracker.remove(player.getUniqueId());
    }
}
