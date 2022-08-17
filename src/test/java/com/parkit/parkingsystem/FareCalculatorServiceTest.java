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
    public void calculateFareCarTest(){
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
    public void calculateFareBikeTest(){
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
    public void calculateFareUnkownTypeTest(){
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
    public void calculateFareBikeWithFutureInTimeTest(){
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
    public void calculateFareBikeWithLessThanOneHourParkingTimeTest(){
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
    public void calculateFareCarWithLessThanOneHourParkingTimeTest(){
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
    public void calculateFareCarWithMoreThanADayParkingTimeTest(){
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
    public void calculateThirtyFareCarTest(){
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
    public void calculateThirtyFareBikeTest(){
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
    public void calculateFareCarWithMoreThanOneHourParkingTimeTest(){
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
    public void calculateFareBikeWithMoreThanOneHourParkingTimeTest(){
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
    public void calculateFivePercentCarForOneHourTest(){
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
    public void calculateFivePercentBikeForOneHourTest(){
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
        assertThat(ticket.getPrice()).isEqualTo(0.95);
    }
    @Test
    public void calculateFivePercentCarTest(){
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
        assertThat(ticket.getPrice()).isEqualTo(6.175);
    }

    @Test
    public void calculateFivePercentBikeTest(){
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
        assertThat(ticket.getPrice()).isEqualTo(2.770833333333333);
    }

    @Test
    public void calculateFareBikeWithLessThanThirtyMinutesParkingTimeTest(){
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
