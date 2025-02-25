package me.dkflab.autoreplant.listeners;

import me.dkflab.autoreplant.AutoReplant;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.NetherWartsState;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.CocoaPlant;
import org.bukkit.material.MaterialData;
import org.bukkit.material.NetherWarts;

public class BlockBreak implements Listener {

    private AutoReplant plugin;
    public BlockBreak(AutoReplant instance) {
        plugin = instance;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        BlockState state = event.getBlock().getState();
        MaterialData data = state.getData();
        String stringdata = data.toString();
        //define itemstacks
        ItemStack seeds = new ItemStack(Material.SEEDS, 1);
        ItemStack carrot = new ItemStack(Material.CARROT_ITEM, 1);
        ItemStack potato = new ItemStack(Material.POTATO_ITEM, 1);
        ItemStack beetroot = new ItemStack(Material.BEETROOT_SEEDS, 1);
        ItemStack cocoa = new ItemStack(Material.INK_SACK, 3, (short) DyeColor.BROWN.getDyeData());
        ItemStack wart = new ItemStack(Material.NETHER_STALK, 1);
        // wheat
        if (stringdata.equals("RIPE CROPS(7)") && plugin.getConfig().getBoolean("wheat")) {
            if (checkInv(player, seeds)) {
                removeInv(player, Material.SEEDS);
                event.setCancelled(true);
                event.getBlock().setType(Material.CROPS);
                // give player wheat/seeds
                dropItem(player, new ItemStack (Material.SEEDS, 2), new ItemStack(Material.WHEAT, 1));
            }
        }
        // carrot
        if (stringdata.equals("RIPE CARROT(7)") && plugin.getConfig().getBoolean("carrot")) {
            if (checkInv(player, carrot)) {
                removeInv(player, Material.CARROT_ITEM);
                event.setCancelled(true);
                event.getBlock().setType(Material.CARROT);
                // give player carrot
                player.getInventory().addItem(new ItemStack(Material.CARROT_ITEM, 3));
            }
        }
        // potato
        if (stringdata.equals("RIPE POTATO(7)")&&plugin.getConfig().getBoolean("potato")) {
            if (checkInv(player, potato)) {
                removeInv(player, Material.POTATO_ITEM);
                event.setCancelled(true);
                event.getBlock().setType(Material.POTATO);
                // give player potato
                player.getInventory().addItem(new ItemStack(Material.POTATO_ITEM, 3));
            }
        }
        // beetroot
        if (stringdata.equals("RIPE BEETROOT_BLOCK(3)")&&plugin.getConfig().getBoolean("beetroot")) {
            if (checkInv(player, beetroot)) {
                removeInv(player, Material.BEETROOT_SEEDS);
                event.setCancelled(true);
                event.getBlock().setType(Material.BEETROOT_BLOCK);
                // beetroot
                if (Math.random() >0.75) {
                    dropItem(player, new ItemStack (Material.BEETROOT), new ItemStack (Material.BEETROOT_SEEDS, 3));
                }
                else  {
                    dropItem(player, new ItemStack (Material.BEETROOT), new ItemStack (Material.BEETROOT_SEEDS, 2));
                }
            }
        }
        //cocoa
        if (data instanceof CocoaPlant) {
            CocoaPlant cocoaPlant = (CocoaPlant) data;
            if (cocoaPlant.getSize() == CocoaPlant.CocoaPlantSize.LARGE) {
                if (checkInv(player, cocoa)) {
                    // coolremoveinv explanation below
                    coolremoveInv(player, new MaterialData(Material.INK_SACK, (byte) 3));
                    // SUCCESS
                    cocoaPlant.setSize(CocoaPlant.CocoaPlantSize.SMALL);
                    state.setData(cocoaPlant);
                    state.update();
                    event.setCancelled(true);
                    // give player cocoa
                    player.getInventory().addItem(cocoa);
                }
            }
        }
        // netherwart
        if (data instanceof NetherWarts) {
            NetherWarts netherWarts = (NetherWarts) data;
            if (netherWarts.getState() == NetherWartsState.RIPE) {
                if (checkInv(player, wart)) {
                    netherWarts.setState(NetherWartsState.SEEDED);
                    state.setData(netherWarts);
                    state.update();
                    event.setCancelled(true);
                    //take out of inventory
                    removeInv(player, Material.NETHER_STALK);
                    //give player netherwart
                    if (Math.random() > 0.5) {
                        player.getInventory().addItem(new ItemStack (Material.NETHER_STALK, 2));
                    }
                    else {
                        player.getInventory().addItem(new ItemStack (Material.NETHER_STALK, 3));
                    }
                }
            }
        }
    }

    private void dropItem(Player player, ItemStack crop, ItemStack seeds) {
        player.getInventory().addItem(crop);
        player.getInventory().addItem(seeds);
    }

    private Boolean checkInv(Player player, ItemStack item) {
        // check inventory of the player to see if there is crop
        ItemStack[] inv = player.getInventory().getContents();
        for (ItemStack i : inv) {
            if (i != null) {
                if (i.isSimilar(item)) {
                    return true;
                }
            }
        }
        return false;
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

    // this method needs to be used for cocoa only as you need to use MaterialData as cocoa beans aren't a material in 1.12.2
    private void coolremoveInv(Player player, MaterialData i) {
        for (ItemStack item : player.getInventory()) {
            if (item != null && item.getType() != null) {
                if (item.getData().equals(i)) {
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
