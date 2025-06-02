package com.ib.openai.demo.service

import org.junit.jupiter.api.Assertions.*

import com.ib.openai.demo.model.Joke
import com.ib.openai.demo.model.JokeList
import com.ib.openai.demo.repository.JokeRedisRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.core.StringRedisTemplate
import org.testcontainers.containers.GenericContainer
import java.util.UUID
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
@SpringBootTest
@EnableRedisRepositories
class JokeGPTServiceIntegrationTest {

    companion object {

        @ServiceConnection
        @JvmStatic
        val redisContainer = GenericContainer<Nothing>("redis:6-alpine")
            .apply { withExposedPorts(6379) }
    }

    @Autowired
    private lateinit var jokeGPTService: JokeGPTService

    @Autowired
    private lateinit var jokeRedisRepository: JokeRedisRepository

    @Autowired
    private lateinit var redisTemplate: StringRedisTemplate

    @BeforeEach
    fun cleanDb() {
        redisTemplate.delete("joke:$defaultLang:all_ids")
        val keys = redisTemplate.keys("user:*:seen_jokes")
        if (!keys.isNullOrEmpty()) {
            redisTemplate.delete(keys)
        }
        jokeRedisRepository.deleteAll()
    }

    private val defaultLang = "en"

    @Test
    fun `test save and retrieve joke`() {
        // Create a joke object
        val jokeId = UUID.randomUUID().toString()
        val joke = Joke(id = jokeId, setup = "Why don't skeletons fight each other?", punchline = "They don't have the guts")

        // Save joke to repository
        jokeGPTService.saveJoke(joke, defaultLang)

        // Verify the joke is stored in Redis
        val savedJoke = jokeRedisRepository.findById(jokeId).orElse(null)
        assertNotNull(savedJoke)
        assertEquals(jokeId, savedJoke?.id)
        assertEquals(joke.punchline, savedJoke?.punchline)
    }

    @Test
    fun `test get next joke for user`() {
        val joke1 = Joke(id = UUID.randomUUID().toString(), setup = "Why don't skeletons fight each other?", punchline = "They don't have the guts")
        val joke2 = Joke(id = UUID.randomUUID().toString(), setup = "Why don't wizards fight each other?", punchline = "They don't have the magic")
        val userId = UUID.randomUUID()

        jokeGPTService.saveAllJokes(JokeList(listOf(joke1, joke2)), defaultLang)

        val allJokes = jokeGPTService.getAllJokeIds(defaultLang)
        assertTrue(allJokes.isNotEmpty())

        val joke = jokeGPTService.getNextJokeForUser(userId, defaultLang)
        assertNotNull(joke)
        assertTrue(allJokes.contains(joke?.id))
    }

    @Test
    fun `test get next joke for user without saved jokes`() {
        val userId = UUID.randomUUID()
        assertThrows<IllegalStateException> {
            jokeGPTService.getNextJokeForUser(userId, defaultLang)
        }
    }

    @Test
    fun `test seen joke id`() {
        val userId1 = UUID.randomUUID()
        val userId2 = UUID.randomUUID()

        val joke1 = Joke(id = userId1.toString(), setup = "Why don't skeletons fight each other?", punchline = "They don't have the guts")
        val joke2 = Joke(id = userId2.toString(), setup = "Why don't wizards fight each other?", punchline = "They don't have the magic")

        jokeGPTService.saveAllJokes(JokeList(listOf(joke1, joke2)), defaultLang)

        val seenJoke = jokeGPTService.getNextJokeForUser(userId1, defaultLang)

        val seenId = redisTemplate.opsForSet().members("user:$userId1:seen_jokes")?.first()

        assertEquals(seenJoke?.id.toString(), seenId)
    }

    @Test
    fun `test empty db`() {
        val userId1 = UUID.randomUUID()
        val userId2 = UUID.randomUUID()

        val joke1 = Joke(id = userId1.toString(), setup = "Why don't skeletons fight each other?", punchline = "They don't have the guts")
        val joke2 = Joke(id = userId2.toString(), setup = "Why don't wizards fight each other?", punchline = "They don't have the magic")

        jokeGPTService.saveAllJokes(JokeList(listOf(joke1, joke2)), defaultLang)

        jokeGPTService.getNextJokeForUser(userId1, defaultLang)

        cleanDb()

        assertEquals(emptySet<String>(), redisTemplate.opsForSet().members("user:$userId1:seen_jokes"))
        assertEquals(emptySet<String>(), redisTemplate.opsForSet().members("joke:$defaultLang:all_ids"))
        assertEquals(emptyList<Joke>(), jokeRedisRepository.findAll())
    }

    @Test
    fun `test different language`() {
        val userId1 = UUID.randomUUID()
        val userId2 = UUID.randomUUID()

        val joke1 = Joke(id = userId1.toString(), setup = "Why don't skeletons fight each other?", punchline = "They don't have the guts")
        val joke2 = Joke(id = userId2.toString(), setup = "Почему скелеты не сражаются против друг друга?", punchline = "Потому что им не хватает духу!")

        jokeGPTService.saveAllJokes(JokeList(listOf(joke1)), defaultLang)
        jokeGPTService.saveAllJokes(JokeList(listOf(joke2)), "ru")

        assertEquals(setOf(joke1.id), redisTemplate.opsForSet().members("joke:$defaultLang:all_ids"))
        assertEquals(setOf(joke2.id), redisTemplate.opsForSet().members("joke:ru:all_ids"))

        redisTemplate.delete("joke:$defaultLang:all_ids")

        assertEquals(emptySet<String>(), redisTemplate.opsForSet().members("joke:$defaultLang:all_ids"))
        assertEquals(setOf(joke2.id), redisTemplate.opsForSet().members("joke:ru:all_ids"))
    }
}