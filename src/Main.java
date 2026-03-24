abstract class Room {
    private String type;
    private int beds;
    private double price;

    public Room(String type, int beds, double price) {
        this.type = type;
        this.beds = beds;
        this.price = price;
    }

    public void displayDetails(int availability) {
        System.out.println("Room Type: " + type);
        System.out.println("Beds: " + beds);
        System.out.println("Price: $" + price);
        System.out.println("Available Units: " + availability);
        System.out.println("---------------------------");
    }
}

class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1, 100.0);
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2, 180.0);
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Luxury Suite", 3, 350.0);
    }
}

 class BookmystayApp {
    public static void main(String[] args) {
        Room single = new SingleRoom();
        Room cpuDouble = new DoubleRoom();
        Room suite = new SuiteRoom();

        int singleAvailability = 10;
        int doubleAvailability = 5;
        int suiteAvailability = 2;

        System.out.println("=== Hotel Room Availability ===");
        single.displayDetails(singleAvailability);
        cpuDouble.displayDetails(doubleAvailability);
        suite.displayDetails(suiteAvailability);
    }
}