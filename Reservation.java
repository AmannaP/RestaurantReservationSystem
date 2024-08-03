import java.time.LocalDateTime;

class Reservation {
    private String customerName;
    private LocalDateTime dateTime;
    private int numberOfPeople;
    private boolean cancelled;
    private Table table;

    public Reservation(String customerName, LocalDateTime dateTime, int numberOfPeople, Table table) {
        this.customerName = customerName;
        this.dateTime = dateTime;
        this.numberOfPeople = numberOfPeople;
        this.cancelled = false;
        this.table = table;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void cancel() {
        this.cancelled = true;
    }

    public String getCustomerName() {
        return customerName;
    }

    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    public Table getTable() {
        return table;
    }
    public void setCancelled(boolean cancelled){
        this.cancelled = cancelled;
    }
    public boolean getCancelled(){
        return this.cancelled;
    }

    @Override
    public String toString() {
        return "Reservation{" +
               "customerName = '" + customerName + '\'' +
               ", dateTime = " + dateTime +
               ", numberOfPeople = " + numberOfPeople +
               ", cancelled = " + cancelled +
               ", table = " + (table != null ? table: "Not assigned") +
               '}';
    }
}