package xyz.raitaki.rweapons.utils.console;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.entity.TextDisplay;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class ConsoleViewer {

    @Getter private List<TextDisplay> displays;
    @Getter private List<TextDisplay> straightLines;
    @Getter private List<ConsoleText> texts;
    @Getter private Location location;

    public ConsoleViewer(Location loc){
        this.location = loc;
        this.displays = new ArrayList<>();
        this.texts = new ArrayList<>();
        this.straightLines = new ArrayList<>();
        addStraightLine();
        for(int i = 0; i < 10; i++){
            TextDisplay line = loc.getWorld().spawn(loc.add(0, 0.06, 0), TextDisplay.class);
            line.setBillboard(Display.Billboard.FIXED);
            line.setShadowed(true);
            line.setLineWidth(9999);
            //line.setBackgroundColor(Color.AQUA.setAlpha(0));
            line.setDisplayHeight(1f);
            line.setDisplayWidth(1f);
            line.setTransformation(new Transformation(new Vector3f(0, 0, 0), new Quaternionf(0, 0, 0, 1), new Vector3f(0.2f, 0.2f, 4), new Quaternionf(0, 0, 0, 1)));
            displays.add(line);
            line.teleport(loc.clone());
        }
        addStraightLine();
    }

    public void addText(String text){
        if(isLogMessage(text)){
            texts.add(0, new ConsoleText("", this).setTextByTime(replaceLocations(text),1, true));
            //texts.add(0, text);
        }
        else{
            texts.add(0, new ConsoleText("", this).setTextByTime(text,1, true));
            //texts.add(0,replaceLocations(text));
        }
        if(texts.size() > 10){
            texts.remove(10);
        }
    }

    public void update(){
        for(int i = 0; i < texts.size(); i++){
            //reverse the text list and display it
            displays.get(i).text(Component.text(texts.get(i).getText()));
        }
    }

    private String replaceLocations(String text) {
        String logMsg = text;
        String[] parts = logMsg.split("\\[world\\]");
        String[] coords = parts[1].substring(0, parts[1].length() - 1).split(",");
        int x = (int) Math.floor(Double.parseDouble(coords[0]));
        int y = (int) Math.floor(Double.parseDouble(coords[1]));
        int z = (int) Math.floor(Double.parseDouble(coords[2]));
        String newLogMsg = String.format("%s[world]%d, %d, %d)", parts[0], x, y, z);
        newLogMsg = newLogMsg.replaceAll("\\[\\/\\d+\\.\\d+\\.\\d+\\.\\d+:\\d+\\]", "[/CENSORED:1]");
        return newLogMsg;
    }

    private boolean isLogMessage(String text) {
        // check if the string contains the log message pattern
        return text.matches("^\\w+\\[\\/(\\d{1,3}\\.){3}\\d{1,3}:\\d+\\] logged in with entity id \\d+ at \\(\\[world\\]-?\\d+(\\.\\d+)?, -?\\d+(\\.\\d+)?, -?\\d+(\\.\\d+)?\\)$");
    }

    private void addStraightLine(){
        TextDisplay line = location.getWorld().spawn(location.add(0, 0.06, 0), TextDisplay.class);
        line.setBillboard(Display.Billboard.FIXED);
        line.setShadowed(true);
        line.setLineWidth(9999);
        //line.setBackgroundColor(Color.AQUA.setAlpha(0));
        line.setDisplayHeight(1f);
        line.setDisplayWidth(1f);
        line.text(Component.text("==============================================================").color(TextColor.color(0,255,255)));
        line.setTransformation(new Transformation(new Vector3f(0, 0, 0), new Quaternionf(0, 0, 0, 1), new Vector3f(0.2f, 0.2f, 4), new Quaternionf(0, 0, 0, 1)));
        straightLines.add(line);
    }

    public void removeDisplays(){
        for(TextDisplay display : displays){
            display.remove();
        }
        for(TextDisplay display : straightLines){
            display.remove();
        }
    }

    public void teleport(Location loc, boolean rotateOnXAxis){
        this.location = loc.clone();

        Location line1loc = this.location.add(0,0.06,0).clone();
        line1loc.setPitch(-loc.getPitch());
        straightLines.get(0).teleport(line1loc);
        for(TextDisplay display : displays){
            Location displayLocation = this.location.add(0,0.06,0).clone();
            displayLocation.setPitch(-loc.getPitch());
            display.teleport(displayLocation);
        }
        Location line2loc = this.location.clone().add(0,0.06,0);
        line2loc.setPitch(-loc.getPitch());
        straightLines.get(1).teleport(line2loc);

        if(rotateOnXAxis)
            rotate(this.location.getPitch());
    }

    public void rotate(float pitch){
        Location center = getCenterLocation();

        for(TextDisplay display : displays){
            Location loc = calculateRotation(center, display.getLocation(), pitch);
            display.teleport(loc);
        }
        for(TextDisplay display : straightLines){
            Location loc = calculateRotation(center, display.getLocation(), pitch);
            display.teleport(loc);
        }
    }

    public Location getCenterLocation(){
        return this.location.clone().add(0,0.06*7,0);
    }

    public Location calculateRotation(Location center, Location loc, double pitch){
        Location diffrence = loc.clone().subtract(center);
        Vector v = diffrence.toVector();



        v.rotateAroundX(Math.toRadians(pitch));
        v.rotateAroundY(Math.toRadians(loc.getYaw()));

        return center.clone().add(v);
    }
}