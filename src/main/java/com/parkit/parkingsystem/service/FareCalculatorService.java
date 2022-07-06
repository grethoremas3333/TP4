package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.Ticket;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Arrays;

public class FareCalculatorService {

    private TicketDAO ticketDAO = new TicketDAO(); ///

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.FLOOR);
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

        double duration = 0.0;
        //String[] preferVehicleRegNumber = {"ABCDEF", "323", "12345", "569"}; ///pour les tests de 5% de fidelite

        if (((outHour - inHour) <= 30) && (daysDifference == 0.0)) {
            duration = 0.0;
        }
        else if (ticketDAO.getTicket(ticket.getVehicleRegNumber()) != null){
            System.out.println("element trouve");
            duration = ( (((outHour - inHour) + (daysDifference * 24.0 * 60.0)) / 60.0) - ((((outHour - inHour) + (daysDifference * 24.0 * 60.0)) / 60.0)*0.05) );
        } ///5% pour les clients fideles
        /*
        else if ((Arrays.asList(preferVehicleRegNumber).contains(ticket.getVehicleRegNumber()))) { //( ticket.getVehicleRegNumber() != ""){ //ticketDAO.getTicket(ticket.getVehicleRegNumber())  (Arrays.asList(preferVehicleRegNumber).contains(ticket.getVehicleRegNumber())){
            System.out.println("element trouve");
            duration = ( (((outHour - inHour) + (daysDifference * 24.0 * 60.0)) / 60.0) - ((((outHour - inHour) + (daysDifference * 24.0 * 60.0)) / 60.0)*0.05) );
        * */
        else {
            duration = ( (((outHour - inHour) + (daysDifference * 24.0 * 60.0)) / 60.0) );
        }

        //double duration = (((outHour - inHour) + (daysDifference * 24.0 * 60.0)) /60.0);

        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
                /*double value = duration * Fare.BIKE_RATE_PER_HOUR;
                double finalValue = Math.round( value * 100.0 ) / 100.0;
                ticket.setPrice(finalValue);*/
                /*
                double value = duration * Fare.BIKE_RATE_PER_HOUR;
                String formate = df.format(value);
                double finalValue = Double.parseDouble(formate);
                ticket.setPrice(finalValue);
                */
                //ticket.setPrice(Double.parseDouble(df.format(duration * Fare.CAR_RATE_PER_HOUR)));
                break;
            }
            case BIKE: {
                ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
                //ticket.setPrice(Double.parseDouble(df.format(duration * Fare.BIKE_RATE_PER_HOUR)));
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
    }
}