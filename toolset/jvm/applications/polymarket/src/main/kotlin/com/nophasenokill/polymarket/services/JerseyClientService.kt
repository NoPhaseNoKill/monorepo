package com.nophasenokill.polymarket.services

import com.nophasenokill.polymarket.dao.JsonPlaceholderDAO
import com.nophasenokill.polymarket.domain.ExampleJsonPlaceholder
import org.springframework.stereotype.Service


@Service
class JerseyClientService(private val jsonPlaceholderDAO: JsonPlaceholderDAO) {
    fun fetchDataFromExternalAPI(): ExampleJsonPlaceholder {
        val posts = jsonPlaceholderDAO.getPosts()
        return posts
    }
}
