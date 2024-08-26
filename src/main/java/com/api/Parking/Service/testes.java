package com.api.Parking.Service;


import java.time.Duration;
import java.time.LocalDateTime;

public class testes {
    public static void main(String[] args) {
        String diff = hourDifferent(LocalDateTime.of(2024, 8, 26, 11, 30, 23, 699301600),5);
        System.out.println(diff);
    }

    public static String hourDifferent(LocalDateTime localDateTime, double valuePerHour) {
        // Data e hora atual
        LocalDateTime now = LocalDateTime.now();


        // Calcula a duração entre 'start' e 'now'
        Duration diff = Duration.between(localDateTime, now);


        long hours = diff.toHours();


        // Converte a diferença de horas para string
        return String.valueOf("R$ " + hours*valuePerHour).replace('.', ',');
    }
}