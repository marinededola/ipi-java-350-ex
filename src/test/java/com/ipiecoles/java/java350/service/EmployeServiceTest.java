package com.ipiecoles.java.java350.service;

import com.ipiecoles.java.java350.exception.EmployeException;
import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.model.NiveauEtude;
import com.ipiecoles.java.java350.model.Poste;
import com.ipiecoles.java.java350.repository.EmployeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;


import javax.persistence.EntityExistsException;
import java.time.LocalDate;


@ExtendWith(MockitoExtension.class)
public class EmployeServiceTest {

    @InjectMocks
    private EmployeService employeService;

    @Mock
    private EmployeRepository employeRepository;

    @Test
    public void testEmbauchePremierEmploye() throws EmployeException {

        //GIVEN
        String nom = "Doe";
        String prenom = "John";
        Poste poste = Poste.TECHNICIEN;
        NiveauEtude niveauEtude = NiveauEtude.BTS_IUT;
        Double tempsPartiel = 1.0;
        Mockito.when(employeRepository.findLastMatricule()).thenReturn(null);
        Mockito.when(employeRepository.findByMatricule("T00001")).thenReturn(null);
        //WHEN
        employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);
        //THEN
        ArgumentCaptor<Employe> employeArgumentCaptor = ArgumentCaptor.forClass(Employe.class);
        Mockito.verify(employeRepository).save(employeArgumentCaptor.capture());
        Employe employe = employeArgumentCaptor.getValue();
        Assertions.assertThat(employe).isNotNull();
        Assertions.assertThat(employe.getNom()).isEqualTo(nom);
        Assertions.assertThat(employe.getPrenom()).isEqualTo(prenom);
        Assertions.assertThat(employe.getSalaire()).isEqualTo(1825.464);
        Assertions.assertThat(employe.getTempsPartiel()).isEqualTo(1.0);
        Assertions.assertThat(employe.getDateEmbauche()).isEqualTo(LocalDate.now());
        Assertions.assertThat(employe.getMatricule()).isEqualTo("T00001");
    }

    @Test
    public void testEmbaucheLimiteMatricule() {
        //GIVEN
        String nom = "Doe";
        String prenom = "John";
        Poste poste = Poste.TECHNICIEN;
        NiveauEtude niveauEtude = NiveauEtude.BTS_IUT;
        Double tempsPartiel = 1.0;
        Mockito.when(employeRepository.findLastMatricule()).thenReturn("99999");
        //WHEN
        try {
            employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);
            Assertions.fail("embaucheEmploye aurait dû lancer une exception");
        } catch (EmployeException e) {
            //Then
            Assertions.assertThat(e.getMessage()).isEqualTo("Limite des 100000 matricules atteinte !");
        }
    }

    @Test
    public void testEmbaucheEmployeExisteDeja() throws EmployeException {

        //GIVEN
        String nom = "Doe";
        String prenom = "John";
        Poste poste = Poste.TECHNICIEN;
        NiveauEtude niveauEtude = NiveauEtude.BTS_IUT;
        Double tempsPartiel = 1.0;
        Employe employeExistant = new Employe("Doe", "Jane", "T00001", LocalDate.now(), 1500d, 1, 1.0);
        Mockito.when(employeRepository.findLastMatricule()).thenReturn(null);
        Mockito.when(employeRepository.findByMatricule("T00001")).thenReturn(employeExistant);
        //WHEN
        try {
            employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);
            Assertions.fail("embaucheEmploye aurait dû lancer une exception");
        } catch (Exception e){
            //THEN
            Assertions.assertThat(e).isInstanceOf(EntityExistsException.class);
            Assertions.assertThat(e.getMessage()).isEqualTo("L'employé de matricule T00001 existe déjà en BDD");
        }
    }

    //Test si le chiffre d'affaire traité est null
    @Test
    public void testCalculPerformanceCommercialeCaTraiteNull() {
        //GIVEN
        employeRepository.save(new Employe("Doe", "John", "C06432", LocalDate.now(), 1825.464, 1, 1.0));
        String matricule = "C06432";
        Long caTraite = null;
        Long objectifCa = 500L;
        //WHEN
        try {
            employeService.calculPerformanceCommercial(matricule,caTraite,objectifCa);
            Assertions.fail("calculPerformanceCommercial aurait dû lancer une exception");
        } catch (EmployeException e) {
            //THEN
            Assertions.assertThat(e.getMessage()).isEqualTo("Le chiffre d'affaire traité ne peut être négatif ou null !");
        }
    }

    //Test si le chiffre d'affaire traité est négatif
    @Test
    public void testCalculPerformanceCommercialeCaTraiteNegatif() {

        //GIVEN
        employeRepository.save(new Employe("Doe", "John", "C06432", LocalDate.now(), 1825.464, 1, 1.0));
        String matricule = "C06432";
        Long caTraite = -500L;
        Long objectifCa = 500L;

        //WHEN
        try {
            employeService.calculPerformanceCommercial(matricule,caTraite,objectifCa);
            Assertions.fail("calculPerformanceCommercial aurait dû lancer une exception");
        } catch (EmployeException e) {
            //THEN
            Assertions.assertThat(e.getMessage()).isEqualTo("Le chiffre d'affaire traité ne peut être négatif ou null !");
        }
    }

    //Test si l'objectif de chiffre d'affaire est null
    @Test
    public void testCalculPerformanceCommercialeObjectifCaNull() {
        //GIVEN
        employeRepository.save(new Employe("Doe", "John", "C06432", LocalDate.now(), 1825.464, 1, 1.0));
        String matricule = "C06432";
        Long caTraite = 500L;
        Long objectifCa = null;
        //WHEN
        try {
            employeService.calculPerformanceCommercial(matricule,caTraite,objectifCa);
            Assertions.fail("calculPerformanceCommercial aurait dû lancer une exception");
        } catch (EmployeException e) {
            //THEN
            Assertions.assertThat(e.getMessage()).isEqualTo("L'objectif de chiffre d'affaire ne peut être négatif ou null !");
        }
    }

    //Test si l'objectif de chiffre d'affaire est négatif
    @Test
    public void testCalculPerformanceCommercialeObjectifCaNegatif() {

        //GIVEN
        employeRepository.save(new Employe("Doe", "John", "C06432", LocalDate.now(), 1825.464, 1, 1.0));
        String matricule = "C06432";
        Long caTraite = 500L;
        Long objectifCa = -500L;

        //WHEN
        try {
            employeService.calculPerformanceCommercial(matricule,caTraite,objectifCa);
            Assertions.fail("calculPerformanceCommercial aurait dû lancer une exception");
        } catch (EmployeException e) {
            //THEN
            Assertions.assertThat(e.getMessage()).isEqualTo("L'objectif de chiffre d'affaire ne peut être négatif ou null !");
        }
    }



    //Test si le matricule en paramètre est null
    @Test
    public void testCalculPerformanceCommercialeMatriculeIsNull() {

        //GIVEN
        String matricule = "null";
        Long caTraite = 500L;
        Long objectifCa = 500L;

        //WHEN
        try {
            employeService.calculPerformanceCommercial(matricule,caTraite, objectifCa);
            Assertions.fail("La méthode calculPerformanceCommercial doit lancer une exception");
        } catch (EmployeException e) {
            //THEN
            Assertions.assertThat(e.getMessage()).isEqualTo("Le matricule ne peut être null et doit commencer par un C !");
        }
    }


    //Test si le matricule en paramètre ne commence pas par un C
    @Test
    public void testCalculPerformanceCommercialeMatriculeNoStartWithC() {

        //GIVEN
        String matricule = "T00001";
        Long caTraite = 500L;
        Long objectifCa = 500L;

        //WHEN
        try {
            employeService.calculPerformanceCommercial(matricule,caTraite, objectifCa);
            Assertions.fail("La méthode calculPerformanceCommercial doit lancer une exception");
        } catch (EmployeException e) {
            //THEN
            Assertions.assertThat(e.getMessage()).isEqualTo("Le matricule ne peut être null et doit commencer par un C !");
        }
    }

    //Test existence client avec le matricule déterminé
    @Test
    public void testCalculPerformanceCommercialeMatriculeDontExist() {
        //GIVE
        String matricule = "C35353";

        Long caTraite = 500L;
        Long objectifCa = 500L;

        //WHEN
        try {
            employeService.calculPerformanceCommercial(matricule,caTraite, objectifCa);
            Assertions.fail("La méthode calculPerformanceCommercial doit lancer une exception");
        } catch (EmployeException e) {
            //THEN
            Assertions.assertThat(e.getMessage()).isEqualTo("Le matricule C35353 n'existe pas !");
        }
    }


    //Test paramétré calcul performance commerciale
    @ParameterizedTest
    @CsvSource({
            "2, 600, 1000, 2", //Cas 1 / Autres
            "1, 1000, 1100, 1", //Cas 2
            "3, 1000, 1000, 4", //Cas 3
            "4, 720, 600, 6", //Cas 4
            "5, 10000, 600, 10", //Cas 5
    })
    void testCalculPerformanceCommerciale(Integer performanceInitiale, Long caTraite, Long objectifCa,  Integer performanceAttendue) throws EmployeException {

        //GIVEN
        Employe employe;
        String matricule  = "C06432";

        Mockito.when(employeRepository.findByMatricule(matricule)).thenReturn(
                employe = new Employe("Doe", "Joe", matricule, LocalDate.now(), 160d, performanceInitiale, 1.0)
        );

        //WHEN
        employeService.calculPerformanceCommercial(employe.getMatricule(),caTraite, objectifCa);
        Integer performanceRecupere = employe.getPerformance();

        //THEN
        Assertions.assertThat(performanceRecupere).isEqualTo(performanceAttendue);
    }

}
