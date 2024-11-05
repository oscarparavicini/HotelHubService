package ch.unil.doplab.demo.rest;

import ch.unil.doplab.demo.domain.HotelApplicationState;
import ch.unil.doplab.Room;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.time.LocalDate;
import java.util.ArrayList;
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
        Room addedRoom = appState.addRoom(hotelId, room);
        if (addedRoom == null) {
            throw new NotFoundException("Hotel not found");
        }
        return addedRoom;
    }

    @DELETE
    @Path("/{roomId}")
    public void deleteRoom(@PathParam("hotelId") UUID hotelId, @PathParam("roomId") UUID roomId) {
        boolean removed = appState.removeRoom(hotelId, roomId);
        if (!removed) {
            throw new NotFoundException("Room not found");
        }
    }

    @GET
    @Path("/available")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Room> getAvailableRooms(@PathParam("hotelId") UUID hotelId,
                                        @QueryParam("checkIn") String checkIn,
                                        @QueryParam("checkOut") String checkOut) {
        try {
            LocalDate checkInDate = LocalDate.parse(checkIn);
            LocalDate checkOutDate = LocalDate.parse(checkOut);
            return new ArrayList<>(appState.findAvailableRooms(hotelId, checkInDate, checkOutDate));
        } catch (Exception e) {
            throw new BadRequestException("Invalid date format");
        }
    }
}
