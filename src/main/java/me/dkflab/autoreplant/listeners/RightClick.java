package me.dkflab.autoreplant.listeners;

import me.dkflab.autoreplant.AutoReplant;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

public class RightClick implements Listener {
    private AutoReplant plugin;
    public RightClick(AutoReplant instance) {
        plugin = instance;
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) throws InterruptedException {
        Action action = event.getAction();
        Player player = event.getPlayer();
        Material mainhand = player.getInventory().getItemInMainHand().getType();
        ItemStack offhand = player.getInventory().getItemInOffHand();
        if (action == Action.RIGHT_CLICK_BLOCK && (event.getClickedBlock().getType().equals(Material.DIRT) || event.getClickedBlock().getType().equals(Material.GRASS))) {
            if (mainhand.equals(Material.DIAMOND_HOE)||mainhand.equals(Material.GOLD_HOE)||mainhand.equals(Material.IRON_HOE)||mainhand.equals(Material.STONE_HOE)||mainhand.equals(Material.WOOD_HOE)) {
                //wheat
                if (offhand.getType().equals(Material.SEEDS) && plugin.getConfig().getBoolean("plantwheat")) {
                    tillSet(event, Material.SEEDS, Material.CROPS);
                }
                //potato
                if (offhand.getType().equals(Material.POTATO_ITEM) && plugin.getConfig().getBoolean("plantpotato")) {
                    tillSet(event, Material.POTATO_ITEM, Material.POTATO);
                }
                //carrot
                if (offhand.getType().equals(Material.CARROT_ITEM) && plugin.getConfig().getBoolean("plantcarrot")) {
                    tillSet(event, Material.CARROT_ITEM, Material.CARROT);
                }
                //beetroot
                if (offhand.getType().equals(Material.BEETROOT_SEEDS) && plugin.getConfig().getBoolean("plantbeetroot")) {
                    tillSet(event, Material.BEETROOT_SEEDS, Material.BEETROOT_BLOCK);
                }
            }
        }
    }

    private void tillSet(PlayerInteractEvent event, Material seed, Material block) {
        int y = event.getClickedBlock().getY();
        int x = event.getClickedBlock().getX();
        int z = event.getClickedBlock().getZ();
        Location loc = new Location(event.getPlayer().getWorld(), x, y, z);
        loc.getBlock().setType(Material.SOIL);
        loc = new Location(event.getPlayer().getWorld(), x, y+1, z);
        loc.getBlock().setType(block);
        //remove seeds
        removeInv(event.getPlayer(), seed);
    }

    private void removeInv(Player player, Material i) {
        for (ItemStack item : player.getInventory()) {
            if (item != null && item.getType() != null) {
                if (item.getType() == i) {
                    if (item.getAmount() > 1) {
                        item.setAmount(item.getAmount() - 1);
                        break;
                    } else {
                        item.setAmount(0);
                        break;
                    }
                }
            }
        }
    }
}
