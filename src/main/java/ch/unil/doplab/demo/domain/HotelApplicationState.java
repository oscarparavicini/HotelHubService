package ch.unil.doplab.demo.domain;

import ch.unil.doplab.*;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDate;
import java.util.*;

@ApplicationScoped
public class HotelApplicationState {

    private Map<UUID, Hotel> hotels;
    private Map<UUID, Guest> guests;
    private Map<UUID, Booking> bookings;
    private Map<UUID, Room> rooms;

    @PostConstruct
    public void init() {
        hotels = new TreeMap<>();
        guests = new TreeMap<>();
        bookings = new TreeMap<>();
        rooms = new TreeMap<>();
        populateApplicationState();
    }

    public Hotel addHotel(Hotel hotel) {
        if (hotel.getId() == null) {
            hotel.setId(UUID.randomUUID());
        }
        hotels.put(hotel.getId(), hotel);
        return hotel;
    }

    public boolean removeHotel(UUID hotelId) {
        Hotel removedHotel = hotels.remove(hotelId);
        return removedHotel != null;
    }

    public Hotel getHotel(UUID hotelId) {
        return hotels.get(hotelId);
    }

    public List<Hotel> getAllHotels() {
        return new ArrayList<>(hotels.values());
    }

    public boolean setHotel(UUID id, Hotel hotel) {
        if (hotels.containsKey(id)) {
            hotel.setId(id);
            hotels.put(id, hotel);
            return true;
        }
        return false;
    }

    public Guest addGuest(Guest guest) {
        if (guest.getId() == null) {
            guest.setID(UUID.randomUUID());
        }
        guests.put(guest.getId(), guest);
        return guest;
    }

    public boolean removeGuest(UUID guestId) {
        return guests.remove(guestId) != null;
    }

    public Optional<Guest> getGuest(UUID guestId) {
        return Optional.ofNullable(guests.get(guestId));
    }

    public List<Guest> getAllGuests() {
        return new ArrayList<>(guests.values());
    }

    public boolean setGuest(UUID id, Guest guest) {
        if (guests.containsKey(id)) {
            guest.setID(id);
            guests.put(id, guest);
            return true;
        }
        return false;
    }

    public Room addRoom(UUID hotelId, Room room) {
        Hotel hotel = hotels.get(hotelId);
        if (hotel == null) {
            throw new IllegalArgumentException("Hotel not found");
        }
        if (room.getId() == null) {
            room.setId(UUID.randomUUID());
        }
        hotel.addRoom(room);
        rooms.put(room.getId(), room);
        return room;
    }

    public boolean removeRoom(UUID hotelId, UUID roomId) {
        Hotel hotel = hotels.get(hotelId);
        if (hotel == null) return false;

        Room roomToRemove = findRoomInHotel(hotel, roomId);
        if (roomToRemove == null) return false;

        hotel.removeRoom(roomToRemove);
        rooms.remove(roomId);
        return true;
    }

    public Room getRoom(UUID roomId) {
        return rooms.get(roomId);
    }

    private Room findRoomInHotel(Hotel hotel, UUID roomId) {
        for (Room room : hotel.getRooms()) {
            if (room.getId().equals(roomId)) {
                return room;
            }
        }
        return null;
    }

    public Object findAvailableRooms(UUID hotelId, LocalDate checkInDate, LocalDate checkOutDate) {
        Hotel hotel = hotels.get(hotelId);
        if (hotel == null) return Optional.empty();

        return hotel.findAvailableRooms(checkInDate, checkOutDate);
    }

    public Booking addBooking(UUID guestId, UUID roomId, LocalDate checkInDate, LocalDate checkOutDate) {
        Guest guest = guests.get(guestId);
        if (guest == null) throw new IllegalArgumentException("Guest not found");

        Room room = findRoomInHotels(roomId);
        if (room == null) throw new IllegalArgumentException("Room not found");

        Booking booking = new Booking(room, checkInDate, checkOutDate, guest);
        bookings.put(booking.getId(), booking);
        guest.addBooking(booking);
        room.addBooking(booking);
        return booking;
    }

    private Room findRoomInHotels(UUID roomId) {
        return rooms.get(roomId);
    }

    public boolean cancelBooking(UUID bookingId) {
        Booking booking = bookings.get(bookingId);
        if (booking == null) return false;

        booking.getGuest().cancelBooking(booking);
        booking.getRoom().removeBooking(booking);
        bookings.remove(bookingId);
        return true;
    }

    public Booking getBooking(UUID bookingId) {
        return bookings.get(bookingId);
    }

    public boolean setBooking(UUID id, Booking updatedBooking) {
        Booking existingBooking = bookings.get(id);
        if (existingBooking != null) {
            existingBooking.setRoom(updatedBooking.getRoom());
            existingBooking.setCheckInDate(updatedBooking.getCheckInDate());
            existingBooking.setCheckOutDate(updatedBooking.getCheckOutDate());
            existingBooking.setGuest(updatedBooking.getGuest());
            existingBooking.setPayment(updatedBooking.getPayment());

            existingBooking.calculateTotalAmount();

            return true;
        }
        return false;
    }

    public Payment createPayment(UUID bookingId, double amount) {
        Booking booking = bookings.get(bookingId);
        if (booking == null) throw new IllegalArgumentException("Booking not found");

        Payment payment = new Payment(amount);
        booking.setPayment(payment);
        return payment;
    }

    public Payment getPayment(UUID bookingId) {
        Booking booking = bookings.get(bookingId);
        if (booking != null) {
            return booking.getPayment();
        }
        return null;
    }

    private void populateApplicationState() {
        Guest guest1 = new Guest("Oscar", "Vici", "oscarvici", "password123", "oscar@email.com");
        Guest guest2 = new Guest("Tiger", "Woods", "tigerking", "securepass123", "tiger@pga.com");
        addGuest(guest1);
        addGuest(guest2);

        Hotel hotel1 = new Hotel("hoteluser1", "hotelpass1", "Intercontinental Lausanne", "435 Route de Geneve", "interlausanne@email.com");
        addHotel(hotel1);

        Room room1 = new Room(101, "Single", 99.99, "WiFi, TV", 1);
        Room room2 = new Room(102, "Double", 129.99, "WiFi, TV, Balcony", 2);
        addRoom(hotel1.getId(), room1);
        addRoom(hotel1.getId(), room2);
    }
}

