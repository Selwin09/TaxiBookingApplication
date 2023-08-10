package taxibooking;

public class Taxi {
    private int taxiId;
    private boolean available;

    public Taxi(int taxiId, boolean available) {
        this.taxiId = taxiId;
        this.available = available;
    }

    public int getTaxiId() {
        return taxiId;
    }

    public void setTaxiId(int taxiId) {
        this.taxiId = taxiId;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}

