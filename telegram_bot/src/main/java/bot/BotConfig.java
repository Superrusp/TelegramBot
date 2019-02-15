package bot;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * The {@code BotConfig} class is used to get the username of the bot and its token from
 * a file with <b>.properties</b> format.
 *
 * @author Amir Dogmosh
 */
public class BotConfig {
    /**
     * The {@value} is used to store the file path.
     */
    private static final String CONFIGURATION_BOT_FILE = "C:\\Users\\PC\\IdeaProjects\\TelegramBot\\bot.properties";

    /**
     * The value identifies the bot username.
     */
    private static String botUserName;

    /**
     * The value identifies the bot token.
     */
    private static String botToken;

    /**
     * This method is used to get the bot username.
     *
     * @return the bot username.
     */
     static String getBotUserName() {
        return botUserName;
    }

    /**
     * This method is used to get the bot token.
     *
     * @return the bot token.
     */
     static String getBotToken() {
        return botToken;
    }

    /**
     * Loads the bot username and token from a file.
     */
    static void load(){
        Properties botSettings = new Properties();
        try(InputStream botConfig = new FileInputStream(new File(CONFIGURATION_BOT_FILE))){
            botSettings.load(botConfig);
            botUserName = botSettings.getProperty("BotUserName");
            botToken = botSettings.getProperty("BotToken");
        } catch(IOException e){
            System.out.println("bot.BotConfig didn't load");
        }
    }
}
