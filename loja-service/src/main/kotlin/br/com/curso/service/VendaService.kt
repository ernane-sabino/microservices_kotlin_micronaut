package br.com.curso.service

import br.com.curso.dto.input.VendaInput
import br.com.curso.dto.output.Parcelas
import br.com.curso.dto.output.Venda
import br.com.curso.producer.VendaProducer
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.inject.Singleton
import java.time.LocalDate
import java.util.UUID

@Singleton
class VendaService(
    private val veiculoService: VeiculoService,
    private val vendaProducer: VendaProducer,
    private val objectMapper: ObjectMapper
) {

    fun realizarVenda(vendaInput: VendaInput): Venda {
        val veiculo = veiculoService.getVeiculo(vendaInput.veiculo)
        var parcelas: List<Parcelas> = ArrayList<Parcelas>()
        var valorParcela = vendaInput.valor.divide(vendaInput.quantidadeParcelas.toBigDecimal())
        var dataVencimento = LocalDate.now().plusMonths(1)

        for (i in 1..vendaInput.quantidadeParcelas) {
            var parcela = Parcelas(valorParcela, dataVencimento.toString())
            parcelas = parcelas.plus(parcela)
            dataVencimento = dataVencimento.plusMonths(1)
        }
        var venda = Venda(vendaInput.cliente, veiculo, vendaInput.valor, parcelas)
        println(venda)
        confirmarVenda(venda)
        return venda;
    }

    fun confirmarVenda(venda: Venda) {
        var vendaJSON = objectMapper.writeValueAsString(venda)
        vendaProducer.publicarVenda(UUID.randomUUID().toString(), vendaJSON)
    }
}