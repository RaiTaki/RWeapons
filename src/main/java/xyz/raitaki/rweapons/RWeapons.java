package xyz.raitaki.rweapons;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.raitaki.rweapons.commands.TestParticleCommand;
import xyz.raitaki.rweapons.weapon.Events;
import xyz.raitaki.rweapons.weapon.weapons.NanoItem;
import xyz.raitaki.rweapons.weapon.weapons.Stone;

import java.util.UUID;

import static org.bukkit.Bukkit.getPluginManager;

public final class RWeapons extends JavaPlugin {

    @Getter private static RWeapons instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        getPluginManager().registerEvents(new Events(), this);
        new Stone(UUID.fromString("33ee38b6-d98f-4782-ac6f-485d59aaa4c2"));
        new NanoItem(UUID.fromString("57b773e7-4a3a-4a0b-b721-7c8b78bd8fb1"));

        getCommand("testparticle").setExecutor(new TestParticleCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
