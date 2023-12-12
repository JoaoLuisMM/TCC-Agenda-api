package br.com.joaoluis.tcc.empresa.controller;

import br.com.joaoluis.tcc.common.exception.ResourceNotFoundException;
import br.com.joaoluis.tcc.common.service.EmpresaService;
import br.com.joaoluis.tcc.empresa.dto.EmpresaId;
import br.com.joaoluis.tcc.empresa.dto.EmpresaIn;
import br.com.joaoluis.tcc.empresa.model.Empresa;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/empresas")
public class EmpresaController {

    private final EmpresaService service;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/novo-cadastro")
    public EmpresaId save(@RequestBody @Valid EmpresaIn empresa) {
        /*
        {
            "responsavel":{
                "nome":"Gilberto",
                "cpf":"81215258968",
                "senha":"agenda4321"
            },
            "tipoPessoa":"PESSOA_FISICA",
            "nome":"Empresa 3",
            "cnpjCpf":"81215258968",
            "celular":"48988534360",
            "telefone":"",
            "email":"gr.martins@gmail.com",
            "ramoAtividade":"CLINICA_VETERINARIA"
        }
        */
        return new EmpresaId(this.service.save(empresa));
    }

    @GetMapping("/{id}")
    public Empresa findById(@PathVariable("id") long id) {
        return this.service.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa não cadastrada"));
    }
}
