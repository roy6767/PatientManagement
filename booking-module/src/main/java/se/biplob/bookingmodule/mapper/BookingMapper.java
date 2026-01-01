package se.biplob.bookingmodule.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import se.biplob.bookingmodule.dtos.request.CreateBookingRequest;
import se.biplob.bookingmodule.dtos.response.BookingResponse;
import se.biplob.bookingmodule.model.Booking;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    BookingResponse toResponse(Booking booking);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", constant = "BOOKED")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Booking toEntity(CreateBookingRequest request);
}

