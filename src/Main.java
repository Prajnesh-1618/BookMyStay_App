import java.util.HashMap;
import java.util.Map;

abstract class Room {
    private String type;
    private double price;

    public Room(String type, double price) {
        this.type = type;
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }
}

class SingleRoom extends Room {
    public SingleRoom() {
        super("Single", 100.0);
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double", 180.0);
    }
}

class RoomInventory {
    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
    }

    public void addRoomType(String type, int count) {
        inventory.put(type, count);
    }

    public int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }

    public void updateAvailability(String type, int change) {
        if (inventory.containsKey(type)) {
            inventory.put(type, inventory.get(type) + change);
        }
    }

    public void displayInventory() {
        System.out.println("--- Current Room Inventory ---");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " Rooms: " + entry.getValue());
        }
    }
}

 class BookmystayApp {
    public static void main(String[] args) {
        RoomInventory hotelInventory = new RoomInventory();

        hotelInventory.addRoomType("Single", 10);
        hotelInventory.addRoomType("Double", 5);

        hotelInventory.displayInventory();

        System.out.println("\nUpdating inventory: 1 Single room booked.");
        hotelInventory.updateAvailability("Single", -1);

        hotelInventory.displayInventory();
    }
}