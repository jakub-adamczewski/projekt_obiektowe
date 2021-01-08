package app

import view.MainView
import javafx.stage.Stage
import tornadofx.App

class Application: App(MainView::class){
    override fun start(stage: Stage) {
        with(stage){
            width = 1500.0
            height = 750.0
        }
        super.start(stage)
    }
}