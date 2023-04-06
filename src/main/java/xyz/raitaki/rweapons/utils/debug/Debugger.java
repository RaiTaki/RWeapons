package xyz.raitaki.rweapons.utils.debug;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import xyz.raitaki.rweapons.RWeapon;

import java.util.ArrayList;
import java.util.List;

public class Debugger {

    @Getter private static List<Debugger> debuggers = new ArrayList<>();

    @Getter private BukkitTask task;
    @Getter private List<DebugSelection> selections;

    public Debugger(){
        selections = new ArrayList<>();

        this.task = new BukkitRunnable() {
            @Override
            public void run() {

                selections.forEach(DebugSelection::playDebugParticle);
            }
        }.runTaskTimer(RWeapon.getInstance(), 1, 1);

        debuggers.add(this);
    }

    public void addSelection(DebugSelection selection){
        if(shouldAdd(selection))
            selections.add(selection);
    }

    public boolean shouldAdd(DebugSelection selection){
        for(DebugSelection sel : selections){
            if(sel.isBlock() && selection.isBlock()){
                if(sel.getBlock().equals(selection.getBlock())){
                    return false;
                }
            }
            else if(sel.isPoint() && selection.isPoint()){
                if(sel.getPoint().equals(selection.getPoint())){
                    return false;
                }
            }
        }
        if(selection.isPoint() && selections.size() > 0)
            if(selections.get(selections.size()-1).isPoint()) {
                boolean b = selections.get(selections.size() - 1).getPoint().distance(selection.getPoint()) > 0.1;
                return b;
            }
        return true;
    }
}
