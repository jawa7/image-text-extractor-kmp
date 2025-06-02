package com.ib.openai.demo.service

import com.ib.openai.demo.model.Joke
import com.ib.openai.demo.model.JokeList
import com.ib.openai.demo.repository.JokeRedisRepository
import com.ib.openai.demo.util.Lang
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor
import org.springframework.ai.chat.client.entity
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class JokeGPTService(
    private val jokeRedisRepository: JokeRedisRepository,
    private val redisTemplate: StringRedisTemplate,
    private val chatClient: ChatClient,
) {

    private val logger = KotlinLogging.logger {}

    private fun getAllJokeIdsKey(lang: String): String {
        return "joke:$lang:all_ids"
    }

    fun saveJoke(joke: Joke, lang: String) {
        jokeRedisRepository.save(joke)
        redisTemplate.opsForSet().add(getAllJokeIdsKey(lang), joke.id)
    }

    fun saveAllJokes(jokes: JokeList, lang: String) {
        jokeRedisRepository.saveAll(jokes.jokes)
        val ops = redisTemplate.opsForSet()
        jokes.jokes.forEach { ops.add(getAllJokeIdsKey(lang), it.id) }
    }

    fun getAllJokeIds(lang: String): Set<String> = redisTemplate.opsForSet().members(getAllJokeIdsKey(lang)) ?: emptySet()

    fun getAllJokes(): JokeList = JokeList(jokes = jokeRedisRepository.findAll().map { it })

    fun getNextJokeForUser(userId: UUID, lang: String): Joke? {
        val ops = redisTemplate.opsForSet()

        val allIds = getAllJokeIds(lang).ifEmpty { throw IllegalStateException("No saved jokes") }
        val seenIds = ops.members("user:$userId:seen_jokes") ?: emptySet()

        val unseenIds = allIds.subtract(seenIds)

        if (unseenIds.isEmpty()) return null

        val selectedId = unseenIds.random()

        val joke = jokeRedisRepository.findById(selectedId).orElseThrow()

        ops.add("user:$userId:seen_jokes", selectedId)

        return joke
    }

    fun generateJokes(lang: String): JokeList {
        val prompt = runCatching {
            Lang.entries.find { it.value == lang } ?: Lang.EN
        }.getOrElse {
            logger.error(it) { "Error while extracting language" }
            Lang.EN
        }.prompt
        return chatClient
            .prompt()
            .advisors(SimpleLoggerAdvisor())
            .user(prompt)
            .call()
            .entity<JokeList>()
    }
}