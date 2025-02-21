package edu.ezip.ing1.pds.services;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.UUID;

import edu.ezip.ing1.pds.business.dto.Reservation;
import edu.ezip.ing1.pds.business.dto.Reservations;
import edu.ezip.ing1.pds.requests.InsertReservationClientRequest;
import edu.ezip.ing1.pds.requests.SelectAllReservationsClientRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import edu.ezip.commons.LoggingUtils;
import edu.ezip.ing1.pds.business.dto.Utilisateur;
import edu.ezip.ing1.pds.business.dto.Utilisateurs;
import edu.ezip.ing1.pds.client.commons.ClientRequest;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.commons.Request;
import edu.ezip.ing1.pds.requests.InsertUtilisateursClientRequest;
import edu.ezip.ing1.pds.requests.SelectAllUtilisateursClientRequest;

public class ReservationService {

    private final static String LoggingLabel = "FrontEnd - ReservationService";
    private final static Logger logger = LoggerFactory.getLogger(LoggingLabel);

    final String insertRequestOrder = "INSERT_RESERVATION";
    final String selectRequestOrder = "SELECT_ALL_RESERVATIONS";
    final String deleteRequestOrder = "DELETE_RESERVATION";

    private final NetworkConfig networkConfig;

    public ReservationService(NetworkConfig networkConfig) {
        this.networkConfig = networkConfig;
    }

    /**
     * Insère un utilisateur dans la base de données.
     */
    public void insertReservation(Reservation reservation) throws InterruptedException, IOException {
        processReservation(reservation, insertRequestOrder);
    }

    /**
     * Fonction générique pour traiter un utilisateur en fonction d'une requête spécifique.
     */
    private void processReservation(Reservation reservation, String requestOrder) throws InterruptedException, IOException {
        final Deque<ClientRequest> reservationRequests = new ArrayDeque<>();

        final ObjectMapper objectMapper = new ObjectMapper();
        final String jsonifiedReservation = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(reservation);
        logger.trace("Reservation en JSON : {}", jsonifiedReservation);

        final String requestId = UUID.randomUUID().toString();
        final Request request = new Request();
        request.setRequestId(requestId);
        request.setRequestOrder(requestOrder);
        request.setRequestContent(jsonifiedReservation);
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        final byte[] requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);

        final InsertReservationClientRequest reservationRequest = new InsertReservationClientRequest(
                networkConfig, 0, request, reservation, requestBytes);
        reservationRequests.push(reservationRequest);

        while (!reservationRequests.isEmpty()) {
            final ClientRequest processedRequest = reservationRequests.pop();
            processedRequest.join();
            final Reservation processedReservation = (Reservation) processedRequest.getInfo();
            logger.debug("Thread {} terminé : {} {} --> {}",
                    processedRequest.getThreadName(),
                    processedReservation.getId(), processedReservation.getName(), processedReservation.getDate(),processedReservation.getHeuredeb(),processedReservation.getHeurefin(),processedReservation.getType(),processedReservation.getDescription(),
                    processedRequest.getResult());
        }
    }

    /**
     * Récupère la liste des utilisateurs de la base de données.
     */
    public Reservations selectReservations() throws InterruptedException, IOException {
        final Deque<ClientRequest> reservationRequests = new ArrayDeque<>();
        final ObjectMapper objectMapper = new ObjectMapper();

        final String requestId = UUID.randomUUID().toString();
        final Request request = new Request();
        request.setRequestId(requestId);
        request.setRequestOrder(selectRequestOrder);
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        final byte[] requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);
        LoggingUtils.logDataMultiLine(logger, Level.TRACE, requestBytes);

        final SelectAllReservationsClientRequest reservationRequest = new SelectAllReservationsClientRequest(
                networkConfig, 0, request, null, requestBytes);
        reservationRequests.push(reservationRequest);

        if (!reservationRequests.isEmpty()) {
            final ClientRequest joinedReservationRequest = reservationRequests.pop();
            joinedReservationRequest.join();
            logger.debug("Thread {} terminé.", joinedReservationRequest.getThreadName());

            Reservations reservations = (Reservations) joinedReservationRequest.getResult();
            if (reservations != null) {
                logger.info("{} reservations récupérés.", reservations.getReservations().size());
                return reservations;
            } else {
                logger.warn("Aucune reservation trouvée.");
                return null;
            }
        } else {
            logger.error("Erreur : Aucune reservation récupérée.");
            return null;
        }
    }
}
