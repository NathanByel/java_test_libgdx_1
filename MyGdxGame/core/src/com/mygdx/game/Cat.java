package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class Cat {
    public static final int CAT_STATE_STANDING    = 0;
    public static final int CAT_STATE_WALK        = 1;
    public static final int CAT_STATE_GO_TO       = 2;
    public static final int CAT_STATE_EAT         = 3;
    public static final int CAT_STATE_DEAD        = 4;

    private int catState = CAT_STATE_STANDING;
    Texture currentFrame;

    boolean textureFlip = false;
    int currentX, currentY;
    int newX, newY;

    Animation<Texture> deadAnimation;
    Animation<Texture> fallAnimation;
    Animation<Texture> hurtAnimation;
    Animation<Texture> idleAnimation;
    Animation<Texture> jumpAnimation;
    Animation<Texture> runAnimation;
    Animation<Texture> slideAnimation;
    Animation<Texture> walkAnimation;

    float stateTime = 0;


    int pointX, pointY;

    public Cat(int x, int y) {
        this.currentX = x;
        this.currentY = y;
        this.newX = x;
        this.newY = y;

        Array<Texture>frames = new Array<Texture>();
        for(int i = 0; i < 10; i++) {
            frames.add( new Texture(Gdx.files.internal("cat/Dead ("+(i+1)+").png")));
        }
        deadAnimation = new Animation<Texture>( 0.07f, frames);
        frames.clear();

        for(int i = 0; i < 10; i++) {
            frames.add( new Texture(Gdx.files.internal("cat/Walk ("+(i+1)+").png")));
        }
        walkAnimation = new Animation<Texture>( 0.05f, frames);
        frames.clear();

        for(int i = 0; i < 10; i++) {
            frames.add( new Texture(Gdx.files.internal("cat/Idle ("+(i+1)+").png")));
        }
        idleAnimation = new Animation<Texture>( 0.1f, frames);
        frames.clear();
    }

    public void goTo(int x, int y) {
        this.newX = x;
        this.newY = y;
        catState = CAT_STATE_GO_TO;
    }


    boolean f = true;
    int oldX=0;
    public void render(SpriteBatch batch) {
        //batch.draw(walkFrames[0], 0, 0); //, 100, 100);

        stateTime += Gdx.graphics.getDeltaTime(); // #15
        update();
        /*if(f) {
            currentFrame = walkAnimation.getKeyFrame(stateTime, true);
            //if( walkAnimation.isAnimationFinished(stateTime) ) {
            //    f = false;
            //    stateTime = 0;
            //}

            currentX += 2;
            if(currentX > 400) {
                currentX = 0;
                oldX = 0;
            }

            if(currentX > oldX + 100) {
                oldX = currentX;
                f = false;
                stateTime = 0;
            }

        } else {
            currentFrame = deadAnimation.getKeyFrame(stateTime, false);
            if( deadAnimation.isAnimationFinished(stateTime) ) {
                f = true;
                stateTime = 0;
            }
        }*/


        //batch.draw(currentFrame, currentX, currentY, 100,100); // #17
        batch.draw (currentFrame, currentX, currentY, 100, 100, 0, 0, currentFrame.getWidth(),
                currentFrame.getHeight(), textureFlip, false);
    }

    public void update() {
        switch(catState) {
            case CAT_STATE_STANDING:
                currentFrame = idleAnimation.getKeyFrame(stateTime, true);
                break;

            case CAT_STATE_WALK:

                break;

            case CAT_STATE_GO_TO:
                if((currentX != newX) || (currentY != newY)) {
                    if (currentX < newX) {
                        currentX++;
                        textureFlip = false;
                    } else if(currentX > newX) {
                        currentX--;
                        textureFlip = true;
                    }

                    if (currentY < newY) {
                        currentY++;
                    } else if(currentY > newY) {
                        currentY--;
                    }

                    currentFrame = walkAnimation.getKeyFrame(stateTime, true);
                } else {
                    catState = CAT_STATE_STANDING;
                    stateTime = 0;
                }
                break;

            case CAT_STATE_EAT:
                break;

            case CAT_STATE_DEAD:
                currentFrame = deadAnimation.getKeyFrame(stateTime, false);
                break;
        }
    }

    public int getCatState() {
        return catState;
    }
}
