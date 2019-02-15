package bot;

import chats.ChatInfo;
import commands.*;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.groupadministration.KickChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.logging.BotLogger;
import java.util.Map;

/**
 * The {@code ModeratorBot} class represents a bot that can act as a moderator in group chats.
 *
 * @author Amir Dogmosh
 */
public class ModeratorBot extends TelegramLongPollingBot {

    /**
     * The {@value} identifies the count of available warnings in the group chat.
     */
    private static final int COUNT_OF_WARNINGS = 3;

    /**
     * List of commands.
     */
    private AddSwearCommand addSwearCommand;
    private RemoveSwearCommand removeSwearCommand;
    private AddPhotoCommand addPhotoCommand;
    private RemovePhotoCommand removePhotoCommand;


    public ModeratorBot(){
       addSwearCommand = new AddSwearCommand(Command.ADD_SWEAR.getName());
       removeSwearCommand = new RemoveSwearCommand(Command.REMOVE_SWEAR.getName());
       addPhotoCommand = new AddPhotoCommand(Command.ADD_PHOTO.getName());
       removePhotoCommand = new RemovePhotoCommand(Command.REMOVE_PHOTO.getName());
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (update.hasMessage() && update.getMessage().isSuperGroupMessage()) {
                Message message = update.getMessage();

                ChatInfo.checkIfStorageExistsForChat(message);
                handleIncomingMessage(message);
            }
        }catch (Exception e){
            BotLogger.error(this.getClass().getName(), e);
        }
    }

    /**
     * This method is used to process incoming message and send the appropriate response to the chat member.
     *
     * @param message the message from group chat.
     */
    private void handleIncomingMessage(Message message){
        if(message.hasText()) {
            if(message.getText().startsWith("/")){
                executeCommand(message);
            }
            else {
                checkMessageOnSwearWord(message);
                checkPhotoTrigger(message);
            }
        }

        else if (message.hasPhoto() && !message.getCaption().isEmpty()) {
            if (message.getCaption().startsWith("/")) {
                executeCommand(message);
            }
        }

        else if(message.getLeftChatMember() != null){
            User user = message.getLeftChatMember();
            sendMessage("Счастливого пути, " + user.getFirstName()+ "!", message.getChatId());
            ChatInfo.getChats().get(message.getChatId()).remove(user);
        }
        else if(!message.getNewChatMembers().isEmpty()) {
            for (User user : message.getNewChatMembers()) {
                int startWarningCount = 0;
                ChatInfo.getChats().get(message.getChatId()).put(message.getFrom(), startWarningCount);
                sendWelcomeMessageWithAttachedRules(user, message);
            }
        }
    }

    /**
     * This method is used to check message on swear word.
     *
     * @param message the message from group chat.
     */
    private void checkMessageOnSwearWord(Message message) {
        for (String swearWord : ChatInfo.getSwearWords().get(message.getChatId())) {
            if (message.getText().contains(swearWord)) {
                for (Map.Entry<User, Integer> member : ChatInfo.getChats().get(message.getChatId()).entrySet()){
                    if (member.getKey().getId().equals(message.getFrom().getId())) {
                        warnChatMember(member.getKey(), message);
                    }
                }
            }
        }
    }

    /**
     * This method is used to check whether a word is a trigger.
     * If a word is a trigger, the bot send the appropriate photo.
     *
     * @param message the message from group chat.
     */
    private void checkPhotoTrigger(Message message){
        for(Map.Entry<Long, Map<String, PhotoSize>> photoTriggers : ChatInfo.getPhotoTriggers().entrySet()){
            if(message.getChatId().equals(photoTriggers.getKey())) {
                for (Map.Entry<String, PhotoSize> photos : photoTriggers.getValue().entrySet()) {
                    if (message.getText().contains(photos.getKey())) {
                        sendPhoto(photoTriggers.getValue().get(message.getText()), message.getChatId());
                    }
                }
            }
        }
    }

    /**
     * This method is used to warn chat member that he has broken the group chat rules.
     *
     * @param member the member of group chat.
     * @param message the message from group chat.
     */
    private void warnChatMember(User member, Message message){
        int currentNumOfWarnings = ChatInfo.getChats().get(message.getChatId()).get(member);
            if(currentNumOfWarnings < COUNT_OF_WARNINGS){
                int newNumOfWarnings = currentNumOfWarnings + 1;
                ChatInfo.getChats().get(message.getChatId()).replace(member, newNumOfWarnings);
                sendMessage(member.getFirstName() + ", Нельзя ругаться!" +
                    " [Предупреждений " + newNumOfWarnings + "/" + COUNT_OF_WARNINGS + "]", message.getChatId());

                if(newNumOfWarnings == COUNT_OF_WARNINGS)
                    sendMessage(member.getFirstName() + ", за следущее нарушение правил получите бан!", message.getChatId());
            }
            else banChatMember(member, message);
    }

    /**
     * This method is used to banish chat member because he has exceeded the number of valid warnings.
     *
     * @param member the member of group chat.
     * @param message the message from group chat.
     */
    private void banChatMember(User member, Message message){
        if(!ChatInfo.isMessageFromAdmin(this, message)) {
            int lockoutTimeInSeconds = 86400;
            KickChatMember kickChatMember = new KickChatMember();
            kickChatMember.setChatId(message.getChatId());
            kickChatMember.setUserId(message.getFrom().getId());
            kickChatMember.setUntilDate(lockoutTimeInSeconds);
            ChatInfo.getChats().get(message.getChatId()).remove(member);
            try {
                execute(kickChatMember);
                sendMessage("Пользователь " + member.getFirstName() + " забанен на сутки.", message.getChatId());
            } catch (TelegramApiException e) {
                BotLogger.error(this.getClass().getName(), e);
            }
        } else {
            sendMessage(member.getFirstName()+ ", соблюдайте правила даже если имеете полномочия администратора!"
                    ,message.getChatId());
        }
    }

    /**
     * This method is used to identify via regular expression if the message from group chat is a command or not.
     *
     * @param message the message from group chat.
     * @param commandName the command name.
     * @return the name of the command with the word as a trigger.
     */
    private boolean isCommand(Message message, String commandName){
        if(message.getText() == null){
            return message.getCaption().matches(commandName + " \\S+");
        }
        else return message.getText().matches(commandName + " \\S+");
    }

    /**
     * This method is used to find a command that matches the message and execute it.
     * If command is not found, the bot sends a message that the command isn't recognized.
     *
     * @param message the message from group chat.
     */
    private void executeCommand(Message message){

        if(isCommand(message, addSwearCommand.getCommandIdentifier())){
           addSwearCommand.execute(this, message);
       }

       else if(isCommand(message, removeSwearCommand.getCommandIdentifier())){
           removeSwearCommand.execute(this, message);
       }

       else if(isCommand(message, addPhotoCommand.getCommandIdentifier())){
           addPhotoCommand.execute(this, message);
         }
         else if(isCommand(message, removePhotoCommand.getCommandIdentifier())){
             removePhotoCommand.execute(this, message);
       }

       else sendMessage("Команда не распознана", message.getChatId());
    }

    /**
     * This method is used to send a chat message.
     *
     * @param text the text that will be sent to the group chat.
     * @param chatId the id chat to send to.
     */
    private void sendMessage(String text, long chatId){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        try {
            execute(sendMessage);
        }catch (TelegramApiException e){
            e.printStackTrace();
        }
    }

    /**
     * This method is used to send a greeting message with attached rules if a new chat member appears.
     *
     * @param member the member of group chat.
     * @param message the message from group chat.
     */
    private void sendWelcomeMessageWithAttachedRules(User member, Message message){
        sendMessage("Привет, " + member.getFirstName() + " !" +
                "\nПожалуйста соблюдай правила и не ругайся.", message.getChatId());

        sendMessage("СПИСОК ПРАВИЛ" +
                "\nУчастникам ЗАПРЕЩАЕТСЯ:" +
                "\n- использовать при общении в чате нецензурные выражения;" +
                "\n- создавать сообщения, относящиеся к оффтопу, флейму, флуду; " +
                "\n- оставлять сообщения, носящие рекламный характер." +
                "\n\nНарушения правил караются предупреждениями, временным или постоянным исключением из чата" +
                " – в зависимости от тяжести нарушения и на усмотрение Администраторов.", message.getChatId());
    }

    /**
     * This method is used to send a photo that matches a specific word.
     *
     * @param photoSize the photo that will be sent.
     * @param chatId the chat id where the photo will be sent.
     */
    private void sendPhoto(PhotoSize photoSize, long chatId) {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(chatId);
        sendPhoto.setPhoto(photoSize.getFileId());
        try {
            execute(sendPhoto);
        } catch (TelegramApiException e) {
            BotLogger.error(this.getClass().getName(), e);
        }
    }

    /**
     * This method is used to get the bot username.
     *
     * @return the bot username.
     */
    @Override
    public String getBotUsername() {
        return BotConfig.getBotUserName();
    }

    /**
     * This method is used to get the bot token.
     *
     * @return the bot token.
     */
    @Override
    public String getBotToken() {
        return BotConfig.getBotToken();
    }
}
