package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import com.parkit.parkingsystem.service.ParkingService.*;
import java.util.Date;

import static com.parkit.parkingsystem.constants.ParkingType.CAR;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MyParkingServiceTest {

    private static ParkingService parkingService;

    @Mock
    private static InputReaderUtil inputReaderUtil;
    @Mock
    private static ParkingSpotDAO parkingSpotDAO;
    @Mock
    private static TicketDAO ticketDAO;

    @BeforeEach
    private void setUpPerTest() {
        try {
            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
            when(inputReaderUtil.readSelection()).thenReturn(1);
            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,true);
            when(parkingService.getNextParkingNumberIfAvailable()).thenReturn(parkingSpot);
            //when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF"); //deja dans ticket.setVehicleRegNumber("ABCDEF");
            when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);
            Date inTime = new Date();
            Ticket ticket = new Ticket();
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            ticket.setPrice(0);
            ticket.setInTime(inTime);
            ticket.setOutTime(null);
            when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
            when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);
            //parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

        } catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException("Failed to set up test mock objects");
        }
    }


    //Test pour processIncomingVehicle
    @Test
    public void processIncomingVehicleTest(){

        parkingService.processIncomingVehicle();
        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));

    }
}

//le type de vehicule (Auto ou Moto), le numero de registration, si espace available
// indique ou garer et enregistrer ticket sinon dire au user pas d'espace dispo
