package commands;

import chats.GroupChat;
import chats.GroupChatHandler;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * The {@code RemovePhotoCommand} class that extends {@link BotCommand} class.
 * This class represents a command that allows group chat administrators to remove a photo with a caption from list.
 * @see GroupChat#photoTriggers
 *
 * @author Amir Dogmosh
 */
public class RemovePhotoCommand extends BotCommand {

    public RemovePhotoCommand(Command commandIdentifier) {
        super(commandIdentifier);
    }

    /**
     * This method overrides {@link IBotCommand#execute(AbsSender, Message)} method.
     * Used to execute a command that allows group chat administrators to remove a photo with a caption from list.
     *
     * @param absSender the bot {@link bot.ModeratorBot}.
     * @param message the message from group chat.
     */
    @Override
    public void execute(AbsSender absSender, Message message) {
        if(GroupChatHandler.isMessageFromAdmin(absSender, message)){
            String triggerText = message.getText().substring(Command.REMOVE_PHOTO.getNameLength()).trim();
            if(GroupChatHandler.getGroupChats().get(message.getChatId()).getPhotoTriggers().containsKey(triggerText)){
                GroupChatHandler.getGroupChats().get(message.getChatId()).getPhotoTriggers().remove(triggerText);
                sendMessage(absSender,"Фотография с подписью " + triggerText +"  была удалена!", message.getChatId());
            }
            else sendMessage(absSender,"Даное слово отсутствует в списке триггеров на фото. " +
                        "\nКоманда не выполнена!", message.getChatId());
        }
        else if(!GroupChatHandler.isMessageFromAdmin(absSender, message)) {
            sendMessage(absSender,"Удалять слова из списка триггеров может только администратор.",message.getChatId());
        }
    }
}
