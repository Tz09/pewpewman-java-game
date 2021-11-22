package com.pewpewman.dialogue;

import com.badlogic.gdx.assets.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.viewport.*;
import com.pewpewman.*;
import com.pewpewman.screen.*;
import com.pewpewman.util.*;

public class StoryBoxUI{

    private Table root;
    private Stage stage;
    private StoryBox storyBox;

    public void createStoryBoxUI(String text) {
        AssetManager assetManager = new AssetManager();
        assetManager.load("packed/ui/uipack.atlas", TextureAtlas.class);
        assetManager.load("font/small_letters_font.fnt", BitmapFont.class);
        assetManager.finishLoading();
        Skin skin = SkinGenerator.generateSkin(assetManager);

        stage = new Stage(new ScreenViewport());
        stage.setDebugAll(false);   // To check the story box layout

        root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        storyBox = new StoryBox(skin);
        storyBox.animateText(text);
        storyBox.isFinished();

        if (Pewpewman.currentScreen == SCREEN.GAMEOVER) {
            root.add(storyBox).expand().align(Align.bottom).pad(50f);
        } else {
            root.add(storyBox).expand().align(Align.center).pad(8f);
        }
    }

    public Table getRoot() {
        return root;
    }

    public Stage getStage() {
        return stage;
    }

    public StoryBox getStoryBox() {
        return storyBox;
    }
}
