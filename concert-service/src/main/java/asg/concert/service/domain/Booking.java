package asg.concert.service.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
public class Booking {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CONCERT_ID", nullable = false)
    private Concert concert;
    private LocalDateTime date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @OneToMany(cascade = CascadeType.REFRESH)
    private List<Seat> seats;
    public Booking() {
    }

    public Booking(Concert concert, LocalDateTime date, List<Seat> seats, User user) {
        this.concert = concert;
        this.date = date;
        this.seats = seats;
        this.user = user;
    }

    public Booking(Concert concertId, LocalDateTime date) {
        this.concert = concertId;
        this.date = date;
    }

    public Concert getConcert() {
        return concert;
    }

    public void setConcert(Concert concert) {
        this.concert = concert;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    public long getId() { return id; }

    public User getUser() {
        return user;
    }
}
