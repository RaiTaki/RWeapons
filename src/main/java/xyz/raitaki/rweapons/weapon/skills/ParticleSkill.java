package xyz.raitaki.rweapons.weapon.skills;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;
import xyz.raitaki.rweapons.weapon.Skill;
import xyz.raitaki.rweapons.weapon.Weapon;

import java.util.Random;

public class ParticleSkill extends Skill {

    public ParticleSkill(Weapon weapon) {
        super("ParticleBall", 1, 0, 0, 0, weapon);
    }

    @Override
    public void activate() {
        Vector direction = new Vector(new Random().nextDouble() * 2, new Random().nextDouble() * 2, new Random().nextDouble() * 2);
        Location loc = getWeapon().getOwner().getLocation().add(1, 0.5, 1);
        double previousTime = System.nanoTime() / 18e7;

        for(int i = 0; i < 200; i++){
            double currentTime = System.nanoTime() / 18e7;
            double deltaTime = currentTime - previousTime;
            previousTime = currentTime;
            loc.add(direction.clone().multiply(deltaTime));
            loc.getWorld().spawnParticle(Particle.REDSTONE, loc, 1, 0, 0, 0, 0, new Particle.DustOptions(Color.FUCHSIA, 0.4f));
            //Bukkit.broadcastMessage("Particle spawned at " + loc.toString() + " with direction " + direction.toString() + " and deltaTime " + deltaTime);
        }
    }
}
