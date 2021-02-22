package com.ipiecoles.java.java350.service;

import com.ipiecoles.java.java350.exception.EmployeException;
import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.model.NiveauEtude;
import com.ipiecoles.java.java350.model.Poste;
import com.ipiecoles.java.java350.repository.EmployeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
/*
    @Test
    public void testEmbauchePremierEmploye() throws EmployeException {

        //GIVEN
        String nom = "Doe";
        String prenom = "John";
        Poste poste = Poste.TECHNICIEN;
        NiveauEtude niveauEtude = NiveauEtude.BTS_IUT;
        Double tempsPartiel = 1.0;
        //WHEN
        employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);
        //THEN
        Employe employe = employeRepository.findByMatricule("T00001");
        Assertions.assertThat(employe).isNotNull();
        Assertions.assertThat(employe.getNom()).isEqualTo(nom);
        Assertions.assertThat(employe.getPrenom()).isEqualTo(prenom);
        Assertions.assertThat(employe.getSalaire()).isEqualTo(1825.464);
        Assertions.assertThat(employe.getTempsPartiel()).isEqualTo(1.0);
        Assertions.assertThat(employe.getDateEmbauche()).isEqualTo(LocalDate.now());
        Assertions.assertThat(employe.getMatricule()).isEqualTo("T00001");
    }
 */


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

    @Test
    public void testCalculPerformanceCommercialeMatriculeDontExist() {
        //given
        String matricule = "C35353";

        Long caTraite = 500L;
        Long objectifCa = 500L;

        //when
        try {
            employeService.calculPerformanceCommercial(matricule,caTraite, objectifCa);
            Assertions.fail("La méthode calculPerformanceCommercial doit lancer une exception");
        } catch (EmployeException e) {
            //Then
            Assertions.assertThat(e.getMessage()).isEqualTo("Le matricule C35353 n'existe pas !");
        }
    }

}
