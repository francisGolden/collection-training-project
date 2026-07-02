package lv.bootcamp.shelter.service;

import lv.bootcamp.shelter.service.data.ShelterReportData;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.time.ZoneId;

public class ReportExportService {

    public void writeReport(Path outputPath, ShelterReportData reportData) {
        // TODO Step 4:
        // 1) Write upload-report.txt in required format.
        // 2) Include generated date, imported/skipped totals.
        // 3) Include unique species and per-species breakdown.
        // 4) Include oldest animal per species.
        // 5) Include animalsNeedingVetInput as name(species), name2(species2).
        // 6) Use UTF-8 and try-with-resources.
        Path path = Path.of("upload-report.txt");
        try {
            if (!Files.exists(path)) {
                Files.createFile(path);
                System.out.println("Empty file created successfully!");
            } else {
                System.out.println("File already exists.");
            }
        } catch (FileAlreadyExistsException e) {
            System.err.println("File already exists: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("IO Exception " + e.getMessage());
        }

        try {
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
                    "Oldest animal per species: " + String.join(", ", oldestAnimalsString),
                    "",
                    "Animals needing vaccination: " + String.join(", ", reportData.animalsNeedingVetInput()).replaceAll("(.{1,60})(?:\\s|$)", "$1\n")
            );
            Files.write(path, reportLines);
        } catch (IOException e) {
            System.out.println("error writing string " + e.getMessage());
        }


//        throw new UnsupportedOperationException("TODO: implement report export");
    }
}
