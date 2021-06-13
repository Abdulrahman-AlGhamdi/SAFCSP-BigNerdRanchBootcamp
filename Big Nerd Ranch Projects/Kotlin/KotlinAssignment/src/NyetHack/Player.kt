package NyetHack

import java.io.File

class Player(
        _name: String,
        override var healthPoints: Int = 89,
        val isBlessed: Boolean,
        private val isImmortal: Boolean
): Fightable {
    var name = _name
        get() = "${field.capitalize()} of ${homeTown.trim()}"
        set(value){
            field = value.trim()
        }

    private val homeTown by lazy {selectHometown()}
    var currentPosition = Coordinate(0,0)

    init {
        require(healthPoints in 40..100){"Health Points must between 40 & 100"}
        require(name.isNotBlank()) {"NyetHack.Player must have a Name"}
    }

    constructor(name: String): this(
            name,
            isBlessed = true,
            isImmortal = false
    ){
        if(name.toLowerCase() == "kar"){
            healthPoints = 40
        }
    }

    private fun selectHometown() = File("data/towns.txt").readText().chunked(5).random()

    fun castFireball(numFireballs: Int = 2): String {
        println("A fireball springs into existence. (x$numFireballs)")
        return when((1..50).random()){
        in 1..10 -> "Fuel is: Empty"
        in 11..20 -> "Fuel is: Fumes"
        in 21..30 -> "Fuel is: Low"
        in 31..40 -> "Fuel is: Good"
        in 41..50 -> "Fuel is: Full"
        else -> "NONE"
        }
    }

    fun formatHealthStatus(): String = when(healthPoints) {
        100 -> "is in excellent condition!"
        in 90..99 -> "has a few scratches."
        in 75..89 -> if (isBlessed) {
            "has some minor wounds but is healing quite quickly!"
        } else {
            "has some minor wounds."
        }
        in 15..74 -> "looks pretty hurt."
        else -> "is in awful condition!"
    }

    fun auraColor(): String = when ((Math.pow(Math.random(), (110 - healthPoints) / 100.0) * 20).toInt()) {
        in 0..5 -> "Red"
        in 6..10 -> "Orange"
        in 11..15 -> "Purple"
        in 16..20 -> "Green"
        else -> "NONE"
    }

    override val diceCount = 3
    override val diceSides = 6
    override fun attack(opponent: Fightable): Int {
        val damageDealt = if(isBlessed){
            damageRoll * 2
        }else{
            damageRoll
        }
        opponent.healthPoints -= damageDealt
        return damageDealt
    }
}