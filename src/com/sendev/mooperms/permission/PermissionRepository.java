package com.sendev.mooperms.permission;

import com.sendev.mooperms.MooPerms;
import com.sendev.mooperms.config.Configuration;
import com.sendev.mooperms.config.ConfigurationRepository.ConfigFile;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public class PermissionRepository
{

    /**
     * The MooPerms plugin instance.
     *
     * @var MooPerms plugin
     */
    private final MooPerms plugin;

    /**
     * This will work as our permission placeholder,
     * allowing us to keep track of which permissions
     * to overwrite, reset, update and delete.
     *
     * @var HashMap permissions
     */
    private final HashMap<String, PermissionState> permissions = new HashMap<>();

    /**
     * Creates a new instance of the permission repository class.
     *
     * @param MooPerms plugin
     */
    public PermissionRepository(MooPerms plugin)
    {
        this.plugin = plugin;
    }

    /**
     * Loads our local and global configuration
     * files into memory and assigning the permission
     * nodes to their parent permissions.
     *
     * @return PermissionStats stats
     */
    public PermissionStats loadFromConfiguration()
    {
        PermissionStats stats = new PermissionStats();

        // Loops through our configurtation files and load in our permission nodes.
        for (ConfigFile file : ConfigFile.values()) {
            Configuration config = plugin.getConfigs().getConfiguration(file);

            // Checks to see if we have the 'permissions' node in the config, if 
            // we don't we're just gonna skip the entire configuration file.
            if (!config.getConfig().contains("permissions")) {
                continue;
            }

            // Creates our permission key set
            Map<String, Object> data = config.getConfig().getConfigurationSection("permissions").getValues(false);

            for (String node : data.keySet()) {
                // Checks to see if we actually have a parent permission 
                // node, if we don't we will skip the permission index
                if (config.getConfig().contains("permissions." + node + ".parent")) {
                    String parent = config.getConfig().getString("permissions." + node + ".parent");
                    stats.addParent();

                    PermissionDefault defaultState = null;

                    // Overwrites our default state if we have one in our config
                    if (config.getConfig().contains("permissions." + node + ".default")) {
                        defaultState = PermissionDefault.getByName(
                                config.getConfig().getString("permissions." + node + ".default")
                        );
                    }

                    // Overwrites our default state if it wasen't found in 
                    // our config or the value from our config is invalid
                    if (defaultState == null) {
                        defaultState = PermissionDefault.FALSE;
                    }

                    // Gets the single child permission carrir
                    if (config.getConfig().contains("permissions." + node + ".child")) {
                        String child = config.getConfig().getString("permissions." + node + ".child", null);

                        if (child == null) {
                            continue;
                        }

                        stats.addChild();

                        addChildPermission(parent, node, defaultState);
                    }

                    // Gets the multiple permission carrir
                    if (config.getConfig().contains("permissions." + node + ".children")) {
                        for (String child : config.getConfig().getStringList("permissions." + node + ".children")) {
                            stats.addChild();
                            addChildPermission(parent, child, defaultState);
                        }
                    }
                }
            }
        }

        return stats;
    }

    /**
     * Adds a new permission to the permission list, the
     * permission will also be registered for bukkit/spigot.
     *
     * @param String            node
     * @param PermissionDefault state
     */
    public void addPermission(String node, PermissionDefault state)
    {
        if (node == null) {
            return;
        }

        node = node.toLowerCase();

        Permission permission = getPermission(node);

        if (permission == null) {
            if (state == null) {
                state = PermissionDefault.FALSE;
            }

            permission = new Permission(node);
        }

        permission.setDefault(state);

        addPermission(permission);

        if (!permissions.containsKey(node)) {
            permissions.put(node, new PermissionState(state));
        }
    }

    /**
     * Adds a new permission to the permission list as well
     * as it's parent permission node, the permission will
     * also be registered for bukkit/spigot.
     *
     * @param String            parent
     * @param String            child
     * @param PermissionDefault state
     */
    public void addChildPermission(String parent, String child, PermissionDefault state)
    {
        Permission permission = getPermission(parent);

        if (permission == null) {
            addPermission(parent, PermissionDefault.FALSE);
            permission = getPermission(parent);
        } else {
            if (!permissions.containsKey(parent)) {
                permissions.put(parent, new PermissionState(state));
            }
        }

        addPermission(child, state);
        getPermission(child).addParent(permission, true);

        permissions.get(parent).addChild(child);
    }

    /**
     * Removes the given permission node from the
     * permissions list and from bukkit/spigot.
     *
     * @param String node
     */
    public void removePermission(String node)
    {
        if (node == null) {
            return;
        }
        node = node.toLowerCase();

        Permission permission = getPermission(node);

        if (permission != null) {
            permission.setDefault(PermissionDefault.FALSE);
        }

        if (permissions.containsKey(node)) {
            PermissionState state = permissions.remove(node);

            if (state.hasChildren()) {
                for (String child : state.getChilderen()) {
                    removePermission(child);
                }
            }
        }

        plugin.getServer().getPluginManager().removePermission(node);

        for (Permissible perm : plugin.getServer().getPluginManager().getPermissionSubscriptions(node)) {
            perm.recalculatePermissions();
        }
    }

    /**
     * Removes all of the permissions from permissions
     * list and from bukkit/spigot.
     *
     * @return void
     */
    public void removeAll()
    {
        for (String node : permissions.keySet()) {
            Permission permission = getPermission(node);

            if (permission != null) {
                permission.setDefault(PermissionDefault.FALSE);
            }

            PermissionState state = permissions.get(node);

            if (state.hasChildren()) {
                for (String child : state.getChilderen()) {
                    plugin.getServer().getPluginManager().removePermission(child);
                }
            }

            plugin.getServer().getPluginManager().removePermission(node);
        }

        permissions.clear();
    }

    /**
     * Gets the bukkit/spigot permission object from the given node.
     *
     * @param String node
     * @return Permission
     */
    private Permission getPermission(String node)
    {
        if (node == null) {
            return null;
        }
        return plugin.getServer().getPluginManager().getPermission(node);
    }

    /**
     * Adds a permission object to the plugin manager so
     * that parent-to-child relationships will be enabled.
     *
     * @param Permission permission
     * @return void
     */
    private void addPermission(Permission permission)
    {
        if (getPermission(permission.getName()) == null) {
            plugin.getServer().getPluginManager().addPermission(permission);
        }
    }
}
