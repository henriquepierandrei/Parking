package com.api.Parking.Service;


import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class testes {
    public static void main(String[] args) {
        report();
    }

    public static void report (){
        LocalDate currentDate = LocalDate.now(); // Obtém a data atual
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM"); // Define o padrão para dois dígitos do mês
        String formattedMonth = currentDate.format(formatter); // Formata o mês

        System.out.println(formattedMonth);

    }


}