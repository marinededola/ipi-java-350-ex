package com.ipiecoles.java.java350.repository;

import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.model.Entreprise;
import org.junit.jupiter.api.AfterEach;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;


    @SpringBootTest
    class EmployeRepositoryTest {

        @Autowired
        private EmployeRepository employeRepository;

        @BeforeEach
        @AfterEach
        public void setup(){
            employeRepository.deleteAll();
        }

        @Test
        public void testFindLastMatricule0Employe(){
            //Given
            //When
            String lastMatricule = employeRepository.findLastMatricule();
            //Then
            Assertions.assertThat(lastMatricule).isNull();
        }

        @Test
        public void testFindLastMatricule1Employe(){
            //Given
            //Insérer des données en base
            employeRepository.save(new Employe("Doe", "John", "T12345", LocalDate.now(), 1500d, 1, 1.0));
            //When
            //Exécuter des requêtes en base
            String lastMatricule = employeRepository.findLastMatricule();
            //Then
            Assertions.assertThat(lastMatricule).isEqualTo("12345");
        }


        @Test
        public void testFindLastMatriculeMultiple(){
            //Given
            employeRepository.save(new Employe("Doe", "John", "T12545", LocalDate.now(), Entreprise.SALAIRE_BASE, 1, 1.0));
            employeRepository.save(new Employe("Doe", "Jane", "M40678", LocalDate.now(), Entreprise.SALAIRE_BASE, 1, 1.0));
            employeRepository.save(new Employe("Doe", "Jim", "C06932", LocalDate.now(), Entreprise.SALAIRE_BASE, 1, 1.0));

            //When
            String lastMatricule = employeRepository.findLastMatricule();

            //Then
            Assertions.assertThat("40678").isEqualTo(lastMatricule);
        }
    }

