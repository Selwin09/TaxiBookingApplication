package taxibooking.model;

import java.time.LocalDateTime;

public class Booking {
    private int bookingId;
    private User user;
    private Taxi taxi;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String pickupLocation;
    private String destination;

    public Booking(int bookingId, User user, Taxi taxi, LocalDateTime startTime, LocalDateTime endTime, String pickupLocation, String destination) {
        this.bookingId = bookingId;
        this.user = user;
        this.taxi = taxi;
        this.startTime = startTime;
        this.endTime = endTime;
        this.pickupLocation = pickupLocation;
        this.destination = destination;
    }


    public int getBookingId() {
        return bookingId;
    }

    public User getUser() {
        return user;
    }

    public Taxi getTaxi() {
        return taxi;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }
    
    public String getPickupLocation() {
        return pickupLocation;
    }
    
    public String getDestination() {
    	return destination;
    }
}
