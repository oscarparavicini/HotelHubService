package ch.unil.doplab.demo.rest;

import ch.unil.doplab.demo.domain.HotelApplicationState;
import ch.unil.doplab.Booking;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Path("/bookings")
public class BookingResource {

    @Inject
    private HotelApplicationState appState;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Booking addBooking(@QueryParam("guestId") UUID guestId,
                              @QueryParam("roomId") UUID roomId,
                              @QueryParam("checkIn") String checkIn,
                              @QueryParam("checkOut") String checkOut) {
        LocalDate checkInDate = LocalDate.parse(checkIn);
        LocalDate checkOutDate = LocalDate.parse(checkOut);
        return appState.addBooking(guestId, roomId, checkInDate, checkOutDate);
    }

    @DELETE
    @Path("/{id}")
    public void cancelBooking(@PathParam("id") UUID id) {
        appState.cancelBooking(id);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Optional<Booking> getBookingById(@PathParam("id") UUID id) {
        return Optional.ofNullable(appState.getBooking(id));
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Booking updateBooking(@PathParam("id") UUID id, Booking updatedBooking) {
        boolean isUpdated = appState.setBooking(id, updatedBooking);

        if (isUpdated) {
            return updatedBooking;
        } else {
            System.out.println("Not found");
            return null;
        }
    }
}
