package com.mygdx.game.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class HotDog {
    private static final float SCALE_FACTOR = 0.1f;
    private static Texture texture = null;

    private Texture currentFrame;
    private boolean textureFlip = false;
    private int currentX,
                currentY,
                scaledWidth,
                scaledHeight;


    public HotDog(int x, int y) {
        if(texture == null) {
            texture = new Texture(Gdx.files.internal("food/hot_dog.png"));
        }
        currentX = x;
        currentY = y;

    }

    public void render(SpriteBatch batch) {
        update();

        scaledWidth  = (int)(currentFrame.getWidth() * SCALE_FACTOR);
        scaledHeight = (int)(currentFrame.getHeight() * SCALE_FACTOR);

        /*fullnessBar.setPosition( currentX + scaledWidth/2 - fullnessBar.getWidth()/2, currentY + scaledHeight);
        fullnessBar.setValue(fullness);
        fullnessBar.act(Gdx.graphics.getDeltaTime());
        fullnessBar.draw(batch, 1f);
        */
        batch.draw (texture, currentX, currentY, scaledWidth, scaledHeight, 0, 0, currentFrame.getWidth(),
                currentFrame.getHeight(), textureFlip, false);
    }

    private void update() {
        currentFrame = texture;
    }

    public int getCurrentX() {
        return currentX;
    }

    public int getCurrentY() {
        return currentY;
    }
}
