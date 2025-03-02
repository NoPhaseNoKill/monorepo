package com.nophasenokill.polymarket.controllers

import com.nophasenokill.polymarket.domain.ExampleJsonPlaceholder
import com.nophasenokill.polymarket.services.JerseyClientService
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import org.springframework.stereotype.Component


@Component
@Path("/posts")
class JerseyClientController(private val jerseyClientService: JerseyClientService) {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun fetchExternalData(): ExampleJsonPlaceholder {
        return jerseyClientService.fetchDataFromExternalAPI()
    }
}

