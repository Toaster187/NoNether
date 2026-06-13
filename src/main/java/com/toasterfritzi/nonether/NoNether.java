package com.toasterfritzi.nonether;

import org.bukkit.plugin.java.JavaPlugin;

public class NoNether extends JavaPlugin {

    @Override
    public void onEnable() {
        // Save default config if it doesn't exist
        saveDefaultConfig();

        // Register event listener
        getServer().getPluginManager().registerEvents(new PortalListener(this), this);

        // Register command
        PortalRuleCommand cmd = new PortalRuleCommand(this);
        if (getCommand("portalrule") != null) {
            getCommand("portalrule").setExecutor(cmd);
            getCommand("portalrule").setTabCompleter(cmd);
        }

        getLogger().info("NoNether has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("NoNether has been disabled!");
    }
}
