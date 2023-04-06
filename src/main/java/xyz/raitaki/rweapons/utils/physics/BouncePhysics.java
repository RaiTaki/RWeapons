package xyz.raitaki.rweapons.utils.physics;


import lombok.Getter;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;
import xyz.raitaki.rweapons.utils.debug.DebugSelection;
import xyz.raitaki.rweapons.utils.debug.Debugger;

public class BouncePhysics {

    private Location location;
    private Vector velocity;
    private final double gravity = 0.1;
    @Getter private final Debugger debugger;

    int trys = 0;

    public BouncePhysics(Location location, Vector initialVelocity) {
        this.location = location;
        this.velocity = initialVelocity;
        debugger = new Debugger();
    }

    public Location update(double deltaTime, double r) {
        Location location = this.location;
        location.add(this.velocity.clone().multiply(deltaTime));
        boolean collided = false;

        // Check for collisions
        double radius = r + deltaTime/1.5;

        // Check for collision with ceiling
        /*Location ceilingLocation = location.clone().add(0,(radius*1.7),0);
        location.getWorld().spawnParticle(Particle.REDSTONE, ceilingLocation, 1, 0, 0, 0, 0, new Particle.DustOptions(Color.YELLOW, 0.4f));
        if (ceilingLocation.getBlock().getType().isSolid()) {
            // Bounce off block below ball
            this.velocity.setY(Math.abs(this.velocity.getY()));
            collided = true;
        }*/

        for (BlockFace blockFace : BlockFace.values()) {
            if (blockFace == BlockFace.SELF || blockFace == BlockFace.DOWN || blockFace == BlockFace.UP) {
                continue;
            }
            Location adjacentLocation = location.clone().add(blockFace.getDirection().clone().multiply(radius));
            Block adjacentBlock = adjacentLocation.getBlock();
            location.getWorld().spawnParticle(Particle.REDSTONE, adjacentLocation, 1, 0, 0, 0, 0, new Particle.DustOptions(Color.FUCHSIA, 0.4f));
            trys++;
            if (adjacentBlock.getType().isSolid()) {
                // Make sure ball is not bouncing off block directly below it
                Vector normal = location.toVector().subtract(adjacentLocation.toVector()).normalize();

                Location hitPoint = adjacentLocation.clone().add(normal.clone().multiply(radius));

                debugger.addSelection(new DebugSelection(hitPoint, Color.LIME));
                debugger.addSelection(new DebugSelection(adjacentBlock.getLocation().add(0,1,0).getBlock(), Color.BLUE));
                // Calculate reflection vector
                Vector reflection = this.velocity.clone().subtract(normal.clone().multiply(this.velocity.clone().dot(normal) * 2));

                // Set new velocity to the reflection vector
                this.velocity = reflection;
                collided = true;

                break;
            }

            Location blockBelowLocation = location.clone().subtract(0,(radius*1.1),0);
            Location belowAdjacentLocation = blockBelowLocation.clone().add(blockFace.getDirection().clone().multiply(radius*0.8));
            Block belowAdjacentBlock = belowAdjacentLocation.getBlock();
            location.getWorld().spawnParticle(Particle.REDSTONE, belowAdjacentLocation, 1, 0, 0, 0, 0, new Particle.DustOptions(Color.YELLOW, 0.4f));
            trys++;
            if (belowAdjacentBlock.getType().isSolid()) {
                // Make sure ball is not bouncing off block directly below it
                this.velocity.setY(Math.abs(this.velocity.getY()));

                debugger.addSelection(new DebugSelection(adjacentLocation, Color.YELLOW));
                debugger.addSelection(new DebugSelection(adjacentBlock, Color.AQUA));
                collided = true;
                break;
            }
        }

        /*for (Location adjacentLocation : ShapeUtils.showSphere(location, radius, 6)) {
            Block adjacentBlock = adjacentLocation.getBlock();
            location.getWorld().spawnParticle(Particle.REDSTONE, adjacentLocation, 1, 0, 0, 0, 0, new Particle.DustOptions(Color.FUCHSIA, 0.4f));
            trys++;
            if (adjacentBlock.getType().isSolid()) {
                // Make sure ball is not bouncing off block directly below it
                Vector normal = adjacentLocation.toVector().subtract(location.toVector()).normalize();

                // Calculate reflection vector
                Vector reflection = this.velocity.clone().subtract(normal.multiply(this.velocity.clone().dot(normal) * 2));

                // Set new velocity to the reflection vector
                this.velocity = reflection;
                collided = true;

                break;
            }
        }*/



        // Check for collision with block below ball

        //location.getWorld().spawnParticle(Particle.REDSTONE, blockBelowLocation, 1, 0, 0, 0, 0, new Particle.DustOptions(Color.YELLOW, 0.4f));

        if (!collided) {
            velocity.divide(new Vector(1.01, 1.01, 1.01));
            this.velocity.setY(this.velocity.getY() - gravity * deltaTime);
            trys = 0;
            return location;
        }else{
            velocity.divide(new Vector(1.3, 1.6, 1.3));
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