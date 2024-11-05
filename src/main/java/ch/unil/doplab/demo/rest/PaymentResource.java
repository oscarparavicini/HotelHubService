package ch.unil.doplab.demo.rest;

import ch.unil.doplab.demo.domain.HotelApplicationState;
import ch.unil.doplab.Payment;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.time.LocalDate;
import java.util.UUID;

@Path("/payments")
public class PaymentResource {

    @Inject
    private HotelApplicationState appState;// Simplified initialization

    @POST
    @Path("/{bookingId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Payment createPayment(@PathParam("bookingId") UUID bookingId, @QueryParam("amount") double amount) {
        return appState.createPayment(bookingId, amount);
    }

    @PUT
    @Path("/{id}/confirm")
    @Produces(MediaType.APPLICATION_JSON)
    public Payment confirmPayment(@PathParam("id") UUID Bookingid,
                                  @QueryParam("cardNumber") String cardNumber,
                                  @QueryParam("expiryDate") String expiryDate) {
        LocalDate expiry = LocalDate.parse(expiryDate);
        Payment payment = appState.getPayment(Bookingid);
        payment.confirmPayment(cardNumber, expiry);
        return payment;
    }
}
