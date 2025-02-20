package edu.ezip.ing1.pds.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import edu.ezip.commons.LoggingUtils;
import edu.ezip.ing1.pds.business.dto.Reservation;
import edu.ezip.ing1.pds.business.dto.Reservations;
import edu.ezip.ing1.pds.client.commons.ClientRequest;
import edu.ezip.ing1.pds.client.commons.NetworkConfig;
import edu.ezip.ing1.pds.commons.Request;

import edu.ezip.ing1.pds.requests.InsertReservationClientRequest;
import edu.ezip.ing1.pds.requests.SelectAllReservationsClientRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.UUID;

public class ReservationService {

    private final static String LoggingLabel = "FrontEnd - ReservationService";
    private final static Logger logger = LoggerFactory.getLogger(LoggingLabel);

    final String insertRequestOrder = "INSERT_RESERVATION";
    final String selectRequestOrder = "SELECT_ALL_RESERVATIONS";
    final String updateRequestOrder = "UPDATE_RESERVATION";
    final String deleteRequestOrder = "DELETE_RESERVATION";
    final String selectOneRequestOrder = "SELECT_ONE_RESERVATION";

    private final NetworkConfig networkConfig;

    public ReservationService(NetworkConfig networkConfig) {
        this.networkConfig = networkConfig;
    }

    public void insertReservation(Reservation reservation)throws InterruptedException, IOException {
        insertDeleteUpdateReservation(reservation, insertRequestOrder);
    }

    public void updateReservation(Reservation reservation)throws InterruptedException, IOException {
        insertDeleteUpdateReservation(reservation, updateRequestOrder);
    }

    public void deleteReservation(Reservation reservation)throws InterruptedException, IOException {
        insertDeleteUpdateReservation(reservation, deleteRequestOrder);
    }

    public void insertDeleteUpdateReservation(Reservation reservation, String requestOrder) throws InterruptedException, IOException {
        final Deque<ClientRequest> clientRequests = new ArrayDeque<ClientRequest>();

        int birthdate = 0;

        final ObjectMapper objectMapper = new ObjectMapper();
        final String jsonifiedGuy = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(reservation);
        logger.trace("Reservation with its JSON face : {}", jsonifiedGuy);
        final String requestId = UUID.randomUUID().toString();
        final Request request = new Request();
        request.setRequestId(requestId);
        request.setRequestOrder(requestOrder);
        request.setRequestContent(jsonifiedGuy);
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        final byte []  requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);

        final InsertReservationClientRequest clientRequest = new InsertReservationClientRequest(
                networkConfig,
                birthdate++, request, reservation, requestBytes);
        clientRequests.push(clientRequest);


        while (!clientRequests.isEmpty()) {
            final ClientRequest clientRequest2 = clientRequests.pop();
            clientRequest2.join();
            final Reservation resa = (Reservation) clientRequest2.getInfo();
            logger.debug("Thread {} complete : {} {} {} {} {} {} {}--> {}",
                    clientRequest2.getThreadName(),
                    reservation.getId(), reservation.getName(), reservation.getDate(), reservation.getHeuredeb(), reservation.getHeurefin(), reservation.getType(), reservation.getDescription(),
                    clientRequest2.getResult());
        }
    }

    public Reservation selectOneReservation(Reservation reservation) throws InterruptedException, IOException{

        int birthdate = 0;
        final Deque<ClientRequest> clientRequests = new ArrayDeque<ClientRequest>();
        final ObjectMapper objectMapper = new ObjectMapper();
        final String jsonifiedGuy = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(reservation);
        final String requestId = UUID.randomUUID().toString();
        final Request request = new Request();
        request.setRequestId(requestId);
        request.setRequestOrder(selectOneRequestOrder);
        request.setRequestContent(jsonifiedGuy);
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        final byte []  requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);
        LoggingUtils.logDataMultiLine(logger, Level.TRACE, requestBytes);
        final SelectAllReservationsClientRequest clientRequest = new SelectAllReservationsClientRequest(
                networkConfig,
                birthdate++, request, null, requestBytes);
        clientRequests.push(clientRequest);

        if(!clientRequests.isEmpty()) {
            final ClientRequest joinedClientRequest = clientRequests.pop();
            joinedClientRequest.join();
            logger.debug("Thread {} complete.", joinedClientRequest.getThreadName());
            Reservations reservations = (Reservations) joinedClientRequest.getResult();
            Reservation resaSelected = null;
            int k=1;
            for (Reservation e : reservations.getReservations()) {
                if(k==1){
                    resaSelected = e;
                }
                k=2;
            }
            return resaSelected;
        }
        else {
            logger.error("Aucune réservation n'existe");
            return null;
        }
    }

    public Reservations selectReservations() throws InterruptedException, IOException {
        int birthdate = 0;
        final Deque<ClientRequest> clientRequests = new ArrayDeque<ClientRequest>();
        final ObjectMapper objectMapper = new ObjectMapper();
        final String requestId = UUID.randomUUID().toString();
        final Request request = new Request();
        request.setRequestId(requestId);
        request.setRequestOrder(selectRequestOrder);
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        final byte []  requestBytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(request);
        LoggingUtils.logDataMultiLine(logger, Level.TRACE, requestBytes);
        final SelectAllReservationsClientRequest clientRequest = new SelectAllReservationsClientRequest(
                networkConfig,
                birthdate++, request, null, requestBytes);
        clientRequests.push(clientRequest);

        if(!clientRequests.isEmpty()) {
            final ClientRequest joinedClientRequest = clientRequests.pop();
            joinedClientRequest.join();
            logger.debug("Thread {} complete.", joinedClientRequest.getThreadName()); //Une fenêtre pour afficher les réservations
            return (Reservations) joinedClientRequest.getResult();
        }
        else {
            logger.error("No students found");
            return null;
        }
    }

}
