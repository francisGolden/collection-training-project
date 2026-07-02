package lv.bootcamp.shelter.service;
import lombok.extern.slf4j.Slf4j;
import lv.bootcamp.shelter.service.data.ShelterReportData;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.time.ZoneId;

@Slf4j
public class ReportExportService {

    public void writeReport(Path outputPath, ShelterReportData reportData) {
        try {
            if (!Files.exists(outputPath)) {
                Files.createFile(outputPath);
                log.info("Empty file created successfully!");
            } else {
                log.info("File already exists.");
            }
        } catch (FileAlreadyExistsException e) {
            log.error("File already exists. Message: {}", e.getMessage());
        } catch (IOException e) {
            log.error("IO Exception: {}", e.getMessage());
        }

        List<String> totalsPerSpecies = new ArrayList<>();
        for (String uniqueSpec: reportData.uniqueSpecies()){
            totalsPerSpecies.add(uniqueSpec + " " + reportData.animalsBySpecies().get(uniqueSpec).size());
        }

        String vaccinatedString = reportData.vaccinationStatusPerSpecies().entrySet().stream()
                .map(entry -> {
                    String species = entry.getKey();
                    long vaccinatedCount = entry.getValue().getOrDefault(true, 0L);
                    return species + ": " + vaccinatedCount;
                })
                .collect(Collectors.joining(", "));

        String oldestAnimalsString = reportData.oldestAnimalPerSpecies().entrySet().stream()
                .map(entry -> entry.getKey() + ": " + entry.getValue())
                .collect(Collectors.joining(", "));

        List<String> reportLines = List.of(
                "Generated report date: " + LocalDate.now(ZoneId.of("UTC")),
                "",
                "Total imported animals: " + reportData.importResult().allAnimals().size(),
                "",
                "Total skipped animals: " + reportData.importResult().skippedRows(),
                "",
                "Unique species: " + String.join(", ", reportData.uniqueSpecies()),
                "",
                "Number of animals per species: " + String.join(", ", totalsPerSpecies),
                "",
                "Vaccinated animals per species: " + vaccinatedString,
                "",
                "Oldest animal per species: " + oldestAnimalsString,
                "",
                "Animals needing vaccination: " + String.join(", ", reportData.animalsNeedingVetInput()).replaceAll("(.{1,60})(?:\\s|$)", "$1\n")
        );

        try (BufferedWriter writer = Files.newBufferedWriter(outputPath, StandardCharsets.UTF_8)) {
            for (String line : reportLines) {
                writer.write(line);
                writer.newLine(); }
        } catch (IOException e) {
            log.error("Error writing string. Message: {}", e.getMessage());
        }
    }
}
