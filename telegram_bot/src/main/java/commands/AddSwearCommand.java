package commands;

import chats.GroupChat;
import chats.GroupChatHandler;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * The {@code AddSwearCommand} class that extends {@link BotCommand} class.
 * This class represents a command that allows group chat administrators to add a new word as a swear.
 * @see GroupChat#swearWords
 *
 * @author Amir Dogmosh
 */
public class AddSwearCommand extends BotCommand {

    public AddSwearCommand(Command commandIdentifier) {
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
        if (GroupChatHandler.isMessageFromAdmin(absSender, message)){
            String newSwearWord = message.getText().substring(Command.ADD_SWEAR.getNameLength()).trim();
            if (!GroupChatHandler.getGroupChats().get(message.getChatId()).getSwearWords().contains(newSwearWord)){
                GroupChatHandler.getGroupChats().get(message.getChatId()).getSwearWords().add(newSwearWord);
                sendMessage(absSender,"Добавлено новое ругательное слово : " + newSwearWord, message.getChatId());
            } else {
                sendMessage(absSender,"Даное слово уже есть в списке как ругательное. \nКоманда не выполнена!", message.getChatId());
            }
        }
        else if(!GroupChatHandler.isMessageFromAdmin(absSender, message)) {
            sendMessage(absSender,"Добавлять новые слова может только администратор.",message.getChatId());
        }
    }
}
