package commands;

import chats.ChatInfo;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * The {@code AddSwearCommand} class that extends {@link BotCommand} class.
 * This class represents a command that allows group chat administrators to add a new word as a swear.
 * @see chats.ChatInfo#swearWords
 *
 * @author Amir Dogmosh
 */
public class AddSwearCommand extends BotCommand {

    public AddSwearCommand(String commandIdentifier) {
        super(commandIdentifier);
    }

    /**
     * This method overrides {@link IBotCommand#execute(AbsSender, Message)} method.
     * Used to execute a command that allows group chat administrator to add new word as a swear.
     *
     * @param absSender the bot {@link bot.ModeratorBot}.
     * @param message the message from group chat.
     */
    @Override
    public void execute(AbsSender absSender, Message message) {
        if (ChatInfo.isMessageFromAdmin(absSender, message)){
            String newSwearWord = message.getText().substring(Command.ADD_SWEAR.getNameLength()).trim();
            if (!ChatInfo.getSwearWords().get(message.getChatId()).contains(newSwearWord)){
                ChatInfo.getSwearWords().get(message.getChatId()).add(newSwearWord);
                sendMessage(absSender,"Добавлено новое ругательное слово : " + newSwearWord, message.getChatId());
            } else {
                sendMessage(absSender,"Даное слово уже есть в списке как ругательное. \nКоманда не выполнена!", message.getChatId());
            }
        }
        else if(!ChatInfo.isMessageFromAdmin(absSender, message)) {
            sendMessage(absSender,"Добавлять новые слова может только администратор.",message.getChatId());
        }
    }
}
