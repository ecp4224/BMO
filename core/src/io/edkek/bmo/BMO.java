package io.edkek.bmo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import io.edkek.bmo.core.logic.Handler;
import io.edkek.bmo.handlers.BlankHandler;
import io.edkek.bmo.utils.ArrayHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileFilter;

public class BMO {
    private static BMOScreen SCREEN;
    private static Handler DEFAULT = new BlankHandler();
    @NotNull
    public static final AssetManager ASSETS = new AssetManager();

    public static final Color TEXT_COLOR = new Color(97f/255f, 187f/255f, 108f/255f, 1f); //97,187,108

    public static void setDefaultHandler(Handler handler) {
        DEFAULT = handler;
    }

    @NotNull
    public static BMOScreen getScreen() {
        if (SCREEN == null) {
            SCREEN = new BMOScreen(DEFAULT);
        }

        return SCREEN;
    }

    private static boolean loaded;
    public static void loadGameAssets() {
        if (loaded)
            return;

        //Load all sprites
        FileHandle[] sprites = Gdx.files.internal("faces").list(pathname -> pathname.getName().endsWith("png") ||
                pathname.getName().endsWith("PNG") ||
                pathname.getName().endsWith("jpg") ||
                pathname.getName().endsWith("JPG"));

        //Load all sprites
        FileHandle[] menuSprites = Gdx.files.internal("sprites").list(pathname -> pathname.getName().endsWith("png") ||
                pathname.getName().endsWith("PNG") ||
                pathname.getName().endsWith("jpg") ||
                pathname.getName().endsWith("JPG"));

        for (FileHandle file: ArrayHelper.combine(sprites, menuSprites)) {
            ASSETS.load(file.path(), Texture.class);
        }

        FileHandle[] sounds = Gdx.files.internal("sounds").list(pathname -> pathname.getName().endsWith("mp3") ||
                pathname.getName().endsWith("wav") ||
                pathname.getName().endsWith("ogg"));

        for (FileHandle file: sounds) {
            ASSETS.load(file.path(), Sound.class);
        }


        //TODO Load other shit

        loaded = true;
    }
}
