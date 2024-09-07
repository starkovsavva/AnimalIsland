package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.animals.Animal;
import org.example.animals.AnimalList.herbivore.*;
import org.example.animals.AnimalList.predator.*;

import javax.lang.model.element.AnnotationMirror;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

public class AnimalFactory {


    private List<Animal> animals;
    public Animal createAnimal(String animalName, Animal animalProperty) {
        switch (animalName) {
            case "Wolf":{
                return new Wolf(animalProperty.getId(),animalProperty.getWeight(),animalProperty.getMaxCount(),animalProperty.getSpeed(), animalProperty.getFood());
            }
            case "Bear":{

                return new Bear(animalProperty.getId(),animalProperty.getWeight(),animalProperty.getMaxCount(),animalProperty.getSpeed(), animalProperty.getFood());
            }
            case "Fox":
                return new Fox(animalProperty.getId(),animalProperty.getWeight(),animalProperty.getMaxCount(),animalProperty.getSpeed(), animalProperty.getFood());
            case "Eagle":
                return new Eagle(animalProperty.getId(),animalProperty.getWeight(),animalProperty.getMaxCount(),animalProperty.getSpeed(), animalProperty.getFood());
            case "Horse":
                return new Horse(animalProperty.getId(),animalProperty.getWeight(),animalProperty.getMaxCount(),animalProperty.getSpeed(), animalProperty.getFood());
            case "Deer":
                return new Deer(animalProperty.getId(),animalProperty.getWeight(),animalProperty.getMaxCount(),animalProperty.getSpeed(), animalProperty.getFood());
            case "Rabbit":
                return new Rabbit(animalProperty.getId(),animalProperty.getWeight(),animalProperty.getMaxCount(),animalProperty.getSpeed(), animalProperty.getFood());
            case "Mouse":
                return new Mouse(animalProperty.getId(),animalProperty.getWeight(),animalProperty.getMaxCount(),animalProperty.getSpeed(), animalProperty.getFood());
            case "Goat":
                return new Goat(animalProperty.getId(),animalProperty.getWeight(),animalProperty.getMaxCount(),animalProperty.getSpeed(), animalProperty.getFood());
            case "Sheep":
                return new Sheep(animalProperty.getId(),animalProperty.getWeight(),animalProperty.getMaxCount(),animalProperty.getSpeed(), animalProperty.getFood());
            case "Boar":
                return new Boar(animalProperty.getId(),animalProperty.getWeight(),animalProperty.getMaxCount(),animalProperty.getSpeed(), animalProperty.getFood());
            case "Buffalo":
                return new Buffalo(animalProperty.getId(),animalProperty.getWeight(),animalProperty.getMaxCount(),animalProperty.getSpeed(), animalProperty.getFood());
            case "Duck":
                return new Duck(animalProperty.getId(),animalProperty.getWeight(),animalProperty.getMaxCount(),animalProperty.getSpeed(), animalProperty.getFood());
            case "Caterpillar":
                return new Caterpillar(animalProperty.getId(),animalProperty.getWeight(),animalProperty.getMaxCount(),animalProperty.getSpeed(), animalProperty.getFood());
            case "Boa":
                return new Boa(animalProperty.getId(),animalProperty.getWeight(),animalProperty.getMaxCount(),animalProperty.getSpeed(),animalProperty.getFood());
            default:
                throw new IllegalArgumentException("Unknown animal: " + animalName);
        }

    }


    public Animal createObject(Animal animal) {
        Animal newEntity = null;
        try {
            Class<?> clazz = animal.getClass();
            Constructor<?> constructor = clazz.getConstructor(double.class, int.class);
            newEntity = (Animal) constructor.newInstance(animal.getWeight(), animal.getSpeed());
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
        return newEntity;
    }

    private static List<Animal> readAnimalsFromJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(new File("C:\\Users\\sdf\\IdeaProjects\\island\\src\\main\\resources\\animalProperties.json"));
        List<Animal> animals = new ArrayList<>();
        for (JsonNode animalNode : rootNode) {
            int id = animalNode.get("id").asInt();
            double weight = animalNode.get("weight").asDouble();
            int maxCount = animalNode.get("maxCount").asInt();
            int speed = animalNode.get("speed").asInt();
            int food = animalNode.get("food").asInt();
            animals.add(new Animal(id,weight,maxCount,speed,food));
        }
        return animals;
    }


}
