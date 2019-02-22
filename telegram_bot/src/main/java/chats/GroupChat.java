package chats;

import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.User;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * The {@code GroupChat} class provides storage for information about each group chat.
 *
 * @author Amir Dogmosh
 */
public class GroupChat {

    /**
     * The collection is used to keep swear words.
     */
    private Set<String> swearWords;

    /**
     * The map is used to keep information about chat members and their warnings.
     */
    private Map<User, Integer> members;

    /**
     * The map is used to keep photos and triggers on it.
     */
    private Map<String, PhotoSize> photoTriggers;

    /**
     * The constructor initializes collections for new group chat.
     */
    public GroupChat(){
        swearWords = new HashSet<>();
        members = new HashMap<>();
        photoTriggers = new HashMap<>();
    }

    /**
     * This method is used to get the swear words.
     *
     * @return the collection of swear words.
     */
    public Set<String> getSwearWords() {
        return swearWords;
    }

    /**
     * This method is used to get chat members and the number of their warnings.
     *
     * @return the map of chat members and the number of their warnings.
     */
    public Map<User, Integer> getMembers() {
        return members;
    }

    /**
     * This method is used to get photos and triggers on it.
     *
     * @return the map of captions and appropriate photos in it.
     */
    public Map<String, PhotoSize> getPhotoTriggers() {
        return photoTriggers;
    }
}
