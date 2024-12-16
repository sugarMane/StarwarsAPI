package StarWarsLib;

import java.util.Scanner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        RequestHandler requestHandler = new RequestHandler();
        Scanner scanner = new Scanner(System.in);

        logger.info("Запуск программы.");
        System.out.println("Введите запрос (или 'exit' для выхода):");

        while (true) {
            String query = scanner.nextLine();
            if (query.equalsIgnoreCase("exit")) {
                logger.info("Выход из программы.");
                break;
            }
            requestHandler.handleRequest(query);
        }

        scanner.close();
        logger.info("Программа завершена.");
    }
}
