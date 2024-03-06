package me.srb.minirpg.listeners;

import me.srb.minirpg.listeners.events.PlayerJoin;
import me.srb.minirpg.listeners.events.PlayerQuit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;

public class EventManagement implements Listener {
    private final Database database;

    public Listeners(Database database) {
        this.database = database;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        PlayerJoin playerJoin = new PlayerJoin(event, database);
        playerJoin.setPrefix();
        playerJoin.setJoinMessage(ChatColor.YELLOW + event.getPlayer().getDisplayName() + ChatColor.GREEN + " pojawił się w lochach!");
        playerJoin.setDefaultScoreboard();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        PlayerQuit playerQuit = new PlayerQuit(event, database);
        playerQuit.updateDatabase();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerExpChange(PlayerExpChangeEvent event) {
        event.setAmount(0);
    }

    @EventHandler
    public void onEnchantItem(EnchantItemEvent event) {
        event.setCancelled(true);
    }
}
