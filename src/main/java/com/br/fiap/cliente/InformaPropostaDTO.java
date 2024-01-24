package com.br.fiap.cliente;

import java.math.BigDecimal;
import java.util.List;

public record InformaPropostaDTO(

		Long idAtendimento,
		BigDecimal valorDaProposta,
		List<ParcelasDTO> parcelas


) {}
