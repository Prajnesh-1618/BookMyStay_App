import java.io.*;
import java.util.*;

// Booking Request (Serializable)
class BookingRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String guestName;
    private int roomsBooked;

    public BookingRequest(String guestName, int roomsBooked) {
        this.guestName = guestName;
        this.roomsBooked = roomsBooked;
    }

    public String getGuestName() {
        return guestName;
    }

    public int getRoomsBooked() {
        return roomsBooked;
    }

    @Override
    public String toString() {
        return guestName + " booked " + roomsBooked + " rooms";
    }
}

// Inventory (Serializable)
class RoomInventory implements Serializable {
    private static final long serialVersionUID = 1L;

    private int availableRooms;

    public RoomInventory(int rooms) {
        this.availableRooms = rooms;
    }

    public void allocateRooms(int rooms) {
        if (availableRooms >= rooms) {
            availableRooms -= rooms;
        }
    }

    public int getAvailableRooms() {
        return availableRooms;
    }
}

// Wrapper class for system state
class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;

    List<BookingRequest> bookings;
    RoomInventory inventory;

    public SystemState(List<BookingRequest> bookings, RoomInventory inventory) {
        this.bookings = bookings;
        this.inventory = inventory;
    }
}

// Persistence Service
class PersistenceService {

    private static final String FILE_NAME = "system_state.ser";

    // Save state
    public static void saveState(SystemState state) {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            oos.writeObject(state);
            System.out.println("✅ System state saved successfully!");

        } catch (IOException e) {
            System.out.println("❌ Error saving state: " + e.getMessage());
        }
    }

    // Load state
    public static SystemState loadState() {
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            System.out.println("⚠️ No previous state found. Starting fresh.");
            return null;
        }

        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            System.out.println("✅ System state restored successfully!");
            return (SystemState) ois.readObject();

        } catch (Exception e) {
            System.out.println("❌ Error loading state. Starting fresh.");
            return null;
        }
    }
}

// Main Class
class BookmystayApp {

    public static void main(String[] args) {

        // Try to restore previous state
        SystemState state = PersistenceService.loadState();

        List<BookingRequest> bookings;
        RoomInventory inventory;

        if (state != null) {
            bookings = state.bookings;
            inventory = state.inventory;
        } else {
            bookings = new ArrayList<>();
            inventory = new RoomInventory(10);
        }

        // Simulate new bookings
        BookingRequest b1 = new BookingRequest("Alice", 2);
        BookingRequest b2 = new BookingRequest("Bob", 3);

        bookings.add(b1);
        bookings.add(b2);

        inventory.allocateRooms(b1.getRoomsBooked());
        inventory.allocateRooms(b2.getRoomsBooked());

        // Display current state
        System.out.println("\n📌 Current Bookings:");
        for (BookingRequest b : bookings) {
            System.out.println(b);
        }

        System.out.println("🏨 Available Rooms: " + inventory.getAvailableRooms());

        // Save state before shutdown
        PersistenceService.saveState(new SystemState(bookings, inventory));
    }
}