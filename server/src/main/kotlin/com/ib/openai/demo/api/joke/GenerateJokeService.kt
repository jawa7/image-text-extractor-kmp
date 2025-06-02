package com.ib.openai.demo.api.joke

import com.ib.openai.demo.model.Joke
import java.util.UUID

interface GenerateJokeService {

    fun getJoke(userId: UUID, lang: String?): Joke?
}
