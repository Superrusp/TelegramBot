package chats;

import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatAdministrators;
import org.telegram.telegrambots.meta.api.objects.ChatMember;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The {@code GroupChatHandler} class works with group chat repositories.
 *
 * @author Amir Dogmosh
 */
public class GroupChatHandler {

    /**
     * The map is used to keep information about chat id and {@link GroupChat} object.
     */
    private static Map<Long, GroupChat> groupChats = new HashMap<>();

    /**
     * This method checks if there are storage for the current group chat.
     *
     * @param message the message from group chat.
     */
    public synchronized static void checkIfStorageExistsForChat(Message message){
        if(!groupChats.containsKey(message.getChatId())){
            groupChats.put(message.getChatId(), new GroupChat());
        }
        putChatMemberOnTheWarningCounter(message);
    }

    /**
     * This method puts chat member in the collection and represents a warning counter for him.
     *
     * @param message the message from group chat.
     */
    public synchronized static void putChatMemberOnTheWarningCounter(Message message){
        if(!groupChats.get(message.getChatId()).getMembers().containsKey(message.getFrom())){
            int startWarningCount = 0;
            groupChats.get(message.getChatId()).getMembers().put(message.getFrom(), startWarningCount);
        }
    }

    /**
     * This method removes member from group chat.
     *
     * @param message the message from group chat.
     */
    public synchronized static void removeMemberFromGroupChat(Message message){
        groupChats.get(message.getChatId()).getMembers().remove(message.getFrom());
    }

    /**
     * This method checks if a message received from the group administrator.
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
     * This method is used to get group chats.
     *
     * @return the map of id chats and {@link GroupChat} object.
     */
    public static Map<Long, GroupChat> getGroupChats() {
        return groupChats;
    }
}
