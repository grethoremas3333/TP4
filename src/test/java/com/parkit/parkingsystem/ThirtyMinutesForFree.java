package com.parkit.parkingsystem;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ThirtyMinutesForFree {

}

/*
* la logique c'est d'utiliser la methode processExitingVehicle() mais au lieu de determiner la facture
* avec la difference de temps obtenu on retranche encore 30 minute pour effectuer le calcul maintenant
* deux cas de figure se pose si le temps (endTime[endtimeoriginal-30minutes] -startTime) > 0
* on applique le calcul si < 0 le montant de la facture est 0 cela signifie que le client est reste
* 30 minutes ou moins de 30 minutes stationne.
* */