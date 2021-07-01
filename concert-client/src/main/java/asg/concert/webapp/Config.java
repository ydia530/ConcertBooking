package asg.concert.webapp;

public class Config {

    /**
     * The URI of the deployed Concert web service. This webapp will communicate with this service to perform
     * all operations.
     */
    public static final String WEB_SERVICE_URI = "http://localhost:8080/webservice/services/concert-service";

    private Config() {}
}
