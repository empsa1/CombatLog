package eportela.combatlog;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PvPHandler {
    private final String IN_PVP_MODE_MESSAGE =
            "You are now in combat for 30seconds!\nDo not logout during this time or staff will be warned!";
    private final String OUT_PVP_MODE_MESSAGE =
            "You are no longer in combat.\nYou may now use commands and logout!\n";

    private CombatLog plugin;
    private HashMap<UUID, Long> pvpCooldowns = new HashMap<>();
    private final Set<UUID> pvpPlayers = new HashSet<>();
    private final HashMap<UUID, PermissionAttachment> permissionAttachments = new HashMap<>();
    private final int PVP_MODE_DURATION = 30 * 20; // 30 seconds in ticks

    public PvPHandler(CombatLog plugin) {
        this.plugin = plugin;
    }
    public void activatePvPMode(Player player) {
        UUID playerId = player.getUniqueId();
        if (!pvpCooldowns.containsKey(playerId)) {
            pvpCooldowns.put(playerId, System.currentTimeMillis() + PVP_MODE_DURATION);
            MessageSender.sendMessage(player, ChatColor.RED, IN_PVP_MODE_MESSAGE);
            restrictCommands(player);
            startPvPTimer(playerId);
        } else {
            pvpCooldowns.put(playerId, System.currentTimeMillis() + PVP_MODE_DURATION);
        }
    }
    private void startPvPTimer(UUID playerId) {
        new BukkitRunnable() {
            @Override
            public void run() {
                pvpCooldowns.remove(playerId);
                Player player = Bukkit.getPlayer(playerId);
                if (player != null) {
                    MessageSender.sendMessage(player, ChatColor.GREEN, OUT_PVP_MODE_MESSAGE);
                    allowCommands(player);
                }
            }
        }.runTaskLater(plugin, PVP_MODE_DURATION);
    }
    private void restrictCommands(Player player) {
        UUID playerId = player.getUniqueId();
        if (!pvpPlayers.contains(playerId)) {
            return; // Player not in PvP mode, no need to restrict commands
        }

        if (!permissionAttachments.containsKey(playerId)) {
            PermissionAttachment attachment = player.addAttachment(plugin);
            permissionAttachments.put(playerId, attachment);
        }

        PermissionAttachment attachment = permissionAttachments.get(playerId);
        attachment.setPermission("*", false);
        player.recalculatePermissions();
    }
    private void allowCommands(Player player) {
        UUID playerId = player.getUniqueId();
        if (!pvpPlayers.contains(playerId)) {
            return; // Player not in PvP mode, no need to allow commands
        }
        PermissionAttachment attachment = permissionAttachments.remove(playerId);
        if (attachment != null) {
            attachment.remove();
        }
        player.recalculatePermissions();
    }
}