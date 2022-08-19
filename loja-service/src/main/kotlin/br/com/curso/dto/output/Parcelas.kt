package br.com.curso.dto.output

import java.math.BigDecimal

data class Parcelas(
    val valor: BigDecimal,
    val dataVencimento: String
)
