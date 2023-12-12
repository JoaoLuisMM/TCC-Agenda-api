package br.com.joaoluis.tcc.seguranca.model;

import br.com.joaoluis.tcc.empresa.model.Empresa;
import br.com.joaoluis.tcc.empresa.model.RamoAtividade;

public record EmpresaResumo(Long id, String nome, String cnpjCpf, RamoAtividade ramoAtividade){
    public EmpresaResumo(Empresa empresa) {
        this(empresa.getId(), empresa.getNome(), empresa.getCnpjCpf(), empresa.getRamoAtividade());
    }
}
