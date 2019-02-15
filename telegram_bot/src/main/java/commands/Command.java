package commands;

/**
 * The {@code Command} enum represents all available bot commands.
 *
 * @author Amir Dogmosh
 */
public enum Command {

    ADD_SWEAR("/addswear", "/addswear@ChatLeader_Bot"),
    REMOVE_SWEAR("/removeswear", "/removeswear@ChatLeader_Bot"),
    ADD_PHOTO("/addphoto", "/addsphoto@ChatLeader_Bot"),
    REMOVE_PHOTO("/removephoto", "/removephoto@ChatLeader_Bot");

    /**
     * The value represents the command name.
     */
    private String name;

    /**
     * The value represents the command full name.
     */
    private String fullName;

    Command(String name, String fullName){
        this.name = name;
        this.fullName = fullName;
    }

    /**
     * This method is used to get the command name.
     *
     * @return the command name.
     */
    public String getName() {
        return name;
    }

    /**
     * This method is used to get the length of command name.
     * @return the length of command name.
     */
    public int getNameLength(){
        return name.length();
    }

    /**
     * This method is used to get the command full name.
     *
     * @return the command full name.
     */
    public String getFullName() {
        return fullName;
    }
}
