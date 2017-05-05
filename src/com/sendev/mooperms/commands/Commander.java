package com.sendev.mooperms.commands;

import com.sendev.mooperms.MooPerms;
import com.sendev.mooperms.utils.Envoyer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class Commander implements CommandExecutor
{

    /**
     * The MooPerms plugin instance.
     *
     * @var MooPerms plugin
     */
    private final MooPerms plugin;

    /**
     * The envoyer chat object, makes it easier
     * to talk to players and the console.
     *
     * @var Envoyer envoyer
     */
    private final Envoyer envoyer;

    /**
     * Creates a new instance of the command class.
     *
     * @param MooPerms plugin
     */
    public Commander(MooPerms plugin)
    {
        this.plugin = plugin;

        this.envoyer = new Envoyer(plugin);
    }

    /**
     * Returns the MooPerms plugin instance.
     *
     * @return MooPerms plugin
     */
    protected MooPerms plugin()
    {
        return plugin;
    }

    /**
     * Returns the MooPerms envoyer instance.
     *
     * @return Envoyer envoyer
     */
    protected Envoyer envoyer()
    {
        return envoyer;
    }

    /**
     * This method will be executed by JavaPlugin(Bukkit/Spigot)
     * when a command is executed that is linked to MooPerm.
     *
     * @param CommandSender sender
     * @param Command       command
     * @param String        label
     * @param String[]      args
     * @return boolean
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player) {
            return runPlayerCommand((Player) sender, command, args);
        }
        return runConsoleCommand(sender, command, args);
    }

    /**
     * The player command method.
     *
     * @param Player   player
     * @param Command  command
     * @param String[] args
     * @return boolean
     */
    public boolean runPlayerCommand(Player player, Command command, String[] args)
    {
        return false;
    }

    /**
     * The console command method.
     *
     * @param CommandSender sender
     * @param Command       command
     * @param String[]      args
     * @return boolean
     */
    public boolean runConsoleCommand(CommandSender sender, Command command, String[] args)
    {
        return false;
    }
}
