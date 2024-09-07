package org.example.JsonService;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.AnimalFactory;
import org.example.animals.Animal;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class JsonService {
    private AnimalFactory animalFactory = new AnimalFactory();

    public List<Animal> readAnimalsFromJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(new File("C:\\Users\\sdf\\IdeaProjects\\island\\src\\main\\resources\\animalProperties.json"));
        List<Animal> animals = new ArrayList<>();
        for (JsonNode animalNode : rootNode) {
            String name = animalNode.path("name").asText();
            double weight = animalNode.path("weight").asDouble();
            int maxCount = animalNode.path("maxCount").asInt();
            int speed = animalNode.path("speed").asInt();
            int food = animalNode.path("food").asInt(); // food - int

            // Создаем Animal, используя имя, которое выдает JSON, для корректной работы с AnimalFactory
            animals.add(animalFactory.createAnimal(name, new Animal(0, weight, maxCount, speed, food))); // Передаем 0 для id
        }
        return animals;
    }
    public List<Map.Entry<Animal, HashMap<Animal, Integer>>> parse() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        AnimalFactory animalFactory = new AnimalFactory();
        File json = new File("C:\\Users\\sdf\\IdeaProjects\\island\\src\\main\\resources\\island_animals.json");

        // 1. Парсинг JSON в промежуточную структуру данных
        List<Map<String, Object>> data = objectMapper.readValue(
                json, new TypeReference<List<Map<String, Object>>>() {}
        );

        List<Animal> animalsProperties = readAnimalsFromJson();
        // 2. Преобразование в List<Animal, HashMap<Animal, Integer>>
        List<Map.Entry<Animal, HashMap<Animal, Integer>>> result = data.stream()
                .map(entry -> {
                    // Получаем имя животного из первого ключа в карте
                    String animalName = (String) entry.values().iterator().next();

                    // Находим Animal в animalsProperties
                    Animal animalProperty = animalsProperties.stream()
                            .filter(animal -> animal.getClass().getSimpleName().equalsIgnoreCase(animalName))
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("Animal not found: " + animalName));

                    Animal animal = animalFactory.createAnimal(animalName, animalProperty);
                    HashMap<Animal, Integer> relations = new HashMap<>();
                    Map<String, Object> relationsMap = (Map<String, Object>) entry.get("relations");
                    relationsMap.forEach((k, v) -> {
                        if (!(v instanceof String && ((String) v).equals("-"))) {
                            Animal relatedAnimalProperty = animalsProperties.stream()
                                    .filter(animal1 -> animal1.getClass().getSimpleName().equalsIgnoreCase(k))
                                    .findFirst()
                                    .orElseThrow(() -> new RuntimeException("Animal not found: " + k));
                            Animal relatedAnimal = animalFactory.createAnimal(k, relatedAnimalProperty);
                            if (v instanceof Integer) {
                                relations.put(relatedAnimal, (Integer) v);
                            }
                        }
                    });
                    return Map.entry(animal, relations);
                })
                .toList();

        // Вывод результата
        return result;
    }





}
