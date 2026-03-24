import java.util.*;

// --- Core Room Logic (UC2) ---
abstract class Room {
    private String type;
    private double price;

    public Room(String type, double price) {
        this.type = type;
        this.price = price;
    }

    public String getType() { return type; }
    public double getPrice() { return price; }
}

class SingleRoom extends Room { public SingleRoom() { super("Single", 100.0); } }
class DoubleRoom extends Room { public DoubleRoom() { super("Double", 180.0); } }

// --- Inventory Management (UC3 & UC4) ---
class RoomInventory {
    private Map<String, Integer> counts = new HashMap<>();

    public void addRooms(String type, int count) { counts.put(type, count); }

    public int getCount(String type) {
        return counts.getOrDefault(type, 0);
    }

    public void updateAvailability(String type, int change) {
        if (counts.containsKey(type)) {
            counts.put(type, counts.get(type) + change);
        }
    }
}

// --- Request Handling (UC5) ---
class ReservationRequest {
    private String guestName;
    private String roomType;

    public ReservationRequest(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
}

// --- Allocation Logic (UC6) ---
class BookingService {
    private RoomInventory inventory;
    private Set<String> allocatedRoomIDs;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
        this.allocatedRoomIDs = new HashSet<>();
    }

    public void processQueue(Queue<ReservationRequest> queue) {
        System.out.println("--- Processing Reservations (FIFO) ---");
        while (!queue.isEmpty()) {
            ReservationRequest request = queue.poll();
            String type = request.getRoomType();

            if (inventory.getCount(type) > 0) {
                // Generate Unique ID and update inventory
                String id = type.substring(0, 1).toUpperCase() + "-" + System.nanoTime() % 1000;
                allocatedRoomIDs.add(id);
                inventory.updateAvailability(type, -1);

                System.out.println("SUCCESS: " + request.getGuestName() + " assigned to " + id);
            } else {
                System.out.println("FAILED: No " + type + " rooms left for " + request.getGuestName());
            }
        }
    }
}

// --- Entry Point ---
 class BookmystayApp {
    public static void main(String[] args) {
        // 1. Initialize Inventory
        RoomInventory inventory = new RoomInventory();
        inventory.addRooms("Single", 2);
        inventory.addRooms("Double", 1);

        // 2. Setup Booking Queue (FIFO)
        Queue<ReservationRequest> queue = new LinkedList<>();
        queue.add(new ReservationRequest("Alice", "Single"));
        queue.add(new ReservationRequest("Bob", "Double"));
        queue.add(new ReservationRequest("Charlie", "Double")); // Should fail (only 1 Double exists)
        queue.add(new ReservationRequest("Diana", "Single"));

        // 3. Run Allocation Service
        BookingService service = new BookingService(inventory);
        service.processQueue(queue);

        System.out.println("\nProcess Complete.");
    }
}