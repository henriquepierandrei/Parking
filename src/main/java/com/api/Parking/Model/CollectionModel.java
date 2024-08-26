package com.api.Parking.Model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollectionModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private double finalValue=0;

    private LocalDate date;

}
