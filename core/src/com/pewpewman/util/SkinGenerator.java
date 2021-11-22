package com.pewpewman.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class SkinGenerator {

    public static Skin generateSkin(AssetManager assetManager) {
        Skin skin = new Skin();
        TextureAtlas uiAtlas = assetManager.get("packed/ui/uipack.atlas");

        NinePatch storybox = new NinePatch(uiAtlas.findRegion("storybox"), 10, 10, 5, 5);
        skin.add("storybox", storybox);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/pkmnrsi.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 36;
        parameter.color = new Color(255, 255, 255, 1f);

        BitmapFont font = generator.generateFont(parameter);
        generator.dispose(); // Dispose to avoid memory leaks!
        font.getData().setLineHeight(16f);
        skin.add("font", font);

        BitmapFont smallFont = assetManager.get("font/small_letters_font.fnt", BitmapFont.class);
        skin.add("small_letters_font", smallFont);

        LabelStyle labelStyle = new LabelStyle();
        labelStyle.font = skin.getFont("font");
        skin.add("default", labelStyle);

        return skin;
    }
}
