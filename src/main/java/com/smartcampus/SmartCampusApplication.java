package com.smartcampus;

import com.smartcampus.model.CampusLocation;
import com.smartcampus.repository.CampusLocationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SmartCampusApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartCampusApplication.class, args);
    }

    @Bean
    CommandLineRunner initializeLocations(CampusLocationRepository repository) {
        return args -> {

            if (repository.count() == 0) {

                repository.save(
                        new CampusLocation("Canteen", "Low Crowd", false)
                );

                repository.save(
                        new CampusLocation("Library", "Low Crowd", false)
                );

                repository.save(
                        new CampusLocation("Parking", "Low Crowd", false)
                );

                repository.save(
                        new CampusLocation("Common Area", "Low Crowd", false)
                );
            }
        };
    }
}