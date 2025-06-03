package com.ib.openai.demo.repository

import com.ib.openai.demo.service.JokeGPTService
import com.ib.openai.demo.util.Lang
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.annotation.Profile
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class JokeRefresher(
    private val jokeGPTService: JokeGPTService,
    private val jokeRepository: JokeRedisRepository,
) {

    private val logger = KotlinLogging.logger {}

    @Scheduled(cron = "0 0 0 * * *") // midnight every day
    fun refresh() {
        logger.info { "Delete all data in db" }
        jokeRepository.deleteAll()
        CoroutineScope(Dispatchers.IO).launch {
            Lang.entries.forEach { lang ->
                val jokes = jokeGPTService.generateJokes(lang.value)
                jokeGPTService.saveAllJokes(jokes, lang.value)
            }
        }
        logger.info { "All jokes successfully generated and saved in db" }
    }

    @Profile("prod")
    @EventListener(ApplicationReadyEvent::class)
    fun onStartupProd() {
        refresh()
    }

    @Profile("local")
    @EventListener(ApplicationReadyEvent::class)
    fun onStartupLocal() {
        val entities = jokeRepository.count()
        if (entities == 0L) {
            logger.info { "No jokes found in DB. Skipping deletion and regeneration." }
            refresh()
        }
    }
}
