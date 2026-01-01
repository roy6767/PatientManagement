package se.biplob.bookingmodule.dtos.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import se.biplob.bookingmodule.model.Enum.BookingStatus;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateBookingStatusRequest {

    @NotNull(message = "STATUS_REQUIRED")
    private BookingStatus status;
}
