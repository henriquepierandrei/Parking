package com.api.Parking.Service;


import com.api.Parking.Model.CarModel;
import com.api.Parking.Model.ParkedModel;
import com.api.Parking.Repository.CarRepository;
import com.api.Parking.Repository.ParkedRepository;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AdminService {
    @Autowired ParkedRepository parkedRepository;
    @Autowired CarRepository carRepository;


    public List<ParkedModel> getParkedByDate(String date) {
        // Verifica se a data é nula ou vazia
        if (date == null || date.isEmpty()){
            throw new IllegalArgumentException("This date is null or empty!");
        }

        // Tenta verificar se existe a entidade pela data!
        try {
            List<ParkedModel> parkeds = this.parkedRepository.findByDate(date);
            return parkeds != null ? parkeds : Collections.emptyList();

        // Caso nao existe retorna um erro!
        } catch (Exception e) {
            throw new ServiceException("Erro ao buscar os dados estacionados", e);
        }
    }

    public String createCode(){


        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        String generatedCode = null;


        while(true){

            // Gera um código de 5 dígitos.
            for (int i = 0 ; i < 5; i++){
                stringBuilder.append(random.nextInt(10));
            }

            // Verifica se este código está disponível através de uma consulta no banco de dados.
            Optional<ParkedModel> parkedModel = this.parkedRepository.findByCode(stringBuilder.toString());

            // Se ele estiver vazio (Não existe no Banco), a variavel "generatedCode" recebe esse codigo e quebra o loop.
            if (parkedModel.isEmpty()){
                generatedCode = stringBuilder.toString();
                break;
            }

            stringBuilder.setLength(0);
        }

        // Retorna o codigo de verificação.
        return stringBuilder.toString();
    }

    public void saveCar(CarModel model){
        this.carRepository.save(model);
    }

    public void saveParked(ParkedModel parkedModel) {
        this.parkedRepository.save(parkedModel);
    }

    public Optional<ParkedModel> getByDateAndPlace(String date, String place) {
        return this.parkedRepository.findByDateAndPlace(date,place);
    }
}
