package asg.concert.service.domain;

import javax.persistence.*;
import java.util.Set;


@Entity
@Table(name="USERS")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USERNAME")
    private String username;
    @Column(name = "PASSWORD")
    private String password;

    @Version
    @Column(name = "VERSION")
    private Long version;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REFRESH)
    private Set<Booking> bookings;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(){
    }
    public long getId() { return id; }
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Set<Booking> getBooking(){return bookings;}

    public Set<Booking> getBookings() {
        return bookings;
    }
}
