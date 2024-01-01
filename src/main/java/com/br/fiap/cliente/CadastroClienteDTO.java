package com.br.fiap.cliente;

public record CadastroClienteDTO (
		String nome,
		String telefone,
		String email,
		String ano,
		String modelo,
		String categoria
) {}
