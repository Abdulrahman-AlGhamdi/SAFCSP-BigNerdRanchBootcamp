package com.ss
import java.io.File

private const val DICE_SIDES_COUNT = 6
private const val DEX_DICE_COUNT = 4
private const val WIS_DICE_COUNT = 3
private const val STR_DICE_COUNT = 2

private fun Int.rollDice(): Int = (1..this).map{(1..DICE_SIDES_COUNT).random()}.sum()

class Character {
    private val name: String = File("data/names.txt").readLines().random()
    private val fantasyRace: String = listOf("Dwarf", "Elf", "Human", "Halfling").random()

    private val dex: Int = DEX_DICE_COUNT.rollDice()
    private val wis: Int = WIS_DICE_COUNT.rollDice()
    private val str: Int = STR_DICE_COUNT.rollDice()

    override fun toString(): String {
        return "$name, $fantasyRace, $dex, $wis, $str"
    }
}