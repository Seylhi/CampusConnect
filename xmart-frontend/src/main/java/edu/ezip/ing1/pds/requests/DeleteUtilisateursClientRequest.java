package edu.ezip.ing1.pds.requests;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ezip.ing1.pds.business.dto.Utilisateur;
import edu.ezip.ing1.pds.client.commons.ClientRequest;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.commons.Request;

import java.io.IOException;
import java.util.Map;

public class DeleteUtilisateursClientRequest extends ClientRequest<Utilisateur, String> {

    public DeleteUtilisateursClientRequest(
            NetworkConfig networkConfig, int myBirthDate, Request request, Utilisateur info, byte[] bytes)
            throws IOException {
        super(networkConfig, myBirthDate, request, info, bytes);
    }

    @Override
    public String readResult(String body) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final Map<String, Object> utilisateurResponse = mapper.readValue(body, Map.class);

        // Vérifie si la mise à jour a réussi
        return utilisateurResponse.get("status") != null ? utilisateurResponse.get("status").toString() : "Erreur";
    }
}
