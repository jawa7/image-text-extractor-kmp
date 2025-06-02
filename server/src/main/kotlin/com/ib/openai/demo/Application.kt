package com.ib.openai.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@EnableConfigurationProperties
@ConfigurationPropertiesScan
@SpringBootApplication
class Application

fun main(args: Array<String>) {
	runApplication<Application>(*args)
}

object Dev {
	@JvmStatic
	fun main(args: Array<String>) {
		System.setProperty("spring.profiles.active", "dev,local")
		runApplication<Application>(*args)
	}
}
