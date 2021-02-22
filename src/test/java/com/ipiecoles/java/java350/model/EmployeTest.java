package com.ipiecoles.java.java350.model;

import com.ipiecoles.java.java350.model.Employe;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;

public class EmployeTest {

    @Test
    public void testGetAnneeAcienneteDateEmbaucheIsNull(){

        //GIVEN
        Employe employe = new Employe();
        employe.setDateEmbauche(null);
        //WHEN
        Integer anneeAnciennete = employe.getNombreAnneeAnciennete();
        //THEN
        Assertions.assertThat(anneeAnciennete).isNull();
    }

    @Test
    public void testGetAnneeAcienneteDateEmbaucheInfNow(){

        //GIVEN
        Employe employe = new Employe("Doe","Jonh","T12345", LocalDate.now().minusYears(6),1500d,1,1.0);
        //WHEN
        Integer anneeAnciennete = employe.getNombreAnneeAnciennete();
        //THEN
        Assertions.assertThat(anneeAnciennete).isGreaterThanOrEqualTo(6);
    }

    @Test
    public void testGetAnneeAcienneteDateEmbaucheSupNow(){

        //GIVEN
        Employe employe = new Employe("Doe","Jonh","T12345", LocalDate.now().plusYears(6),1500d,1,1.0);
        //WHEN
        Integer anneeAnciennete = employe.getNombreAnneeAnciennete();
        //THEN
        Assertions.assertThat(anneeAnciennete).isNull();
    }

    @Test
    public void testGetPrimeAnnuelleMatriculeNull(){

        //GIVEN
        Employe employe = new Employe("Doe","John",null,LocalDate.now(),1500d,1,1.0);
        //WHEN
        Double prime = employe.getPrimeAnnuelle();
        //THEN
        Assertions.assertThat(prime).isEqualTo(1000.0);
    }


    @ParameterizedTest(name = "Perf{0}, matricule {1} txActivite{2}, anciennete {3} => prime {4} ")
    @CsvSource({"1, 'T12345', 1.0, 0, 1000.0",
                "1, 'T12345', 0.5, 0, 500.0",
                "2, 'T12345', 1.0, 0, 2300.0",
                "1, 'T12345', 1.0, 2, 1200.0",
                "1,'M12345',1.0,0,1700.0",
                "1,'M12345',1.0,3,2000.0"})
    public void testGetPrimeAnnuelle(Integer perforance,String matricule,Double tauxActivite,Long nbAnneesAnciennete,Double primeAttendue){

        //GIVEN
        Employe employe = new Employe("Doe","Jonh",matricule, LocalDate.now().minusYears(nbAnneesAnciennete),1500d,perforance,tauxActivite);
        //WHEN
        Double prime = employe.getPrimeAnnuelle();
        //THEN
        Assertions.assertThat(prime).isEqualTo(primeAttendue);
    }

    //Test si le salaire n'est pas null
    @Test
    public void testSalaireIsNull(){

        //GIVEN
        Employe employe = new Employe();
        employe.setSalaire(null);
        //WHEN
        //THEN
        Assertions.assertThat(employe.getSalaire()).isNull();
    }


    // Salaire augmenté par rapport au salaire de base
    @Test
    public void testSalaireSupSalaireBase(){

        //GIVEN
        Employe employe = new Employe("Doe","Jonh","T12345", LocalDate.now().minusYears(6),1600d,1,1.0);
        //WHEN
        //THEN
        Assertions.assertThat( employe.getSalaire()).isGreaterThanOrEqualTo(Entreprise.SALAIRE_BASE);
    }


    //Test augmentation du salaire
    @Test
    public void testAugmenterSalairePourcentage0(){

        //GIVEN
        Employe employe = new Employe("Doe","Jonh","T12345", LocalDate.now().minusYears(6),1500d,1,1.0);
        //WHEN
        employe.augmenterSalaire(10);
        Double salaireAugmente = employe.getSalaire();
        //THEN
        Assertions.assertThat(salaireAugmente).isEqualTo(1650d);
    }


    //Test si le salaire augmenté est supérieur au salaire initial
    //Vérifie indirectement que le pourcentage en paramètre est positif
    @Test
    public void testSalaireIsAugmente(){

        //GIVEN
        Employe employe = new Employe("Doe","Jonh","T12345", LocalDate.now().minusYears(6),1500d,1,1.0);
        //WHEN
        employe.augmenterSalaire(5);
        Double salaireAugmente = employe.getSalaire();
        //THEN
        Assertions.assertThat(salaireAugmente).isGreaterThanOrEqualTo(1500d);
    }


    //Test paramétré CSV augmenterSalaire
    @ParameterizedTest(name = "pourcentage{0}, salaire {1}, salaireFinal{2}")
    @CsvSource({"10,'160d',176d",
            "-1,'160d',160d",
            "0.5,'1600',1608",
            "25,'160d',200d"})

    public void testAugmenterSalaireManyValue(Double pourcentage,Double salaire,Double salaireFinal){
        //GIVEN
        Employe employe = new Employe("Doe","John",null,LocalDate.now(),salaire,1,1.0);
        //WHEN
        employe.augmenterSalaire(pourcentage);
        Double salaireAugmente = employe.getSalaire();
        //THEN
        Assertions.assertThat(salaireFinal).isEqualTo(salaireAugmente);
    }


    //Test paramétré CSV nombre RTT
    @ParameterizedTest(name = "dateReference{0}, rtt{1}")
    @CsvSource({"2019-01-01,8",
            "2021-01-01,10",
            "2022-01-01,10",
            "2032-01-01,11",
            "2012-01-01,12"})
    public void testGetNbrRtt(LocalDate dateReference, int rtt){
        //GIVEN
        Employe employe = new Employe("Doe","John",null,LocalDate.now(),1500d,1,1.0);
        //WHEN
        int nbRtt = employe.getNbRtt(dateReference);
        //THEN
        Assertions.assertThat(nbRtt).isEqualTo(rtt);

    }

}
