package com.sendev.mooperms;

import com.sendev.mooperms.commands.MooCommand;
import com.sendev.mooperms.config.ConfigurationRepository;
import com.sendev.mooperms.contracts.MooCoreContract;
import com.sendev.mooperms.permission.PermissionRepository;
import org.bukkit.plugin.java.JavaPlugin;

public class MooPerms extends JavaPlugin implements MooCoreContract
{

    /**
     * The configuration repository handler object.
     *
     * @var ConfigurationRepository configs
     */
    private ConfigurationRepository configs;

    /**
     * The permissions repository handler object.
     *
     * @var PermissionRepository permissions
     */
    private PermissionRepository permissions;

    /**
     * JavaPlugin onEnable method, this method
     * will run by itself when the plugin is loaded.
     *
     * @return void
     */
    @Override
    public void onEnable()
    {
        configs = new ConfigurationRepository(this);
        permissions = new PermissionRepository(this);

        permissions.loadFromConfiguration();

        getCommand("mooperm").setExecutor(new MooCommand(this));
    }

    /**
     * JavaPlugin onDisable method, this method
     * will run by itself when the plugin is disabled.
     *
     * @return void
     */
    @Override
    public void onDisable()
    {
        permissions.removeAll();
    }

    /**
     * Returns you the configuration repositories object.
     *
     * @return ConfigurationRepository configs
     */
    @Override
    public ConfigurationRepository getConfigs()
    {
        return configs;
    }

    /**
     * Returns you the permission repositories object.
     *
     * @return PermissionRepository permissions
     */
    @Override
    public PermissionRepository getPermissions()
    {
        return permissions;
    }

    /**
     * Reloads the configurations associated with the config repository.
     *
     * @return void
     */
    @Override
    public void reloadConfigurationRepositories()
    {
        configs.reload();
    }
}
