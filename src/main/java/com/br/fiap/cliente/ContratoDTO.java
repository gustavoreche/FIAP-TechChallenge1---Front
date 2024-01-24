package com.br.fiap.cliente;

public record ContratoDTO(
		String nome,
		String email,
		String cpf,
		String diaDataVencimento,
		String quantidadeDeParcelas,
		String valorDaParcela
) {}
