package com.pewpewman.audio;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.*;

public class SoundEffectManager {

    private Music music;

    public void playSoundEffect(SOUNDEFFECT soundEffect) {
        if (soundEffect == SOUNDEFFECT.BUTTONCLICKED) {
            music = Gdx.audio.newMusic(Gdx.files.internal("audio/soundEffect/buttonClicked.mp3"));
        } else if (soundEffect == SOUNDEFFECT.SHOOTING) {
            music = Gdx.audio.newMusic(Gdx.files.internal("audio/soundEffect/shooting.mp3"));
        } else if (soundEffect == SOUNDEFFECT.PLAYERSHOT) {
//            music = Gdx.audio.newMusic(Gdx.files.internal("audio/soundEffect/shooting.mp3"));
        } else if (soundEffect == SOUNDEFFECT.ENEMYSHOT) {
            music = Gdx.audio.newMusic(Gdx.files.internal("audio/soundEffect/enemyShot.mp3"));
        }

//        music.setLooping(false);
        music.setVolume(0.4f);
        music.play();
    }
}
