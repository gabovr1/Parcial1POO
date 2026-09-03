package MainHotel;

import java.util.ArrayList;
import java.util.List;

public class Room {
    int number;
    String type;
    double baseRate;
    int nights;
    double adjustment;

    public Room(int number, String type, double baseRate, int nights) {
        this.number = number;
        this.type = type;
        this.baseRate = baseRate;
        this.nights = nights;

        validate();
    }

    private void validate() {
        if (baseRate < 0 || nights <= 0) {
            throw new IllegalArgumentException("Valores inválidos, la tarífa no puede ser negativa y los días deben ser mayores a cero");
        }
    }

    public double calculateTotal() {
        return baseRate * nights * (1 + adjustment / 100.0);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Room)) return false;
        Room r = (Room)o;
        return this.number == r.number &&
                this.type.equals(r.type) &&
                Double.compare(this.baseRate, r.baseRate) == 0;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(number);
    }
}

class Reservation {
    private static int reservationCounter = 2032;
    private final int id;
    private final List<Room> rooms;

    public Reservation() {
        this.id = reservationCounter++;
        this.rooms = new ArrayList<>();
    }

    public boolean addRoom(Room room) {
        if (rooms.size() >= 5) {
            return false;
        }

        for (Room existing : rooms) {
            if (existing.equals(room)) {
                System.out.println("Duplicado detectado: " + room.number + " de tipo " + room.type);
                return false;
            }
        }

        rooms.add(room);
        return true;
    }

    public void showSummary() {
        System.out.printf("Reserva #%d%n", id);
        double totalReservation = 0.0;

        for (Room r : rooms) {
            System.out.printf("Habitación %s %-12s Tarifa base: %.2f Noches: %d Ajuste: %.1f%% Total: %.2f%n",
                    r.number,
                    r.type,
                    r.baseRate,
                    r.nights,
                    r.adjustment,
                    r.calculateTotal());

            totalReservation += r.calculateTotal();
        }

        System.out.printf("Total reserva: %.2f%n", totalReservation);
    }

    public double getTotalCost() {
        return rooms.stream().mapToDouble(Room::calculateTotal).sum();
    }
}

class HotelReservationSystem {
    public static void main(String[] args) {

        Reservation r = new Reservation();

        Room suite301 = new Room(301, "Suite", 300_000.0, 2);
        suite301.adjustment = 20; // 20% recargo

        Room offer208 = new Room(208, "Oferta temporada baja", 100_000.0, 4);
        offer208.adjustment = -15; // 15% descuento

        Room standard105 = new Room(105, "Estándar", 150_000.0, 3);

        Room suite402 = new Room(402, "Suite", 280_000.0, 1);
        suite402.adjustment = 20; // 20% recargo

        Room offer110 = new Room(110, "Oferta temporada baja", 90_000.0, 2);

        boolean added301 = r.addRoom(suite301);
        boolean added208 = r.addRoom(offer208);
        boolean added105 = r.addRoom(standard105);
        boolean added402 = r.addRoom(suite402);
        boolean added110 = r.addRoom(offer110);

        if (added301 && added208 && added105 && added402 && added110) {
            System.out.println("Habitaciones añadidas satisfactoriamente");
            r.showSummary();
        } else {
            System.out.println("Fallo al añadir habitaciones");
        }

        try {
            Room invalidStandard = new Room(150, "Estándar", 150_000.0, -2);
            System.out.println("No debe imprimir");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        try {
            Room invalidSuite = new Room(456, "Suite", -50_000.0, 3);
            System.out.println("No debe imprimir");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        Reservation r2 = new Reservation();

        Room std120 = new Room(120, "Estándar", 120_000.0, 1);
        Room std100 = new Room(100, "Estándar", 100_000.0, 1);
        Room suite250 = new Room(250, "Suite", 250_000.0, 1);
        Room suite300 = new Room(300, "Suite", 300_000.0, 1);
        Room offer90 = new Room(90, "Oferta temporada baja", 90_000.0, 1);

        boolean addedStd120 = r2.addRoom(std120);
        boolean addedStd100 = r2.addRoom(std100);
        boolean addedSuite250 = r2.addRoom(suite250);
        boolean addedSuite300 = r2.addRoom(suite300);
        boolean addedOffer90 = r2.addRoom(offer90);

        if (addedStd120 && addedStd100 && addedSuite250 && addedSuite300 && addedOffer90) {
            System.out.println("\nReserva 2: Habitaciones añadidas satisfactoriamente");
            r2.showSummary();
        }

        Reservation r3 = new Reservation();

        Room dupSuite = new Room(300, "Suite", 300_000.0, 2);
        dupSuite.adjustment = 20;

        boolean addedDup1 = r3.addRoom(dupSuite);
        boolean addedDup2 = r3.addRoom(dupSuite); // fallo

        if (addedDup1 && !addedDup2) {
            System.out.println("\nSuite duplicada encontrada y prevenida");
            System.out.println("\nReserva ya tiene 5 habitaciones");
        }
    }
}




