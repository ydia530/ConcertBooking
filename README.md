Project - A Concert Booking Service
==========

The aim of this project is to build a Web service for concert booking. 

System description
----------
A booking service is required for a small venue, with a capacity of 120 seats. The venue's seats are classified into three price bands, Silver, Gold and Platinum. A concert may run on multiple dates, and each concert has at least one performer. Additionally, a performer may feature in multiple concerts.

The service is to allow clients to retrieve information about concerts and performers, to make and enquire about seats and reservations, and to authenticate users.

When making a reservation, clients may enquire about available seats. They should be informed of any unbooked seats, along with their pricing. Clients may select any number of seats to book. When making a reservation request, double-bookings are not permitted. Only one client should be allowed to book a given seat for a given concert on a given date. Clients may not make partial bookings - either *all* of a client's selected seats are booked, or *none* of them are.

Clients may view concert and performer information, and enquire about available seating, whether or not they are authenticated. When authenticated, clients gain the ability to make reservations, and to view their own reservations. Clients should never be able to view the reservation information for other guests.

In addition, clients should be able to subscribe to information regarding concert bookings. Specifically, they should be able to subscribe to "watch" one or more concerts / dates, and be notified when those concerts / dates are about to sell out.

A particular quality attribute that the service must satisfy is scalability. It is expected that the service will experience high load when concert tickets go on sale. 

Resources
----------
A multi-module Maven project, `asg-concert`, is comprises 3 modules: 

- `concert-client`. A module that contains resources for building a concert client application.

- `concert-common`. This module defines several entities that are shared by the `client` and `service` modules.

- `concert-service`. The `service` module is responsible for implementing the Web service and persistence aspects of the concert application.

The parent project's POM includes common dependencies and properties that are inherited by the 3 modules.

#### System architecture
The system as a whole is a *tiered* architecture, in which a browser application, driven by a *web app*, communicates via HTTP with the *web service* you will create for this project. A high-level overview can be seen in the below diagram:

![](./spec/system-architecture.png)

#### Client module
The client module contains a *web application*, built using technologies such as HTML5, CSS, JavaScript, JSP, and JSTL. This web application is capable of acting as a *client* to the web service which you will create for this project, as shown in the diagram above. The client contains both browser-based and server-based code (written in JavaScript and Java, respectively) which communicate with the `service` layer over HTTP.


#### Common module
This module includes a number of packages:

- `asg.concert.common.dto`. This package contains complete implementations for 7 DTO classes. The `client` webapp above, and the integration tests (see below), are defined in terms of the DTO classes.
	
- `asg.concert.common.jackson`. This package includes the Jackson serializer and deserializer implementations for Java's `LocalDateTime` class.

- `asg.concert.common.types`. This package defines basic data types that are common to the `client` and `service` modules. The types comprise 2 enumerations: `Genre` (for performers), and `BookingStatus` (for querying available / unavailable seats).

#### Service module
The `service` module will contain your completed Web service for the project. It contains the following packages:

- `asg.concert.service.domain`. This package will contain the web service's completed domain model.

- `asg.concert.service.jaxrs`. This package contains the `LocalDateTimeParam` class. This class allows us to receive `LocalDateTime` instances as *query* or *path* parameters.

- `asg.concert.service.mapper`. This package is intended to contain mapper classes.

- `asg.concert.service.services`. This package contains the `ConcertResource` class, which you must implement for this project. It also contains a `PersistenceManager` class which can be used to obtain `EntityManager` instances when required, a complete `ConcertApplication` class, and a `TestResource` class which implements a web service endpoint to reset the database. This is used for testing purposes.

- `asg.concert.service.util`. This package contains the `ConcertUtils` class, which can re-initialize the database, and the `TheatreLayout` class, which can be used to generate `Seat`s for a particular concert date.

Beyond packages and source files, the `service` module contains the JPA `persistence.xml` file, a database initialise script (`db-init.sql`) and a `log4j.properties` file (in `src/main/resources`). Finally, integration tests are included in the `src/test/java` folder. As usual, these tests may be run by executing Maven's `verify` goal on the parent project, and a 100% pass-rate (along with correct client functionality) is a good indication that service implementation is correct.

The `persistence.xml` file includes a `javax.persistence.sql-load-script-source` element whose value is the supplied`db-init.sql` script. The effect of this element is to run the script when the `EntityManagerFactory` is created.`db-init.sql` file populates the database with concert and performer data.



