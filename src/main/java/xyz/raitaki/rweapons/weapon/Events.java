package xyz.raitaki.rweapons.weapon;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;

public class Events implements Listener {

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        ItemStack item = event.getItem();
        for(Weapon weapon : Weapon.getWeapons()) {
            if(weapon.getOwnerUUID().equals(p.getUniqueId())) {
                if(p.getLocation().getPitch() > 80){
                    if(p.isSneaking())
                        p.getInventory().addItem(weapon.getItemStack());
                }
                if(item != null && item.isSimilar(weapon.getItemStack())) {
                    weapon.activateSkill(event);
                    event.setCancelled(true);
                }
            }
        }
    }


    @EventHandler
    public void onPlayerSneakEvent(PlayerToggleSneakEvent event){
        Player p = event.getPlayer();
        if(p.isSneaking()) return;

        for(Weapon weapon : Weapon.getWeapons()){
            if(weapon.getOwnerUUID().equals(p.getUniqueId()) && !weapon.doesDisplayExists()){
                if(weapon.getDisplay().getLocation().distance(p.getLocation()) < 1.5){
                    weapon.getDisplay().remove();
                    p.getInventory().addItem(weapon.getItemStack());
                }
            }
        }
    }
}
