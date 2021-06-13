package com.ss

import io.ktor.application.*
import io.ktor.html.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.coroutines.delay
import kotlinx.html.body
import kotlinx.html.h1
import kotlinx.html.li
import kotlinx.html.ul
import java.io.File
import java.nio.charset.Charset

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    routing {
        get("/") {
            delay(5000)
            call.respondText(Character().toString(), contentType = ContentType.Text.Plain)
        }

        get("/monsters") {
            val monster = getJsonFromFile("data/Monsters.json")
            call.respondText(monster, ContentType.Text.JavaScript)
        }

        get("/characters") {
            call.respondHtml {
                body {
                    h1 { +"Characters" }
                    ul { for (n in 1..10) { li { +"Character $n: ${Character()}" } } }
                }
            }
        }
    }
}

fun getJsonFromFile(dataFile: String): String {
    val file = File(dataFile)
    return file.readText(Charset.defaultCharset())
}


