import java.util.*;

// Custom Exception
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Booking System
class BookingSystem {
    private Map<String, Integer> inventory = new HashMap<>();
    private List<String> history = new ArrayList<>();

    // Add room type
    public void addRoomType(String type, int count) {
        inventory.put(type, inventory.getOrDefault(type, 0) + count);
    }

    // Process booking
    public void processBooking(String bookingId, String type, int qty)
            throws InvalidBookingException {

        if (!inventory.containsKey(type)) {
            throw new InvalidBookingException("Room type '" + type + "' does not exist.");
        }

        int available = inventory.get(type);

        if (qty > available) {
            throw new InvalidBookingException(
                    "Not enough rooms available. Requested: " + qty + ", Available: " + available);
        }

        // Deduct inventory
        inventory.put(type, available - qty);

        // Save history
        history.add("ID: " + bookingId + " | Room: " + type + " | Qty: " + qty);

        System.out.println("Success: Booking " + bookingId + " confirmed!");
    }

    // Show booking history
    public void showHistory() {
        System.out.println("\n--- Current Booking History ---");
        for (String record : history) {
            System.out.println(record);
        }

        int total = inventory.values().stream().mapToInt(Integer::intValue).sum();
        System.out.println("Remaining Inventory: " + total);
    }
}

// Main Class (ONLY public class)
 class BookmystayApp {
    public static void main(String[] args) {

        BookingSystem system = new BookingSystem();

        // Setup inventory
        system.addRoomType("Standard", 5);
        system.addRoomType("Deluxe", 3);
        system.addRoomType("Penthouse", 2);

        try {
            system.processBooking("BK001", "Standard", 2);
            system.processBooking("BK002", "Penthouse", 1);
            system.processBooking("BK003", "Standard", 3); // valid now
        } catch (InvalidBookingException e) {
            System.err.println("Error: " + e.getMessage());
        }

        system.showHistory();
    }
}