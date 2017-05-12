package io.edkek.bmo.handlers.scenes

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import io.edkek.bmo.BMO
import io.edkek.bmo.core.render.Text
import io.edkek.bmo.core.render.scene.AbstractScene

class SampleFaceScene : AbstractScene() {
    private lateinit var face: Sprite

    override fun onInit() {
        val faceTexture = Texture("faces/confused.png")
        faceTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest)
        face = Sprite(faceTexture)
        face.scale(1.5f)
        face.setCenter(640f, 500f)
        face.setOriginCenter()

        requestOrder(1)
        BMO.loadGameAssets()
    }

    override fun render(camera: OrthographicCamera, batch: SpriteBatch) {
        batch.begin()
        face.draw(batch)
        batch.end()
    }

    override fun dispose() {

    }

}