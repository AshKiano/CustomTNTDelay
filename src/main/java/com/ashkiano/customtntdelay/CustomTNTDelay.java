package com.ashkiano.customtntdelay;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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
        checkForUpdates();
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

    private void checkForUpdates() {
        try {
            String pluginName = this.getDescription().getName();
            URL url = new URL("https://plugins.ashkiano.com/version_check.php?plugin=" + pluginName);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                String jsonResponse = response.toString();
                JSONObject jsonObject = new JSONObject(jsonResponse);
                if (jsonObject.has("error")) {
                    this.getLogger().warning("Error when checking for updates: " + jsonObject.getString("error"));
                } else {
                    String latestVersion = jsonObject.getString("latest_version");

                    String currentVersion = this.getDescription().getVersion();
                    if (currentVersion.equals(latestVersion)) {
                        this.getLogger().info("This plugin is up to date!");
                    } else {
                        this.getLogger().warning("There is a newer version (" + latestVersion + ") available! Please update!");
                    }
                }
            } else {
                this.getLogger().warning("Failed to check for updates. Response code: " + responseCode);
            }
        } catch (Exception e) {
            this.getLogger().warning("Failed to check for updates. Error: " + e.getMessage());
        }
    }
}