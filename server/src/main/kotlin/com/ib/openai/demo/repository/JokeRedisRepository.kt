package com.ib.openai.demo.repository

import com.ib.openai.demo.model.Joke
import org.springframework.data.repository.CrudRepository

interface JokeRedisRepository : CrudRepository<Joke, String>
