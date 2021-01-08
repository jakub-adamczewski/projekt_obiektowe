package view

import controller.MainController
import javafx.scene.Scene
import javafx.scene.control.CheckBox
import javafx.scene.control.ChoiceBox
import javafx.scene.control.TextField
import javafx.scene.text.FontSmoothingType
import javafx.scene.text.FontWeight
import model.Size
import tornadofx.*
import utils.Style
import utils.Toast
import java.lang.NumberFormatException
import java.time.format.TextStyle

class MainView : View("Projekt - Programowanie Objektowe") {

    private val controller by inject<MainController>()

    override val root = borderpane {

        top = hbox {
            spacing = Style.mediumSpacing
            paddingAll = Style.mediumPadding

            textfield {
                promptText = "Search by name"
                textProperty().addListener { _, _, newValue ->
                    controller.onQueryChanged(newValue)
                }
            }

            hbox {
                spacing = Style.smallSpacing
                text("Search by type:")

                checkbox {
                    selectedProperty().addListener { _, _, newValue ->
                        controller.onInstanceFilterEnabledChanged(newValue)
                    }
                }

                choicebox(
                    values = MainController.animalTypeValues
                ) {
                    value = MainController.defaultAnimalTypeValue
                    valueProperty().addListener { _, _, newValue ->
                        controller.onTypeToFilterChanged(newValue!!)
                    }
                }

                button("Run abstract eat function") {
                    setOnMouseClicked {
                        controller.runAbstractEatFunction()
                    }
                }

                button("Run interface swim function") {
                    setOnMouseClicked {
                        controller.runInterfaceSwimFunction()
                    }
                }
            }
        }

        left = vbox {
            spacing = Style.mediumSpacing
            paddingAll = Style.mediumPadding

            text("Add animal")

            choicebox(
                values = MainController.animalTypeValues
            ) {
                id = "animalTypeChoiceBox"
                value = MainController.defaultAnimalTypeValue

                valueProperty().addListener { _, _, newValue ->
                    controller.onAnimalTypePicked(newValue!!)
                    clearForm(scene)
                }
            }

            textfield {
                id = "idField"
                promptText = "id: Int"
            }
            textfield {
                id = "nameField"
                promptText = "name: String"
            }
            hbox {
                spacing = Style.mediumSpacing
                text("size: Size")
                choicebox(values = MainController.getSizeValues()) {
                    id = "sizeChoiceBox"
                    value = MainController.getDefaultSizeValue()
                }

            }
            hbox {
                spacing = Style.mediumSpacing
                text {
                    bind(controller.fourthFieldName)
                }
                checkbox {
                    id = "fourthPropertyCheckBox"
                }
            }

            button("Add") {
                setOnMouseClicked {
                    addAnimal(scene)
                }
            }
        }

        center = vbox {
            spacing = Style.mediumSpacing
            paddingAll = Style.mediumPadding
            label("Animals")
            listview(controller.filteredAnimals)
        }

        right = vbox {
            spacing = Style.mediumSpacing
            paddingAll = Style.mediumPadding

            text("Remove animal")

            textfield {
                id = "idToRemoveField"
                promptText = "Id of Animal to remove"
            }

            button("Remove") {
                action {
                    wrongInputTryCatchingAction {
                        val idToRemoveField = (scene.lookup("#idToRemoveField") as TextField)
                        val animalId = idToRemoveField.text.toInt()
                        if (!controller.removeAnimalWithId(animalId)) {
                            Toast.makeText(primaryStage, "There is no animal with such id")
                        } else {
                            idToRemoveField.clear()
                        }
                    }
                }
            }
        }

        bottom = hbox {
            spacing = Style.mediumSpacing
            paddingAll = Style.mediumPadding
            vbox {
                spacing = Style.mediumSpacing
                paddingAll = Style.mediumPadding

                style {
                    borderColor += box(c("#000000"))
                }

                text("Abstract function action:"){
                    style {
                        fontWeight = FontWeight.BOLD
                    }
                }
                text {
                    id = "abstractActionText"
                    bind(controller.abstractActionText)
                }
            }
            vbox {
                spacing = Style.mediumSpacing
                paddingAll = Style.mediumPadding

                style {
                    borderColor += box(c("#000000"))
                }

                text("Interface function action:"){
                    style {
                        fontWeight = FontWeight.BOLD
                    }                }
                text {
                    id = "interfaceActionText"
                    bind(controller.interfaceActionText)
                }
            }
        }
    }

    private fun addAnimal(scene: Scene) {
        val typeChoiceBox = (scene.lookup("#animalTypeChoiceBox") as ChoiceBox<String>)
        val idField = (scene.lookup("#idField") as TextField)
        val nameField = (scene.lookup("#nameField") as TextField)
        val sizeChoiceBox = (scene.lookup("#sizeChoiceBox") as ChoiceBox<Size>)
        val fourthPropertyCheckBox = (scene.lookup("#fourthPropertyCheckBox") as CheckBox)

        wrongInputTryCatchingAction {
            if (idField.text != "" && nameField.text != "") {

                val type = typeChoiceBox.value!!
                val id = idField.text.toInt()
                val name = nameField.text
                val size = sizeChoiceBox.value
                val fourthField = fourthPropertyCheckBox.isSelected

                if (!controller.addAnimal(type, id, name, size, fourthField)) {
                    Toast.makeText(primaryStage, "There is animal with same id")
                }
            } else {
                Toast.makeText(primaryStage, "Fill all text fields")
            }

        }
    }

    private fun clearForm(scene: Scene) {
        scene.apply {
            (lookup("#idField") as TextField).clear()
            (lookup("#nameField") as TextField).clear()
            (lookup("#sizeChoiceBox") as ChoiceBox<Size>).value = MainController.getDefaultSizeValue()
            (lookup("#fourthPropertyCheckBox") as CheckBox).isSelected = false
        }
    }

    private fun wrongInputTryCatchingAction(action: () -> Unit) {
        try {
            action()
        } catch (e: NumberFormatException) {
            Toast.makeText(primaryStage, "Wrong input format in number field")
        }
    }
}