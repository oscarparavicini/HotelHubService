package ch.unil.doplab.demo.rest;

import ch.unil.doplab.demo.domain.HotelApplicationState;
import ch.unil.doplab.Guest;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import java.util.UUID;

@Path("/guests")
public class GuestResource {

    @Inject
    private HotelApplicationState appState;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Guest> getAllGuests() {
        return appState.getAllGuests();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Guest getGuestById(@PathParam("id") UUID id) {
        Guest guest = appState.getGuest(id).orElse(null);
        if (guest != null) {
            return guest;
        } else {
            throw new NotFoundException("Guest not found");
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Guest addGuest(Guest guest) {
        return appState.addGuest(guest);
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public boolean updateGuest(@PathParam("id") UUID id, Guest guest) {
        boolean updated = appState.updateGuest(id, guest);
        if (updated) {
            return true;
        } else {
            throw new NotFoundException("Guest not found");
        }
    }

    @DELETE
    @Path("/{id}")
    public void deleteGuest(@PathParam("id") UUID id) {
        boolean removed = appState.removeGuest(id);
        if (!removed) {
            throw new NotFoundException("Guest not found");
        }
    }
}
