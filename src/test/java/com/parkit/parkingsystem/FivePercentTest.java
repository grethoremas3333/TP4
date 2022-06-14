package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FivePercent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FivePercentTest {
    private static FivePercent fareCalculatorService;
    private Ticket ticket;

    @BeforeAll
    private static void setUp() {
        fareCalculatorService = new FivePercent();
    }

    @BeforeEach
    private void setUpPerTest() {
        ticket = new Ticket();
    }

    @Test
    public void calculateFivePercentCarForOneHour(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFareWithFivePercent(ticket);
        //assertEquals((Fare.CAR_RATE_PER_HOUR - (Fare.CAR_RATE_PER_HOUR * 0.05)), ticket.getPrice() );
        assertEquals((((((60*Fare.CAR_RATE_PER_HOUR)/60)-((60*Fare.CAR_RATE_PER_HOUR)/60) * 0.05))), ticket.getPrice() );
        //System.out.println("la valeur de ticket.getPrice est: "+ticket.getPrice()+" et celle de Fare.CarRate: "+Fare.CAR_RATE_PER_HOUR+" le test doit done 0.75 et le resultat attendu pour se scenario est 0.75 mais le Fare.CarRate doit donner 1.5 pour le moment");
    }

    @Test
    public void calculateFivePercentBikeForOneHour(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFareWithFivePercent(ticket);
        //assertEquals((Fare.BIKE_RATE_PER_HOUR - (Fare.BIKE_RATE_PER_HOUR * 0.05)), ticket.getPrice() );
        assertEquals((((((60*Fare.BIKE_RATE_PER_HOUR)/60)-((60*Fare.BIKE_RATE_PER_HOUR)/60) * 0.05))), ticket.getPrice() );
    }

    @Test
    public void calculateFivePercentCar(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  260 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFareWithFivePercent(ticket);
        assertEquals((((((260*Fare.CAR_RATE_PER_HOUR)/60)-((260*Fare.CAR_RATE_PER_HOUR)/60) * 0.05))), ticket.getPrice() );
        //System.out.println("la valeur de ticket.getPrice est: "+ticket.getPrice()+" et celle de Fare.CarRate: "+Fare.CAR_RATE_PER_HOUR+" le test doit done 0.75 et le resultat attendu pour se scenario est 0.75 mais le Fare.CarRate doit donner 1.5 pour le moment");
    }

    @Test
    public void calculateFivePercentBike(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  175 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFareWithFivePercent(ticket);
        assertEquals((((((175*Fare.BIKE_RATE_PER_HOUR)/60)-((175*Fare.BIKE_RATE_PER_HOUR)/60) * 0.05))), ticket.getPrice() );
    }


}
