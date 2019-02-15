package commands;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * The {@code IBotCommand} interface presents abstract methods {@link #execute(AbsSender, Message)} and
 * {@link #getCommandIdentifier()} for classes of heirs.
 *
 * @author Amir Dogmosh
 */
public interface IBotCommand {

    void execute(AbsSender absSender, Message message);
    String getCommandIdentifier();
}
