package com.sendev.mooperms.permission;

public class PermissionStats
{

    /**
     * The parent permission node counter.
     *
     * @var int parent
     */
    private int parent = 0;

    /**
     * The child permission node counter.
     *
     * @var int children
     */
    private int children = 0;

    /**
     * Adds one to the parent counter.
     *
     * @return void
     */
    public void addParent()
    {
        parent++;
    }

    /**
     * Gets the parent counter
     *
     * @return int parent
     */
    public int getParent()
    {
        return parent;
    }

    /**
     * Adds one to the child counter.
     *
     * @return void
     */
    public void addChild()
    {
        children++;
    }

    /**
     * Gets the child counter
     *
     * @return int children
     */
    public int getChildren()
    {
        return children;
    }
}
