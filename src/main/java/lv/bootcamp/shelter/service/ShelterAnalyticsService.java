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

        Map<String, String> oldestAnimalPerSpecies = allAnimals.stream()
                .filter(animal -> animal.getAge() != null)
                .collect(Collectors.groupingBy(
                        Animal::getSpecies,
                        Collectors.collectingAndThen(
                                Collectors.maxBy(Comparator.comparingInt(Animal::getAge)),
                                opt -> opt.get().getName()
                        )
                ));

        Map<String, Map<Boolean, Long>> vaccinationStatusPerSpecies = allAnimals.stream()
                .collect(Collectors.groupingBy(
                        Animal::getSpecies,
                        Collectors.partitioningBy(
                                Animal::isVaccinated,
                                Collectors.counting()
                        )
                ));

        return new ShelterReportData(importResult,
                uniqueSpecies,
                animalsBySpecies,
                animalsNeedingVetInput,
                vaccinationStatusPerSpecies,
                oldestAnimalPerSpecies);
    }
}
