package com.ipiecoles.java.java350.repository;
import com.ipiecoles.java.java350.Java350Application;
import com.ipiecoles.java.java350.model.Employe;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

@SpringBootTest
class EmployeRepositoryTest {

    @Autowired
    EmployeRepository employeRepository;

    @Test
    public void testFindLastMatricule0Employe(){

        //GIVEN
        //WHEN
        String lastMatricule = employeRepository.findLastMatricule();
        //THEN
        Assertions.assertThat(lastMatricule).isNull();
    }

    @Test
    public void testFindLastMatricule1Employe() {

        //GIVEN
        employeRepository.save(new Employe("Doe", "John", "T12345", LocalDate.now(), 1500d, 1, 1.0));
        //WHEN
        String lastMatricule = employeRepository.findLastMatricule();
        //THEN
        Assertions.assertThat(lastMatricule).isEqualTo("12345");
    }

    @Test
    public void testFindLastMatriculeNEmploye() {
        //GIVEN
        employeRepository.save(new Employe("Doe", "John", "T12345", LocalDate.now(), 1500d, 1, 1.0));
        employeRepository.save(new Employe("Doe", "John", "M40325", LocalDate.now(), 1500d, 1, 1.0));
        employeRepository.save(new Employe("Doe", "John", "C06432", LocalDate.now(), 1500d, 1, 1.0));
        //WHEN
        String lastMatricule = employeRepository.findLastMatricule();
        //THEN
        Assertions.assertThat(lastMatricule).isEqualTo("40325");
    }


    //Test intégré AvgPerformanceWhereMatriculeStartsWith
    @Test
    void testAvgPerformanceWhereMatriculeStartsWith(){

        //GIVEN
        employeRepository.save(new Employe("Doe", "John", "C12345", LocalDate.now(), 1500d, 1, 1.0));
        //WHEN
        Double avgPerf = employeRepository.avgPerformanceWhereMatriculeStartsWith("C");
        //THEN
        Assertions.assertThat(avgPerf).isEqualTo(1);
    }

    @BeforeEach
    @AfterEach
    public void purgeBDD(){
        employeRepository.deleteAll();
    }

}