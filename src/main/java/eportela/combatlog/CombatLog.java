package eportela.combatlog;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class CombatLog extends JavaPlugin implements Listener {

    private PvPHandler pvpHandler;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        pvpHandler = new PvPHandler(this);
        getLogger().info("CombatLog was loaded correctly!");
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        Entity damagerEntity = event.getDamager();
        Entity victimEntity = event.getEntity();

        if (damagerEntity instanceof Player && victimEntity instanceof Player) {
            Player damager = (Player) damagerEntity;
            Player victim = (Player) victimEntity;
            pvpHandler.activatePvPMode(damager);
            pvpHandler.activatePvPMode(victim);
        }
        /*else if (damagerEntity instanceof Player ) {
            Player damager = (Player) damagerEntity;
            pvpHandler.activatePvPMode(damager);
        }
        else if (victimEntity instanceof Player) {
            Player victim = (Player) victimEntity;
            pvpHandler.activatePvPMode(victim);
        }*/
    }

    @Override
    public void onDisable() {
        getLogger().info("CombatLog was disabled correctly!");
    }
}
