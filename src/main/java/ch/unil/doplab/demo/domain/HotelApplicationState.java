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
        if (removedHotel != null) {
            for (Room room : removedHotel.getRooms()) {
                rooms.remove(room.getId());
            }
        }
        return removedHotel != null;
    }

    public Hotel getHotel(UUID hotelId) {
        return hotels.get(hotelId);
    }

    public List<Hotel> getAllHotels() {
        return new ArrayList<>(hotels.values());
    }

    public boolean updateHotel(UUID id, Hotel updatedHotel) {
        Hotel existingHotel = hotels.get(id);
        if (existingHotel != null) {
            if (updatedHotel.getName() != null) existingHotel.setName(updatedHotel.getName());
            if (updatedHotel.getUsername() != null) existingHotel.setUsername(updatedHotel.getUsername());
            if (updatedHotel.getPassword() != null) existingHotel.setPassword(updatedHotel.getPassword());
            if (updatedHotel.getAddress() != null) existingHotel.setAddress(updatedHotel.getAddress());
            if (updatedHotel.getContactInfo() != null) existingHotel.setContactInfo(updatedHotel.getContactInfo());
            return true;
        }
        System.out.println("Hotel not found");
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

    public Guest getGuest(UUID guestId) {
        return guests.get(guestId);
    }

    public List<Guest> getAllGuests() {
        return new ArrayList<>(guests.values());
    }

    public boolean updateGuest(UUID id, Guest updatedGuest) {
        Guest guest = guests.get(id);
        if (guest != null) {
            if (updatedGuest.getFirstName() != null) guest.setFirstName(updatedGuest.getFirstName());
            if (updatedGuest.getLastName() != null) guest.setLastName(updatedGuest.getLastName());
            if (updatedGuest.getUsername() != null) guest.setUsername(updatedGuest.getUsername());
            if (updatedGuest.getPassword() != null) guest.setPassword(updatedGuest.getPassword());
            if (updatedGuest.getContactInfo() != null) guest.setContactInfo(updatedGuest.getContactInfo());
            return true;
        }
        System.out.println("Guest not found");
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
        if (hotel == null) {
            System.out.println("Hotel not found");
            return false;
        }

        Room roomToRemove = findRoomInHotel(hotel, roomId);
        if (roomToRemove == null) {
            System.out.println("Room not found in this hotel");
            return false;
        }

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

    public Set<Room> findAvailableRooms(UUID hotelId, LocalDate checkInDate, LocalDate checkOutDate) {
        Hotel hotel = hotels.get(hotelId);
        if (hotel == null) {
            throw new IllegalArgumentException("Hotel not found");
        }
        Set<Room> availableRooms = new HashSet<>(hotel.getRooms());
        for (Booking booking : bookings.values()) {
            if (hotel.getRooms().contains(booking.getRoom()) &&
                    datesOverlap(booking.getCheckInDate(), booking.getCheckOutDate(), checkInDate, checkOutDate)) {
                availableRooms.remove(booking.getRoom());
            }
        }
        return availableRooms;
    }

    private boolean datesOverlap(LocalDate existingStart, LocalDate existingEnd, LocalDate newStart, LocalDate newEnd) {
        boolean startsBeforeExistingEnds = newStart.isBefore(existingEnd);
        boolean endsAfterExistingStarts = newEnd.isAfter(existingStart);
        return startsBeforeExistingEnds && endsAfterExistingStarts;
    }

    public Booking addBooking(UUID guestId, UUID roomId, LocalDate checkInDate, LocalDate checkOutDate) {
        Guest guest = guests.get(guestId);
        if (guest == null) {
            throw new IllegalArgumentException("Guest not found");
        }

        Room room = rooms.get(roomId);
        if (room == null) {
            throw new IllegalArgumentException("Room not found");
        }

        Booking booking = new Booking(room, checkInDate, checkOutDate, guest);
        bookings.put(booking.getId(), booking);
        return booking;
    }

    public boolean cancelBooking(UUID bookingId) {
        return bookings.remove(bookingId) != null;
    }

    public Booking getBooking(UUID bookingId) {
        return bookings.get(bookingId);
    }

    public Payment createPayment(UUID bookingId, double amount) {
        Booking booking = bookings.get(bookingId);
        if (booking == null) {
            throw new IllegalArgumentException("Booking not found");
        }

        Payment payment = new Payment(amount);
        booking.setPayment(payment);
        return payment;
    }

    public Payment getPayment(UUID bookingId) {
        Booking booking = bookings.get(bookingId);
        if (booking != null) {
            return booking.getPayment();
        } else {
            return null;
        }
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
