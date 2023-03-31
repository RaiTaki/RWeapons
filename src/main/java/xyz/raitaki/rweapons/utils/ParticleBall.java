package xyz.raitaki.rweapons.utils;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

public class ParticleBall {

    private final World world;
    private Vector position;
    private Vector velocity;
    private final double radius;

    public ParticleBall(Location location, Vector velocity, double radius) {
        this.world = location.getWorld();
        this.position = location.toVector();
        this.velocity = velocity;
        this.radius = radius;
    }

    public void update() {
        Vector newPosition = position.clone().add(velocity);
        boolean collided = false;
        for (int x = (int) Math.floor(position.getX() - radius); x <= Math.ceil(position.getX() + radius); x++) {
            for (int y = (int) Math.floor(position.getY() - radius); y <= Math.ceil(position.getY() + radius); y++) {
                for (int z = (int) Math.floor(position.getZ() - radius); z <= Math.ceil(position.getZ() + radius); z++) {
                    world.spawnParticle(Particle.REDSTONE, x,y,z, 0, 0, 0, 0, 1, new Particle.DustOptions(Color.FUCHSIA, 1f));

                    Block block = world.getBlockAt(x, y, z);
                    if (block.getType().isSolid()) {
                        Bukkit.broadcastMessage("a");
                        collided = true;
                        Vector normal = getSurfaceNormal(block, newPosition);
                        velocity = velocity.subtract(normal.multiply(2 * velocity.dot(normal)));
                        break;
                    }
                }
                if (collided) {
                    break;
                }
            }
            if (collided) {
                break;
            }
        }

        if(collided) {
            velocity.multiply(0.8);
        }else{
            velocity.setY(velocity.getY() - 0.1);
        }
        position.add(velocity);
        drawHitboxSphere(position, radius);
        world.spawnParticle(Particle.REDSTONE, position.getX(), position.getY(), position.getZ(), 0, 0, 0, 0, 1,new Particle.DustOptions(Color.YELLOW, 0.4f));

    }

    private void drawHitboxSphere(Vector center, double radius) {
        int radiusCeil = (int) Math.ceil(radius);
        int radiusFloor = (int) Math.floor(radius);
        for (int x = -radiusCeil; x <= radiusCeil; x++) {
            for (int y = -radiusCeil; y <= radiusCeil; y++) {
                for (int z = -radiusCeil; z <= radiusCeil; z++) {
                    Vector blockPos = center.clone().add(new Vector(x, y, z));
                    if (center.distance(blockPos) <= radius) {
                        world.spawnParticle(Particle.REDSTONE, position.getX(), position.getY(), position.getZ(), 0, 0, 0, 0, 1,new Particle.DustOptions(Color.LIME, 0.4f));

                    }
                }
            }
        }
    }

    private Vector getSurfaceNormal(Block block, Vector point) {
        Location blockLoc = block.getLocation();
        double x = point.getX() - blockLoc.getX();
        double y = point.getY() - blockLoc.getY();
        double z = point.getZ() - blockLoc.getZ();
        if (x < 0) x = -x;
        if (y < 0) y = -y;
        if (z < 0) z = -z;
        if (x > y && x > z) {
            return new Vector(Math.signum(point.getX() - blockLoc.getX()), 0, 0);
        } else if (y > x && y > z) {
            return new Vector(0, Math.signum(point.getY() - blockLoc.getY()), 0);
        } else {
            return new Vector(0, 0, Math.signum(point.getZ() - blockLoc.getZ()));
        }
    }
}
