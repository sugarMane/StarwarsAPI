package StarWarsLib;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Dictionary {
    private final Map<String, String> russianTerms;
    private final Map<String, String> englishTerms;
    private final Map<String, String> translations;
    private final Map<String, String> descriptionsEN; // Описания на английском

    public Dictionary() {
        russianTerms = new HashMap<>();
        russianTerms.put("Люк Скайуокер", "people");
        russianTerms.put("Йода", "people");
        russianTerms.put("Татуин", "planets");
        russianTerms.put("Дагоба", "planets");
        russianTerms.put("Дарт Вейдер", "people");
        russianTerms.put("Рей", "people");
        russianTerms.put("Алдераан", "planets");
        russianTerms.put("Звезда смерти", "starships");
        russianTerms.put("Новая надежда", "films");
        russianTerms.put("Р5-Д4", "people");
        russianTerms.put("Ши Скайуокер", "people");
        russianTerms.put("Набу", "planets");
        russianTerms.put("Мон Кала", "planets");
        russianTerms.put("Атака клонов", "films");
        russianTerms.put("Оуэн Ларс", "people");
        russianTerms.put("Гридо", "people");

        englishTerms = new HashMap<>();
        englishTerms.put("Luke Skywalker", "people");
        englishTerms.put("Yoda", "people");
        englishTerms.put("Tatooine", "planets");
        englishTerms.put("Dagobah", "planets");
        englishTerms.put("Darth Vader", "people");
        englishTerms.put("Rey", "people");
        englishTerms.put("Alderaan", "planets");
        englishTerms.put("Death Star", "starships");
        englishTerms.put("A New Hope", "films");
        englishTerms.put("R5-D4", "people");
        englishTerms.put("Shmi Skywalker", "people");
        englishTerms.put("Naboo", "planets");
        englishTerms.put("Mon Cala", "planets");
        englishTerms.put("Attack of the Clones", "films");
        englishTerms.put("Owen Lars", "people");
        englishTerms.put("Greedo", "people");

        translations = new HashMap<>();
        translations.put("Люк Скайуокер", "Luke Skywalker");
        translations.put("Йода", "Yoda");
        translations.put("Татуин", "Tatooine");
        translations.put("Дагоба", "Dagobah");
        translations.put("Дарт Вейдер", "Darth Vader");
        translations.put("Рей", "Rey");
        translations.put("Алдераан", "Alderaan");
        translations.put("Звезда смерти", "Death Star");
        translations.put("Новая надежда", "A New Hope");
        translations.put("Р5-Д4", "R5-D4");
        translations.put("Ши Скайуокер", "Shmi Skywalker");
        translations.put("Набу", "Naboo");
        translations.put("Мон Кала", "Mon Cala");
        translations.put("Атака клонов", "Attack of the Clones");
        translations.put("Оуэн Ларс", "Owen Lars");
        translations.put("Гридо", "Greedo");

        // Описания на английском
        descriptionsEN = new HashMap<>();
        descriptionsEN.put("Luke Skywalker", "Luke Skywalker - a hero, Jedi who fights the Empire.");
        descriptionsEN.put("Yoda", "Yoda - a green character, Jedi Master.");
        descriptionsEN.put("Tatooine", "Tatooine - a desert planet, Luke's home.");
        descriptionsEN.put("Dagobah", "Dagobah - a swampy planet where Yoda trains Jedi.");
        descriptionsEN.put("Darth Vader", "Darth Vader - the main antagonist of the franchise.");
        descriptionsEN.put("Rey", "Rey - the new heroine exploring the Jedi power.");
        descriptionsEN.put("Alderaan", "Alderaan - a planet destroyed by the Death Star.");
        descriptionsEN.put("Death Star", "Death Star - a massive space station and superweapon.");
        descriptionsEN.put("A New Hope", "A New Hope - the first film of the original trilogy.");
        descriptionsEN.put("R5-D4", "R5-D4 - a droid serving Luke.");
        descriptionsEN.put("Shmi Skywalker", "Shmi Skywalker - Luke's mother.");
        descriptionsEN.put("Naboo", "Naboo - a planet with beautiful water landscapes.");
        descriptionsEN.put("Mon Cala", "Mon Cala - the home of Mon Calamari.");
        descriptionsEN.put("Attack of the Clones", "Attack of the Clones - the second film of the prequel trilogy.");
        descriptionsEN.put("Owen Lars", "Owen Lars - Luke's uncle.");
        descriptionsEN.put("Greedo", "Greedo - the smuggler competing with Han Solo.");
    }

    public String getDescription(String term, boolean isRussian) {
        return isRussian ? null : descriptionsEN.get(term); // Возврат описания в зависимости от языка
    }

    public int getRandomPopularity() {
        return new Random().nextInt(101); // Случайная популярность от 0 до 100
    }

    public Map<String, String> getTerms(boolean isRussian) {
        return isRussian ? russianTerms : englishTerms; // Возвращаем термины в зависимости от языка
    }

    public String translate(String russianTerm) {
        return translations.get(russianTerm); // Возврат английского термина
    }
}