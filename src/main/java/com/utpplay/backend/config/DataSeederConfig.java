package com.utpplay.backend.config;

import com.utpplay.backend.model.Implemento;
import com.utpplay.backend.repository.ImplementoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeederConfig {

    @Bean
    CommandLineRunner seedImplementos(ImplementoRepository implementoRepository) {
        return args -> {
            if (implementoRepository.count() == 0) {
                String[] deportes = { "Fútbol", "Básquetbol", "Vóley", "Tenis" };
                for (String d : deportes) {
                    Implemento pelota = new Implemento();
                    pelota.setTipo("PELOTA");
                    pelota.setDeporte(d);
                    pelota.setStockTotal(5);
                    implementoRepository.save(pelota);
                }
                Implemento chalecos = new Implemento();
                chalecos.setTipo("CHALECO");
                chalecos.setDeporte("General");
                chalecos.setStockTotal(10);
                implementoRepository.save(chalecos);
            }
        };
    }
}