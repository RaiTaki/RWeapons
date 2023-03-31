package xyz.raitaki.rweapons.utils;


import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class BouncePhysics {

    private Location location;
    private Vector velocity;
    private final double gravity = 0.1;

    int trys = 0;

    public BouncePhysics(Location location, Vector initialVelocity) {
        this.location = location;
        this.velocity = initialVelocity;
        if(velocity.getY() < -1)
            velocity.setY(-1);
    }

    public Location update(double deltaTime) {
        Location location = this.location;
        location.add(this.velocity.clone().multiply(deltaTime));
        boolean collided = false;


        // Check for collisions
        double radius = 0.12 + deltaTime/1.5;

        // Check for collision with ceiling
        Location ceilingLocation = location.clone().add(0,(radius*1.7),0);
        location.getWorld().spawnParticle(Particle.REDSTONE, ceilingLocation, 1, 0, 0, 0, 0, new Particle.DustOptions(Color.YELLOW, 0.4f));
        if (ceilingLocation.getBlock().getType().isSolid()) {
            // Bounce off block below ball
            this.velocity.setY(Math.abs(this.velocity.getY()));
            collided = true;
        }

        /*for (BlockFace blockFace : BlockFace.values()) {
            if (blockFace == BlockFace.SELF) {
                continue;
            }
            Location adjacentLocation = location.clone().add(blockFace.getDirection().multiply(radius));
            Block adjacentBlock = adjacentLocation.getBlock();
            location.getWorld().spawnParticle(Particle.REDSTONE, adjacentLocation, 1, 0, 0, 0, 0, new Particle.DustOptions(Color.FUCHSIA, 0.4f));
            trys++;
            if (adjacentBlock.getType().isSolid()) {
                // Make sure ball is not bouncing off block directly below it
                Vector normal = location.toVector().subtract(adjacentLocation.toVector()).normalize();

                // Calculate reflection vector
                Vector reflection = this.velocity.clone().subtract(normal.clone().multiply(this.velocity.clone().dot(normal) * 2));

                // Set new velocity to the reflection vector
                this.velocity = reflection;
                collided = true;

                break;
            }
        }*/

        for(Location adjacentLocation : ShapeUtils.showSphere(location, radius, 6)){
            Block adjacentBlock = adjacentLocation.getBlock();
            location.getWorld().spawnParticle(Particle.REDSTONE, adjacentLocation, 1, 0, 0, 0, 0, new Particle.DustOptions(Color.FUCHSIA, 0.4f));
            trys++;
            if (adjacentBlock.getType().isSolid()) {
                // Make sure ball is not bouncing off block directly below it
                Vector normal = adjacentLocation.toVector().subtract(location.toVector()).normalize();

                // Calculate reflection vector
                Vector reflection = this.velocity.clone().subtract(normal.clone().multiply(this.velocity.clone().dot(normal) * 2));

                // Set new velocity to the reflection vector
                this.velocity = reflection;
                collided = true;

                break;
            }
        }

        // Check for collision with block below ball
        Location blockBelowLocation = location.clone().subtract(0,(radius*1.7),0);
        location.getWorld().spawnParticle(Particle.REDSTONE, blockBelowLocation, 1, 0, 0, 0, 0, new Particle.DustOptions(Color.YELLOW, 0.4f));
        if (blockBelowLocation.getBlock().getType().isSolid()) {
            // Bounce off block below ball
            this.velocity.setY(Math.abs(this.velocity.getY()));
            collided = true;
        }

        if (!collided) {
            location.getWorld().spawnParticle(Particle.REDSTONE, location, 1, 0, 0, 0, 0, new Particle.DustOptions(Color.LIME, 1));
            trys = 0;
            this.velocity.setY(this.velocity.getY() - gravity * deltaTime);
            return location;
        }else{
            location.getWorld().spawnParticle(Particle.REDSTONE, location, 1, 0, 0, 0, 0, new Particle.DustOptions(Color.RED, 1));
            velocity.divide(new Vector(1.2, 1.4, 1.2));
        }
        if(trys > 1000){
            /*if(Math.abs(velocity.getY()) > gravity){
                trys = 0;
                Bukkit.broadcastMessage(velocity.getY()+"");
                Bukkit.broadcastMessage("teleported because of trys");
                return (location.add(0, radius * 1.3, 0));
            }*/
        }
        return location;
    }

}