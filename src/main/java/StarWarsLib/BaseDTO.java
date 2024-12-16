package StarWarsLib;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class BaseDTO {
    // Общие методы для всех DTO классов(виды, корабли и т.д)
    public abstract String getName();
    public abstract void setName(String name);
}
