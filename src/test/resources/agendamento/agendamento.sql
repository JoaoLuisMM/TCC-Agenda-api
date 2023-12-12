INSERT INTO public.empresa(id, celular, cpf_cnpj, email, nome, telefone, tipo_pessoa, ramo_atividade)
VALUES
(1, '48999956822', '1122233300011', 'email@clinicamedica.com.br', 'Clínica Médica', '48999999999', 'PESSOA_JURIDICA', 'CLINICA_MEDICA');
ALTER SEQUENCE empresa_id_seq RESTART WITH 2;

INSERT INTO public.usuario(id, account_expired, account_locked, cpf, email, enabled, nome, papel, password, password_expired, empresa_id)
VALUES
(1, false, false, '89207495074', 'email@email.com', true, 'Usuario1', 'ROLE_ADMINISTRADOR', '$2a$10$kEIp39D6e9Ok.ueLHMkxLOisQ/GP0g9nL1AL.as2a9cyHUyadGDa2', false, 1);
ALTER SEQUENCE usuario_id_seq RESTART WITH 2;

INSERT INTO public.cliente(id, empresa_id, nome, cpf, email, data_cadastro, st_inativo)
VALUES
(1, 1, 'Cliente clinica médica', '22233344455', 'cliente@email.com.br', '2023-10-09 12:00:00', false);

INSERT INTO public.especialista(
    id, empresa_id, cpf, email, celular, st_inativo, nome
)
VALUES(1, 1, '11122233344', 'email@email.com', '4834343343', false, 'Médico1');

INSERT INTO public.medico(id, crm, data_cadastro)
VALUES
(1, '1234', '2023-10-09 12:00:00');
ALTER SEQUENCE especie_cliente_id_seq RESTART WITH 2;

INSERT INTO public.agendamento(id, empresa_id, especialista_id, cliente_id, data_inicial, data_final, situacao, observacao)
VALUES
(1, 1, 1, 1, CURRENT_TIMESTAMP(), null, 'ATIVO', null);
ALTER SEQUENCE agendamento_id_seq RESTART WITH 2;
