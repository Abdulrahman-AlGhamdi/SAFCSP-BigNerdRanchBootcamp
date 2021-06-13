package NyetHack

import java.lang.IllegalStateException
import kotlin.system.exitProcess

fun main() {

    //Regex

    val doubleNumber = 2.05
    var floatNumber: Float? = doubleNumber as? Float
    println(floatNumber)
    if(doubleNumber is Double){
        floatNumber = doubleNumber.toFloat()
        println(floatNumber)
    }

    val room1 = Room("Castle")
    val room2 = Room("Castle")
    if (room1.equals(room2)){
        println("They are Equal...")
    }

    typeAny(townSquare())
    typeAny(Room("Castle"))
    typeAny("New Town")

    townSquare().toString()
    Room("Castle").toString()

    Game.play()
}

fun typeAny(any: Any){
    when (any) {
        is townSquare -> println("3")
        is Room -> println("2")
        else -> println("1")
    }
}

object Game{
    private var playGame = true
    private val player = Player("Madrigal")
    private var currentRoom: Room = townSquare()
    private var worldMap = listOf(
            listOf(currentRoom, Room("Tavern"), Room("Back NyetHack.Room")),
            listOf(Room("Long Corridor"), Room("Generic NyetHack.Room"))
    )

    private fun fight(monsterType: String): String{
        when(currentRoom.name){
            in "Town Square" -> currentRoom.monster = null
        }

        val firstMonster = currentRoom.monster?.get(0)
        val secondMonster = currentRoom.monster?.get(1)

        return if(firstMonster == null || secondMonster == null){
            "There is nothing here to fight"
        }else{
            if (monsterType == "goblin"){
                while (player.healthPoints > 0 && firstMonster.healthPoints > 0){
                    slay(firstMonster)
                    Thread.sleep(1000)
                }
            }
            println()
            if(monsterType == "dragon"){
                while (player.healthPoints > 0 && secondMonster?.healthPoints!! > 0){
                    slay(secondMonster!!)
                    Thread.sleep(1000)
                }
            }
            "Compat Complete."
        }
    }

    private fun slay(monster: Monster){
        println("${monster.name} did ${monster.attack(player)} damage!")
        println("${player.name} did ${player.attack(monster)} damage!")

        if(player.healthPoints <= 0){
            println(">>>You have been defeated! Thanks for playing.<<<")
            exitProcess(0)
        }
        if(monster.healthPoints <= 0){
            println(">>>The ${monster.name} has been defeated!<<<")
            currentRoom.monster?.remove(monster)
        }
    }

    private fun move(directionInput: String): String {
        return try {
            val direction = Direction.valueOf(directionInput.toUpperCase())
            val newPosition = direction.updateCoordinate(player.currentPosition)
            if(!newPosition.isInBounds){
                throw IllegalStateException("$direction is out of Bounds")
            }
            val newRoom = worldMap[newPosition.y][newPosition.x]
            player.currentPosition = newPosition
            currentRoom = newRoom
            "Ok, you Move $direction to the ${newRoom.name}.\n${newRoom.load()}"
        }catch (e: Exception){
            "Invalid NyetHack.Direction: $directionInput"
        }
    }

    private fun map(worldMap: List<List<Room>>): String {
        var currentPosition = ""
        println()
        worldMap.forEach {
            it.forEach {
                if(currentRoom == it){
                    currentPosition += " x"
                }else
                    currentPosition += " o"
            }
            currentPosition += "\n"
        }
        return currentPosition
    }

    private fun ring(currentRoom: Room): String{
        return if (currentRoom.name == "Town Square"){
            townSquare().ringBell()
        }else{
            "You Can Ring Only in Town Square"
        }
    }

    init {
        println("Welcome, adventurer.")
        player.castFireball()
    }

    private class GameInput(arg: String?){
        private val input = arg ?: ""
        val command = input.split(" ")[0]
        val argument = input.split(" ").getOrElse(1){ "" }

        fun processCommand(): String = when (command.toLowerCase()){
            "fight" -> fight(argument)
            "move" -> move(argument)
            "map" -> map(worldMap)
            "ring" -> ring(currentRoom)
            "quit" , "exit" -> {
                playGame = false; "Farewell"}
            else -> commandNotFound()
        }
        private fun commandNotFound() = "I'm not quite sure what you are trying to do!"
    }

    fun play(){
        while (playGame){
            //NyetHack.Room
            if(currentRoom.name != "Town Square"){
                println(currentRoom.monsterDescription())
            }
            println(currentRoom.load())

            //Health Status:
            println("Name: ${player.name} (Blessed: ${if (player.isBlessed) "YES" else "NO"})")

            //NyetHack.Player Status:
            printPlayerStatus(player)
            println()

            //Command:
            print("> Enter your Command: ")
            println(GameInput(readLine()).processCommand())
        }
    }

    private fun printPlayerStatus(player: Player){
        val auraColor = player.auraColor()
        val auraVisible = player.isBlessed && player.healthPoints > 50
        val playerStatusFormat = "(HP)(A) -> H"
        val playerStatus = playerStatusFormat
                .replace("HP","HP: ${player.healthPoints}")
                .replace("A", "Aura: ${if(auraVisible) auraColor else "Aura not Visible"}")
                .replace(" H", " ${player.name} ${player.formatHealthStatus()}")
        println(playerStatus)
    }
}