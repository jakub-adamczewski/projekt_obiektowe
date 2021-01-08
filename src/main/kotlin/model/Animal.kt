package model

import javafx.stage.Stage
import utils.Toast

sealed class Animal {
    abstract val id: Int
    abstract val name: String
    abstract val size: Size

    abstract fun eat(): String

    companion object {
        val animalSubclasses = Animal::class.sealedSubclasses
    }

    data class Arachnid(
        override val id: Int,
        override val name: String,
        override val size: Size,
        val isToxic: Boolean
    ) : Animal() {
        override fun eat(): String = "Arachnid $name is eating some insects!"
    }

    data class Bird(
        override val id: Int,
        override val name: String,
        override val size: Size,
        val canFly: Boolean
    ) : Animal() {
        override fun eat(): String = "Bird $name is eating some worms!"
    }

    data class Fish(
        override val id: Int,
        override val name: String,
        override val size: Size,
        val livesInSaltyWater: Boolean
    ) : Animal(), Swimmable {

        override fun swim(): String = "Fish ${this.name} is swimming!"

        override fun eat(): String = "Fish $name is eating some plankton!"
    }

    data class Insect(
        override val id: Int,
        override val name: String,
        override val size: Size,
        val hasWings: Boolean
    ) : Animal() {
        override fun eat(): String = "Insect $name is eating some leaves!"
    }

    data class Mammal(
        override val id: Int,
        override val name: String,
        override val size: Size,
        val hasFur: Boolean
    ) : Animal() {
        override fun eat(): String = "Mammal $name is eating some apples!"
    }

    data class Reptile(
        override val id: Int,
        override val name: String,
        override val size: Size,
        val hasLegs: Boolean
    ) : Animal() {
        override fun eat(): String = "Reptile $name is eating some insects!"
    }
}