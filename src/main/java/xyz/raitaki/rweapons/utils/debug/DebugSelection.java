package xyz.raitaki.rweapons.utils.debug;

import lombok.Getter;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import xyz.raitaki.rweapons.utils.math.ShapeUtils;

import java.util.ArrayList;
import java.util.List;

public class DebugSelection {

    @Getter private List<Location> points;

    @Getter private boolean isBlock;
    @Getter private Block block;

    @Getter private boolean isPoint;
    @Getter private Location point;

    @Getter private Color color;

    public DebugSelection(Block block, Color color){
        this.block = block;
        this.isBlock = true;
        this.color = color;
        points = ShapeUtils.showCuboid(block.getLocation(), block.getLocation().clone().add(1,-1,1), 0.1);
    }

    public DebugSelection(Location point, Color color){
        this.point = point;
        this.isPoint = true;
        this.color = color;
        points = new ArrayList<>();
        points.add(point);
    }

    public void playDebugParticle(){
        points.forEach(location -> location.getWorld().spawnParticle(Particle.REDSTONE, location, 1, 0, 0, 0, 0,
                                    new Particle.DustOptions(color, 0.5f)));
    }
}
