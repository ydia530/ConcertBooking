package asg.concert.service.domain;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BookingRequest {

    private long concertId;
    private LocalDateTime date;
    private List<String> seatLabels = new ArrayList<>();

    public BookingRequest(){}

    public BookingRequest(long concertId, LocalDateTime date) {
        this.concertId = concertId;
        this.date = date;
    }

    public BookingRequest(long concertId, LocalDateTime date, List<String> seatLabels) {
        this.concertId = concertId;
        this.date = date;
        this.seatLabels = seatLabels;
    }

    public long getConcertId() {
        return concertId;
    }

    public void setConcertId(long concertId) {
        this.concertId = concertId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public List<String> getSeatLabels() {
        return seatLabels;
    }

    public void setSeatLabels(List<String> seatLabels) {
        this.seatLabels = seatLabels;
    }
}
