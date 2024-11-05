package ch.unil.doplab.demo.rest;

import ch.unil.doplab.demo.domain.HotelApplicationState;
import ch.unil.doplab.Guest;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;
import java.util.Optional;
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
    public Optional<Guest> getGuestById(@PathParam("id") UUID id) {
        return appState.getGuest(id);
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
    public Guest updateGuest(@PathParam("id") UUID id, Guest guest) {
        appState.setGuest(id, guest);
        return guest;
    }

    @DELETE
    @Path("/{id}")
    public void deleteGuest(@PathParam("id") UUID id) {
        appState.removeGuest(id);
    }
}
