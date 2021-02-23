package com.ipiecoles.java.java350.repository;
import com.ipiecoles.java.java350.service.EmployeService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class EmployeRepositoryTest {

        @InjectMocks
        private EmployeService employeService;

        @Mock
        private EmployeRepository employeRepository;

}