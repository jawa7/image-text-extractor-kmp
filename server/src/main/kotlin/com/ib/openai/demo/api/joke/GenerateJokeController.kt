package com.ib.openai.demo.api.joke

import com.ib.openai.demo.model.Joke
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api")
class GenerateJokeController(
    private val generateJokeService: GenerateJokeService
) {

    @GetMapping("/joke")
    fun getJoke(@RequestParam userId: UUID, @RequestParam lang: String?): ResponseEntity<Joke> {
        val joke = generateJokeService.getJoke(userId, lang)
        return if (joke != null) {
            ResponseEntity.ok(joke)
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
