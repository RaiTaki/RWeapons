package xyz.raitaki.rweapons.utils.console;

import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import xyz.raitaki.rweapons.weapon.skills.ConsoleSkill;

public class JDAMessageListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        String text = "";
        if (event.isFromType(ChannelType.TEXT)) {

            text = String.format("[%s][%s] %#s: %s", event.getGuild().getName(),
                    event.getChannel().getName(), event.getAuthor(), event.getMessage().getContentDisplay());
        }
        else {
            text = String.format("[%s][%s] %#s: %s", event.getGuild().getName(),
                    event.getChannel().getName(), event.getAuthor(), event.getMessage().getContentDisplay());
        }
        for(ConsoleSkill consoleSkill : ConsoleSkill.getConsoleViewers()){
            consoleSkill.getViewer().addText(text);
        }
    }
}
