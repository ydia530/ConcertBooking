Team Member

Neil He @neilissac

Yuan Diao @ydia530

GuoCheng Lester Jiang @gjia636


We started by clone the repo from GitHub and studied the entire project. Since we all had other assignments and tests to prepared for, we started a bit late. We more like use the Agile way to finish the project. @ydia530 build the first version of the domain model and DTO based on the SQL in the support file, then @neiliissac and @gjia636 add methods to the concertResource file, all members participated about the same for everything. We did some modifies when we finished most of the test, but there are still few errors there. Thus we did the second version of the domain model to fulfill requests.

We use Debug and printLine to test the result and find out where might have the problem, so we can create some breaking points and do some modifications. However, Debug verify sometimes will cause issues such as H2 database initial error. We reckon that may cause by unclosed connection upon shutting down pool? Since after we run verify again, everything back to normal, but if we just re-run debug, the pool will not be refreshed.

The domain model design is based on the db-init.SQL file in the resources file. We have essential Booking, Concert, Performer, Seat, User. And support file Booking Request, ConcertInfoNotification, ConcertSummary, and Subscrip. Some of those may be could be much more explicit by normalization, but this is our’s best understanding.