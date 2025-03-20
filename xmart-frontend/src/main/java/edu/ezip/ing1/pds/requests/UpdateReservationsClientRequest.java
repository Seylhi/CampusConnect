package edu.ezip.ing1.pds.requests;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ezip.ing1.pds.business.dto.Reservation;
import edu.ezip.ing1.pds.client.commons.ClientRequest;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.commons.Request;

import java.io.IOException;
import java.util.Map;

public class UpdateReservationsClientRequest extends ClientRequest<Reservation, String> {

    public UpdateReservationsClientRequest(
            NetworkConfig networkConfig, int myBirthDate, Request request, Reservation info, byte[] bytes)
            throws IOException {
        super(networkConfig, myBirthDate, request, info, bytes);
    }

    @Override
    public String readResult(String body) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final Map<String, Object> reservationResponse = mapper.readValue(body, Map.class);

        // Vérifie si la mise à jour a réussi
        return reservationResponse.get("status") != null ? reservationResponse.get("status").toString() : "Erreur";
    }
}
