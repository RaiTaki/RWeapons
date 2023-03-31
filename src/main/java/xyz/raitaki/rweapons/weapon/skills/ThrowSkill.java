package xyz.raitaki.rweapons.weapon.skills;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.joml.Vector3f;
import xyz.raitaki.rweapons.RWeapons;
import xyz.raitaki.rweapons.utils.BouncePhysics;
import xyz.raitaki.rweapons.weapon.Skill;
import xyz.raitaki.rweapons.weapon.SkillClickType;
import xyz.raitaki.rweapons.weapon.Weapon;

public class ThrowSkill extends Skill {

    @Getter private BouncePhysics physics;
    @Getter private final Vector3f scale;

    public ThrowSkill(Weapon weapon, Vector3f scale) {
        super("Throw", 1, 0, 0, 0, weapon);
        this.scale = scale;
    }

    @Override
    public void activate() {
        this.physics = new BouncePhysics(getWeapon().getOwner().getLocation().add(0,0.5,0), getWeapon().getOwner().getLocation().getDirection().multiply(1));
        getWeapon().createDisplay(scale);

        getWeapon().getOwner().getInventory().getItemInMainHand().setAmount(0);
        new BukkitRunnable(){
            double previousTime = System.nanoTime() / 18e7;
            public void run() {
                if(getWeapon().getDisplay().isDead()){
                    cancel();
                    return;
                }
                double currentTime = System.nanoTime() / 18e7;
                double deltaTime = currentTime - previousTime;
                previousTime = currentTime;

                getWeapon().getDisplay().teleport(physics.update(deltaTime));

            }
        }.runTaskTimer(RWeapons.getInstance(), 0, 1);
    }
}
