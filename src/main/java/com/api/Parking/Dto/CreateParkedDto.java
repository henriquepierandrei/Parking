package com.api.Parking.Dto;

import com.api.Parking.Model.CarModel;

public record CreateParkedDto(String place, CarModel car, String dateAndTimeArrival, String dateAndTimeExit) {
}
