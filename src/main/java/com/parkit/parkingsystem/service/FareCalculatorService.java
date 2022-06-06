package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        double inHour = ticket.getInTime().getHours();
        double outHour = ticket.getOutTime().getHours();

        double inMinutes = ticket.getInTime().getMinutes();
        double outMinutes = ticket.getOutTime().getMinutes();

        //TODO: Some tests are failing here. Need to check if this logic is correct
        ///System.out.println("HelloICI");
        inHour = (inHour * 60.0) + inMinutes;
        System.out.println("heure arrive: "+inHour); ///
        outHour = (outHour * 60.0) + outMinutes;
        System.out.println("heure sortie: "+outHour); ///
        double duration = 0.0;
        duration = (outHour - inHour)/60.0;

        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
                break;
            }
            case BIKE: {
                ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
    }
}