package io.edkek.bmo.handlers

import com.boxtrotstudio.ghost.client.handlers.scenes.LoadingScene
import io.edkek.bmo.BMO
import io.edkek.bmo.core.logic.Handler
import io.edkek.bmo.handlers.scenes.SampleFaceScene

class BlankHandler : Handler {
    override fun start() {
        val loading = LoadingScene()
        BMO.getScreen().addScene(loading)

        loading.setLoadedCallback(Runnable {
            //BMO.getScreen().removeScene(loading)
            //BMO.getScreen().addScene(SampleFaceScene())
        })
    }

    override fun tick() {

    }

}