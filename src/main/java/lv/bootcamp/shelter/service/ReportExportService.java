package lv.bootcamp.shelter.service;

import lv.bootcamp.shelter.service.data.ShelterReportData;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;

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


        throw new UnsupportedOperationException("TODO: implement report export");
    }
}
