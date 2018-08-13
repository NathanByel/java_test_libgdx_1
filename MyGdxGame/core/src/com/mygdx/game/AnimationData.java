package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Array;

public class AnimationData {
    private int id;
    private boolean loop;
    private Animation<Texture> frames = null;
    private float frameDuration = 1f;

    public AnimationData(String[] files, boolean loop) {
        /*this.loop = loop;
        frameDuration = frame
        if(frames == null) {
            Array<Texture> textureFiles = new Array<Texture>();
            for (int i = 0; i < files.length; i++) {
                textureFiles.add(new Texture(Gdx.files.internal(files[i])));
            }
            frames = new Animation<Texture>(0.07f, textureFiles);
            textureFiles = null;
        }*/
    }

    public void setFrameDuration(float frameDuration) {
        this.frameDuration = frameDuration;
        frames.setFrameDuration(frameDuration);
    }

    /*public Texture getFrame() {
        return frames.getKeyFrame(stateTime, loop);
    }*/
}
