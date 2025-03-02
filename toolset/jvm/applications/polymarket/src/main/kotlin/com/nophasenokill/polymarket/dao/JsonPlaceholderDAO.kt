package com.nophasenokill.polymarket.dao

import com.nophasenokill.polymarket.domain.ExampleJsonPlaceholder
import jakarta.ws.rs.client.Client
import jakarta.ws.rs.core.MediaType
import org.springframework.stereotype.Service

@Service
class JsonPlaceholderDAO(
    private val client: Client
) {
    fun getPosts(): ExampleJsonPlaceholder {
        val api = "https://jsonplaceholder.typicode.com/posts/1"
        val response = client
            .target(api)
            .request(MediaType.APPLICATION_JSON)
            .get()

        if(response.status == 200) {
            return response.readEntity(ExampleJsonPlaceholder::class.java)
        } else {
            throw RuntimeException("Failed: HTTP error code: " + response.status)
        }
    }
}
