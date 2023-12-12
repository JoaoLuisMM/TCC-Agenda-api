package br.com.joaoluis.tcc.agendamento.service;

import br.com.joaoluis.tcc.agendamento.dto.AgendamentoIn;
import br.com.joaoluis.tcc.agendamento.dto.ClienteId;
import br.com.joaoluis.tcc.agendamento.dto.EspecialistaId;
import br.com.joaoluis.tcc.common.dto.EntityId;
import br.com.joaoluis.tcc.common.exception.ValidationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

@Sql(value = "/agendamento/agendamento.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@SpringBootTest
class AgendamentoServiceTest {

    @Autowired
    private AgendamentoService service;

    @WithUserDetails("89207495074")
    @Test
    void tentaSalvarAgendamentoComDataInicioNoPassado() {
        LocalDateTime dataAtual = LocalDateTime.now().withNano(0).minusMinutes(1);
        AgendamentoIn agendamento = AgendamentoIn.builder()
                .cliente(ClienteId.builder().id(1L).build())
                .especialista(EspecialistaId.builder().id(1L).build())
                .dataInicial(dataAtual)
                .dataFinal(dataAtual.plusMinutes(20))
                .build();
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            this.service.save(agendamento);
        });
        assertEquals(1, exception.getErrors().size());
        String expectedMessage = "A data de início deve ser maior do que a data atual";
        String actualMessage = exception.getErrors().get(0);
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @WithUserDetails("89207495074")
    @Test
    void tentaSalvarAgendamentoComDataEmConflito() {
        LocalDateTime dataAtual = LocalDateTime.now().withNano(0).minusMinutes(1);
        AgendamentoIn agendamento = AgendamentoIn.builder()
                .cliente(ClienteId.builder().id(1L).build())
                .especialista(EspecialistaId.builder().id(1L).build())
                .dataInicial(dataAtual)
                .dataFinal(dataAtual.plusMinutes(20))
                .build();
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            this.service.save(agendamento);
        });
        assertEquals(1, exception.getErrors().size());
        String expectedMessage = "A data de início deve ser maior do que a data atual";
        String actualMessage = exception.getErrors().get(0);
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @WithUserDetails("89207495074")
    @Test
    void tentaSalvarAgendamentoComDataAndMedicoEmConflito() {
        LocalDateTime dataAtual = LocalDateTime.now().withNano(0).plusMinutes(10);
        AgendamentoIn agendamento = AgendamentoIn.builder()
                .cliente(ClienteId.builder().id(1L).build())
                .especialista(EspecialistaId.builder().id(1L).build())
                .dataInicial(dataAtual)
                .dataFinal(dataAtual.plusMinutes(20))
                .build();
        this.service.save(agendamento);
        dataAtual = dataAtual.plusMinutes(2);
        AgendamentoIn agendamento1 = AgendamentoIn.builder()
                .cliente(ClienteId.builder().id(1L).build())
                .especialista(EspecialistaId.builder().id(1L).build())
                .dataInicial(dataAtual)
                .dataFinal(dataAtual.plusMinutes(22))
                .build();
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            final EntityId save = this.service.save(agendamento1);
        });

        assertEquals(1, exception.getErrors().size());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String expectedMessage = String.format("Já existe um agendamento para a data de início %s", dataAtual.format(formatter));
//        exception.getErrors().forEach(System.out::println);
//        System.out.println("esperado: " + expectedMessage);
        String actualMessage = exception.getErrors().get(0);
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @WithUserDetails("89207495074")
    @Test
    void salvaAgendamento() {
        LocalDateTime dataAtual = LocalDateTime.now().withNano(0).plusMinutes(10);
        AgendamentoIn agendamento = AgendamentoIn.builder()
                .cliente(ClienteId.builder().id(1L).build())
                .especialista(EspecialistaId.builder().id(1L).build())
                .dataInicial(dataAtual)
                .dataFinal(dataAtual.plusMinutes(20))
                .build();
        final EntityId save = this.service.save(agendamento);
        assertTrue(save.id() > 1);
    }
}