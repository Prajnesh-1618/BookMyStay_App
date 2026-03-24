import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

abstract class Room {
    private String type;
    private double price;

    public Room(String type, double price) {
        this.type = type;
        this.price = price;
    }

    public String getType() { return type; }
    public double getPrice() { return price; }

    public void displayInfo() {
        System.out.println("Room: " + type + " | Price: $" + price);
    }
}

class SingleRoom extends Room { public SingleRoom() { super("Single", 100.0); } }
class DoubleRoom extends Room { public DoubleRoom() { super("Double", 180.0); } }

class RoomInventory {
    private Map<String, Integer> counts = new HashMap<>();

    public void addRooms(String type, int count) { counts.put(type, count); }

    public int getCount(String type) {
        return counts.getOrDefault(type, 0);
    }

    public List<String> getAllRoomTypes() {
        return new ArrayList<>(counts.keySet());
    }
}

class SearchService {
    private RoomInventory inventory;
    private List<Room> roomTemplates;

    public SearchService(RoomInventory inventory) {
        this.inventory = inventory;
        this.roomTemplates = new ArrayList<>();
        roomTemplates.add(new SingleRoom());
        roomTemplates.add(new DoubleRoom());
    }

    public void searchAvailableRooms() {
        System.out.println("--- Search Results: Available Rooms ---");
        boolean found = false;

        for (Room room : roomTemplates) {
            int available = inventory.getCount(room.getType());

            if (available > 0) {
                room.displayInfo();
                System.out.println("Status: " + available + " units left.");
                System.out.println("------------------------------------");
                found = true;
            }
        }

        if (!found) {
            System.out.println("No rooms currently available.");
        }
    }
}

 class BookmystatApp {
    public static void main(String[] args) {
        RoomInventory hotelInventory = new RoomInventory();
        hotelInventory.addRooms("Single", 5);
        hotelInventory.addRooms("Double", 0); // Sold out

        SearchService searchService = new SearchService(hotelInventory);

        // Guest initiates search
        searchService.searchAvailableRooms();
    }
}