package Interop;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.random.Random;
import kotlin.ranges.IntRange;
import kotlin.ranges.RangesKt;

import java.io.IOException;

public class Jhava {

    public static void main(String[] args) {

//        System.out.println(Hero.makeProclamation());
//        System.out.println("Spells:");
//        Spellbook spellBook = new Spellbook();
//        for (String spell : spellBook.spells){
//            System.out.println(spell);
//        }
//        System.out.println("Max Spell Count: " + Spellbook.MAX_SPELL_COUNT);
//
//        Spellbook.getSpellbookGreeting();
//
//        Function1<String, Unit> translator = Hero.getTranslator();
//        translator.invoke("TRUCE");

        simulation();
    }

    public static void simulation(){
        Hero.runSimulation("SS", (String, Integer) -> "Abdulrahman " + Integer);
    }

    public static void runSimulation(String playerName, Function2 greetingFunction) {
        int numBuildings = (int) ((Math.random() * 3)+1);
        System.out.println(greetingFunction.invoke(playerName, numBuildings));
    }

    public void offerFood(){
        Hero.handOverFood("Pizza");
    }

    public void apology(){
        try{
            Hero.acceptApology();
        }catch (IOException e){
            System.out.println("Caught!");
        }
    }

    public void extendHandInFriendship() throws Exception{
        throw new Exception();
    }

    private int hitPoint = 52489112;
    private String greeting = "BLARGH";

    @NotNull
    public String utterGreeting(){
        return greeting;
    }

    public String getGreeting(){
        return greeting;
    }

    public void setGreeting(String greeting){
        this.greeting = greeting;
    }

    @Nullable
    public String determineFriendshipLevel(){
        return null;
    }

    public int getHitPoint(){
        return hitPoint;
    }
}
