package com.ashkiano.customtntdelay;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class CustomTNTDelay extends JavaPlugin implements Listener {

    private int tntDelay; // delay in ticks

    @Override
    public void onEnable() {
        // Load the configuration
        saveDefaultConfig();
        FileConfiguration config = getConfig();
        int delayInSeconds = config.getInt("tnt-delay-seconds", 4);
        tntDelay = delayInSeconds * 20; // convert seconds to ticks

        // Register the event listener
        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info("CustomTNTDelay enabled with TNT delay of " + delayInSeconds + " seconds.");
        Metrics metrics = new Metrics(this, 22004);
        this.getLogger().info("Thank you for using the CustomTNTDelay plugin! If you enjoy using this plugin, please consider making a donation to support the development. You can donate at: https://donate.ashkiano.com");
    }

    @Override
    public void onDisable() {
        getLogger().info("CustomTNTDelay disabled!");
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        if (event.getEntityType() == EntityType.PRIMED_TNT) {
            TNTPrimed tnt = (TNTPrimed) event.getEntity();
            tnt.setFuseTicks(tntDelay);
        }
    }
}