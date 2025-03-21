package edu.ezip.ing1.pds.services;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import edu.ezip.ing1.pds.business.dto.Reservation;
import edu.ezip.ing1.pds.business.dto.Reservations;
import edu.ezip.ing1.pds.client.commons.ClientRequest;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.commons.Request;
import edu.ezip.ing1.pds.requests.InsertReservationsClientRequest;
import edu.ezip.ing1.pds.requests.SelectAllReservationsClientRequest;
import edu.ezip.ing1.pds.requests.UpdateReservationsClientRequest;
import edu.ezip.ing1.pds.requests.DeleteReservationsClientRequest;

public class ReservationService {

    private final static String LoggingLabel = "FrontEnd - ReservationService";
    private final static Logger logger = LoggerFactory.getLogger(LoggingLabel);

    final String insertRequestOrder = "INSERT_RESERVATION";
    final String selectRequestOrder = "SELECT_ALL_RESERVATIONS";
    final String updateRequestOrder = "UPDATE_RESERVATION";
    final String deleteRequestOrder = "DELETE_RESERVATION";

    private final NetworkConfig networkConfig;

    public ReservationService(NetworkConfig networkConfig) {
        this.networkConfig = networkConfig;
    }

    /**
     * Insère une réservation dans la base de données.
     */
    public void insertReservation(Reservation reservation) throws InterruptedException, IOException {
        processReservation(reservation, insertRequestOrder);
    }

    /**
     * Modifier une réservation et la mettre à jour dans la base de données.
     */
    public void updateReservation(Reservation reservation) throws InterruptedException, IOException {
        processReservation(reservation, updateRequestOrder);
    }

    /**
     * Supprimer une réservation.
     */
    public void deleteReservation(Reservation reservation) throws InterruptedException, IOException {
        processReservation(reservation, deleteRequestOrder);
    }

    /**
     * Fonction générique pour traiter une réservation en fonction d'une requête spécifique.
     */
    private void processReservation(Reservation reservation, String requestOrder) throws InterruptedException, IOException {
        final Deque<ClientRequest> reservationRequests = new ArrayDeque<>();

        final ObjectMapper objectMapper = new ObjectMapper();
        final String jsonifiedReservation = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(reservation);
        logger.trace("Réservation en JSON : {}", jsonifiedReservation);

        final String requestId = UUID.randomUUID().toString();
        final Request request = new Request();
        request.setRequestId(requestId);
        request.setRequestOrder(requestOrder);
        request.setRequestContent(jsonifiedReservation);
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        final byte[] requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);

        ClientRequest reservationRequest;

        if ("INSERT_RESERVATION".equals(requestOrder)) {
            reservationRequest = new InsertReservationsClientRequest(networkConfig, 0, request, reservation, requestBytes);
        } else if ("UPDATE_RESERVATION".equals(requestOrder)) {
            reservationRequest = new UpdateReservationsClientRequest(networkConfig, 0, request, reservation, requestBytes);
        } else if ("DELETE_RESERVATION".equals(requestOrder)) {
            reservationRequest = new DeleteReservationsClientRequest(networkConfig, 0, request, reservation, requestBytes);
        } else {
            throw new IllegalArgumentException("Requête non supportée : " + requestOrder);
        }

        reservationRequests.push(reservationRequest);

        while (!reservationRequests.isEmpty()) {
            final ClientRequest processedRequest = reservationRequests.pop();
            processedRequest.join();
            final Reservation processedReservation = (Reservation) processedRequest.getInfo();
            logger.debug("Thread {} terminé : {} {} {} {} {} {} --> {}",
                    processedRequest.getThreadName(),
                    processedReservation.getName(), processedReservation.getDate(),processedReservation.getHeuredeb(),processedReservation.getHeurefin(),processedReservation.getType(),processedReservation.getDescription(),
                    processedRequest.getResult());
        }
    }

    /**
     * Récupère la liste des réservations de la base de données.
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

        logger.debug("Envoi de la requête SELECT_ALL_RESERVATIONS avec ID {}", requestId);

        final SelectAllReservationsClientRequest reservationRequest = new SelectAllReservationsClientRequest(
                networkConfig, 0, request, null, requestBytes);
        reservationRequests.push(reservationRequest);

        logger.debug("Taille de reservationRequests : {}", reservationRequests.size());

        if (!reservationRequests.isEmpty()) {
            final ClientRequest joinedReservationRequest = reservationRequests.pop();
            joinedReservationRequest.join();
            logger.debug("Thread {} terminé.", joinedReservationRequest.getThreadName());

            Object result = joinedReservationRequest.getResult();
            logger.debug("Résultat de getResult() : {}", result);

            if (result instanceof Reservations) {
                Reservations reservations = (Reservations) result;
                logger.info("{} réservations récupérées.", reservations.getReservations().size());
                return reservations;
            } else {
                logger.warn("Aucune réservation trouvée.");
                return null;
            }
        } else {
            logger.error("Aucune réservation récupérée.");
            return null;
        }
    }

}