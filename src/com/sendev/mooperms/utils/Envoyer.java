package com.sendev.mooperms.utils;

import com.sendev.mooperms.MooPerms;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Envoyer
{

    /**
     * The plugin logger instance.
     *
     * @var Logger logger
     */
    private final Logger logger;
    
    /**
     * Creates a new envoyer instance.
     * 
     * @param MooPerms plugin 
     */
    public Envoyer(MooPerms plugin)
    {
        this.logger = plugin.getLogger();
    }

    /**
     * Gives you the plugin logger instance.
     *
     * @return Logger logger
     */
    public Logger getLogger()
    {
        return logger;
    }

    /**
     * Colorize a message.
     *
     * @param String message
     * @return String message
     */
    public String colorize(String message)
    {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    /**
     * Colorize a list of messages.
     *
     * @param List messages
     * @return List messages
     */
    public List<String> colorize(List<String> messages)
    {
        List<String> message = new ArrayList<>();
        for (String str : messages) {
            if (str == null) {
                continue;
            }
            message.add(colorize("&7" + str));
        }
        return message;
    }

    /**
     * Decolorizes a message back to white text with no formating.
     *
     * @param String message
     * @return String message
     */
    public String decolorize(String message)
    {
        return ChatColor.stripColor(message);
    }

    /**
     * Sends a missing permission message to the given player.
     *
     * @param Player player
     * @param String permission
     * @return void
     */
    public void missingPermission(Player player, String permission)
    {
        player.sendMessage(ChatColor.RED + "Influent permissions to execute this command.");
        player.sendMessage(ChatColor.RED + "You're missing the permission node " + ChatColor.ITALIC + permission);
    }

    /**
     * Sends a missing permission message to the given command sender.
     *
     * @param CommandSender player
     * @param String        permission
     * @return void
     */
    public void missingPermission(CommandSender player, String permission)
    {
        player.sendMessage(ChatColor.RED + "Influent permissions to execute this command.");
        player.sendMessage(ChatColor.RED + "You're missing the permission node " + ChatColor.ITALIC + permission);
    }

    /**
     * Send a message to the given player or console object.
     *
     * @param CommandSender player
     * @param String        message
     */
    public void sendMessage(CommandSender player, String message)
    {
        player.sendMessage(colorize(message));
    }

    public void broadcast(String string)
    {
        for (Player player : Bukkit.getOnlinePlayers()) {
            sendMessage(player, string);
        }
    }
}
