package com.sendev.mooperms.contracts;

import com.sendev.mooperms.config.ConfigurationRepository;
import com.sendev.mooperms.permission.PermissionRepository;

public interface MooCoreContract
{

    /**
     * Gives you access to the configuration repository.
     *
     * @return ConfigurationRepository
     */
    public ConfigurationRepository getConfigs();

    /**
     * Gives you access to the permissions repository.
     *
     * @return ConfigurationRepository
     */
    public PermissionRepository getPermissions();

    /**
     * Reloads the configurations associated with the config repository.
     *
     * @return void
     */
    public void reloadConfigurationRepositories();
}
