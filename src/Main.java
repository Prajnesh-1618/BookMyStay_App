import java.util.*;
import java.util.concurrent.*;

// Booking Request Class
class BookingRequest {
    private String guestName;
    private int roomsRequested;

    public BookingRequest(String guestName, int roomsRequested) {
        this.guestName = guestName;
        this.roomsRequested = roomsRequested;
    }

    public String getGuestName() {
        return guestName;
    }

    public int getRoomsRequested() {
        return roomsRequested;
    }
}

// Shared Inventory (Thread Safe)
class RoomInventory {
    private int availableRooms;

    public RoomInventory(int rooms) {
        this.availableRooms = rooms;
    }

    // Critical Section
    public synchronized boolean allocateRooms(int rooms) {
        if (availableRooms >= rooms) {
            System.out.println(Thread.currentThread().getName() +
                    " allocating " + rooms + " rooms...");
            availableRooms -= rooms;
            System.out.println("Remaining rooms: " + availableRooms);
            return true;
        } else {
            System.out.println(Thread.currentThread().getName() +
                    " failed (Not enough rooms)");
            return false;
        }
    }
}

// Booking Processor (Thread)
class BookingProcessor implements Runnable {
    private Queue<BookingRequest> bookingQueue;
    private RoomInventory inventory;

    public BookingProcessor(Queue<BookingRequest> bookingQueue, RoomInventory inventory) {
        this.bookingQueue = bookingQueue;
        this.inventory = inventory;
    }

    @Override
    public void run() {
        while (true) {
            BookingRequest request;

            // Synchronize queue access
            synchronized (bookingQueue) {
                if (bookingQueue.isEmpty()) {
                    break;
                }
                request = bookingQueue.poll();
            }

            if (request != null) {
                System.out.println(Thread.currentThread().getName() +
                        " processing booking for " + request.getGuestName());

                inventory.allocateRooms(request.getRoomsRequested());

                try {
                    Thread.sleep(100); // simulate delay
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

// Main Class
 class BookmystayApp{

    public static void main(String[] args) {

        // Shared Queue
        Queue<BookingRequest> bookingQueue = new LinkedList<>();

        // Add booking requests
        bookingQueue.add(new BookingRequest("Alice", 2));
        bookingQueue.add(new BookingRequest("Bob", 3));
        bookingQueue.add(new BookingRequest("Charlie", 4));
        bookingQueue.add(new BookingRequest("David", 2));
        bookingQueue.add(new BookingRequest("Eve", 1));

        // Shared Inventory
        RoomInventory inventory = new RoomInventory(7);

        // Create Threads
        Thread t1 = new Thread(new BookingProcessor(bookingQueue, inventory), "Thread-1");
        Thread t2 = new Thread(new BookingProcessor(bookingQueue, inventory), "Thread-2");
        Thread t3 = new Thread(new BookingProcessor(bookingQueue, inventory), "Thread-3");

        // Start Threads
        t1.start();
        t2.start();
        t3.start();

        // Wait for completion
        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\nAll bookings processed safely!");
    }
}