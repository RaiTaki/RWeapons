package xyz.raitaki.rweapons.weapon.skills;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.TextDisplay;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.raitaki.rweapons.RWeapon;
import xyz.raitaki.rweapons.utils.console.ConsoleViewer;
import xyz.raitaki.rweapons.weapon.Skill;
import xyz.raitaki.rweapons.weapon.Weapon;

import java.util.ArrayList;

public class ConsoleSkill extends Skill {


    @Getter private static ArrayList<ConsoleSkill> consoleViewers = new ArrayList<>();
    @Getter private ConsoleViewer viewer;
    private boolean started = false;
    private boolean stopped = false;
    private int timer = 0;

    public ConsoleSkill(Weapon weapon) {
        super("ConsoleSkill", 1,0,0,0, weapon);
    }

    @Override
    public void activate() {
        if(started || timer > 0){
            consoleViewers.remove(this);
            viewer.removeDisplays();
            stopped = true;
            started = false;
            timer = 0;
            return;
        }
        viewer = new ConsoleViewer(getWeapon().getOwner().getLocation());
        viewer.getStraightLines().get(1).text(Component.text("============================raiOS-0.01============================").color(TextColor.color(0,255,255)));

        new BukkitRunnable(){
            public void run() {
                if(stopped){
                    this.cancel();
                    stopped = false;
                    return;
                }
                if(!started){
                    timer++;
                }
                if(timer == 1){
                    viewer.addText("&braiOS &7is starting up. Please wait.");
                }
                if(timer == 1*15){
                    viewer.addText("&7Booting.");
                }
                if(timer == 2*15){
                    viewer.addText("&7Booting..");
                }
                if(timer == 3*15){
                    viewer.addText("&7Booting...");
                }
                if(timer == 4*15){
                    viewer.addText("&7Booted.");
                }
                if(timer == 5*15){
                    viewer.addText("&7Logining as " + getWeapon().getOwner().getName() + ".");
                }
                if(timer == 6*15){
                    viewer.addText("&7Login complete.");
                }
                if(timer == 7*15){
                    viewer.addText("&7Loaded all plugins.");
                }
                if(timer == 8*15){
                    viewer.addText("&7Loaded all worlds.");
                }
                if(timer == 9*15){
                    viewer.addText("&7Everything is ready.");
                }
                if(timer == 11*15){
                    viewer.addText("&bWelcome sir. Its good to see you here.");
                    started = true;
                    consoleViewers.add(ConsoleSkill.this);
                    timer++;
                }

                if(getWeapon().isheld()){
                    Location loc = getWeapon().getOwner().getLocation().add(0,1,0).add(getWeapon().getOwner().getEyeLocation().getDirection().setY(0).multiply(2));
                    loc.setYaw(loc.getYaw() + 180);
                    loc.setPitch(0);
                    viewer.teleport(loc, false);
                }
                else {
                    Location loc = getWeapon().getDisplay().getLocation().add(0,0.5,0);
                    loc.setYaw(loc.getYaw() + 180);
                    loc.setPitch(0);
                    viewer.teleport(loc, false);
                }
            }
        }.runTaskTimer(RWeapon.getInstance(), 0,1);
    }
}
