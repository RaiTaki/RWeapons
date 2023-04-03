package xyz.raitaki.rweapons.utils.console;


import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import xyz.raitaki.rweapons.weapon.skills.ConsoleSkill;

public class LogAppender extends AbstractAppender {

    // your variables

    public LogAppender() {
        // do your calculations here before starting to capture
        super("MyLogAppender", null, null);
        start();
    }

    @Override
    public void append(LogEvent event) {
        // if you don`t make it immutable, than you may have some unexpected behaviours
        LogEvent log = event.toImmutable();

        // do what you have to do with the log

        // you can get only the log message like this:
        String message = log.getMessage().getFormattedMessage();

        // and you can construct your whole log message like this:
        for(ConsoleSkill consoleSkill : ConsoleSkill.getConsoleViewers()){
            consoleSkill.getViewer().addText(message);
        }
    }

}