import java.util.*;

// --- Domain Model ---
class Reservation {
    private String id;
    private String roomType;
    private boolean isCancelled;

    public Reservation(String id, String roomType) {
        this.id = id;
        this.roomType = roomType;
        this.isCancelled = false;
    }

    public String getId() { return id; }
    public boolean isCancelled() { return isCancelled; }
    public void setCancelled(boolean cancelled) { isCancelled = cancelled; }

    @Override
    public String toString() {
        return String.format("ID: %s | Room: %s | Status: %s",
                id, roomType, (isCancelled ? "CANCELLED" : "CONFIRMED"));
    }
}

// --- Cancellation & Rollback Service ---
class BookingService {
    private int inventoryCount = 5;
    private Map<String, Reservation> reservations = new HashMap<>();
    // Stack tracks released room IDs for LIFO rollback logic
    private Stack<String> releasedRooms = new Stack<>();

    public void createBooking(String id, String type) {
        if (inventoryCount > 0) {
            Reservation res = new Reservation(id, type);
            reservations.put(id, res);
            inventoryCount--;
            System.out.println("Confirmed: " + id);
        }
    }

    public void cancelBooking(String bookingId) {
        System.out.println("\nAttempting to cancel: " + bookingId);

        // 1. Validation: Does it exist?
        Reservation res = reservations.get(bookingId);
        if (res == null || res.isCancelled()) {
            System.out.println("Error: Cancellation failed. Booking invalid or already cancelled.");
            return;
        }

        // 2. State Reversal
        res.setCancelled(true);
        inventoryCount++; // Restore inventory

        // 3. Rollback Structure (Stack)
        // Simulating the room ID associated with this booking being returned
        String roomId = "ROOM-" + bookingId.substring(2);
        releasedRooms.push(roomId);

        System.out.println("Success: Inventory restored. Room " + roomId + " added to rollback stack.");
    }

    public void showStatus() {
        System.out.println("\n--- Current System State ---");
        System.out.println("Available Inventory: " + inventoryCount);
        System.out.println("Released Rooms (Stack): " + releasedRooms);
        reservations.values().forEach(System.out::println);
        System.out.println("----------------------------\n");
    }
}

 class BookmystayApp {
    public static void main(String[] args) {
        BookingService service = new BookingService();

        // Setup: Create some bookings
        service.createBooking("BK101", "Deluxe");
        service.createBooking("BK102", "Standard");

        service.showStatus();

        // Test Case: Valid Cancellation
        service.cancelBooking("BK102");

        // Test Case: Invalid Cancellation (Already cancelled)
        service.cancelBooking("BK102");

        // Test Case: Invalid Cancellation (Non-existent)
        service.cancelBooking("BK999");

        service.showStatus();
    }
}