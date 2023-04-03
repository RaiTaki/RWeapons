package xyz.raitaki.rweapons.utils.console;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.raitaki.rweapons.RWeapons;

import java.util.ArrayList;

public class ConsoleText {

    @Getter private String text;
    @Getter private boolean playing;
    private ConsoleViewer viewer;

    public ConsoleText(String text, ConsoleViewer viewer){
        this.text = text;
        this.viewer = viewer;
    }

    public ConsoleText setText(String text){
        this.text = text;
        viewer.update();
        return this;
    }

    public ConsoleText setTextByTime(String text, int charPerSecond, boolean sound){
        playing = false;
        String finaltext = replaceColorCodes(text);
        //String finaltext = checkPixelLength(text);

        if(viewer == null){
            this.text = text;
            viewer.update();
            return this;
        }
        new BukkitRunnable(){
            @Override
            public void run() {
                if(finaltext.length() <= ConsoleText.this.text.length() + charPerSecond){
                    this.cancel();
                    ConsoleText.this.text = finaltext;
                    playing = false;
                    viewer.update();
                    return;
                }
                else {
                    ConsoleText.this.text = finaltext.substring(0, ConsoleText.this.text.length() + charPerSecond);
                }
                if(!isAnyTimerPlays() && sound){
                    viewer.getLocation().getWorld().playSound(viewer.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 1, 0.6f);
                    playing = true;
                }
                viewer.update();
            }
        }.runTaskTimer(RWeapons.getInstance(), 1,1);
        return this;
    }

    private boolean isAnyTimerPlays(){
        for(ConsoleText text : viewer.getTexts()){
            if(text.isPlaying()){
                if(text != this)
                    return true;
            }
        }
        return false;
    }

    private String checkPixelLength(String text){
        int length = 0;
        int length2 = 0;

        for(char c : text.toCharArray())
            length += DefaultFontInfo.getDefaultFontInfo(c).getLength();

        for(char c : viewer.getStraightLines().get(0).getText().toCharArray())
            length2 += DefaultFontInfo.getDefaultFontInfo(c).getLength();


        if(length > length2)
            return text;

        int leftPixels = Math.abs(length2 - length);
        int leftSpaces = leftPixels / DefaultFontInfo.SPACE.getLength();

        for(int i = 0; i < leftSpaces; i++){
            text += " ";
            leftPixels -= DefaultFontInfo.SPACE.getLength();
        }

        for(int i = 0; i < leftPixels; i++){
            text += "'";
        }

        return text;
    }

    private String replaceColorCodes(String text){
        text = text.replace("\u007F", "&");
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}