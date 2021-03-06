package commands;

import chats.GroupChat;
import chats.GroupChatHandler;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * The {@code AddPhotoCommand} class that extends {@link BotCommand} class.
 * This class represents a command that allows group chat administrators to add a photo with a caption to the list.
 * @see GroupChat#photoTriggers
 *
 * @author Amir Dogmosh
 */
public class AddPhotoCommand extends BotCommand {

    public AddPhotoCommand(Command commandIdentifier) {
        super(commandIdentifier);
    }

    /**
     * This method overrides {@link IBotCommand#execute(AbsSender, Message)} method.
     * Used to execute a command that allows group chat administrators to add a photo with a caption to the list.
     *
     * @param absSender the bot {@link bot.ModeratorBot}.
     * @param message the message from group chat.
     */
    @Override
    public void execute(AbsSender absSender, Message message){
        if(message.hasPhoto()) {
            for (PhotoSize photoSize : message.getPhoto()) {
                String triggerText = message.getCaption().substring((Command.ADD_PHOTO.getNameLength())).trim();
                GroupChatHandler.getGroupChats().get(message.getChatId()).getPhotoTriggers().put(triggerText, photoSize);
            }
            sendMessage(absSender, "Фото добавлено", message.getChatId());
        }
    }
}
