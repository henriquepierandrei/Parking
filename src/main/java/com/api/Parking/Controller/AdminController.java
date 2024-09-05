package com.api.Parking.Controller;

import com.api.Parking.Dto.CreateParkedDto;
import com.api.Parking.Dto.ResponseParkedDto;
import com.api.Parking.Dto.UpdateParkedDto;
import com.api.Parking.Model.CarModel;
import com.api.Parking.Model.CollectionModel;
import com.api.Parking.Model.ParkedModel;
import com.api.Parking.Service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@ApiResponse()
public class AdminController {
    private final AdminService adminService;


    @GetMapping("/parkeds")
    @Operation(summary = "Obter todos os veículos estacionados, de acordo com a data atual!", description = "Retorna as informações do veículo e a vaga estacionada.")
    public ResponseEntity<List<ResponseParkedDto>> getParkeds(@RequestParam(value = "date") String dateString) {
        List<ResponseParkedDto> responseParkedDtos = new ArrayList<>();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Ajuste o formato conforme necessário

        try {
            // Tenta converter a string para LocalDate
            LocalDate date;
            try {
                date = LocalDate.parse(dateString, dateFormatter);
            } catch (DateTimeParseException e) {
                // Retorna um erro se a data estiver no formato incorreto
                return ResponseEntity.badRequest().body(null);
            }

            System.out.println("Solicitado");

            // Obtém a lista de veículos estacionados pela data
            List<ParkedModel> parkeds = this.adminService.getParkedByDate(date);

            // Verificação caso a lista esteja vazia
            if (parkeds.isEmpty()){
                System.out.println("Lista vazia :(");
            }

            // Formatação das datas
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss");

            // Loop para criar e adicionar os DTOs à lista
            for (ParkedModel parkedModel : parkeds) {
                String dateTimeArrivalFormatted = parkedModel.getDateTimeArrival() != null ? formatter.format(parkedModel.getDateTimeArrival()) : "N/A";
                String dateTimeExitFormatted = parkedModel.getDateTimeExit() != null ? formatter.format(parkedModel.getDateTimeExit()) : "N/A";

                ResponseParkedDto responseParkedDto = new ResponseParkedDto(
                        parkedModel.getPlace(),
                        dateTimeArrivalFormatted,
                        dateTimeExitFormatted,
                        parkedModel.getCar().getPlate(),
                        parkedModel.getCar().getColor(),
                        parkedModel.getCar().getCarMark(),
                        parkedModel.getCar().getCarModel()
                );

                // Adiciona o DTO à lista
                responseParkedDtos.add(responseParkedDto);
            }

            return ResponseEntity.ok(responseParkedDtos);
        } catch (Exception e) {
            e.printStackTrace(); // Log da stack trace para depuração
            // Retorna um status 500 em caso de erro
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }




    @PostMapping("/create")
    @Operation(summary = "Registrar veículo no estacionamento!")
    public ResponseEntity<?> createParked(@RequestBody CreateParkedDto createParkedDto){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss");

        // Verifica se já existe algum carro na vaga!
        Optional<ParkedModel> optionalParkedModel = this.adminService.getByPlace(createParkedDto.place());
        if (optionalParkedModel.isPresent()){
            // Se existe, retornar uma mensagem dizendo que está indisponível a vaga para estacionamento!
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not available!");
        }

        // Se não existe a entidade de arrecadação, é criado um de acordo com a data!
        if (this.adminService.getCollectionByDate(LocalDate.now()) == null){
            CollectionModel collectionModel = new CollectionModel();
            collectionModel.setFinalValue(0);



            collectionModel.setDate(LocalDate.now());
            this.adminService.saveCollection(collectionModel);
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
        parkedModel.setDate(LocalDate.now());
        parkedModel.setDateTimeArrival(LocalDateTime.now());
        parkedModel.setPlace(createParkedDto.place().toString());
        parkedModel.setCode(this.adminService.createCode());
        parkedModel.setCar(model);
        this.adminService.saveParked(parkedModel);


        return ResponseEntity.status(HttpStatus.CREATED).body(parkedModel);

    }



    @Operation(summary = "Obtém o valor a pagar de acordo com o tempo estacionado!", description = "Retorna o valor em reais!")
    @GetMapping("/parking/value")
    public ResponseEntity<Object> finishParkingPerPlace(@RequestParam(value = "code") String code){
        // Obtém a entidade de acordo com o seu código de acesso!
        Optional<ParkedModel> optionalParkedModel = this.adminService.getByCode(code);

        // Se a entidade for encontrada, cai nessa condição!
        if (optionalParkedModel.isPresent()){

            Double value = this.adminService.hourDifferent(optionalParkedModel.get().getDateTimeArrival(),5);

            
            return ResponseEntity.status(HttpStatus.FOUND).body(value);
        }
        // Caso não ache a entidade, retorna um erro!
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("NOT FOUND!");
    }



    @DeleteMapping("/parking/delete")
    @Operation(summary = "Deleta o veiculo após o pagamento, da vaga estacionada, podendo outro veículo usá-la!")
    public ResponseEntity<Object> paidPark(@RequestParam(value = "code") String code){
        // Obtém a entidade pelo código de acesso!
        Optional<ParkedModel> optionalParkedModel = this.adminService.getByCode(code);

        // Verifica se foi encontrado, se sim ele cai na condição!
        if (optionalParkedModel.isPresent()){


            // Adiciona a entidade de arrecadação o valor arrecadado, antes de deletar a entidade que armazena os dados!
            Double value = this.adminService.hourDifferent(optionalParkedModel.get().getDateTimeArrival(),5);
            CollectionModel collectionModel = this.adminService.getCollectionByDate(LocalDate.now());
            collectionModel.setFinalValue(collectionModel.getFinalValue()+value);


            // Deleta os dados do banco e retornar a vaga que foi liberada!
            this.adminService.deleteParkedAndCarModel(optionalParkedModel.get(),optionalParkedModel.get().getCar());
            return ResponseEntity.status(HttpStatus.OK).body("Place: "+optionalParkedModel.get().getPlace()+" disponível!");
        }

        // Caso não encontra, ele retornar um erro NOT FOUND!
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found!");

    }



    @PutMapping("/parking/update")
    @Operation(summary = "Atualiza algum dado estacionamento!")
    public ResponseEntity<ParkedModel> updatePark(@RequestParam(value = "code") String code, @RequestBody UpdateParkedDto updateParkedDto) {
        Optional<ParkedModel> optionalParkedModel = this.adminService.getByCode(code);

        if (optionalParkedModel.isPresent()) {
            ParkedModel parkedModel = optionalParkedModel.get();

            // Atualiza o campo 'place' se necessário
            if (updateParkedDto.place() != null && !updateParkedDto.place().isEmpty()) {
                parkedModel.setPlace(updateParkedDto.place());
            }

            // Atualiza o 'CarModel' se necessário
            if (updateParkedDto.model() != null) {
                CarModel carModel = updateParkedDto.model();

                // Verificar se todos os campos obrigatórios estão presentes e não vazios
                if (carModel != null &&
                        carModel.getPlate() != null && !carModel.getPlate().isEmpty() &&
                        carModel.getColor() != null && !carModel.getColor().isEmpty() &&
                        carModel.getCarMark() != null && !carModel.getCarMark().isEmpty() &&
                        carModel.getCarModel() != null && !carModel.getCarModel().isEmpty()) {

                    parkedModel.setCar(carModel);
                    this.adminService.saveCar(carModel);
                } else {
                    // Se os campos obrigatórios estiverem faltando, pode-se retornar um erro
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
                }
            }

            // Salva a entidade atualizada
            this.adminService.saveParked(parkedModel);

            // Retorna a resposta de sucesso com o modelo atualizado
            return ResponseEntity.status(HttpStatus.OK).body(parkedModel);
        }

        // Caso a entidade não seja encontrada, retornar uma resposta de conflito
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }



    @Operation(summary = "Deleta todos os registros de acordo com a sua data!")
    @DeleteMapping("/parking/reset")
    public ResponseEntity<?> deleteAllParkPerDate(@RequestParam(value = "date") String date){
        List<?> parkeds = Collections.singletonList(this.adminService.getParkedByDate(LocalDate.parse(date)));
        if (parkeds.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not Found in this date!");
        }
        return ResponseEntity.status(HttpStatus.OK).body(this.adminService.resetByDate(LocalDate.parse(date)));

    }


    @Operation(summary = "Obtém o veículo pelo código!", description = "Retorna o Veículo!")
    @GetMapping("/parking/find")
    public ResponseEntity<ResponseParkedDto> ParkingPerCode(@RequestParam(value = "code") String code){
        // Obtém a entidade de acordo com o seu código de acesso!
        Optional<ParkedModel> optionalParkedModel = this.adminService.getByCode(code);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        if (optionalParkedModel.isPresent()){
            // Formata as datas
            String dateTimeArrivalFormatted = optionalParkedModel.get().getDateTimeArrival() != null
                    ? dateFormatter.format(optionalParkedModel.get().getDateTimeArrival())
                    : "N/A";
            String dateTimeExitFormatted = optionalParkedModel.get().getDateTimeExit() != null
                    ? dateFormatter.format(optionalParkedModel.get().getDateTimeExit())
                    : "N/A";

            // Cria o DTO de resposta
            ResponseParkedDto responseParkedDto = new ResponseParkedDto(
                    optionalParkedModel.get().getPlace(),
                    dateTimeArrivalFormatted,
                    dateTimeExitFormatted,
                    optionalParkedModel.get().getCar().getPlate(),
                    optionalParkedModel.get().getCar().getColor(),
                    optionalParkedModel.get().getCar().getCarMark(),
                    optionalParkedModel.get().getCar().getCarModel()
            );

            // Retorna a resposta com status 200 OK
            return ResponseEntity.status(HttpStatus.OK).body(responseParkedDto);
        }

        // Caso não ache a entidade, retorna um erro 404 NOT FOUND
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }






}
