package xyz.raitaki.rweapons.utils.physics;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class BallPhysics {
    private World world;
    private Location position;
    private double radius;
    private Vector velocity;

    public BallPhysics(World world, Location position, double radius, Vector velocity) {
        this.world = world;
        this.position = position;
        this.radius = radius;
        this.velocity = velocity;
    }

    public void tick() {
        // Calculate the new position of the ball based on its velocity
        Vector displacement = velocity.clone().multiply(1.0 / 20.0);
        position.add(displacement);

        // Check for collision with blocks
        Hitbox hitbox = new Hitbox(
                position.getX() - radius, position.getY() - radius, position.getZ() - radius,
                position.getX() + radius, position.getY() + radius, position.getZ() + radius
        );
        List<Block> blocks = getBlocksInHitbox(hitbox);
        if (!blocks.isEmpty()) {
            Vector reflection = calculateReflection(blocks);
            if (reflection != null) {
                // Reflect the velocity of the ball
                velocity = reflection.multiply(velocity.length());
            }
        }

        // Check for collision with the world border
        if (position.getX() < 0 || position.getX() > world.getWorldBorder().getSize() ||
                position.getY() < 0 || position.getY() > world.getMaxHeight() ||
                position.getZ() < 0 || position.getZ() > world.getWorldBorder().getSize()) {
            velocity = velocity.multiply(-1);
        }

        position.getWorld().spawnParticle(Particle.REDSTONE, position, 1, 0, 0, 0, 0, new Particle.DustOptions(Color.FUCHSIA, 0.4f));
    }

    private Vector calculateReflection(List<Block> blocks) {
        // Calculate the normal of the intersecting blocks
        Vector normal = new Vector(0, 0, 0);
        for (Block block : blocks) {
            @NotNull BoundingBox blockHitbox = block.getBoundingBox();
            normal.add(blockHitbox.getCenter().subtract(position.toVector()).normalize());
        }
        if (normal.lengthSquared() == 0) {
            return null;
        }
        normal.normalize();

        // Calculate the reflected velocity of the ball
        Vector incoming = velocity.normalize();
        Vector reflected = incoming.subtract(normal.multiply(2 * incoming.dot(normal)));
        return reflected;
    }

    private List<Block> getBlocksInHitbox(Hitbox hitbox) {
        List<Block> blocks = new ArrayList<>();
        int x1 = (int) Math.floor(hitbox.getBoundingBox().getMinX());
        int y1 = (int) Math.floor(hitbox.getBoundingBox().getMinY());
        int z1 = (int) Math.floor(hitbox.getBoundingBox().getMinZ());
        int x2 = (int) Math.floor(hitbox.getBoundingBox().getMaxX());
        int y2 = (int) Math.floor(hitbox.getBoundingBox().getMaxY());
        int z2 = (int) Math.floor(hitbox.getBoundingBox().getMaxZ());
        for (int x = x1; x <= x2; x++) {
            for (int y = y1; y <= y2; y++) {
                for (int z = z1; z <= z2; z++) {
                    blocks.add(world.getBlockAt(x, y, z));
                }
            }
        }
        return blocks;
    }
}