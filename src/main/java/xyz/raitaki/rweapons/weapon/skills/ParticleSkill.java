package xyz.raitaki.rweapons.weapon.skills;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import xyz.raitaki.rweapons.RWeapon;
import xyz.raitaki.rweapons.utils.math.Quat;
import xyz.raitaki.rweapons.utils.math.VectorUtils;
import xyz.raitaki.rweapons.weapon.Skill;
import xyz.raitaki.rweapons.weapon.Weapon;

import javax.vecmath.Vector3f;

public class ParticleSkill extends Skill {

    public ParticleSkill(Weapon weapon) {
        super("ParticleBall", 1, 0, 0, 0, weapon);
    }

    @Override
    public void activate() {

        /*Vector start = getWeapon().getOwner().getLocation().toVector();
        Vector end = getWeapon().getOwner().getLocation().clone().set(82.5,108.5,-28.5).toVector();
        Vector direction = end.clone().subtract(start).normalize();

        Quat quat = new Quat((float) direction.getX(), (float) direction.getY(), (float) direction.getZ(), 0);

        float yaw = quat.yaw();
        float pitch = quat.pitch();
        float roll = quat.roll();
        Vector axis = new Vector(-pitch, yaw, -roll);

        Vector curveStart = start.clone().add(direction.clone().multiply(0.5));
        Vector curveEnd = end.clone().add(direction.clone().multiply(-0.5));
        double curveLength = start.distance(end) * 0.5;
        for (double t = 0; t <= 3; t += 0.1) {
            double x = curveStart.getX() + (curveEnd.getX() - curveStart.getX()) * t;
            double y = curveStart.getY() + (curveEnd.getY() - curveStart.getY()) * t - (t * t - t) * curveLength;
            double z = curveStart.getZ() + (curveEnd.getZ() - curveStart.getZ()) * t;
            Vector position = new Vector(x, y, z);
            position.subtract(curveStart);
            position.rotateAroundAxis(axis, Math.toRadians(roll));
            position.add(curveStart);

            getWeapon().getOwner().getWorld().spawnParticle(Particle.REDSTONE, position.toLocation(getWeapon().getOwner().getWorld()), 1, 0, 0, 0, 0,
                    new Particle.DustOptions(Color.AQUA, 0.5f));
            // Do something with the position, such as spawn a particle or create a block
        }*/

        new BukkitRunnable(){
            @Override
            public void run() {
                Location loc = getWeapon().getOwner().getLocation();
                Location loc2 = loc.clone().set(82.5,108.5,-28.5);
                Vector v = loc.toVector().subtract(loc2.toVector()).normalize();
                Quat oldq = new Quat();
                oldq.lookAt(v, new Vector(0,1   ,0));
                Quat newq = new Quat();
                newq.lookAt(loc2.toVector(), new Vector(0, 1, 0));
                Quat slerp = new Quat();

                for(float i = 0; i < 100; i++){
                    slerp.slerp(oldq, newq, (i/(100)));
                    Vector rotated = new Vector(0,0,1);
                    VectorUtils.rotateVector(rotated, slerp.getPitchRad(), slerp.getYawRad(), slerp.getRollRad());
                    rotated.multiply(100/(i+1));

                    loc.getWorld().spawnParticle(Particle.REDSTONE, getWeapon().getOwner().getLocation().clone().add(rotated), 1, 0, 0, 0, 0, new Particle.DustOptions(Color.YELLOW, 0.4f));
                    loc.getWorld().spawnParticle(Particle.REDSTONE, loc2, 1, 0, 0, 0, 0, new Particle.DustOptions(Color.LIME, 0.4f));

                }
            }
        }.runTaskTimer(RWeapon.getInstance(), 0, 1);
    }
}
