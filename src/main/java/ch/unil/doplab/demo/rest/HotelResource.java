package ch.unil.doplab.demo.rest;

import ch.unil.doplab.demo.domain.HotelApplicationState;
import ch.unil.doplab.Hotel;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Path("/hotels")
public class HotelResource {

    @Inject
    private HotelApplicationState appState;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Hotel> getAllHotels() {
        return appState.getAllHotels();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Optional<Hotel> getHotelById(@PathParam("id") UUID id) {
        return Optional.ofNullable(appState.getHotel(id));
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Hotel addHotel(Hotel hotel) {
        return appState.addHotel(hotel);
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Hotel updateHotel(@PathParam("id") UUID id, Hotel hotel) {
        appState.setHotel(id, hotel);
        return hotel;
    }

    @DELETE
    @Path("/{id}")
    public void deleteHotel(@PathParam("id") UUID id) {
        appState.removeHotel(id);
    }
}
