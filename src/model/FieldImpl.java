package model;

public class FieldImpl implements Field {
    private int amountOfRobots;
    private int robotId;
    private int coordinateX;
    private int coordinateY;
    private int id; 

    /**
     * 
     * @param amountOfRobots
     * @param robotId
     * @param coordinateX
     * @param coordinateY
     */
    public FieldImpl(int amountOfRobots, int robotId, int coordinateX, int coordinateY) {
        this.amountOfRobots = amountOfRobots;
        this.robotId = robotId;
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
    }

    public FieldImpl(int id, int coordinateX, int coordinateY) {
        amountOfRobots = 0;
        this.id = id;
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
    }

    public int hasRobots() {
        return amountOfRobots;
    }


    public boolean isBoxingPlant() {
        return true;
    }

    public void reg(Robot bot) {
        amountOfRobots++;
        this.robotId = bot.id();
    }

    public void unReg() {
        amountOfRobots--;
        this.robotId = 0;
    }

    public int robotID() {
        return robotId;
    }

    @Override
    public int coordinateX() {
        return this.coordinateX;
    }

    @Override
    public int coordinateY() {
        return this.coordinateY;
    }

    public int getId() {
        return this.id;
    }

}
