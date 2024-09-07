package org.example;

public class Statistics  {
    private static Statistics statistics_instance;
    private int bornAnimals;
    private int deadAnimals;

    private int deadStarve;

    private int deadEating;
    private int totalAnimals;
    private int grownPlants;
    private int totalPlants;
    public static synchronized Statistics getInstance() {
        if (statistics_instance == null) {
            statistics_instance = new Statistics();
        }
        return statistics_instance;
    }
    private Statistics() {
        this.bornAnimals = 0;
        this.deadAnimals = 0;
        this.totalAnimals = 0;
        this.grownPlants = 0;
        this.totalPlants = 0;
        this.deadStarve=0;
        this.deadEating=0;
    }

    public void addBornAnimal() {
        bornAnimals++;
    }
    public void addDeadAnimal(int count) {
        this.deadAnimals += count;
    }


    public void setTotalAnimals(int totalAnimals) {
        this.totalAnimals = totalAnimals;
    }

    public void addGrownPlant(int count) {
        this.grownPlants+=count;
    }

    public void setTotalPlants(int totalPlants) {
        this.totalPlants = totalPlants;
    }

    public void resetDailyStatistics() {
        bornAnimals = 0;
        deadAnimals = 0;
        grownPlants = 0;
        deadStarve=0;
        deadEating=0;
    }

    public void addDeadFromStarve()
    {
        deadStarve++;
    }
    public void addDeadFromPredation()
    {
        deadEating++;
    }

    @Override
    public String toString() {
        return "Статистика дня:\n" +
                "Количество рожденных животных: " + bornAnimals + "\n" +
                "Количество умерших животных: " + deadAnimals + "(от голода: "+deadStarve+","+
                "в результате умерщвления: "+deadEating+")"+"\n" +
                "Общее количество животныхна конец дня: " + totalAnimals + "\n" +
                "Количество выросших растений: " + grownPlants + "\n" +
                "Общее количество рвстений нв  конец дня:: " + totalPlants + "\n";
    }

    public int getDeadStarve() {
        return deadStarve;
    }

    public int getDeadEating() {
        return deadEating;
    }
}