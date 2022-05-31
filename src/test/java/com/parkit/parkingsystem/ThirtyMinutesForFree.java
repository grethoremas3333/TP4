package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ThirtyMinutesForFree {
    private static FareCalculatorService fareCalculatorService;
    private Ticket ticket;

    @BeforeAll
    private static void setUp() {
        fareCalculatorService = new FareCalculatorService();
    }

    @BeforeEach
    private void setUpPerTest() {
        ticket = new Ticket();
    }

    @Test
    public void calculateThirtyFareCar(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
        //System.out.println("l'heure d'entree est: "+inTime);
        Date startTime = new Date();
        startTime.setTime(inTime.getTime() - (30 * 60 * 1000));
        //System.out.println("l'heure d'entree final est: "+startTime);
        //System.out.println("l'heure de sortie est: "+outTime);
        ticket.setInTime(startTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals(ticket.getPrice(), Fare.CAR_RATE_PER_HOUR);
    }

    @Test
    public void calculateThirtyFareBike(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);
        Date startTime = new Date();
        startTime.setTime(inTime.getTime() - (30 * 60 * 1000));
        ticket.setInTime(startTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals(ticket.getPrice(), Fare.BIKE_RATE_PER_HOUR);
    }

}

/*
* la logique c'est d'utiliser la methode processExitingVehicle() mais au lieu de determiner la facture
* avec la difference de temps obtenu on retranche encore 30 minute pour effectuer le calcul maintenant
* deux cas de figure se pose si le temps (endTime[endtimeoriginal-30minutes] -startTime) > 0
* on applique le calcul si < 0 le montant de la facture est 0 cela signifie que le client est reste
* 30 minutes ou moins de 30 minutes stationne.
* */