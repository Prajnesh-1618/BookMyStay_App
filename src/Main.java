import java.util.*;

// Reservation Request
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

// Room Inventory
class RoomInventory {
    private Map<String, Integer> rooms;

    public RoomInventory() {
        rooms = new HashMap<>();
    }

    public void addRooms(String type, int count) {
        rooms.put(type, rooms.getOrDefault(type, 0) + count);
    }

    public int getCount(String type) {
        return rooms.getOrDefault(type, 0);
    }

    public void updateAvailability(String type, int change) {
        int current = rooms.getOrDefault(type, 0);
        int updated = current + change;

        if (updated < 0) {
            throw new IllegalArgumentException("Not enough rooms available");
        }

        rooms.put(type, updated);
    }
}

// Booking Service
class BookingService {
    private RoomInventory inventory;
    private Map<String, Set<String>> allocatedRooms;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
        this.allocatedRooms = new HashMap<>();
        this.allocatedRooms.put("Single", new HashSet<>());
        this.allocatedRooms.put("Double", new HashSet<>());
    }

    public void processBooking(ReservationRequest request) {
        String type = request.getRoomType();
        int available = inventory.getCount(type);

        if (available > 0) {
            // Generate Room ID (S-2, D-1)
            String roomId = type.substring(0, 1) + "-" + available;

            allocatedRooms.get(type).add(roomId);

            // Reduce inventory
            inventory.updateAvailability(type, -1);

            System.out.println("CONFIRMED: " + request.getGuestName() +
                    " assigned Room " + roomId + " (" + type + ")");
        } else {
            System.out.println("FAILED: No availability for " + request.getGuestName() +
                    " (" + type + ")");
        }
    }
}

// Main Class (ONLY public class)
 class BookmystayApp {
    public static void main(String[] args) {

        // Setup Inventory
        RoomInventory inventory = new RoomInventory();
        inventory.addRooms("Single", 2);
        inventory.addRooms("Double", 1);

        // Booking Queue (FIFO)
        Queue<ReservationRequest> queue = new LinkedList<>();
        queue.add(new ReservationRequest("Alice", "Single"));
        queue.add(new ReservationRequest("Bob", "Single"));
        queue.add(new ReservationRequest("Charlie", "Single")); // should fail

        BookingService bookingService = new BookingService(inventory);

        // Process bookings
        System.out.println("--- Processing Allocations ---");
        while (!queue.isEmpty()) {
            bookingService.processBooking(queue.poll());
        }
    }
}