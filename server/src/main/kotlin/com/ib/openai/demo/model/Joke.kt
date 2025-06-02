package com.ib.openai.demo.model

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash

@RedisHash("Joke")
data class Joke(
    @Id
    val id: String,
    val setup: String,
    val punchline: String
)

data class JokeList(
    val jokes: List<Joke>
)