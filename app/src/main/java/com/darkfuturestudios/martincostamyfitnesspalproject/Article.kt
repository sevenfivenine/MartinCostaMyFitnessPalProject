package com.darkfuturestudios.martincostamyfitnesspalproject

import java.util.*

class Article(val headline: String, val thumbnailUrl: String?) {

    private val id: UUID

    init {
        id = UUID.randomUUID()
    }


}