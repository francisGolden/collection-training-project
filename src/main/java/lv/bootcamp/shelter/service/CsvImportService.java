package lv.bootcamp.shelter.service;

import lombok.extern.slf4j.Slf4j;
import lv.bootcamp.shelter.model.Animal;
import lv.bootcamp.shelter.service.data.ImportResult;

import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.Month;

@Slf4j
public class CsvImportService {

    public ImportResult importAnimals(Path inputPath) {
        log.info("Starting import from {}", inputPath);

        List<Animal> allAnimals = new ArrayList<>();

        // TODO Step 1:
        // 1) Read intake.csv with UTF-8.
        // 2) Skip header row.
        // 3) Skip malformed rows and log warnings.
        // 4) Allow blank age as unknown (null), but reject non-numeric age values.
        // 5) Parse intakeDate using DateTimeFormatter.
        // 6) Map each row to Animal object.
        try (Stream<String> lines = Files.lines(inputPath, StandardCharsets.UTF_8)) {
            lines.skip(1).forEach(line -> {
                String[] parts = line.split(",");

                String name = parts[0];
                if (name.trim().isEmpty()){
                    log.warn("Skipping string because no name was provided");
                    return;
                }

                String animal = parts[1];
                if (animal.trim().isEmpty()){
                    log.warn("Skipping string because no animal species was provided");
                    return;
                }

                Integer age;
                try {
                    String ageString = parts[2].trim();
                    age = ageString.isEmpty() ? null : Integer.parseInt(ageString);
                } catch (NumberFormatException e){
                    log.warn("Catching NumberFormatException: [{}] and skipping row.", e.getMessage());
                    return;
                }

                boolean adopted = Boolean.parseBoolean(parts[3]);

                String dateString = parts[4];
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

                LocalDate intakeDate;

                try {
                    intakeDate = LocalDate.parse(dateString, formatter);
                } catch (DateTimeParseException e) {
                    log.error("Invalid date. {}", e.getMessage());
                    return;
                }

                allAnimals.add(new Animal(name, animal, age, adopted, intakeDate));
            });
        } catch (Exception e) {
            log.error(e.getMessage());
        }


        return new ImportResult(allAnimals, 0);
    }
}
