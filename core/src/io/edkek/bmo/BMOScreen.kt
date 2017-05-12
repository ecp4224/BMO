package io.edkek.bmo

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.Scaling
import com.badlogic.gdx.utils.viewport.ScalingViewport
import com.badlogic.gdx.utils.viewport.Viewport
import io.edkek.bmo.core.logic.Handler
import io.edkek.bmo.core.logic.LogicHandler
import io.edkek.bmo.core.logic.Logical
import io.edkek.bmo.core.render.scene.Scene
import java.util.*

class BMOScreen(val handler: Handler) : ApplicationAdapter() {
    //187,224,206
    var backColor = Color(187f/255f, 224f/255f, 206f/255f, 1f) //#b7ffc7
    lateinit var batch : SpriteBatch; //We need to delay this
    private var loaded = false;
    lateinit var camera : OrthographicCamera; //We need to delay this
    lateinit var viewport: Viewport; //We need to delay this
    private val scenes = ArrayList<Scene>()
    private val logicalHandler = LogicHandler()

    override fun create() {
        batch = SpriteBatch()
        camera = OrthographicCamera(1280f, 720f)
        camera.setToOrtho(false, 1280f, 720f)

        viewport = ScalingViewport(Scaling.stretch, 1280f, 720f, camera)

        logicalHandler.init()

        handler.start()

        val widthMult = Gdx.graphics.width / 1280f
        val heightMult = Gdx.graphics.height / 720f
    }

    override fun render() {
        try {
            _render()
            //debugRenderer.render(world, camera.combined)
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
    }

    fun _render() {
        Gdx.gl.glClearColor(backColor.r, backColor.g, backColor.b, backColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        logicalHandler.tick(handler)

        camera.update()

        batch.projectionMatrix = camera.combined;

        for (scene in scenes) {
            if (scene.isVisible) {
                scene.render(camera, batch)
                batch.color = Color.WHITE //reset color
            }
        }
    }

    public fun addScene(scene: Scene) {
        if (!scenes.contains(scene)) {
            Gdx.app.postRunnable {
                scene.init()
                scenes.add(scene)

                Collections.sort(scenes, { o1, o2 -> o2.requestedOrder() - o1.requestedOrder() })
            }
        }
    }

    public fun removeScene(scene: Scene) {
        if (scenes.contains(scene)) {
            scene.dispose()
            Gdx.app.postRunnable {
                scenes.remove(scene)
            }
        }
    }

    public fun addLogical(logic: Logical) {
        logicalHandler.addLogical(logic)
    }

    public fun removeLogical(logic: Logical) {
        logicalHandler.removeLogical(logic)
    }

    fun clearScreen() {
        Gdx.app.postRunnable {
            for (scene in scenes) {
                removeScene(scene)
            }
            logicalHandler.clear()
        }
    }

    override fun dispose() {
        for (scene in scenes) {
            removeScene(scene)
        }
    }
}
