package asg.concert.service.mapper;

import asg.concert.common.dto.SeatDTO;
import asg.concert.service.domain.Seat;

public class SeatMapper {
    public static Seat toDomainModel(SeatDTO seatDto){
        Seat seat = new Seat(seatDto.getLabel(),seatDto.getPrice());
        return seat;
    }
    public static SeatDTO toDto(Seat seat){
        SeatDTO seatDTO = new SeatDTO(seat.getLabel(), seat.getPrice());
        return seatDTO;
    }
}
