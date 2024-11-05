package ch.unil.doplab.demo.rest;

import ch.unil.doplab.demo.domain.HotelApplicationState;
import ch.unil.doplab.Hotel;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
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
    public Hotel getHotelById(@PathParam("id") UUID id) {
        Hotel hotel = appState.getHotel(id);
        if (hotel != null) {
            return hotel;
        } else {
            throw new NotFoundException("Hotel not found");
        }
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
        boolean updated = appState.updateHotel(id, hotel);
        if (updated) {
            return hotel;
        } else {
            throw new NotFoundException("Hotel not found");
        }
    }

    @DELETE
    @Path("/{id}")
    public void deleteHotel(@PathParam("id") UUID id) {
        boolean removed = appState.removeHotel(id);
        if (!removed) {
            throw new NotFoundException("Hotel not found");
        }
    }
}
