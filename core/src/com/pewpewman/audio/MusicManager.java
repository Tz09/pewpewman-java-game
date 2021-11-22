package com.pewpewman.audio;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.*;

public class MusicManager {

    private Music music;
    private boolean playing;
    private final float defaultVolume = 0.5f;

    public void playMusic(TRACK track) {
        if (playing) {
            stopMusic();
        }
        if (track == TRACK.MAINMENU) {
            music = Gdx.audio.newMusic(Gdx.files.internal("audio/music/mainmenu.mp3"));
            music.setVolume(defaultVolume*1.5f);
        } else if (track == TRACK.GAMEOVER) {
            music = Gdx.audio.newMusic(Gdx.files.internal("audio/music/gameover.mp3"));
        } else if (track == TRACK.VICTORY) {
            music = Gdx.audio.newMusic(Gdx.files.internal("audio/music/victory.mp3"));
        } else if (track == TRACK.LEVEL1) {
            music = Gdx.audio.newMusic(Gdx.files.internal("audio/music/level1.mp3"));
        } else if (track == TRACK.LEVEL2) {
            music = Gdx.audio.newMusic(Gdx.files.internal("audio/music/level2.mp3"));
        } else if (track == TRACK.LEVEL3) {
            music = Gdx.audio.newMusic(Gdx.files.internal("audio/music/level3.mp3"));
            music.setVolume(defaultVolume*2.5f);
        } else if (track == TRACK.FINISHGAME) {
            music = Gdx.audio.newMusic(Gdx.files.internal("audio/music/finishGame.mp3"));
            music.setVolume(defaultVolume*2f);
        }

        if (track != TRACK.MAINMENU && track != TRACK.LEVEL3 && track != TRACK.FINISHGAME) {
            music.setVolume(defaultVolume);
        }
        music.setLooping(true);
        music.play();
        playing = true;
    }

    public void stopMusic() {
        music.setVolume(defaultVolume/2);
        music.stop();
        playing = false;
    }
}
