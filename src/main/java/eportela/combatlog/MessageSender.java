package eportela.combatlog;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MessageSender {

    // Send a message to a player with a specific color
    public static void sendMessage(Player player, ChatColor color, String message) {
        player.sendMessage(color + message);
    }

    // Send a message to a player with a specific color and format
    public static void sendMessage(Player player, ChatColor color, String format, Object... args) {
        player.sendMessage(color + String.format(format, args));
    }

    // Example usage:
    // MessageUtil.sendMessage(player, ChatColor.RED, "This is a red message");
    // MessageUtil.sendMessage(player, ChatColor.GREEN, "Hello, %s!", player.getName());
}