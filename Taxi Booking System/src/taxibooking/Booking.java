package taxibooking;

import java.time.LocalDateTime;

public class Booking {
    private int bookingId;
    private User user;
    private Taxi taxi;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Booking(int bookingId, User user, Taxi taxi, LocalDateTime startTime, LocalDateTime endTime) {
        this.bookingId = bookingId;
        this.user = user;
        this.taxi = taxi;
        this.startTime = startTime;
        this.endTime = endTime;
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
}
