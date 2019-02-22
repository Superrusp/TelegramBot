package commands;

import chats.GroupChat;
import chats.GroupChatHandler;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * The {@code RemoveSwearCommand} class that extends {@link BotCommand} class.
 * This class represents a command that allows group chat administrators to remove a swear word from list.
 * @see GroupChat#swearWords
 *
 * @author Amir Dogmosh
 */
public class RemoveSwearCommand extends BotCommand {

    public RemoveSwearCommand(Command commandIdentifier) {
        super(commandIdentifier);
    }

    /**
     * This method overrides {@link IBotCommand#execute(AbsSender, Message)} method.
     * Used to execute a command that allows group chat administrator to remove a swear word from list.
     *
     * @param absSender the bot {@link bot.ModeratorBot}.
     * @param message the message from group chat.
     */
    @Override
    public void execute(AbsSender absSender, Message message){
        if (GroupChatHandler.isMessageFromAdmin(absSender, message)){
            String swearWord = message.getText().substring(Command.REMOVE_SWEAR.getNameLength()).trim();
            if (GroupChatHandler.getGroupChats().get(message.getChatId()).getSwearWords().contains(swearWord)){
                GroupChatHandler.getGroupChats().get(message.getChatId()).getSwearWords().remove(swearWord);
                sendMessage(absSender,"Слово " + swearWord+"  было удалено!", message.getChatId());
            } else {
                sendMessage(absSender,"Даное слово отсутствует в списке как ругательное. " +
                        "\nКоманда не выполнена!", message.getChatId());
            }
        }
        else if(!GroupChatHandler.isMessageFromAdmin(absSender, message)) {
            sendMessage(absSender,"Удалять слова из списка ругательных может только администратор.",message.getChatId());
        }
    }
}
