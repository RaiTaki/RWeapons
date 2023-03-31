package xyz.raitaki.rweapons.commands;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.raitaki.rweapons.utils.BouncePhysics;

import java.util.ArrayList;
import java.util.List;

public class TestParticleCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player p)) return false;

        for(Location loc : showCuboid(p.getLocation().add(5,5,5), p.getLocation(), 0.1)){
            loc.getWorld().spawnParticle(Particle.REDSTONE, loc, 1, 0, 0, 0, 0, new Particle.DustOptions(Color.RED, 0.6f));

        }

        return false;
    }

    public static List<Location> showCuboid(Location aLoc, Location bLoc, double step) {
        World world = aLoc.getWorld();
        List<Location> locs = new ArrayList<>();

        double[] xArr = {Math.min(aLoc.getX(), bLoc.getX()), Math.max(aLoc.getX(), bLoc.getX())};
        double[] yArr = {Math.min(aLoc.getY(), bLoc.getY()), Math.max(aLoc.getY(), bLoc.getY())};
        double[] zArr = {Math.min(aLoc.getZ(), bLoc.getZ()), Math.max(aLoc.getZ(), bLoc.getZ())};

        for (double x = xArr[0]; x < xArr[1]; x += step) for (double y : yArr) for (double z : zArr) {
            locs.add(new Location(world, x, y, z));
        }
        for (double y = yArr[0]; y < yArr[1]; y += step) for (double x : xArr) for (double z : zArr) {
            locs.add(new Location(world, x, y, z));
        }
        for (double z = zArr[0]; z < zArr[1]; z += step) for (double y : yArr) for (double x : xArr) {
            locs.add(new Location(world, x, y, z));
        }

        return locs;
    }

}
