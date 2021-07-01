package asg.concert.service.mapper;

import asg.concert.common.dto.BookingRequestDTO;
import asg.concert.service.domain.BookingRequest;

public class BookingRequestMapper {
    public static BookingRequest toDomainModel(BookingRequestDTO bookingRequestDTO){
        BookingRequest bookingRequest = new BookingRequest(bookingRequestDTO.getConcertId(),
                bookingRequestDTO.getDate(),
                bookingRequestDTO.getSeatLabels());
        return bookingRequest;
    }
    public static BookingRequestDTO toDto(BookingRequest bookingRequest){
        BookingRequestDTO bookingRequestDTO = new BookingRequestDTO(bookingRequest.getConcertId(),
                bookingRequest.getDate(),
                bookingRequest.getSeatLabels());
        return bookingRequestDTO;
    }
}
