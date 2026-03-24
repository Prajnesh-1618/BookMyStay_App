import java.util.LinkedList;
import java.util.Queue;

class ReservationRequest {
    private String guestName;
    private String roomType;

    public ReservationRequest(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    @Override
    public String toString() {
        return "Request [Guest: " + guestName + ", Room Type: " + roomType + "]";
    }
}

class BookingQueueManager {
    private Queue<ReservationRequest> requestQueue;

    public BookingQueueManager() {
        this.requestQueue = new LinkedList<>();
    }

    public void addRequest(ReservationRequest request) {
        requestQueue.add(request);
        System.out.println("Added to queue: " + request);
    }

    public void displayQueue() {
        System.out.println("\n--- Current Booking Queue (FIFO Order) ---");
        if (requestQueue.isEmpty()) {
            System.out.println("Queue is empty.");
        } else {
            for (ReservationRequest req : requestQueue) {
                System.out.println(req);
            }
        }
    }

    public ReservationRequest nextRequest() {
        return requestQueue.peek();
    }
}

 class BookmystayApp {
    public static void main(String[] args) {
        BookingQueueManager queueManager = new BookingQueueManager();

        // Simulating simultaneous requests arriving at different times
        queueManager.addRequest(new ReservationRequest("Alice", "Single"));
        queueManager.addRequest(new ReservationRequest("Bob", "Double"));
        queueManager.addRequest(new ReservationRequest("Charlie", "Single"));

        // Displaying the preserved arrival order
        queueManager.displayQueue();

        System.out.println("\nNext request to be processed: " + queueManager.nextRequest());
    }
}