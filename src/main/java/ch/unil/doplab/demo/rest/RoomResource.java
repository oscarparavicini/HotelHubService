package ch.unil.doplab.demo.rest;

import ch.unil.doplab.demo.domain.HotelApplicationState;
import ch.unil.doplab.Room;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Path("/hotels/{hotelId}/rooms")
public class RoomResource {

    @Inject
    private HotelApplicationState appState;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Room addRoom(@PathParam("hotelId") UUID hotelId, Room room) {
        return appState.addRoom(hotelId, room);
    }

    @DELETE
    @Path("/{roomId}")
    public void deleteRoom(@PathParam("hotelId") UUID hotelId, @PathParam("roomId") UUID roomId) {
        appState.removeRoom(hotelId, roomId);
    }

    @GET
    @Path("/available")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Room> getAvailableRooms(@PathParam("hotelId") UUID hotelId,
                                        @QueryParam("checkIn") String checkIn,
                                        @QueryParam("checkOut") String checkOut) {
        LocalDate checkInDate = LocalDate.parse(checkIn);
        LocalDate checkOutDate = LocalDate.parse(checkOut);
        return (List<Room>) appState.findAvailableRooms(hotelId, checkInDate, checkOutDate);
    }
}
