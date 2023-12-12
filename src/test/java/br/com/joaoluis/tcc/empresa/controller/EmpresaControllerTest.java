package br.com.joaoluis.tcc.empresa.controller;

import br.com.joaoluis.tcc.empresa.dto.EmpresaIn;
import br.com.joaoluis.tcc.empresa.dto.ResponsavelIn;
import br.com.joaoluis.tcc.empresa.model.RamoAtividade;
import br.com.joaoluis.tcc.empresa.model.TipoPessoa;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

//@Sql(value = "/db/migration/test/dados_weblic.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//@Sql(value = "/db/migration/test/pncp/uploadautorizacao/processo-weblic.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//@Sql(value = "/db/migration/test/limpa_dados_tabelas.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EmpresaControllerTest {

    @Value("${local.server.port}")
    protected int porta;

    private String token;

    @BeforeEach
    void setUp() {
        RestAssured.port = this.porta;
//        Response retorno = given().auth()
//                .basic("angular", "@ngul@r0")
//                .contentType("application/x-www-form-urlencoded")
//                .body("client=angular&grant_type=password&username=34264108934&password=weblic4231")
//                .post("/oauth/token");
//        this.token = retorno.getBody().jsonPath().getString("access_token");
    }

    @Test
    void whenCnpjMenorDoQue14CaracteresThenResultBadRequest() {
        EmpresaIn empresa = new EmpresaIn("Uma empresa", "124213423", TipoPessoa.PESSOA_JURIDICA,
                "48977653541", null, "email@email.com", RamoAtividade.CLINICA_MEDICA,
                new ResponsavelIn("Responsavel 1", "91501707086", "agenda1234"));
        given()
            .contentType("application/json")
            .body(empresa)
            .post("/empresas/novo-cadastro")
            .then()
//            .log().all().and()
            .statusCode(HttpStatus.BAD_REQUEST.value()).and()
            .assertThat()
            .body("message", hasItem("O CNPJ deve conter 14 caracteres"))
            .body("message", hasItem("CNPJ inválido. Por favor, informe corretamente"))
        ;
    }

    @Test
    void whenCnpjInvalidoThenResultBadRequest() {
        EmpresaIn empresa = new EmpresaIn("Uma empresa", "53684767000141", TipoPessoa.PESSOA_JURIDICA,
                "48977653541", null, "email@email.com", RamoAtividade.CLINICA_MEDICA,
                new ResponsavelIn("Responsavel 1", "91501707086", "agenda1234"));
        given()
                .contentType("application/json")
                .body(empresa)
                .post("/empresas/novo-cadastro")
                .then()
//                .log().all().and()
                .statusCode(HttpStatus.BAD_REQUEST.value()).and()
                .assertThat()
                .body("message[0]", equalTo("CNPJ inválido. Por favor, informe corretamente"))
        ;
    }

    @Test
    void whenEmailInvalidoThenResultBadRequest() {
        EmpresaIn empresa = new EmpresaIn("Uma empresa", "53684767000142", TipoPessoa.PESSOA_JURIDICA,
                "48977653541", null, "email@@email.com", RamoAtividade.CLINICA_MEDICA,
                new ResponsavelIn("Responsavel 1", "91501707086", "agenda1234"));
        given()
                .contentType("application/json")
                .body(empresa)
                .post("/empresas/novo-cadastro")
                .then()
//                .log().all().and()
                .statusCode(HttpStatus.BAD_REQUEST.value()).and()
                .assertThat()
                .body("message[0]", equalTo("O e-mail deve ser válido"))
        ;
    }

    @Test
    void whenNomeResponsavelInvalidoThenResultBadRequest() {
        EmpresaIn empresa = new EmpresaIn("Uma empresa", "53684767000142", TipoPessoa.PESSOA_JURIDICA,
                "48977653541", null, "email@email.com", RamoAtividade.CLINICA_MEDICA,
                new ResponsavelIn("    ", "91501707086", "agenda1234"));
        given()
                .contentType("application/json")
                .body(empresa)
                .post("/empresas/novo-cadastro")
                .then()
                .log().all().and()
                .statusCode(HttpStatus.BAD_REQUEST.value()).and()
                .assertThat()
                .body("message[0]", equalTo("O nome do responsável é obrigatório"))
        ;
    }

    @Test
    void whenCpfResponsavelInvalidoThenResultBadRequest() {
        EmpresaIn empresa = new EmpresaIn("Uma empresa", "53684767000142", TipoPessoa.PESSOA_JURIDICA,
                "48977653541", null, "email@email.com", RamoAtividade.CLINICA_MEDICA,
                new ResponsavelIn("  nome  ", "91501707081", "agenda1234"));
        given()
                .contentType("application/json")
                .body(empresa)
                .post("/empresas/novo-cadastro")
                .then()
                .log().all().and()
                .statusCode(HttpStatus.BAD_REQUEST.value()).and()
                .assertThat()
                .body("message[0]", equalTo("CPF do responsável é inválido. Por favor, informe corretamente"))
        ;
    }

    @Test
    void whenDadosValidoThenResultCreatedAndEmpresaId() {
        EmpresaIn empresa = new EmpresaIn("Uma empresa", "53684767000143", TipoPessoa.PESSOA_JURIDICA,
                "48977653541", null, "email@email.com", RamoAtividade.CLINICA_MEDICA,
                new ResponsavelIn("  nome  ", "91501707086", "agenda1234"));
        given()
                .contentType("application/json")
                .body(empresa)
                .post("/empresas/novo-cadastro")
                .then()
                .log().all().and()
                .statusCode(HttpStatus.CREATED.value()).and()
                .assertThat()
                .body("id", equalTo(3))
        ;
    }
}