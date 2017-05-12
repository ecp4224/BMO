package io.edkek.bmo.core.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import io.edkek.bmo.BMO;
import io.edkek.bmo.utils.PRunnable;

public class Text {
    protected static final Blend TEXT_BLEND = new Blend(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    protected final FileHandle handle;
    protected final int size;
    protected final Color color;
    protected String characters = null;
    protected boolean visible = true;

    protected BitmapFont font;
    protected float x, y;
    protected String text = "";

    protected GlyphLayout layout;

    protected int align = Align.center;

    protected PRunnable<Text> onClick = null;
    private boolean didClick = false;

    public Text(int size, Color color, FileHandle file) {
        this.size = size;
        this.color = color;
        handle = file;
    }

    public Text(int size, Color color, FileHandle file, String characters) {
        this.size = size;
        this.color = color;
        handle = file;
        this.characters = characters;
    }


    public Text(int size, Color color, FileHandle file, String characters, int align) {
        this.size = size;
        this.color = color;
        handle = file;
        this.characters = characters;
        this.align = align;
    }

    public void draw(SpriteBatch batch) {
        if (font == null)
            return;

        if (layout == null) {
            layout = new GlyphLayout(font, text);
        }

        font.draw(batch, text, x - (layout.width / 2f), y + (layout.height / 2f), layout.width, align, true);

        if (onClick != null) {
            Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0f);
            BMO.getScreen().camera.unproject(mousePos);

            if (contains(mousePos.x, mousePos.y) && Gdx.input.justTouched()) {
                onClick.run(this);
            }
        }
    }

    private boolean contains(float x, float y) {
        return (x >= getX() - (getWidth() / 2f) && x <= getX() + (getWidth() / 2f) && y >= (getY() - (getHeight() / 2f)) && y <= getY() + (getHeight() / 2f));
    }

    public void load() {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                FreeTypeFontGenerator gen = new FreeTypeFontGenerator(handle);
                FreeTypeFontGenerator.FreeTypeFontParameter parm = new FreeTypeFontGenerator.FreeTypeFontParameter();
                parm.size = size;
                parm.color = color;
                if (characters != null)
                    parm.characters = characters;

                font = gen.generateFont(parm);
                gen.dispose();

                layout = new GlyphLayout(font, text);
            }
        });
    }

    public void unload() {
        font.dispose();
        text = null;
        font = null;
    }

    public Blend blendMode() {
        return TEXT_BLEND;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setAlpha(float alpha) {
        color.a = alpha;
        if (font != null)
            font.setColor(color);
    }

    public String getText() {
        return text;
    }

    public void onClick(PRunnable<Text> onClick) {
        this.onClick = onClick;
    }

    public void setText(String text) {
        this.text = text;

        if (layout != null)
            layout.setText(font, text);
    }

    public float getWidth() {
        if (layout == null && font != null) {
            layout = new GlyphLayout(font, text);
        } else if (layout == null)
            return 0;

        return (int) Math.ceil(layout.width);
    }

    @Deprecated
    public float getHeight() {
        if (font == null)
            return 0;

        return (float) Math.ceil(font.getCapHeight());
    }

    public float getRenderHeight() {
        if (font == null)
            return 0;

        return font.getLineHeight() * getLineCount();
    }

    public int getLineCount() {
        return text.split("\\r\\n|\\n|\\r").length + 1;
    }

    public BitmapFont getFont() {
        return font;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
