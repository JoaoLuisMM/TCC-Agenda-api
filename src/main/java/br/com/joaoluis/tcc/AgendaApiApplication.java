package br.com.joaoluis.tcc;

import br.com.joaoluis.tcc.common.service.TesteService;
import br.com.joaoluis.tcc.config.security.ApiProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(ApiProperty.class)
@SpringBootApplication
public class AgendaApiApplication implements CommandLineRunner {

	@Autowired
	private TesteService testeService;

	public static void main(String[] args) {
		SpringApplication.run(AgendaApiApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		this.testeService.initiateData();
	}
}
























