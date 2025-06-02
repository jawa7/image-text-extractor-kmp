package com.ib.openai.demo.api.joke

import com.ib.openai.demo.model.Joke
import com.ib.openai.demo.service.JokeGPTService
import com.ib.openai.demo.util.Lang
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class GenerateJokeServiceImpl(
    private val jokeGPTService: JokeGPTService,
): GenerateJokeService {

    private val logger = KotlinLogging.logger {}

    override fun getJoke(userId: UUID, lang: String?): Joke? {
        logger.info { "Start to getting joke for userId=$userId" }
        val jokeLang = lang ?: Lang.EN.value
        val jokeIds = jokeGPTService.getAllJokeIds(jokeLang)
        if (jokeIds.isEmpty()) {
            logger.info { "No jokes yet for $lang, start generate them" }
            val jokes = jokeGPTService.generateJokes(jokeLang)
            jokeGPTService.saveAllJokes(jokes, jokeLang)
            logger.info { "All jokes for $lang successfully saved" }
        }

        return jokeGPTService.getNextJokeForUser(userId, jokeLang)
    }
}
