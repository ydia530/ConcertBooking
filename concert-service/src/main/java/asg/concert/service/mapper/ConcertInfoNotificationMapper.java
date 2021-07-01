package asg.concert.service.mapper;

import asg.concert.common.dto.ConcertInfoNotificationDTO;
import asg.concert.service.domain.ConcertInfoNotification;

public class ConcertInfoNotificationMapper {
    public static ConcertInfoNotification toDomainModel(ConcertInfoNotificationDTO concertInfoNotificationDTO){
        ConcertInfoNotification concertInfoNotification = new ConcertInfoNotification(concertInfoNotificationDTO.getNumSeatsRemaining());
        return concertInfoNotification;
    }

    public static  ConcertInfoNotificationDTO toDto(ConcertInfoNotification concertInfoNotification){
        ConcertInfoNotificationDTO concertInfoNotificationDTO = new ConcertInfoNotificationDTO(concertInfoNotification.getNumSeatsRemaining());
        return concertInfoNotificationDTO;
    }
}
