package com.ipiecoles.java.java350.service;

import com.ipiecoles.java.java350.exception.EmployeException;
import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.model.NiveauEtude;
import com.ipiecoles.java.java350.model.Poste;
import com.ipiecoles.java.java350.repository.EmployeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
 class EmployeServiceIntegrationTest {

    @Autowired
    private EmployeService employeService;

    @Autowired
    private EmployeRepository employeRepository;


    @Test
     void testEmbauchePremierEmploye() throws EmployeException {

        //GIVEN
        String nom = "Doe";
        String prenom = "John";
        Poste poste = Poste.TECHNICIEN;
        NiveauEtude niveauEtude = NiveauEtude.BTS_IUT;
        Double tempsPartiel = 1.0;
        //WHEN
        Employe employe = new Employe(nom, prenom, "T00001", LocalDate.now(), 1825.464, 3, 1.0);
        employeRepository.save(employe);

        employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);

        //THEN
        Employe employeFind = employeRepository.findByMatricule("T00001");
        Assertions.assertThat(employeFind).isNotNull();
        Assertions.assertThat(employeFind.getNom()).isEqualTo(nom);
        Assertions.assertThat(employeFind.getPrenom()).isEqualTo(prenom);
        Assertions.assertThat(employeFind.getSalaire()).isEqualTo(1825.464);
        Assertions.assertThat(employeFind.getTempsPartiel()).isEqualTo(1.0);
        Assertions.assertThat(employeFind.getDateEmbauche()).isEqualTo(LocalDate.now());
        Assertions.assertThat(employeFind.getMatricule()).isEqualTo("T00001");
    }

    //Test intégré calcul performance commerciale
    @Test
     void testCalculPerformanceCommerciale() throws EmployeException {

        //GIVEN
        String nom = "Doe";
        String prenom = "John";
        String matricule = "C35353";
        Long caTraite = 1000L;
        Long objectifCa = 1000L;
        Employe employe = new Employe(nom, prenom, matricule, LocalDate.now(), 1500d, 3, 1.0);
        employeRepository.save(employe);
        //WHEN
        employeService.calculPerformanceCommercial(employe.getMatricule(), caTraite, objectifCa);
        //THEN
        Employe employeFinal = employeRepository.findByMatricule(employe.getMatricule());
        Assertions.assertThat(employeFinal.getPerformance()).isEqualTo(3);
    }


}