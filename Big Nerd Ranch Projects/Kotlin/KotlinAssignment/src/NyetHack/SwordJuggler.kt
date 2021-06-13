package NyetHack

import java.lang.IllegalStateException
import kotlin.random.Random

fun main() {
    var swordJuggling: Int? = null
    val isJugglingProficient =  Random.nextInt(3) == 0

    val player = Player("Madrigal")

    if(isJugglingProficient){
        swordJuggling = 2
    }
    try {
        proficiencyCheck(swordJuggling)
        swordJuggling = swordJuggling!!.plus(1)
    }catch (e: Exception){
        println(e)
    }
    println("You Juggle $swordJuggling Swords")

    //Challenge 1:
    require(player.healthPoints in 0..100){
        println("The Health isn't Within the Range")
    }

    //Challenge 2 & 3:
    checkHealthPoints(player.healthPoints)
}

fun checkHealthPoints(healthPoints: Int?){
    require(healthPoints in 0..100){
        throw IllegalStateException("The Health isn't Within the Range")
    }
}

fun proficiencyCheck(swordJuggling: Int?) {
    checkNotNull(swordJuggling){"NyetHack.Player Cannot Juggle Swords"}
}

class UnskilledSwordJugglerException():
        IllegalStateException("NyetHack.Player cannot juggle swords")
