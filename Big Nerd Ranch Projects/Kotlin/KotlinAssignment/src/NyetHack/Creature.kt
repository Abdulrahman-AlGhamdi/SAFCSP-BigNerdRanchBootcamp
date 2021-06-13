package NyetHack

import kotlin.random.Random

interface Fightable{
    var healthPoints: Int
    val diceCount: Int
    val diceSides: Int
    val damageRoll: Int
    get() = (0 until diceCount).map {
        Random.nextInt(diceCount + 1)
    }.sum()

    fun attack(opponent: Fightable): Int
}

abstract class Monster(
        val name: String,
        val description: String,
        override var healthPoints: Int
): Fightable {
    override fun attack(opponent: Fightable): Int {
        val damageDealt = damageRoll
        opponent.healthPoints -= damageDealt
        return damageDealt
    }
}

class Goblin(
        name: String = "NyetHack.Goblin",
        description: String = "a nasty-looking NyetHack.Goblin",
        healthPoints: Int = 30
): Monster(name, description, healthPoints){
    override val diceCount = 2
    override val diceSides = 8
}

class Dragon(
        name: String = "NyetHack.Dragon",
        description: String = "a huge-dangerous NyetHack.Dragon",
        healthPoints: Int = 50
): Monster(name, description, healthPoints){
    override val diceCount = 4
    override val diceSides = 10
}