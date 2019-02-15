package commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.logging.BotLogger;

/**
 * The {@code BotCommand} abstract class that implements {@link IBotCommand} interface.
 *
 * @author Amir Dogmosh
 */
public abstract class BotCommand implements IBotCommand {

    /**
     * The value identifies the command name.
     */
    private String commandIdentifier;

    public BotCommand(String commandIdentifier){
        if(commandIdentifier == null || commandIdentifier.isEmpty()){
            throw new IllegalArgumentException("commandIdentifier cannot be null or empty");
        }
        this.commandIdentifier = commandIdentifier;
    }

    /**
     * This method is used to send a chat message.
     *
     * @param sender the bot {@link bot.ModeratorBot}.
     * @param text the text that will be sent to the group chat.
     * @param chatId the id chat to send to.
     */
    public void sendMessage(AbsSender sender, String text, long chatId){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        try {
            sender.execute(sendMessage);
        }catch (TelegramApiException e){
            BotLogger.error(this.getClass().getName(), e);
        }
    }

    /**
     * This method is used to get the command name.
     *
     * @return the command name.
     */
    @Override
    public final String getCommandIdentifier() {
        return commandIdentifier;
    }
}
