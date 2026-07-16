package com.utpplay.backend.config;

import com.utpplay.backend.model.Implemento;
import com.utpplay.backend.repository.ImplementoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeederConfig {

    @Bean
    CommandLineRunner seedImplementos(ImplementoRepository repo) {
        return args -> {
            if (repo.count() > 0)
                return;

            // Generales — compartidos por todos los deportes
            seed(repo, "CHALECO", "General", 10);
            seed(repo, "SILBATO", "General", 3);
            seed(repo, "BOTIQUIN", "General", 2);

            // Fútbol
            seed(repo, "PELOTA", "Fútbol", 5);
            seed(repo, "CONO", "Fútbol", 20);

            // Básquetbol
            seed(repo, "PELOTA", "Básquetbol", 5);
            seed(repo, "CRONOMETRO", "Básquetbol", 2);

            // Vóley
            seed(repo, "PELOTA", "Vóley", 5);
            seed(repo, "RED", "Vóley", 2);

            // Tenis
            seed(repo, "PELOTA", "Tenis", 8);
            seed(repo, "RAQUETA", "Tenis", 6);
        };
    }

    private void seed(ImplementoRepository repo, String tipo, String deporte, int stock) {
        Implemento i = new Implemento();
        i.setTipo(tipo);
        i.setDeporte(deporte);
        i.setStockTotal(stock);
        repo.save(i);
    }
}