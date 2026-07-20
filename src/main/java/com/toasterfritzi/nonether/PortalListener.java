package com.toasterfritzi.nonether;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World.Environment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PortalListener implements Listener {

    private final NoNether plugin;
    private final Map<UUID, Long> messageCooldowns = new HashMap<>();
    private static final long COOLDOWN_MS = 2000;

    public PortalListener(NoNether plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerPortal(PlayerPortalEvent event) {
        boolean allowNether = plugin.getConfig().getBoolean("allow-nether", false);
        boolean allowEnd = plugin.getConfig().getBoolean("allow-end", false);

        if (event.getCause() == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) {
            if (!allowNether && event.getFrom().getWorld().getEnvironment() != Environment.NETHER) {
                event.setCancelled(true);
                sendMessageWithCooldown(event.getPlayer(), ChatColor.RED + "The Nether is currently disabled.");
            }
        } else if (event.getCause() == PlayerTeleportEvent.TeleportCause.END_PORTAL) {
            if (!allowEnd && event.getFrom().getWorld().getEnvironment() != Environment.THE_END) {
                event.setCancelled(true);
                sendMessageWithCooldown(event.getPlayer(), ChatColor.RED + "The End is currently disabled.");
            }
        }
    }

    @EventHandler
    public void onEntityPortal(EntityPortalEvent event) {
        boolean allowNether = plugin.getConfig().getBoolean("allow-nether", false);
        boolean allowEnd = plugin.getConfig().getBoolean("allow-end", false);

        if (event.getPortalType() == org.bukkit.PortalType.NETHER) {
            if (!allowNether && event.getFrom().getWorld().getEnvironment() != Environment.NETHER) {
                event.setCancelled(true);
            }
        } else if (event.getPortalType() == org.bukkit.PortalType.ENDER) {
            if (!allowEnd && event.getFrom().getWorld().getEnvironment() != Environment.THE_END) {
                event.setCancelled(true);
            }
        }
    }

    private void sendMessageWithCooldown(Player player, String message) {
        if (!plugin.getConfig().getBoolean("show-portal-disabled-message", true)) {
            return;
        }

        UUID uuid = player.getUniqueId();
        long now = System.currentTimeMillis();
        long lastMsg = messageCooldowns.getOrDefault(uuid, 0L);

        if (now - lastMsg > COOLDOWN_MS) {
            player.sendMessage(message);
            messageCooldowns.put(uuid, now);
        }
    }
}
