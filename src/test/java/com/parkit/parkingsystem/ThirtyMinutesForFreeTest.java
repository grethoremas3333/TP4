package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import com.parkit.parkingsystem.service.ThirtyMinutesForFree;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ThirtyMinutesForFreeTest {
    private static ThirtyMinutesForFree fareCalculatorService;
    private Ticket ticket;

    @BeforeAll
    private static void setUp() {
        fareCalculatorService = new ThirtyMinutesForFree();
    }

    @BeforeEach
    private void setUpPerTest() {
        ticket = new Ticket();
    }

    @Test
    public void calculateThirtyFareCar(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  30 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFareWith30Remise(ticket);
        assertEquals((0.50 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice() );;  //0.75
        System.out.println("la valeur de ticket.getPrice est: "+ticket.getPrice()+" et celle de Fare.CarRate: "+Fare.CAR_RATE_PER_HOUR+" le test doit done 0.75 et le resultat attendu pour se scenario est 0.75 mais le Fare.CarRate doit donner 1.5 pour le moment");
    }

    @Test
    public void calculateThirtyFareBike(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  30 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFareWith30Remise(ticket);
        assertEquals((0.50 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice() ); //0.5
    }

    @Test
    public void calculateFareBikeWithLessThanThirtyMinutesParkingTime(){
        //Arrange
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  20 * 60 * 1000) );//45 minutes parking time should give 3/4th parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        //Act
        fareCalculatorService.calculateFareWith30Remise(ticket);
        //Assert
        assertEquals((0.0 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice() );
    }

    @Test
    public void calculateFareCarWithLessThanThirtyMinutesParkingTime(){
        //Arrange
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  5 * 60 * 1000) );//5 minutes parking time should give 3/4th parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        //Act
        fareCalculatorService.calculateFareWith30Remise(ticket);
        //Assert
        assertEquals((0.0 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice() );
    }

    @Disabled
    public void calculateFareBikeWithLessThanOneHourParkingTime(){
        //Arrange
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  45 * 60 * 1000) );//45 minutes parking time should give 3/4th parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ///System.out.println("l'heure d'entree est: "+inTime); //
        ///System.out.println(ticket.getInTime());
        ///System.out.println("l'heure de sortie est: "+outTime); //
        ///System.out.println(ticket.getOutTime());
        ticket.setParkingSpot(parkingSpot);
        //Act
        fareCalculatorService.calculateFareWith30Remise(ticket);
        //Assert
        assertEquals((0.75 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice() );
    }

    @Disabled
    public void calculateFareCarWithMoreThanADayParkingTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  24 * 60 * 60 * 1000) );//24 hours parking time should give 24 * parking fare per hour
        ///System.out.println("la premiere valeur est de: "+inTime);
        Date outTime = new Date();
        ///System.out.println("la seconde valeur est de: "+outTime);
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFareWith30Remise(ticket);
        assertEquals( (24 * Fare.CAR_RATE_PER_HOUR) , ticket.getPrice());
    }

}

/*
* la logique c'est d'utiliser la methode processExitingVehicle() mais au lieu de determiner la facture
* avec la difference de temps obtenu on retranche encore 30 minute pour effectuer le calcul maintenant
* deux cas de figure se pose si le temps (endTime[endtimeoriginal-30minutes] -startTime) > 0
* on applique le calcul si < 0 le montant de la facture est 0 cela signifie que le client est reste
* 30 minutes ou moins de 30 minutes stationne.
* */