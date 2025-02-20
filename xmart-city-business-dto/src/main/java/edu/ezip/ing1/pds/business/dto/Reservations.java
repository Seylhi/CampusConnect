package edu.ezip.ing1.pds.business.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.util.LinkedHashSet;
import java.util.Set;

public class Reservations {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("Reservations")
    private  Set<Reservation> reservations = new LinkedHashSet<Reservation>();

    public Set<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(Set<Reservation> reservations) {
        this.reservations = reservations;
    }

    public final Reservations add (final Reservation reservation) {
        reservations.add(reservation);
        return this;
    }

    @Override
    public String toString() {
        return "Reservations{" +
                "reservations=" + reservations +
                '}';
    }
}
