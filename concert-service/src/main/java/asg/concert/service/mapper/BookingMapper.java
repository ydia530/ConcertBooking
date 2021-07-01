package asg.concert.service.mapper;

import asg.concert.common.dto.BookingDTO;
import asg.concert.common.dto.SeatDTO;
import asg.concert.service.domain.Booking;
import asg.concert.service.domain.Seat;

import java.util.ArrayList;
import java.util.List;


public class BookingMapper {


    public static BookingDTO toDto(Booking booking){
        List<SeatDTO> seatDTOS = new ArrayList<>();
        for(Seat seat: booking.getSeats()){
            seatDTOS.add(SeatMapper.toDto(seat));
        }
        BookingDTO bookingDTO = new BookingDTO(booking.getConcert().getId(),
                booking.getDate(),seatDTOS);
        return bookingDTO;
    }
}
