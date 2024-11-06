package ch.unil.doplab.demo.rest;

import ch.unil.doplab.demo.domain.HotelApplicationState;
import ch.unil.doplab.Booking;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.time.LocalDate;
import java.util.UUID;

@Path("/bookings")
public class BookingResource {

    @Inject
    private HotelApplicationState appState;

    @POST
    @Path("/guest/{guestId}/room/{roomId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Booking addBooking(@PathParam("guestId") UUID guestId,
                              @PathParam("roomId") UUID roomId,
                              @QueryParam("checkIn") String checkIn,
                              @QueryParam("checkOut") String checkOut) {
        try {
            LocalDate checkInDate = LocalDate.parse(checkIn);
            LocalDate checkOutDate = LocalDate.parse(checkOut);
            return appState.addBooking(guestId, roomId, checkInDate, checkOutDate);
        } catch (Exception e) {
            throw new BadRequestException("Invalid date format or input");
        }
    }

    @PUT
    @Path("/{bookingId}/guest/{guestId}/room/{roomId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Booking updateBooking(@PathParam("bookingId") UUID bookingId,
                                 @PathParam("guestId") UUID guestId,
                                 @PathParam("roomId") UUID roomId,
                                 @QueryParam("checkIn") String checkIn,
                                 @QueryParam("checkOut") String checkOut) {
        try {
            LocalDate checkInDate = LocalDate.parse(checkIn);
            LocalDate checkOutDate = LocalDate.parse(checkOut);
            Booking existingBooking = appState.getBooking(bookingId);
            if (existingBooking == null) {
                throw new NotFoundException("Booking not found");
            }
            existingBooking.setGuest(appState.getGuest(guestId));
            existingBooking.setRoom(appState.getRoom(roomId));
            existingBooking.setCheckInDate(checkInDate);
            existingBooking.setCheckOutDate(checkOutDate);
            return existingBooking;
        } catch (Exception e) {
            throw new BadRequestException("Invalid date format or input");
        }
    }

    @DELETE
    @Path("/{id}")
    public void cancelBooking(@PathParam("id") UUID id) {
        boolean canceled = appState.cancelBooking(id);
        if (!canceled) {
            throw new NotFoundException("Booking not found");
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Booking getBookingById(@PathParam("id") UUID id) {
        Booking booking = appState.getBooking(id);
        if (booking != null) {
            return booking;
        } else {
            throw new NotFoundException("Booking not found");
        }
    }
}
