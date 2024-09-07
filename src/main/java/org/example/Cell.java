package org.example.animals;


import lombok.Data;
import org.example.Constants;
import org.example.animals.Animal;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
public class Cell {
    List<Animal> syncListAnimals = Collections.synchronizedList(new ArrayList<Animal>(Constants.animalsCount/2));
    List<Herbivore> syncList = Collections.synchronizedList(new ArrayList<Herbivore>(Constants.animalsCount/2));
    List<Grass> grasses = new ArrayList<>(); // Добавлен список для травы

    public void addAnimal(Animal animal) {
        synchronized (syncListAnimals) {
            syncListAnimals.add(animal);
        }
    }

    public void removeAnimal(Animal animal) {
        synchronized (syncListAnimals) {
            syncListAnimals.remove(animal);
        }
    }

    public void addHerbivore(Herbivore herbivore) {
        synchronized (syncList) {
            syncList.add(herbivore);
        }
    }

    public void removeHerbivore(Herbivore herbivore) {
        synchronized (syncList) {
            syncList.remove(herbivore);
        }
    }

    public void addGrass(Grass grass) {
        synchronized (grasses) {
            grasses.add(grass);
        }
    }

    public void removeGrass() {
        synchronized (grasses) {
            grasses.remove(0); // Удаляем первый объект Grass
        }
    }

    public boolean isEmpty() {
        return syncListAnimals.isEmpty() && syncList.isEmpty();
    }

    public List<Animal> getSyncListAnimals() {
        return syncListAnimals;
    }

    public List<Herbivore> getSyncList() {
        return syncList;
    }

    public List<Grass> getGrasses() {
        return grasses;
    }
}
