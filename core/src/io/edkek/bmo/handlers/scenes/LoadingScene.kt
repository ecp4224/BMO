package com.boxtrotstudio.ghost.client.handlers.scenes

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import io.edkek.bmo.BMO
import io.edkek.bmo.core.render.Text
import io.edkek.bmo.core.render.scene.AbstractScene

public class LoadingScene() : AbstractScene() {
    private lateinit var progressBarBack : Sprite;
    private lateinit var progressBarFront : Sprite;
    private lateinit var progressText : Text
    private var didCall = false
    private var onFinished = Runnable {  }

    override fun onInit() {
        var back = Texture("sprites/progress_back.png")
        var front = Texture("sprites/progress_front.png");

        progressBarBack = Sprite(back)
        progressBarFront = Sprite(front)

        progressBarFront.setCenter(640f, 32f)
        progressBarBack.setCenter(640f, 32f)

        progressBarFront.setOriginCenter()
        progressBarBack.setOriginCenter()

        progressBarFront.color = BMO.TEXT_COLOR

        progressText = Text(76, BMO.TEXT_COLOR, Gdx.files.internal("fonts/INFO53_0.ttf"));
        progressText.x = 800f/1.25f
        progressText.y = 600f/1.5f
        progressText.text = "Booting..."
        progressText.load()

        requestOrder(1)
        BMO.loadGameAssets()
    }

    override fun render(camera: OrthographicCamera, batch: SpriteBatch) {
        var temp = BMO.ASSETS.progress * 720f

        progressBarFront.setSize(temp, 16f)

        batch.begin()

        progressText.draw(batch)
        //progressBarBack.draw(batch)
        //progressBarFront.draw(batch)

        batch.end()
        if (BMO.ASSETS.update() && !didCall) {
            onFinished.run()
            didCall = true
        }
    }

    public fun isLoaded() : Boolean {
        return BMO.ASSETS.update()
    }

    override fun dispose() {
        progressText.unload()
    }

    public fun setLoadedCallback(callback: Runnable) {
        this.onFinished = callback
    }

    //Code taken from: https://code.google.com/p/replicaisland/source/browse/trunk/src/com/replica/replicaisland/Lerp.java?r=5
    //Because I'm a no good dirty scrub
    fun ease(start: Float, target: Float, duration: Float, timeSinceStart: Float): Float {
        var value = start
        if (timeSinceStart > 0.0f && timeSinceStart < duration) {
            val range = target - start
            val percent = timeSinceStart / (duration / 2.0f)
            if (percent < 1.0f) {
                value = start + range / 2.0f * percent * percent * percent
            } else {
                val shiftedPercent = percent - 2.0f
                value = start + range / 2.0f * (shiftedPercent * shiftedPercent * shiftedPercent + 2.0f)
            }
        } else if (timeSinceStart >= duration) {
            value = target
        }
        return value
    }
}
