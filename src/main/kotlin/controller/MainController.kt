package controller

import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import model.Animal
import model.Size
import model.Swimmable
import tornadofx.Controller
import utils.Toast
import java.lang.UnsupportedOperationException

class MainController : Controller() {

    private val allAnimals = mutableListOf<Animal>()
    val filteredAnimals: ObservableList<Animal> = FXCollections.observableArrayList(allAnimals)

    companion object {
        val animalTypeValues = Animal.animalSubclasses.map { it.simpleName }
        val defaultAnimalTypeValue: String = animalTypeValues.first()!!

        fun getSizeValues(): List<Size> {
            return Size.values().toList()
        }

        fun getDefaultSizeValue(): Size {
            return getSizeValues().first()
        }
    }

    val abstractActionText: StringProperty = SimpleStringProperty("No abstract action yet")
    val interfaceActionText: StringProperty = SimpleStringProperty("No interface action yet")

    fun runAbstractEatFunction() {
        var resultText = ""
        filteredAnimals.forEach { animal ->
            resultText += animal.eat() + "\n"
        }
        abstractActionText.set(resultText)
    }

    fun runInterfaceSwimFunction(){
        var resultText = ""
        filteredAnimals.filterIsInstance(Swimmable::class.java)
            .forEach { swimmableAnimal ->
                resultText += swimmableAnimal.swim() + "\n"
            }
        interfaceActionText.set(resultText)
    }

    private var query: String = ""
    private var instanceFilterEnabled: Boolean = false
    private var typeToFilter = defaultAnimalTypeValue

    private fun updateList() {
        val instanceFilteredList = if (instanceFilterEnabled) {
            allAnimals.filterIsInstance(getAnimalClassForItsSimpleName(typeToFilter).java)
        } else {
            allAnimals
        }

        val newList = if (query.isEmpty()) {
            instanceFilteredList
        } else {
            instanceFilteredList.filter { it.name.contains(query) }
        }

        filteredAnimals.setAll(newList)
    }

    fun onQueryChanged(query: String) {
        this.query = query
        updateList()
    }

    fun onInstanceFilterEnabledChanged(instanceFilterEnabled: Boolean) {
        this.instanceFilterEnabled = instanceFilterEnabled
        updateList()
    }

    fun onTypeToFilterChanged(typeToFilter: String) {
        this.typeToFilter = typeToFilter
        updateList()
    }

    fun addAnimal(type: String, id: Int, name: String, size: Size, fourthField: Boolean): Boolean =
        if (allAnimals.firstOrNull { it.id == id } == null) {

            allAnimals.add(
                when (type) {
                    Animal.Arachnid::class.simpleName -> Animal.Arachnid(id, name, size, fourthField)
                    Animal.Bird::class.simpleName -> Animal.Bird(id, name, size, fourthField)
                    Animal.Fish::class.simpleName -> Animal.Fish(id, name, size, fourthField)
                    Animal.Insect::class.simpleName -> Animal.Insect(id, name, size, fourthField)
                    Animal.Mammal::class.simpleName -> Animal.Mammal(id, name, size, fourthField)
                    Animal.Reptile::class.simpleName -> Animal.Reptile(id, name, size, fourthField)
                    else -> throw UnsupportedOperationException("Unsupported Animal type")
                }
            )
            updateList()
            true
        } else {
            false
        }

    fun removeAnimalWithId(id: Int): Boolean {
        val animalToRemove = allAnimals.firstOrNull { animal -> animal.id == id } ?: return false
        allAnimals.remove(animalToRemove)
        updateList()
        return true
    }

    val fourthFieldName: StringProperty = SimpleStringProperty("isToxic: Boolean")

    fun onAnimalTypePicked(type: String) {
        val text = when (type) {
            Animal.Arachnid::class.simpleName -> "isToxic: Boolean"
            Animal.Bird::class.simpleName -> "canFly: Boolean"
            Animal.Fish::class.simpleName -> "livesInSaltyWater: Boolean"
            Animal.Insect::class.simpleName -> "hasWings: Boolean"
            Animal.Mammal::class.simpleName -> "hasFur: Boolean"
            Animal.Reptile::class.simpleName -> "hasLegs: Boolean"
            else -> throw UnsupportedOperationException("Unsupported Animal type")
        }
        fourthFieldName.set(text)
    }

    private fun getAnimalClassForItsSimpleName(simpleName: String) =
        Animal.animalSubclasses.firstOrNull { animalClass ->
            animalClass.simpleName == simpleName
        } ?: throw RuntimeException("No class for given simple name")
}