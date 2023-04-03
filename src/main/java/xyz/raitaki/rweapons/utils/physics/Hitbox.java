package xyz.raitaki.rweapons.utils.physics;

import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

public class Hitbox {

    private final BoundingBox boundingBox;

    public Hitbox(double x1, double y1, double z1, double x2, double y2, double z2) {
        boundingBox = new BoundingBox(x1, y1, z1, x2, y2, z2);
    }

    public Hitbox(Vector vector1, Vector vector2) {
        boundingBox = new BoundingBox(vector1.getX(), vector1.getY(), vector1.getZ(), vector2.getX(), vector2.getY(), vector2.getZ());
    }

    public boolean checkCollision(BoundingBox other) {
        return boundingBox.overlaps(other);
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }
}