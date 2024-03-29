package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Date;

@ExtendWith(MockitoExtension.class)
public class FareCalculatorServiceTest {

    private static FareCalculatorService fareCalculatorService;
    private Ticket ticket;

    @Mock
    private static TicketDAO ticketDAO;

    @BeforeAll
    private static void setUp() {
        fareCalculatorService = new FareCalculatorService();
    }

    @BeforeEach
    private void setUpPerTest() {
        ticket = new Ticket();
    }

    @Test
    public void calculateFareCar(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService = new FareCalculatorService(ticketDAO);
        fareCalculatorService.calculateFare(ticket);
        assertThat(ticket.getPrice()).isEqualTo(Fare.CAR_RATE_PER_HOUR);
    }

    @Test
    public void calculateFareBike(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService = new FareCalculatorService(ticketDAO);
        fareCalculatorService.calculateFare(ticket);
        assertThat(ticket.getPrice()).isEqualTo(Fare.BIKE_RATE_PER_HOUR);
    }

    @Test
    public void calculateFareUnkownType(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, null,false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @Test
    public void calculateFareBikeWithFutureInTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() + (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @Test
    public void calculateFareBikeWithLessThanOneHourParkingTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  45 * 60 * 1000) );//45 minutes parking time should give 3/4th parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService = new FareCalculatorService(ticketDAO);
        fareCalculatorService.calculateFare(ticket);
        assertThat(ticket.getPrice()).isEqualTo(0.75 * Fare.BIKE_RATE_PER_HOUR);
    }

    @Test
    public void calculateFareCarWithLessThanOneHourParkingTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  45 * 60 * 1000) );//45 minutes parking time should give 3/4th parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService = new FareCalculatorService(ticketDAO);
        fareCalculatorService.calculateFare(ticket);
        assertThat(ticket.getPrice()).isEqualTo(0.75 * Fare.CAR_RATE_PER_HOUR);
    }

    @Test
    public void calculateFareCarWithMoreThanADayParkingTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  24 * 60 * 60 * 1000) );//24 hours parking time should give 24 * parking fare per hour
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService = new FareCalculatorService(ticketDAO);
        fareCalculatorService.calculateFare(ticket);
        assertThat(ticket.getPrice()).isEqualTo(24 * Fare.CAR_RATE_PER_HOUR);
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
        fareCalculatorService = new FareCalculatorService(ticketDAO);
        fareCalculatorService.calculateFare(ticket);
        assertThat(ticket.getPrice()).isEqualTo(0 * Fare.CAR_RATE_PER_HOUR);
    }

    @Test
    public void calculateThirtyFareBike(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  30 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);
        ticket.setVehicleRegNumber("6969"); ///
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService = new FareCalculatorService(ticketDAO);
        fareCalculatorService.calculateFare(ticket);
        assertThat(ticket.getPrice()).isEqualTo(0.0 * Fare.BIKE_RATE_PER_HOUR);
    }

    @Test
    public void calculateFareCarWithMoreThanOneHourParkingTime(){
        //Arrange
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  105 * 60 * 1000) );//45 minutes parking time should give 3/4th parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService = new FareCalculatorService(ticketDAO);
        //Act
        fareCalculatorService.calculateFare(ticket);
        //Assert
        assertThat(ticket.getPrice()).isEqualTo(1.75 * Fare.CAR_RATE_PER_HOUR);
    }

    @Test
    public void calculateFareBikeWithMoreThanOneHourParkingTime(){
        //Arrange
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  105 * 60 * 1000) );//45 minutes parking time should give 3/4th parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService = new FareCalculatorService(ticketDAO);
        //Act
        fareCalculatorService.calculateFare(ticket);
        //Assert
        assertThat(ticket.getPrice()).isEqualTo(1.75 * Fare.BIKE_RATE_PER_HOUR);
    }

    @Test
    public void calculateFivePercentCarForOneHour(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
        ticket.setVehicleRegNumber("12345");
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService = new FareCalculatorService(ticketDAO);
        when(ticketDAO.getTicket(anyString())).thenReturn(ticket);

        fareCalculatorService.calculateFare(ticket);

        //System.out.println("le montant dans ticket.getPrice() est: "+ticket.getPrice()+" et le montant dans le test est: "+(((((60*Fare.CAR_RATE_PER_HOUR)/60)-((60*Fare.CAR_RATE_PER_HOUR)/60) * 0.05))));
        assertThat(ticket.getPrice()).isEqualTo(1.425);

    }

    @Test
    public void calculateFivePercentBikeForOneHour(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);
        ticket.setVehicleRegNumber("189");
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService = new FareCalculatorService(ticketDAO);
        when(ticketDAO.getTicket(anyString())).thenReturn(ticket);

        fareCalculatorService.calculateFare(ticket);
        //assertEquals((Fare.BIKE_RATE_PER_HOUR - (Fare.BIKE_RATE_PER_HOUR * 0.05)), ticket.getPrice() );
        assertThat(ticket.getPrice()).isEqualTo((((((60*Fare.BIKE_RATE_PER_HOUR)/60)-((60*Fare.BIKE_RATE_PER_HOUR)/60) * 0.05))));
    }
    @Test
    public void calculateFivePercentCar(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  260 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
        ticket.setVehicleRegNumber("12345");
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService = new FareCalculatorService(ticketDAO);
        when(ticketDAO.getTicket(anyString())).thenReturn(ticket);

        fareCalculatorService.calculateFare(ticket);

        //System.out.println("le montant dans ticket.getPrice() est: "+ticket.getPrice()+" et le montant dans le test est: "+(((((260*Fare.CAR_RATE_PER_HOUR)/60)-((260*Fare.CAR_RATE_PER_HOUR)/60) * 0.05))));
        assertThat(ticket.getPrice()).isEqualTo((((((260*Fare.CAR_RATE_PER_HOUR)/60)-((260*Fare.CAR_RATE_PER_HOUR)/60) * 0.05))));
        //assertThat(ticket.getPrice()).isEqualTo((((((260*Fare.CAR_RATE_PER_HOUR)/60)-((260*Fare.CAR_RATE_PER_HOUR)/60) * 0.05))));
    }

    @Test
    public void calculateFivePercentBike(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  175 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);
        ticket.setVehicleRegNumber("189");
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService = new FareCalculatorService(ticketDAO);
        when(ticketDAO.getTicket(anyString())).thenReturn(ticket);

        fareCalculatorService.calculateFare(ticket);
        assertThat(ticket.getPrice()).isEqualTo((((((175*Fare.BIKE_RATE_PER_HOUR)/60)-((175*Fare.BIKE_RATE_PER_HOUR)/60) * 0.05))));
        //assertEquals((((((175*Fare.BIKE_RATE_PER_HOUR)/60)-((175*Fare.BIKE_RATE_PER_HOUR)/60) * 0.05))), ticket.getPrice() );
    }

    @Test
    public void calculateFareBikeWithLessThanThirtyMinutesParkingTime(){
        //Arrange
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  20 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);
        ticket.setVehicleRegNumber("ABCDEF");
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService = new FareCalculatorService(ticketDAO);
        //Act
        fareCalculatorService.calculateFare(ticket);
        //Assert
        assertEquals((0.0 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice() );
    }
}
