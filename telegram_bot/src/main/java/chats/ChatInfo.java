package chats;

import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatAdministrators;
import org.telegram.telegrambots.meta.api.objects.ChatMember;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

/**
 * The {@code ChatInfo} class provides storage for information about each chat.
 *
 * @author Amir Dogmosh
 */
public class ChatInfo {

    /**
     * The map is used to keep swear words in each group chat.
     */
    private static Map<Long, Set<String>> swearWords = new HashMap<>();

    /**
     * The map is used to keep information about members and their count of warnings in each group chat.
     */
    private static Map<Long, Map<User, Integer>> chats = new HashMap<>();

    /**
     * The map is used to keep photos and triggers on it in each group chat.
     */
    private static Map<Long, Map<String, PhotoSize>> photoTriggers = new HashMap<>();

    /**
     * The map is used to keep information about chat member and his warnings in the current group chat.
     */
    private static Map<User, Integer> userWarnings;

    /**
     * This method is used to
     *
     * @param absSender the bot {@link bot.ModeratorBot}.
     * @param message the message from group chat.
     * @return {@code true} if the chat member is the group administrator.
     */
    public static boolean isMessageFromAdmin(AbsSender absSender, Message message){
        try {
            GetChatAdministrators chatAdministrators = new GetChatAdministrators();
            chatAdministrators.setChatId(message.getChatId());
            List<ChatMember> administrators = absSender.execute(chatAdministrators);

            boolean isAdmin = false;
            for (ChatMember administrator : administrators) {
                if (administrator.getUser().getId().equals(message.getFrom().getId())) {
                    isAdmin = true;
                    break;
                }
            }
            return isAdmin;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * This method checks if there are storage facilities for the current group chat.
     *
     * @param message the message from group chat.
     */
    public static void checkIfStorageExistsForChat(Message message){

        if (!photoTriggers.containsKey(message.getChatId())) {
            Map<String, PhotoSize> chatPhotoTriggers = new HashMap<>();
            photoTriggers.put(message.getChatId(), chatPhotoTriggers);
        }
        if (!swearWords.containsKey(message.getChatId())) {
            Set<String> chatSwearWords = new HashSet<>();
            swearWords.put(message.getChatId(), chatSwearWords);
            userWarnings = new HashMap<>();
            chats.put(message.getChatId(), userWarnings);
        }
        if (!chats.get(message.getChatId()).containsKey(message.getFrom())) {
            int startWarningCount = 0;
            userWarnings.put(message.getFrom(), startWarningCount);
            chats.put(message.getChatId(), userWarnings);
        }
    }

    /**
     * This method is used to get the swear words in each chat.
     *
     * @return the map of id chats and a collection of swear words in it.
     */
    public static Map<Long, Set<String>> getSwearWords() {
        return swearWords;
    }

    /**
     * This method is used to get the chats.
     *
     * @return the map of id chats and a map of chat members and their warnings in it.
     */
    public static Map<Long, Map<User, Integer>> getChats() {
        return chats;
    }

    /**
     * This method is used to get the photos and triggers on it in each group chat.
     *
     * @return the map of id chats and a map of captions and appropriate photos in it.
     */
    public static Map<Long, Map<String, PhotoSize>> getPhotoTriggers() {
        return photoTriggers;
    }
}
