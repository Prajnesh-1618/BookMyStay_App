import java.io.*;
import java.util.ArrayList;
import java.util.List;

// --- Domain Model (Must be Serializable) ---
class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;
    public int availableRooms;
    public List<String> bookingHistory;

    public SystemState(int rooms, List<String> history) {
        this.availableRooms = rooms;
        this.bookingHistory = history;
    }
}

// --- Persistence Service ---
class PersistenceService {
    private static final String FILE_NAME = "hotel_data.ser";

    public void saveState(int rooms, List<String> history) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            SystemState state = new SystemState(rooms, history);
            oos.writeObject(state);
            System.out.println(">> System state saved successfully to " + FILE_NAME);
        } catch (IOException e) {
            System.err.println("Error saving state: " + e.getMessage());
        }
    }

    public SystemState loadState() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println(">> No saved state found. Starting fresh.");
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            System.out.println(">> Recovery successful! Restoring previous state...");
            return (SystemState) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error recovering state: " + e.getMessage());
            return null;
        }
    }
}

 class BookmystayApp {


    public static void main(String[] args) {
        PersistenceService persistence = new PersistenceService();

        // 1. System Startup / Recovery
        SystemState recovered = persistence.loadState();
        int currentRooms = (recovered != null) ? recovered.availableRooms : 10;
        List<String> history = (recovered != null) ? recovered.bookingHistory : new ArrayList<>();

        System.out.println("Current Inventory: " + currentRooms);
        System.out.println("Current History Size: " + history.size());

        // 2. Simulate some activity
        System.out.println("\nProcessing new booking...");
        if (currentRooms > 0) {
            currentRooms--;
            history.add("Booking_" + System.currentTimeMillis());
        }

        // 3. System Shutdown / Persistence
        System.out.println("\nShutting down...");
        persistence.saveState(currentRooms, history);

        System.out.println("Final State - Rooms: " + currentRooms + ", History: " + history.size());
        System.out.println("(Run the program again to see the values persist!)");
    }
}