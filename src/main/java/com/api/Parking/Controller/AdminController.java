package com.api.Parking.Controller;

import com.api.Parking.Dto.CreateParkedDto;
import com.api.Parking.Model.CarModel;
import com.api.Parking.Model.ParkedModel;
import com.api.Parking.Service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;


    @GetMapping("/parkeds")
    public ResponseEntity<List<?>> getParkeds(@RequestParam(value = "date") String date) {
        try {
            List<?> parkeds = this.adminService.getParkedByDate(date);
            return ResponseEntity.ok(parkeds);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/create")
    public Object createParked(@RequestBody CreateParkedDto createParkedDto){

        // Verifica se já existe algum carro na vaga!
        Optional<ParkedModel> optionalParkedModel = this.adminService.getByPlace(createParkedDto.place());
        if (optionalParkedModel.isPresent()){
            // Se existe, retornar uma mensagem dizendo que está indisponível a vaga para estacionamento!
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not available!");
        }

        // Cria a entidade CarModel para armazenar no ParkedModel e salvando no Banco de Dados.
        CarModel model = new CarModel();
        model.setCarModel(createParkedDto.car().getCarModel());
        model.setCarMark(createParkedDto.car().getCarMark());
        model.setPlate(createParkedDto.car().getPlate());
        model.setColor(createParkedDto.car().getColor());
        this.adminService.saveCar(model);


        // Criando ParkedModel e salvando no Banco de Dados.
        ParkedModel parkedModel = new ParkedModel();
        parkedModel.setDateArrival(createParkedDto.dateArrival());
        parkedModel.setArrivalTime(createParkedDto.arrivalTime());
        parkedModel.setPlace(createParkedDto.place().toString());
        parkedModel.setCode(this.adminService.createCode());
        parkedModel.setCar(model);
        this.adminService.saveParked(parkedModel);


        return ResponseEntity.status(HttpStatus.CREATED).body(parkedModel);




    }

}
