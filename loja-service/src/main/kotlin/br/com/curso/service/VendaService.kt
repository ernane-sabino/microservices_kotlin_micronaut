package br.com.curso.service

import br.com.curso.dto.input.VendaInput
import br.com.curso.http.VeiculoHttp
import jakarta.inject.Singleton

@Singleton
class VendaService(
    private val veiculoHttp: VeiculoHttp
) {

    fun realizarVenda(vendaInput: VendaInput) {
        val veiculo = veiculoHttp.findById(vendaInput.veiculo)
        println(veiculo)
    }
}