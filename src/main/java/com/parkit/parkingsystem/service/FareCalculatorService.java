package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.Ticket;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Date;

public class FareCalculatorService {

    private TicketDAO ticketDAO;

    public FareCalculatorService(TicketDAO ticketDAO) {
        this.ticketDAO = ticketDAO;
    }

    public FareCalculatorService() {
    }

    public void calculateFare(Ticket ticket) {
        if ((ticket.getOutTime() == null) ||
                (ticket.getOutTime().before(ticket.getInTime()))) {
            throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
        }

        long durationInMinutes = durationInMinutes(ticket.getInTime(), ticket.getOutTime());
        boolean fivePercentOff = false;

        if (durationInMinutes <= 30) return;

        if (ticketDAO.getTicket(ticket.getVehicleRegNumber()) != null) {
            //System.out.println("client beneficiant des 5% de reduction!!!");
            fivePercentOff = true;
        }

        switch (ticket.getParkingSpot().getParkingType()) {
            case CAR: {
                double price = (durationInMinutes * Fare.CAR_RATE_PER_HOUR)/60;
                double finalValue = price;
                if (fivePercentOff) {
                    try {
                        price = price * 0.95;
                        DecimalFormat df = new DecimalFormat("0.000)");
                        df.setRoundingMode(RoundingMode.UP);
                        String formatage = df.format(price);
                        finalValue = (Double) df.parse(formatage);
                    } catch (ParseException pe){
                        System.out.println("Erreur formatage!!!");
                    }
                }
                ticket.setPrice(finalValue);
                break;
            }
            case BIKE: {
                double price = (durationInMinutes * Fare.BIKE_RATE_PER_HOUR)/60;
                if (fivePercentOff) price = price * 0.95;
                ticket.setPrice(price);
                break;
            }
            default:
                throw new IllegalArgumentException("Unkown Parking Type");
        }

    }

    public long durationInMinutes(Date from, Date to) {
        long differenceInMs = to.getTime() - from.getTime();
        return differenceInMs / 60000;
    }
}