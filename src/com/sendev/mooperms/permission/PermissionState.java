package com.sendev.mooperms.permission;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.permissions.PermissionDefault;

public class PermissionState
{

    /**
     * Lists of child permissions associated
     * with the parent permission node, the
     * node can be found in the hashmap
     * under the PermissionRepository.
     *
     * @var List permissions
     */
    private final List<String> permissions = new ArrayList<>();

    /**
     * Permission state.
     *
     * @var PermissionDefault state
     */
    private final PermissionDefault state;

    /**
     * Creates a new permission state instance.
     *
     * @param PermissionDefault state
     */
    public PermissionState(PermissionDefault state)
    {
        this.state = state;
    }

    /**
     * Checks to see if our parent permission node
     * has any children nodes associated with.
     *
     * @return boolean
     */
    public boolean hasChildren()
    {
        return !permissions.isEmpty();
    }

    /**
     * Gets the parent permission node state.
     *
     * @return PermissionDefault state
     */
    public PermissionDefault getState()
    {
        return state;
    }

    /**
     * Gets the parent permission nodes children nodes.
     *
     * @return List permissions
     */
    public List<String> getChilderen()
    {
        return permissions;
    }

    /**
     * Adds a new child permission node.
     *
     * @param String node
     */
    public void addChild(String node)
    {
        node = node.toLowerCase();

        if (!permissions.contains(node)) {
            permissions.add(node);
        }
    }

    /**
     * Removes a child permission node.
     *
     * @param String node
     */
    public void removeChild(String node)
    {
        node = node.toLowerCase();

        permissions.remove(node);
    }

    /**
     * Removes all of the child permission nodes.
     *
     * @return void
     */
    public void removeAll()
    {
        permissions.clear();
    }
}
