package com.sendev.mooperms.commands;

import com.sendev.mooperms.MooPerms;
import com.sendev.mooperms.permission.PermissionStats;
import com.sendev.mooperms.utils.Permissions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MooCommand extends Commander
{

    /**
     * Creates a new instance of MooCommand and
     * sets up our plugin instance and envoyer.
     *
     * @param MooPerms plugin
     */
    public MooCommand(MooPerms plugin)
    {
        super(plugin);
    }

    /**
     * The player command method.
     *
     * @param Player   player
     * @param Command  command
     * @param String[] args
     * @return boolean
     */
    @Override
    public boolean runPlayerCommand(Player player, Command command, String[] args)
    {
        if (!player.hasPermission(Permissions.RELOAD)) {
            envoyer().missingPermission(player, Permissions.RELOAD);
            return false;
        }

        envoyer().sendMessage(player, "&8[&aツ&8]&m-&8[ &aReloading MooPerms..");
        plugin().reloadConfigurationRepositories();

        plugin().getPermissions().removeAll();
        envoyer().sendMessage(player, "&8[&aツ&8]&m-&8[ &a &f &2- &aAll old permissions were removed and updated.");

        PermissionStats stats = plugin().getPermissions().loadFromConfiguration();
        envoyer().sendMessage(player, "&8[&aツ&8]&m-&8[ &a &f &2- &2" + stats.getParent() + " &aparent and &2" + stats.getChildren() + " &achild permissions were loaded.");

        envoyer().sendMessage(player, "&8[&aツ&8]&m-&8[ &aMooPerms was reloaded successfully!");

        return true;
    }

    /**
     * The console command method.
     *
     * @param CommandSender sender
     * @param Command       command
     * @param String[]      args
     * @return boolean
     */
    @Override
    public boolean runConsoleCommand(CommandSender sender, Command command, String[] args)
    {
        envoyer().sendMessage(sender, "[+]-[ Reloading MooPerms..");
        plugin().reloadConfigurationRepositories();

        plugin().getPermissions().removeAll();
        envoyer().sendMessage(sender, "[+]-[     - All old permissions were removed and updated.");

        PermissionStats stats = plugin().getPermissions().loadFromConfiguration();
        envoyer().sendMessage(sender, "[+]-[     - " + stats.getParent() + " parent and " + stats.getChildren() + " child permissions were loaded.");

        envoyer().sendMessage(sender, "[+]-[ MooPerms was reloaded successfully!");

        return true;
    }

}
