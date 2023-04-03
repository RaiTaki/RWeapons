package xyz.raitaki.rweapons;

import de.leonhard.storage.Json;
import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.raitaki.rweapons.commands.TestParticleCommand;
import xyz.raitaki.rweapons.utils.console.JDAMessageListener;
import xyz.raitaki.rweapons.utils.console.LogAppender;
import xyz.raitaki.rweapons.weapon.Events;
import xyz.raitaki.rweapons.weapon.skills.ConsoleSkill;
import xyz.raitaki.rweapons.weapon.weapons.NanoItem;
import xyz.raitaki.rweapons.weapon.weapons.Stone;
import xyz.raitaki.rweapons.weapon.weapons.UsainItem;

import java.io.File;
import java.time.Duration;
import java.util.UUID;

import static org.bukkit.Bukkit.getPluginManager;

public final class RWeapons extends JavaPlugin {

    @Getter private static RWeapons instance;
    @Getter private static Json jsonConfig;
    @Getter private static JDA jda;
    private Logger logger;
    private LogAppender appender;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        getPluginManager().registerEvents(new Events(), this);
        new Stone(UUID.fromString("33ee38b6-d98f-4782-ac6f-485d59aaa4c2"));
        new NanoItem(UUID.fromString("57b773e7-4a3a-4a0b-b721-7c8b78bd8fb1"));
        new UsainItem(UUID.fromString("f9556281-e908-4d06-8962-f9aada1d091d"));
        getCommand("testparticle").setExecutor(new TestParticleCommand());

        logger = (Logger) LogManager.getRootLogger();
        appender = new LogAppender();
        logger.addAppender(appender);

        jsonConfig = new Json(new File(this.getDataFolder().getAbsolutePath() + "/config.json"));

        String token = jsonConfig.getOrSetDefault("discord.token", "token");
        if(token.length() < 10){
            getLogger().warning("§4Please set a valid discord bot token in the config.json file!");
        }else{
            jda = JDABuilder.createDefault(token)
                    .enableIntents(GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS))
                    .addEventListeners(new JDAMessageListener()).build();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        logger.removeAppender(appender);
        for(ConsoleSkill consoleSkill : ConsoleSkill.getConsoleViewers()){
            consoleSkill.getViewer().removeDisplays();
        }
        jda.shutdownNow();
    }
}
