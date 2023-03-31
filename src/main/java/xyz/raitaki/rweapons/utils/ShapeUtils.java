package xyz.raitaki.rweapons.utils;

import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

public class ShapeUtils {

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

    public static List<Location> showSphere(Location center, double r, int step) {
        List<Location> locs = new ArrayList<>();

        for(double phi=0; phi<=Math.PI; phi+=Math.PI/step) {
            for(double theta=0; theta<=2*Math.PI; theta+=Math.PI/step) {
                double x = r*Math.cos(theta)*Math.sin(phi);
                double y = r*Math.cos(phi);
                double z = r*Math.sin(theta)*Math.sin(phi);

                locs.add(center.clone().add(x,y,z));
            }
        }

        return locs;
    }
}
