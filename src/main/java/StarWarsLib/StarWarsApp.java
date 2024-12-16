package StarWarsLib;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class StarWarsApp {
    private final RequestHandler requestHandler;
    private JLabel warningLabel; // Для вывода предупреждения

    public StarWarsApp() {
        this.requestHandler = new RequestHandler();
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Star Wars Info App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLayout(new BorderLayout());

        // Установка фона
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(43, 76, 92)); // Светло-синий фон
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Создание элементов интерфейса
        JTextArea outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setBackground(Color.WHITE); // Белый фон для текстовой области
        outputArea.setForeground(Color.BLACK); // Черный текст
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12)); // Шрифт
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        outputArea.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        JTextField inputField = new JTextField();
        inputField.setBackground(Color.WHITE); // Белый фон для поля ввода
        inputField.setForeground(Color.BLACK); // Черный текст
        inputField.setFont(new Font("Monospaced", Font.PLAIN, 14)); // Шрифт
        inputField.setBorder(BorderFactory.createTitledBorder("Введите запрос. Пример: Dagobah"));

        JButton searchButton = new JButton("Поиск");
        JButton showDictionaryButton = new JButton("Словарь");
        JButton switchLanguageButton = new JButton("Смена языка");

        // Создание предупреждающей метки
        warningLabel = new JLabel("Для поиска на русском языке смените язык.", JLabel.CENTER);
        warningLabel.setForeground(Color.RED); // Красный текст для предупреждений
        warningLabel.setVisible(!requestHandler.isRussianDictionary); // Скрыть, если английский язык

        // Задаем стили для кнопок
        JButton[] buttons = {searchButton, showDictionaryButton, switchLanguageButton};
        for (JButton button : buttons) {
            button.setBackground(new Color(69, 86, 108)); // Темно-синий фон
            button.setForeground(Color.WHITE); // Белый текст
            button.setFocusPainted(false);
            button.setBorder(BorderFactory.createRaisedBevelBorder());
            button.setFont(new Font("Arial", Font.BOLD, 12));
        }

        // Добавление компонентов в панель
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(new JScrollPane(outputArea), BorderLayout.CENTER);
        mainPanel.add(inputField, BorderLayout.NORTH);
        mainPanel.add(warningLabel, BorderLayout.SOUTH); // Добавление предупреждения под inputField

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(173, 216, 230)); // Светло-синие кнопки
        buttonPanel.add(searchButton);
        buttonPanel.add(showDictionaryButton);
        buttonPanel.add(switchLanguageButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.getContentPane().add(mainPanel);
        frame.setVisible(true);

        // Обработчик для кнопки поиска
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String query = inputField.getText().trim();
                if (!query.isEmpty()) {
                    // Проверка языка и отображение предупреждения
                    if (requestHandler.isRussianDictionary && !isRussian(query)) {
                        showAlert("Ошибка", "Для поиска на английском смените язык.");
                        return;
                    }
                    if (!requestHandler.isRussianDictionary && isRussian(query)) {
                        showAlert("Ошибка", "Для поиска на русском смените язык.");
                        return;
                    }

                    String response = requestHandler.handleRequest(query);
                    outputArea.setText(response);
                    System.out.println("Обработан запрос: " + query); // Логирование
                } else {
                    showAlert("Ошибка", "Введите запрос.");
                }
            }
        });

        // Обработчик для кнопки выбора языка
        switchLanguageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                requestHandler.setDictionary(!requestHandler.isRussianDictionary);
                String currentLang = requestHandler.isRussianDictionary ? "Русский" : "English";
                warningLabel.setVisible(requestHandler.isRussianDictionary); // Показываем/скрываем предупреждение
                showAlert("Смена языка", "Текущий язык: " + currentLang);
            }
        });

        // Обработчик для кнопки показа словаря
        showDictionaryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showDictionaryDialog();
            }
        });
    }

    private boolean isRussian(String text) {
        return text.chars().anyMatch(ch -> Character.UnicodeScript.of(ch) == Character.UnicodeScript.CYRILLIC);
    }

    private void showDictionaryDialog() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Доступные запросы");
        dialog.setSize(300, 250);
        dialog.setLocationRelativeTo(null);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        JTextArea dictArea = new JTextArea();
        dictArea.setEditable(false);
        dictArea.setText(getDictionaryInfo());
        dictArea.setWrapStyleWord(true);
        dictArea.setLineWrap(true);
        dictArea.setForeground(Color.BLACK); // Черный текст
        dictArea.setBackground(Color.WHITE); // Белый фон
        dictArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        dictArea.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        dialog.getContentPane().add(new JScrollPane(dictArea), BorderLayout.CENTER);
        JButton closeButton = new JButton("Закрыть");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        dialog.getContentPane().add(closeButton, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private String getDictionaryInfo() {
        StringBuilder dictionaryInfo = new StringBuilder();
        Map<String, String> terms = requestHandler.dictionary.getTerms(requestHandler.isRussianDictionary);

        if (terms.isEmpty()) {
            return "Словарь пуст.";
        }

        for (String key : terms.keySet()) {
            dictionaryInfo.append(key).append(" - ").append(terms.get(key)).append("\n");
        }
        return dictionaryInfo.toString();
    }

    private void showAlert(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.WARNING_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StarWarsApp::new);
    }
}
