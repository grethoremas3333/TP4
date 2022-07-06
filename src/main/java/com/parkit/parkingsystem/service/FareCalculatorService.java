package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.Ticket;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import java.util.Arrays;
import java.io.IOException;
import java.text.*;

public class FareCalculatorService {

    private TicketDAO ticketDAO = new TicketDAO(); ///

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        double daysDifference = ticket.getOutTime().getDay() - ticket.getInTime().getDay(); //on recupere le nombre de jours
        ///System.out.println("le nombre de jour de difference est de: "+ daysDifference);

        DecimalFormat df = new DecimalFormat("0.00"); ///* pour la conversion a deux decimals
        df.setRoundingMode(RoundingMode.FLOOR);  //*/

        double inHour = ticket.getInTime().getHours(); /// on recupere la vaheur des heures HH de l'heure d'entree
        double outHour = ticket.getOutTime().getHours(); /// on recupere la vaheur des heures HH de l'heure de sortie

        double inMinutes = ticket.getInTime().getMinutes(); /// on recupere la vaheur des minutes MM de l'heure d'entree
        double outMinutes = ticket.getOutTime().getMinutes(); /// on recupere la vaheur des minutes MM de l'heure de sortie

        //TODO: Some tests are failing here. Need to check if this logic is correct

        inHour = (inHour * 60.0) + inMinutes;
        ///System.out.println("heure arrive: "+inHour); ///
        outHour = (outHour * 60.0) + outMinutes;
        ///System.out.println("heure sortie: "+outHour); ///

        double duration = (((outHour - inHour) + (daysDifference * 24.0 * 60.0)) /60.0);

       duration = 0.0;
        ///String[] preferVehicleRegNumber = {"ABCDEF", "323", "12345", "569"}; ///pour les tests de 5% de fidelite

        if (((outHour - inHour) <= 30) && (daysDifference == 0.0)) {
            duration = 0.0;
        }
        else if (ticketDAO.getTicket(ticket.getVehicleRegNumber()) != null){ //marche avec connection directe a la BDD
            System.out.println("client beneficiant des 5% de reduction!!!");
            duration = ( (((outHour - inHour) + (daysDifference * 24.0 * 60.0)) / 60.0) - ((((outHour - inHour) + (daysDifference * 24.0 * 60.0)) / 60.0)*0.05) );
        } ///5% pour les clients fideles
        else {
            duration = ( (((outHour - inHour) + (daysDifference * 24.0 * 60.0)) / 60.0) );
        }

        /* else if ((Arrays.asList(preferVehicleRegNumber).contains(ticket.getVehicleRegNumber()))) {
            duration = ( (((outHour - inHour) + (daysDifference * 24.0 * 60.0)) / 60.0) - ((((outHour - inHour) + (daysDifference * 24.0 * 60.0)) / 60.0)*0.05) );
        } //avec le tableau de preference */

        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
                //ticket.setPrice(Double.parseDouble(df.format(duration * Fare.CAR_RATE_PER_HOUR)));
                /*try {
                    double value = duration * Fare.CAR_RATE_PER_HOUR;
                    String formate = df.format(value);
                    double finalValue = (Double)df.parse(formate);
                    ticket.setPrice(finalValue);
                } catch (ParseException pe){
                    System.out.println("Erreur formatage!!!");
                }*/
                break;
            }
            case BIKE: {
                ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
                //ticket.setPrice(Double.parseDouble(df.format(duration * Fare.BIKE_RATE_PER_HOUR)));
                /*try {
                    double value = duration * Fare.BIKE_RATE_PER_HOUR;
                    String formate = df.format(value);
                    double finalValue = (Double)df.parse(formate);
                    ticket.setPrice(finalValue);
                } catch (ParseException pe){
                    System.out.println("Erreur formatage!!!");
                }*/
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
    }
}