package xyz.raitaki.rweapons.weapon.skills;

import org.bukkit.scheduler.BukkitRunnable;
import xyz.raitaki.rweapons.RWeapons;
import xyz.raitaki.rweapons.utils.ParticleBall;
import xyz.raitaki.rweapons.weapon.Skill;
import xyz.raitaki.rweapons.weapon.Weapon;

public class ParticleSkill extends Skill {

    public ParticleSkill(Weapon weapon) {
        super("ParticleBall", 1, 0, 0, 0, weapon);
    }

    @Override
    public void activate() {
        ParticleBall particleBall = new ParticleBall(getWeapon().getOwner().getLocation().add(0,0.5,0), getWeapon().getOwner().getLocation().getDirection(), 1);
        new BukkitRunnable(){
            public void run() {
                particleBall.update();
            }
        }.runTaskTimer(RWeapons.getInstance(), 0, 1);
    }
}
