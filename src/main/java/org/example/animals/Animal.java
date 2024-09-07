package org.example.animals;


import lombok.Data;

@Data
public class Animal {
    private final int id;
    private double weight;
    private final int maxCount;
    private final int speed;
    public int food;
    private boolean alive = true;

    public Animal(int id, double weight, int maxCount, int speed, int food){
        this.id = id;
        this.weight = weight;
        this.maxCount = maxCount;
        this.speed = speed;
        this.food = food;
    }

}


//public synchronized void performTurn() {
//        int initialAnimalCount = getAllAnimals().size();
//        int finalAnimalCount = 0;
//        List<Animal> allAnimals = getAllAnimals();
//
//        allAnimals.parallelStream().forEach(animal -> {
//            Coordinate oldCoord = animal.getCoordinates();
//            animal.moveAnimal(this);
//
//            Coordinate newCoord = animal.getCoordinates();
//
//                grid[oldCoord.getX()][oldCoord.getY()].removeAnimal(animal);
//
//            if (newCoord.getX() >= 0 && newCoord.getX() < size && newCoord.getY() >= 0 && newCoord.getY() < size) {
//                    grid[newCoord.getX()][newCoord.getY()].addAnimal(animal);
//            }
//
//        });
//        List<Animal> animalsToRemoveFromHunger = new ArrayList<>();
//        allAnimals.forEach(animal -> {
//            animal.eat(this);
//            if (animal.isStarving()) {
//                animalsToRemoveFromHunger.add(animal);
//                statistics.addDeadFromStarve();
//            }
//        });
//
//        int countBeforeHunger=allAnimals.size();
//        for (int i = 0; i < size; i++) {
//            for (int j = 0; j < size; j++) {
//                Cell cell = grid[i][j];
//                    cell.getAnimals().removeAll(animalsToRemoveFromHunger);
//            }
//        }
//
//    statistics.addDeadAnimal(statistics.getDeadEating()+statistics.getDeadStarve());
//
//
//
//        allAnimals.forEach(animal -> animal.reproduce(this));
//        updateTotalAnimals();
//        updateTotalPlants();
//        System.out.println(statistics.toString());
//        statistics.resetDailyStatistics();
//    }
