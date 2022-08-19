package br.com.curso.http

import br.com.curso.dto.output.Veiculo
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.client.annotation.Client

@Client(id = "http://localhost:8080")
interface VeiculoHttp {
    @Get("/veiculos/{id}")
    fun findById(@PathVariable id: Int): Veiculo
}