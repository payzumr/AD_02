package model;

public class Field implements IField {
    private int amountOfRobots;
    private IRobot robot;
    private final int coordinateX;
    private final int coordinateY;
    private int id;

    public Field(int amountOfRobots, int robotId, int coordinateX, int coordinateY) {
        this.amountOfRobots = amountOfRobots;
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
    }

    public Field(int id, int coordinateX, int coordinateY) {
        amountOfRobots = 0;
        this.id = id;
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
    }
    
    public Field(Item item) {
      amountOfRobots = 0;

      coordinateX = item.productPosX();
      coordinateY = item.productPosY();
  }

    public int robotCount() {
        return amountOfRobots;
    }

    public boolean isBoxingPlant() {
        return this.id != 0;
    }

    public void registerRobot(IRobot bot) {
        amountOfRobots++;
        robot = bot;
    }

    public void unregisterRobot() {
        amountOfRobots--;
        robot = null;
    }

    public int robotID() {
        if (robot == null) {
            return 0;
        }
        return robot.id();
    }

    public int[] Target() {
        if (robot != null) {
            return robot.Target();
        } else {
            int i[] = { -1, -1 };
            return i;
        }
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
