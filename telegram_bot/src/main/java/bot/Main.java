package bot;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * The {@code Main} class is used to launch the Bot.
 *
 * @author Amir Dogmosh
 */
public class Main {
    public static void main(String[] args) {
        BotConfig.load();
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new ModeratorBot());
        } catch (TelegramApiException ex) {
            ex.printStackTrace();
        }
    }
}
