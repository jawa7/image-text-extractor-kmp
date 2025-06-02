package com.ib.openai.demo.util

enum class Lang(
    val value: String,
    val prompt: String
) {
    RU("ru", "Напиши 50 шуток"),
    EN("en", "Tell me 50 jokes"),
    CZ("cz", "Napiš 50 vtipů"),
    KZ("kz", "50 әзіл жазыңыз")
}