package com.api.Parking.Dto;

import com.api.Parking.Enum.PlaceEnum;
import com.api.Parking.Model.CarModel;

public record CreateParkedDto(PlaceEnum place, CarModel car, String date, String arrivalTime) {
}
