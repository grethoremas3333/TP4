package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        double daysDifference = ticket.getOutTime().getDay() - ticket.getInTime().getDay(); //on recupere le nombre de jours
        ///System.out.println("le nombre de jour de difference est de: "+ daysDifference);

        double inHour = ticket.getInTime().getHours(); /// on recupere la vaheur des heures HH de l'heure d'entree
        double outHour = ticket.getOutTime().getHours(); /// on recupere la vaheur des heures HH de l'heure de sortie

        double inMinutes = ticket.getInTime().getMinutes(); /// on recupere la vaheur des minutes MM de l'heure d'entree
        double outMinutes = ticket.getOutTime().getMinutes(); /// on recupere la vaheur des minutes MM de l'heure de sortie

        //TODO: Some tests are failing here. Need to check if this logic is correct
        ///System.out.println("HelloICI");
        inHour = (inHour * 60.0) + inMinutes;
        ///System.out.println("heure arrive: "+inHour); ///
        outHour = (outHour * 60.0) + outMinutes;
        ///System.out.println("heure sortie: "+outHour); ///
        double duration = (((outHour - inHour) + (daysDifference * 24.0 * 60.0)) /60.0);

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