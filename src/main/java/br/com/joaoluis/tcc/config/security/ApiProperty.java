package br.com.joaoluis.tcc.config.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "agendamento")
public class ApiProperty {

    private final Seguranca seguranca = new Seguranca();

    public Seguranca getSeguranca() {
        return seguranca;
    }

    @Setter
    @Getter
    public static class Seguranca {

        private String originPermitida = "http://127.0.0.1:4200";
        private List<String> redirectsPermitidos;
        private String authServerUrl;
    }

}
