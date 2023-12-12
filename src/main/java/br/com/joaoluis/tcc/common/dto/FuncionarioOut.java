package br.com.joaoluis.tcc.common.dto;

import br.com.joaoluis.tcc.usuario.model.PapelUsuario;
import br.com.joaoluis.tcc.usuario.model.Usuario;

public record FuncionarioOut(
        Long id,
        String nome,
        String cpf,
        String email,
        String celular,
        Boolean inativo,
        PapelUsuario papel
) {
    public static FuncionarioOut of(Usuario usuario) {
        return new FuncionarioOut(usuario.getId(), usuario.getNome(), usuario.getCpf(), usuario.getEmail(),
                usuario.getCelular(), !usuario.getEnabled(), usuario.getPapel());
    }
}
