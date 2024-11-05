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
    private HotelApplicationState appState;

    @POST
    @Path("/{bookingId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Payment createPayment(@PathParam("bookingId") UUID bookingId, @QueryParam("amount") double amount) {
        if (amount <= 0) {
            throw new BadRequestException("Amount must be greater than zero.");
        }
        return appState.createPayment(bookingId, amount);
    }

    @PUT
    @Path("/{id}/confirm")
    @Produces(MediaType.APPLICATION_JSON)
    public Payment confirmPayment(@PathParam("id") UUID bookingID,
                                  @QueryParam("cardNumber") String cardNumber,
                                  @QueryParam("expiryDate") String expiryDate) {
        Payment payment = appState.getPayment(bookingID);
        if (payment == null) {
            throw new NotFoundException("Payment not found.");
        }
        try {
            LocalDate expiry = LocalDate.parse(expiryDate);
            payment.confirmPayment(cardNumber, expiry);
        } catch (Exception e) {
            throw new BadRequestException("Invalid expiry date format.");
        }
        return payment;
    }
}
