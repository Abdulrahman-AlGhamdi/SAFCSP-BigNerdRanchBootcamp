package NyetHack

open class Room(val name: String) {
    protected open val dangerLevel = 5
    var monster: MutableList<Monster>? = mutableListOf(Goblin(), Dragon())
    var monsterName = monster?.joinToString{it.name}

    fun monsterDescription() = "NyetHack.Room: $name\nDanger Level: $dangerLevel\nCreature: $monsterName"

    open fun load() = "Nothing much to see here..."

    override fun equals(other: Any?): Boolean {
        if (other == name){
            return true
        }
        return false
    }
}

open class townSquare : Room("Town Square"){
    private val bellSound = "GWONG"
    override val dangerLevel = super.dangerLevel - 3
    final override fun load() = "The villagers rally and cheer as you enter!\n${ringBell()}"
    fun ringBell() = "The bell tower announces your arrival. $bellSound"
}