----------

- **Token-based authentication**. The web service specification sometimes calls for an authenticated user. Authentication should be implemented using *token-based authentication*, which is where the client exchanges credentials (username and password) for a token. Once authenticated, the client sends the token with each subsequent request to the web service. For any requests requiring authentication, the Web service checks for the presence of the token. If the token's missing, the request fails; if the token doesn't identify a user (based upon accessing storage to lookup any user to whom the token is associated), the request fails. Where the token is bound to a particular user, the user is then authenticated, and the web service may then determine whether the user is authorised to make the request.
	
- **Returning appropriate HTTP status codes**. There are several HTTP status codes that are defined by the HTTP specification. While there isn't complete agreement on which codes to use in REST-based Web services, the following conventions are widely accepted.
	
   - `200 OK`. This is appropriate where a request has been successfully processed, and the HTTP response message's body contains entity data.
   
   - `201 Created`. This response is appropriate where a request to create a new resource has been successful. The response message should contain a `Location` header storing the URI of the newly created resource.
   
   - `204 No Content`. Similar to `200 OK`, but where the corresponding HTTP response message doesn't contain entity data.
   
   - `400 Bad Request`. This is suitable where a client makes a bad request, e.g. where a DTO argument is expected to have particular fields filled in but doesn't, or where the combination of field values don't make sense, e.g. a concert reservation request specifies a date when the concert isn't scheduled.
   
   - `401 Unauthorized`. In practice, a \texttt{401 Unauthorized} status code is used to indicate that the requesting client/user was not *authenticated* (as opposed to not *authorized*, e.g. based on having insufficient access rights). Where a web service request that requires an authentication token lacks the token, a `401` code is thus appropriate.
   
   - `403 Forbidden`. Whereas `401` indicates a user who is not authenticated, `403` indicates a user who *is* authenticated, but is not *authorized* to perform a certain operation. In our case, this might include attempting to view another user's bookings, or book seats which have already been acquired by another user.
   
   - `404 Not Found`. This is appropriate where a named resource doesn't exist in the Web service.
   
   - `500 Internal Server Error`. Where an error other than an application condition has occurred in processing a Web service request, a `500` status code is appropriate.



The client webapp.
----------
The `concert-client` project contains a complete web application which is designed to communicate with web service from both its server-side (Java) and client-side (JavaScript) code, to deliver its functionality.

