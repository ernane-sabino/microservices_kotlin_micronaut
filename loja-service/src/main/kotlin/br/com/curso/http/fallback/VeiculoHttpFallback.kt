package br.com.curso.http.fallback

import br.com.curso.dto.output.Veiculo
import br.com.curso.http.VeiculoHttp
import io.micronaut.retry.annotation.Fallback

@Fallback
class VeiculoHttpFallback: VeiculoHttp {
    override fun findById(id: Int): Veiculo {
        print("Chamou fallback")
        return Veiculo(3, "Celta", "GM", "AHA3216")
    }
}