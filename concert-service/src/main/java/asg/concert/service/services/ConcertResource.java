package asg.concert.service.services;

import asg.concert.common.dto.*;
import asg.concert.common.types.BookingStatus;
import asg.concert.service.domain.*;
import asg.concert.service.jaxrs.LocalDateTimeParam;
import asg.concert.service.mapper.*;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.io.*;


@Path("/concert-service")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ConcertResource {
    private static Logger LOGGER = LoggerFactory.getLogger(ConcertResource.class);
    private AtomicLong idCounter = new AtomicLong();

    @GET
    @Path("/concerts/{id}")
    public Response retrieveSingleConcert(@PathParam("id") long id){
        EntityManager em = PersistenceManager.instance().createEntityManager();

        try {
            em.getTransaction().begin();
            Concert concert = em.find(Concert.class, id);

            if(concert == null){
                return Response.status(404).build();
            }
            em.getTransaction().commit();
            ConcertDTO result = ConcertMapper.toDto(concert);
            return Response.ok(result).build();
        } finally {
            em.close();
        }
    }



    @GET
    @Path("/concerts")
    public Response retrieveAllConcert(@CookieParam(Config.CLIENT_COOKIE) Cookie clientId){
        EntityManager em = PersistenceManager.instance().createEntityManager();
        String query = "select c from Concert c";
        TypedQuery<Concert> concertTypedQuery = em.createQuery(query,Concert.class);
        List<Concert> concerts = concertTypedQuery.getResultList();
        List<ConcertDTO> concertDTOS = new ArrayList<>();
        try {
            em.getTransaction().begin();
            for(Concert concert:concerts){
                concertDTOS.add(ConcertMapper.toDto(concert));
            }
            em.getTransaction().commit();
            GenericEntity<List<ConcertDTO>> entity = new GenericEntity<List<ConcertDTO>>(concertDTOS){};
            Response.ResponseBuilder builder = Response.ok(entity);
            Response response = builder.build();
            return response;
        } finally {
            em.close();
        }
    }
    @GET
    @Path("/concerts/summaries")
    public Response retrieveConcertsSummaries(@CookieParam(Config.CLIENT_COOKIE) Cookie clientId){
        EntityManager em = PersistenceManager.instance().createEntityManager();
        String query = "select c from Concert c";
        TypedQuery<Concert> concertTypedQuery = em.createQuery(query,Concert.class);
        List<Concert> concerts = concertTypedQuery.getResultList();
        List<ConcertSummaryDTO> concertSummariesDTO = new ArrayList<>();
        try {
            em.getTransaction().begin();
            for(Concert concert:concerts){
                ConcertSummary concertSummary = new ConcertSummary(concert.getId(),
                        concert.getTitle(),
                        concert.getImageName());
                concertSummariesDTO.add(ConcertSummaryMapper.toDto(concertSummary));
            }
            em.getTransaction().commit();
            GenericEntity<List<ConcertSummaryDTO>> entity = new GenericEntity<List<ConcertSummaryDTO>>(concertSummariesDTO){};
            Response.ResponseBuilder builder = Response.ok(entity);
            Response response = builder.build();
            return response;
        } finally {
            em.close();
        }
    }

    @GET
    @Path("/performers/{id}")
    public Response retrievePerformer(@PathParam("id") long id){
        EntityManager em = PersistenceManager.instance().createEntityManager();

        try
        {
            em.getTransaction().begin();
            Performer performer = em.find(Performer.class,id);

            if (performer == null){
                return Response.status(404).build();
            }
            em.getTransaction().commit();
            PerformerDTO result = PerformerMapper.toDto(performer);

            return Response.ok(result).build();

        }
        finally
        {
            em.close();
        }
    }

    @GET
    @Path("performers")
    public Response retrieveAllPerformer(@CookieParam(Config.CLIENT_COOKIE) Cookie clientId){
        EntityManager em = PersistenceManager.instance().createEntityManager();
        String query = "select c from Performer c";
        TypedQuery<Performer> concertTypedQuery = em.createQuery(query, Performer.class);
        List<Performer> performers = concertTypedQuery.getResultList();
        List<PerformerDTO> performerDTOS = new ArrayList<>();
        try {
            em.getTransaction().begin();
            for(Performer performer:performers){
                performerDTOS.add(PerformerMapper.toDto(performer));
            }
            em.getTransaction().commit();
            GenericEntity<List<PerformerDTO>> entity = new GenericEntity<List<PerformerDTO>>(performerDTOS){};
            Response.ResponseBuilder builder = Response.ok(entity);
            Response response = builder.build();
            return response;
        } finally {
            em.close();
        }
    }

    @POST
    @Path("/login")
    public Response Login(UserDTO user){
        EntityManager em = PersistenceManager.instance().createEntityManager();
        em.getTransaction().begin();
        NewCookie nc = makeCookie(null);

        String name = user.getUsername();
        String password = user.getPassword();
        String query = "select c from User c where c.username =:username and c.password = :password";
        TypedQuery<User> UserTypedQuery = em.createQuery(query, User.class)
                .setParameter("username", user.getUsername())
                .setParameter("password", user.getPassword());
        List<User> users = UserTypedQuery.getResultList();
        if(users.size()==0){
            return Response.status(401).build();
        }
        em.persist(new AuthToken(nc.getValue(), users.get(0)));
        em.getTransaction().commit();
        em.close();
        return Response.ok().cookie(nc).build();
    }

    @POST
    @Path("/bookings")
    public Response Booking(BookingRequestDTO booking, @CookieParam(Config.CLIENT_COOKIE) Cookie clientId) throws URISyntaxException {
        EntityManager em = PersistenceManager.instance().createEntityManager();
        em.getTransaction().begin();
        if (clientId == null){
            return Response.status(401).build();
        }
        List<String> bookingSeatLabels = booking.getSeatLabels();
        String auth = clientId.getValue();
        System.out.println(auth);
        AuthToken authToken = em.find(AuthToken.class, auth);
        if (authToken == null) {
            em.getTransaction().rollback();
            em.close();
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        User user = authToken.getUser();
        em.lock(user, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
        Concert concert = em.find(Concert.class, booking.getConcertId());
        if (concert==null || ! concert.getDates().contains(booking.getDate())){
            em.getTransaction().rollback();
            em.close();
            return Response.status(Response.Status.BAD_REQUEST).build();
        }


        String query = "select s from Seat s where s.dateTime = :date and s.label in :label";
        TypedQuery<Seat> SeatTypedQuery = em.createQuery(query, Seat.class)
                .setParameter("date", booking.getDate())
                .setParameter("label", booking.getSeatLabels());

        List<Seat> seats = SeatTypedQuery.setLockMode(LockModeType.OPTIMISTIC).getResultList();
        for (Seat seat: seats) {
            if(seat.getIsBooked()){
                em.getTransaction().rollback();
                em.close();
                return Response.status(403).build();
            }
            seat.setIsBooked(true);
        }


        Booking bookingDomain = new Booking(concert,booking.getDate(),seats,user);
        em.persist(bookingDomain);
        em.getTransaction().commit();

        em.close();
        return Response.created(new URI("concert-service/bookings/" + bookingDomain.getId())).build();
    }

    @GET
    @Path("/bookings/{id}")
    public Response getBooking(@CookieParam(Config.CLIENT_COOKIE) Cookie clientId,
                               @Context UriInfo uriInfo,
                               @PathParam("id")long id){
        if (clientId == null){
            return Response.status(403).build();
        }
        EntityManager em = PersistenceManager.instance().createEntityManager();
        em.getTransaction().begin();
        String query = "select distinct b from Booking b " +
                "LEFT OUTER JOIN FETCH b.seats " +
                "LEFT OUTER JOIN FETCH b.user " +
                "LEFT OUTER JOIN FETCH b.concert where b.id = :id ";
        TypedQuery<Booking> bookingTypedQuery = em.createQuery(query, Booking.class)
                .setParameter("id", id);
        List<Booking> b = bookingTypedQuery.getResultList();
        Booking booking = b.get(0);
        User user = booking.getUser();
        AuthToken authentication = em.find(AuthToken.class, clientId.getValue());

        if (!(user.getId() == authentication.getUser().getId())) {
            return Response.status(403).build();
        }
        em.getTransaction().commit();
        em.close();

        return Response.ok(BookingMapper.toDto(booking)).build();

    }


    @GET
    @Path("/bookings")
    public Response BookingResponse(@CookieParam(Config.CLIENT_COOKIE) Cookie clientId){
        if (clientId == null){
            return Response.status(401).build();
        }
        EntityManager em = PersistenceManager.instance().createEntityManager();
        em.getTransaction().begin();
        List<AuthToken> authenticationList = em.createQuery("SELECT DISTINCT a " +
                "FROM AuthToken a " +
                "LEFT OUTER JOIN FETCH a.user user " +
                "LEFT OUTER JOIN FETCH user.bookings bookings " +
                "LEFT OUTER JOIN FETCH bookings.seats " +
                "LEFT OUTER JOIN FETCH bookings.concert " +
                "WHERE a.authToken = :authToken", AuthToken.class)
                .setParameter("authToken", clientId.getValue())
                .getResultList();
        if (authenticationList.size()==0){
            return Response.status(403).build();
        }
        em.getTransaction().commit();
        em.close();

        AuthToken auth = authenticationList.get(0);
        User user = auth.getUser();
        Set<Booking> bookings = user.getBookings();
        List<BookingDTO> bookingDTOS = new ArrayList<>();
        for (Booking booking: bookings) {
            bookingDTOS.add(BookingMapper.toDto(booking));
        }
        return Response.status(200).entity(bookingDTOS).build();

    }

    @GET
    @Path("/seats/{date}")
    public Response getSeat(@PathParam("date") LocalDateTimeParam dateParam,
                            @QueryParam("status") BookingStatus status,
                            @CookieParam(Config.CLIENT_COOKIE) Cookie clientId){
        EntityManager em = PersistenceManager.instance().createEntityManager();
        try {
            em.getTransaction().begin();
            String query = "select s from Seat s where s.dateTime = :date";
            TypedQuery<Seat> SeatTypedQuery = em.createQuery(query, Seat.class)
                    .setParameter("date", dateParam.getLocalDateTime());
            List<Seat> seats = SeatTypedQuery.getResultList();
            List<SeatDTO> any = new ArrayList<>();
            List<SeatDTO> booked = new ArrayList<>();
            List<SeatDTO> unbooked = new ArrayList<>();
            if (clientId == null){
                return Response.ok(booked).build();
            }
            for (Seat seat:seats){
                if (seat.getIsBooked()){
                    booked.add(SeatMapper.toDto(seat));
                }
                else
                    unbooked.add(SeatMapper.toDto(seat));
            }

            for (Seat seat: seats) {
                any.add(SeatMapper.toDto(seat));
            }
            em.getTransaction().commit();

            if (status.toString() == "Booked"){
                return Response.ok(booked).build();
            }
            if(status.toString() =="Unbooked"){
                return Response.ok(unbooked).build();
            }
            else return Response.ok(any).build();
        } finally {
            em.close();
        }
    }


    @POST
    @Path("/subscribe/concertInfo")
    public Response testSubscription(ConcertInfoSubscriptionDTO Subscription,
                                     @CookieParam(Config.CLIENT_COOKIE) Cookie clientId){
        if (clientId==null){
            return Response.status(401).build();
        }
        EntityManager em = PersistenceManager.instance().createEntityManager();
        try {
            em.getTransaction().begin();
            Concert concert = em.find(Concert.class, Subscription.getConcertId());
            if(concert == null){
                return Response.status(400).build();
            }
            TypedQuery<Concert> concertQuery = em.createQuery("select c from Concert c", Concert.class);
            List<Concert> concerts = concertQuery.getResultList();
            boolean check = false;
            LocalDateTime date = Subscription.getDate();
            for (int i =0; i<concerts.size(); i++){
                if (concerts.get(i).getDates().equals(date)){
                    check = true;
                }
            }
            if (check==false){
                return Response.status(400).build();
            }
            em.getTransaction().commit();
            ConcertDTO result = ConcertMapper.toDto(concert);
            return Response.status(200).build();
        } finally {
            em.close();
        }
    }


    private NewCookie makeCookie(Cookie clientId) {
        NewCookie newCookie = null;

        if (clientId == null) {
            newCookie = new NewCookie(Config.CLIENT_COOKIE, UUID.randomUUID().toString());
            LOGGER.info("Generated cookie: " + newCookie.getValue());
        }

        return newCookie;
    }

}