#### App usage
Using the application, browsing to `/Concerts` or simply `/` (within the webapp's *context*) will present the "homepage" of the app, allowing the user to view concerts. On this page, users may scroll through the list of concerts on the left side of the page. Clicking on a concert will cause that concert's info to be displayed in the detail view.

![](./spec/concerts-page.PNG)

On the detail view, users can see more information about the currently selected concert, as well as a list of performers and dates for that concert.

Clicking on a performer's image will display a modal dialog containing more information about that performer:

![](./spec/view-performer.PNG)

If the user hasn't logged in, then clicking on the "Book!" button next to one of a concert's dates will prompt the user to sign in:

![](./spec/please-login.PNG)

If the user has already signed in, they will be redirected to the seat selection page for their selected concert and date. See `concert-service/src/main/resources/db-init.sql` for the data that the server is initialised with, including possible users and their passwords.

![](./spec/booking-page.PNG)

Here, users can see a visual seat map of the venue, along with an indicator of which seats have already been booked. Clicking unbooked seats will toggle a blue halo around that seat - an indicator that the seat has been selected for booking, as can be seen in the above screenshot. A list of selected seats - along with the total ticket price - can be seen below the seat map.

Once the user has made their selection, they can click the "Book!" button. Assuming they have "sufficient funds" (which will always be true for this test case), and that no other user has booked the same seats in the meantime, the user will be presented with a success dialog box:

![](./spec/booking-success.PNG)

Dismissing the dialog will redirect the user back to the concerts page, where they may continue to book tickets at other concerts / dates. (or more tickets for the same concert / date).

#### Limitations
The webapp currently *partially* exercises the web service you are required to create for this project. Functionality for a user to view their bookings is not implemented, though this is required by the web service specification. Additionally, the webapp does not participate in the *publish / subscribe* functionality required of the service.

#### Running the complete system
The only way to run the complete system is to package both the `client` and `service` projects into `WAR` files, and deploy them to a running servlet container such as Tomcat.


#### From Eclipse
**Note: This only works with Eclipse for Java EE Developers, with Tomcat support enabled**. If you don't see any of the options mentioned here, it's likely that you don't have these options. You can get them through the *Help* &rarr; *Install new software* menu, searching for *Java EE* and *Tomcat*, respectively. Alternatively you could try using the 
[marketplace](https://marketplace.eclipse.org/content/eclipse-java-ee-developer-tools-0) directly.

1. Open *Window* &rarr; *Preferences*, then choose *Server* &rarr; *Runtime Environments*. Click *Add*, then choose the Apache Tomcat version matching the one you downloaded (which should be v8.5 if you're following along).

2. Click *Next*, then browse to where you unzipped Tomcat. Click *Select folder*, then *Apply and Close*.

3. Next, open the *run configurations* menu (the small dropdown arrow next to the green Play button). Locate *Apache Tomcat*, and double-click to create a new run configuration.

4. Go to the *Servers* View. if it's not on-screen, open it with *Window* &rarr; *Show view*. Create a new Tomcat server of matching version, and click *Finish*.

5. Right-click the newly created server, and choose *Open*. here, you can configure the *HTTP/1.1* port on the right (say, to 10000). Save the config (`Ctrl+s`).

6. In the above menu, you can also see / change the *Server path* and *Deploy path*. the default is something like `.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps` - which you can use, or you can change it to anywhere on your machine. Again, save the config.

7. Run maven's `package` goal to create the WARs if you haven't already done so.

8. Rename the `client` WAR to `concert-webapp.war`, and the `server` WAR to `webservice.war`. This will prevent you from needing to make changes to `Config.java` and `fetch-api.js` in the `client` project. Alternatively, you may make the changes to these files as required.

9. Start the server running. (right-click it to get the option).

10. In a file explorer, browse to the server path / deploy path as in step 6 - except, locate the `webapps` folder, rather than `wtpwebapps`.

11. Copy / paste your WARs into the `webapps` folder. If the server is started, you should notice folders shortly being created by Tomcat. And, you can see Tomcat's logging output from the Eclipse console.

12. Browse to the appropriate URL from your browser (which should be <http://localhost:10000/concert-webapp/> if you've followed along), and enjoy.

13. When you make modifications and repackage your WARs, you can delete the contents of the `webapps` folder and re-copy your new WARs over (while the server is stopped).

#### From IntelliJ
**Note: This ONLY works in IntelliJ Ultimate edition.** 

1. Within your project, open the *Run Configurations* dialog (usually available in the top-right corner, or open the search box by tapping double-shift, then type "run configurations"). Either way, pick *Edit Configurations*.

2. Click on the **+** button on the top-left of the dialog, then choose *Tomcat server* &rarr; *Local*.

3. Name the configuration whatever you like. For the *HTTP Port*, pick one which is available on your machine, such as *10000*.

4. For the *application server*, pick one from the list. If none are available, click *configure*. You'll be able to add one (by browsing to the location where you downloaded Tomcat earlier).

5. Under the *Deployment* tab, select both WAR artifacts (`client` and `service`) for deployment - these would have been created by IntelliJ's Maven integration (you shouldn't select the *exploded* versions, but it probably doesn't matter). For the application context, set the `service` WAR's context to `/webservice`, and the `client` WAR's context to `/concert-webapp`.

6. Go back to the *Server* tab. Edit the *URL* to be `http://localhost:10000/concert-webapp/` (altering the port as necessary). Then, click `OK`.

7. In the `client` project, open `src/main/java/.../Config.java`, and edit the `WEB_SERVICE_URI` field as appropriate (assuming port `10000`, and the contexts identified in step 5 above, you won't need to change this).

8. In the `client` project, open `src/main/webapp/js/fetch-api.js` and edit `WEB_URI` as appropriate (assuming the contexts identified in step 5 above, you won't need to change this).

9. From the *run configurations* dropdown, slect your new configuration and click the *play* button. The webapp and web service should start, and the browser should eventually open at the URL identified in step 6 above. If it doesn't, you can browse there manually.

10. When you're done, hit the *stop* button. Sometimes you may need to click it twice (do so until the stop button is greyed out, and you see a *Disconnected from server* message in the server output).
