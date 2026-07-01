package lv.bootcamp.shelter.service;

import lv.bootcamp.shelter.model.Animal;
import lv.bootcamp.shelter.service.data.ImportResult;
import lv.bootcamp.shelter.service.data.ShelterReportData;

import java.util.*;
import java.util.stream.Collectors;

public class ShelterAnalyticsService {
    public ShelterReportData buildReportData(ImportResult importResult) {
        List<Animal> allAnimals = importResult.allAnimals();
        Set<String> uniqueSpecies =
                allAnimals
                        .stream()
                        .map(Animal::getSpecies)
                        .collect(Collectors.toCollection(TreeSet::new)
        );

        Map<String, List<Animal>> animalsBySpecies = allAnimals
                .stream()
                .collect(Collectors.groupingBy(Animal::getSpecies));

        List<String> animalsNeedingVetInput = allAnimals
                .stream().
                filter(animal -> !animal.isVaccinated()).
                map(animal -> animal.getName()+"("+animal.getSpecies()+")")
                .toList();



        // TODO Step 3:
        // Add necessary fields to ShelterReportData
        // Use stream pipelines for:
        // - vaccinated vs unvaccinated counts per species
        // - oldest animal per species (excluding unknown ages)

        return new ShelterReportData(importResult);
    }
}
