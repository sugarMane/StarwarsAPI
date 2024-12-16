package StarWarsLib;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class RequestHandler {

    private final SWAPIService swapiService;
    public final Dictionary dictionary;
    private ObjectMapper objectMapper;
    private static final Logger logger = LogManager.getLogger(RequestHandler.class);
    public boolean isRussianDictionary;

    public RequestHandler() {
        this.swapiService = new SWAPIService();
        this.dictionary = new Dictionary();
        this.objectMapper = new ObjectMapper();
        this.isRussianDictionary = false;
    }

    public String handleRequest(String query) {
        logger.info("Обрабатываем запрос: {}", query);
        String translatedQuery = isRussianDictionary ? dictionary.translate(query) : query;

        if (translatedQuery == null) {
            return "Перевод термина не найден. Пожалуйста, введите корректный запрос.";
        }

        String resourceType = getResourceType(translatedQuery);
        if (resourceType == null) {
            return "Запрос не распознан. Пожалуйста, проверьте ввод или смените язык.";
        }

        if (!checkApiAvailability()) {
            logger.error("Ошибка подключения к SWAPI.");
            return "Ошибка подключения к SWAPI. Пожалуйста, проверьте соединение.";
        }

        Response response = swapiService.searchResource(resourceType, translatedQuery);

        if (response != null && response.statusCode() == 200) {
            String description = dictionary.getDescription(translatedQuery, isRussianDictionary);
            if (description == null || description.isEmpty()) {
                description = "Описание не найдено."; // Обработка отсутствующего описания
            }
            int popularity = dictionary.getRandomPopularity();
            String popularityInfo = (isRussianDictionary ? "Популярность: " : "Popularity: ") + popularity + "%\n";
            StringBuilder result = new StringBuilder(description + "\n" + popularityInfo);

            switch (resourceType) {
                case "people":
                    List<CharacterDTO> characters = response.jsonPath().getList("results", CharacterDTO.class);
                    return characters.isEmpty() ? result.append("Персонаж не найден.").toString()
                            : result.append(displayCharacterInfo(characters.get(0))).toString();
                case "planets":
                    List<PlanetDTO> planets = response.jsonPath().getList("results", PlanetDTO.class);
                    return planets.isEmpty() ? result.append("Планета не найдена.").toString()
                            : result.append(displayPlanetInfo(planets.get(0))).toString();
                case "films":
                    List<FilmDTO> films = response.jsonPath().getList("results", FilmDTO.class);
                    return films.isEmpty() ? result.append("Фильм не найден.").toString()
                            : result.append(displayFilmInfo(films.get(0))).toString();
                case "species":
                    List<SpeciesDTO> species = response.jsonPath().getList("results", SpeciesDTO.class);
                    return species.isEmpty() ? result.append("Вид не найден.").toString()
                            : result.append(displaySpeciesInfo(species.get(0))).toString();
                case "vehicles":
                    List<VehicleDTO> vehicles = response.jsonPath().getList("results", VehicleDTO.class);
                    return vehicles.isEmpty() ? result.append("Транспортное средство не найдено.").toString()
                            : result.append(displayVehicleInfo(vehicles.get(0))).toString();
                case "starships":
                    List<StarshipDTO> starships = response.jsonPath().getList("results", StarshipDTO.class);
                    return starships.isEmpty() ? result.append("Звездный корабль не найден.").toString()
                            : result.append(displayStarshipInfo(starships.get(0))).toString();
                default:
                    return result.append("Неизвестный тип ресурса.").toString();
            }
        } else {
            logger.error("Ошибка при получении данных для запроса '{}': ресурс не найден.", translatedQuery);
            return "Ошибка при получении данных или ресурс не найден.";
        }
    }

    private String getResourceType(String query) {
        String resourceType = dictionary.getTerms(isRussianDictionary).get(query);
        if (resourceType != null) {
            return resourceType;
        }

        resourceType = dictionary.getTerms(!isRussianDictionary).get(query);
        return resourceType;
    }

    private String displayPlanetInfo(PlanetDTO planet) {
        StringBuilder info = new StringBuilder();
        info.append(getLabel("Имя")).append(": ").append(planet.getName()).append("\n")
                .append(getLabel("Период вращения")).append(": ").append(planet.getRotationPeriod()).append("\n")
                .append(getLabel("Орбитальный период")).append(": ").append(planet.getOrbitalPeriod()).append("\n")
                .append(getLabel("Диаметр")).append(": ").append(planet.getDiameter()).append("\n")
                .append(getLabel("Климат")).append(": ").append(planet.getClimate()).append("\n")
                .append(getLabel("Гравитация")).append(": ").append(planet.getGravity()).append("\n")
                .append(getLabel("Рельеф")).append(": ").append(planet.getTerrain()).append("\n")
                .append(getLabel("Водная поверхность")).append(": ").append(planet.getSurfaceWater()).append("\n")
                .append(getLabel("Население")).append(": ").append(planet.getPopulation()).append("\n");
        logger.info("Выведена информация о планете: {}", planet.getName());
        return info.toString();
    }

    private boolean checkApiAvailability() {
        try {
            Response response = swapiService.getResource("people", "1");
            return response.statusCode() == 200;
        } catch (Exception e) {
            logger.error("Ошибка при проверке доступности SWAPI: {}", e.getMessage());
            return false;
        }
    }

    private String displayCharacterInfo(CharacterDTO character) {
        StringBuilder info = new StringBuilder();
        info.append(getLabel("Имя")).append(": ").append(character.getName()).append("\n")
                .append(getLabel("Рост")).append(": ").append(character.getHeight()).append("\n")
                .append(getLabel("Вес")).append(": ").append(character.getMass()).append("\n")
                .append(getLabel("Фильмы")).append(": \n");
        for (String film : character.getFilms()) {
            info.append(" - ").append(film).append("\n");
        }
        logger.info("Выведена информация о персонаже: {}", character.getName());
        return info.toString();
    }

    private String displayFilmInfo(FilmDTO film) {
        StringBuilder info = new StringBuilder();
        info.append(getLabel("Название")).append(": ").append(film.getTitle()).append("\n")
                .append(getLabel("Эпизод")).append(": ").append(film.getEpisodeId()).append("\n")
                .append(getLabel("Режиссер")).append(": ").append(film.getDirector()).append("\n")
                .append(getLabel("Продюсер")).append(": ").append(film.getProducer()).append("\n")
                .append(getLabel("Дата выхода")).append(": ").append(film.getReleaseDate()).append("\n");
        logger.info("Выведена информация о фильме: {}", film.getTitle());
        return info.toString();
    }

    private String displaySpeciesInfo(SpeciesDTO species) {
        StringBuilder info = new StringBuilder();
        info.append(getLabel("Имя")).append(": ").append(species.getName()).append("\n")
                .append(getLabel("Классификация")).append(": ").append(species.getClassification()).append("\n")
                .append(getLabel("Средняя продолжительность жизни")).append(": ").append(species.getAverageLifespan()).append("\n");
        logger.info("Выведена информация о виде: {}", species.getName());
        return info.toString();
    }

    private String displayVehicleInfo(VehicleDTO vehicle) {
        StringBuilder info = new StringBuilder();
        info.append(getLabel("Имя")).append(": ").append(vehicle.getName()).append("\n")
                .append(getLabel("Модель")).append(": ").append(vehicle.getModel()).append("\n")
                .append(getLabel("Производитель")).append(": ").append(vehicle.getManufacturer()).append("\n");
        logger.info("Выведена информация о транспортном средстве: {}", vehicle.getName());
        return info.toString();
    }

    private String displayStarshipInfo(StarshipDTO starship) {
        StringBuilder info = new StringBuilder();
        info.append(getLabel("Имя")).append(": ").append(starship.getName()).append("\n")
                .append(getLabel("Модель")).append(": ").append(starship.getModel()).append("\n")
                .append(getLabel("Производитель")).append(": ").append(starship.getManufacturer()).append("\n");
        logger.info("Выведена информация о звездном корабле: {}", starship.getName());
        return info.toString();
    }

    private String getLabel(String term) {
        return isRussianDictionary ? term : translateLabel(term); // Поменять язык меток
    }

    private String translateLabel(String term) {
        switch (term) {
            case "Имя":
                return "Name";
            case "Период вращения":
                return "Rotation Period";
            case "Орбитальный период":
                return "Orbital Period";
            case "Диаметр":
                return "Diameter";
            case "Климат":
                return "Climate";
            case "Гравитация":
                return "Gravity";
            case "Рельеф":
                return "Terrain";
            case "Водная поверхность":
                return "Surface Water";
            case "Население":
                return "Population";
            case "Рост":
                return "Height";
            case "Вес":
                return "Mass";
            case "Фильмы":
                return "Films";
            case "Эпизод":
                return "Episode";
            case "Режиссер":
                return "Director";
            case "Продюсер":
                return "Producer";
            case "Дата выхода":
                return "Release Date";
            case "Классификация":
                return "Classification";
            case "Средняя продолжительность жизни":
                return "Average Lifespan";
            case "Модель":
                return "Model";
            case "Производитель":
                return "Manufacturer";
            default:
                return term;
        }
    }

    public void setDictionary(boolean isRussian) {
        this.isRussianDictionary = isRussian;
    }
}
