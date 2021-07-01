package asg.concert.service.domain;

import javax.persistence.*;

/**
 * Class to represent an Authentication. An Authentication is characterised
 * by a unique authentication token (primary key), and the User it is for.
 */
@Entity
@Table(name = "AUTHENTICATIONS")
public class AuthToken{

    @Id
    @Column(name = "AUTHENTICATION_TOKEN", nullable = false)
    private String authToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    public AuthToken(){};

    public AuthToken(String authToken, User user) {
        this.authToken = authToken;
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
