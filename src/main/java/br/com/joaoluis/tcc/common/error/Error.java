package br.com.joaoluis.tcc.common.error;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class Error {
    private String message;
}
