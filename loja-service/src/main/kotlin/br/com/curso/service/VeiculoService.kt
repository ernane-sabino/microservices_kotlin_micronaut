package br.com.curso.service

import br.com.curso.dto.output.Veiculo
import br.com.curso.http.VeiculoHttp
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.inject.Singleton
import redis.clients.jedis.JedisPool
import redis.clients.jedis.JedisPoolConfig

@Singleton
class VeiculoService(
    private val veiculoHttp: VeiculoHttp,
    private val objectMapper: ObjectMapper
) {

    fun getVeiculo(id: Int): Veiculo {
        return veiculoHttp.findById(id)
    }

    fun gravarCache(veiculo: Veiculo) {
        val jedisPool = JedisPool(JedisPoolConfig(), "127.0.0.1", 6379)
        var jedis = jedisPool.resource
        var veiculoJSON = objectMapper.writeValueAsString(veiculo)
        jedis.set(veiculo.id.toString(), veiculoJSON)
    }
}