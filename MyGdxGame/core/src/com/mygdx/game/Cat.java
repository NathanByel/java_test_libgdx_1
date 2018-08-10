package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class Cat {
    private int fullness = 100;
    private int health = 100;
    private int hungerSpeed = 1;

    private HealthBar healthBar;
    private HealthBar fullnessBar;

    private boolean catIsAlive = true;
    private static final float SCALE_FACTOR = 0.2f;

    private Texture currentFrame;
    private long timer = 0;

    private int scaledWidth;
    private int scaledHeight;

    boolean textureFlip = false;
    int currentX, currentY;
    int newX, newY;

    private static boolean animationsLoaded = false;
    private static Animation<Texture> deadAnimation  = null;
    private static Animation<Texture> fallAnimation  = null;
    private static Animation<Texture> hurtAnimation  = null;
    private static Animation<Texture> idleAnimation  = null;
    private static Animation<Texture> jumpAnimation  = null;
    private static Animation<Texture> runAnimation   = null;
    private static Animation<Texture> slideAnimation = null;
    private static Animation<Texture> walkAnimation  = null;

    private enum AnimState {
        DEAD_ANIMATION,
        FALL_ANIMATION,
        HURT_ANIMATION,
        IDLE_ANIMATION,
        JUMP_ANIMATION,
        RUN_ANIMATION,
        SLIDE_ANIMATION,
        WALK_ANIMATION
    }
    /*private enum AnimState {
        DEAD_ANIMATION( deadAnimation.getKeyFrame(this.stateTime, false)),
        FALL_ANIMATION,
        HURT_ANIMATION,
        IDLE_ANIMATION,
        JUMP_ANIMATION,
        RUN_ANIMATION,
        SLIDE_ANIMATION,
        WALK_ANIMATION;

        private float stateTime;
        private Texture getFrame(float stateTime) {
            this.stateTime = stateTime;
        }
    }*/

    private AnimState animState = AnimState.IDLE_ANIMATION;
    private float stateTime = 0;

    public enum CatState {
        STANDING,
        WALK_TO,
        RUN_TO,
        EAT,
        DEAD,
        PUNCHED
    }
    private CatState catState = CatState.STANDING;



    public Cat() {
        this(0,0);
    }

    public Cat(int x, int y) {
        this.currentX = x;
        this.currentY = y;
        this.newX = x;
        this.newY = y;

        healthBar = new HealthBar(50, 3, Color.GREEN, Color.BLACK);
        healthBar.setRange(1f, 100f);
        healthBar.setValue(health);

        fullnessBar = new HealthBar(50, 3, Color.BLUE, Color.BLACK);
        fullnessBar.setRange(1f, 100f);
        fullnessBar.setValue(fullness);

        if( !animationsLoaded ) {
            animationsLoaded = true;

            Array<Texture> frames = new Array<Texture>();
            for (int i = 0; i < 10; i++) {
                frames.add(new Texture(Gdx.files.internal("cat/Dead (" + (i + 1) + ").png")));
            }
            deadAnimation = new Animation<Texture>(0.07f, frames);
            frames.clear();

            for (int i = 0; i < 8; i++) {
                frames.add(new Texture(Gdx.files.internal("cat/Fall (" + (i + 1) + ").png")));
            }
            fallAnimation = new Animation<Texture>(0.07f, frames);
            frames.clear();

            for (int i = 0; i < 10; i++) {
                frames.add(new Texture(Gdx.files.internal("cat/Hurt (" + (i + 1) + ").png")));
            }
            hurtAnimation = new Animation<Texture>(0.07f, frames);
            frames.clear();

            for (int i = 0; i < 10; i++) {
                frames.add(new Texture(Gdx.files.internal("cat/Idle (" + (i + 1) + ").png")));
            }
            idleAnimation = new Animation<Texture>(0.1f, frames);
            frames.clear();

            for (int i = 0; i < 8; i++) {
                frames.add(new Texture(Gdx.files.internal("cat/Jump (" + (i + 1) + ").png")));
            }
            jumpAnimation = new Animation<Texture>(0.1f, frames);
            frames.clear();

            for (int i = 0; i < 8; i++) {
                frames.add(new Texture(Gdx.files.internal("cat/Run (" + (i + 1) + ").png")));
            }
            runAnimation = new Animation<Texture>(0.1f, frames);
            frames.clear();

            for (int i = 0; i < 10; i++) {
                frames.add(new Texture(Gdx.files.internal("cat/Slide (" + (i + 1) + ").png")));
            }
            slideAnimation = new Animation<Texture>(0.1f, frames);
            frames.clear();

            for (int i = 0; i < 10; i++) {
                frames.add(new Texture(Gdx.files.internal("cat/Walk (" + (i + 1) + ").png")));
            }
            walkAnimation = new Animation<Texture>(0.05f, frames);
            frames.clear();
        }
    }


    public void punch(int damage) {
        if(health > 0) {
            stateTime = 0;
            int dl = health - damage;
            if (dl > 0) {
                health = dl;
                changeCatState(CatState.PUNCHED);
                changeAnimState(AnimState.HURT_ANIMATION);
            } else {
                health = 0;
                changeCatState(CatState.DEAD);
                changeAnimState(AnimState.DEAD_ANIMATION);
            }
            System.out.printf("damage %d, health %d, dl %d\n", damage, health, dl);
        }
    }

    public void goTo(int x, int y, boolean run) {
        this.newX = x;
        this.newY = y;
        if (run) {
            changeCatState(CatState.RUN_TO);
            changeAnimState(AnimState.RUN_ANIMATION);
        } else {
            changeCatState(CatState.WALK_TO);
            changeAnimState(AnimState.WALK_ANIMATION);
        }
    }

    public boolean isCat(int x, int y) {
        return  (x >= currentX + scaledWidth/4) && (x <= currentX + scaledWidth - scaledWidth/4) &&
                (y >= currentY + scaledHeight/4) && (y <= currentY + scaledHeight - scaledHeight/4);
    }

    public void render(SpriteBatch batch) {
        stateTime += Gdx.graphics.getDeltaTime(); // #15
        update();

        scaledWidth  = (int)(currentFrame.getWidth() * SCALE_FACTOR);
        scaledHeight = (int)(currentFrame.getHeight() * SCALE_FACTOR);


        healthBar.setPosition( currentX + scaledWidth/2 - healthBar.getWidth()/2, currentY + scaledHeight + fullnessBar.getHeight()+2);
        healthBar.setValue(health);
        healthBar.act(Gdx.graphics.getDeltaTime());
        healthBar.draw(batch, 1f);

        fullnessBar.setPosition( currentX + scaledWidth/2 - fullnessBar.getWidth()/2, currentY + scaledHeight);
        fullnessBar.setValue(fullness);
        fullnessBar.act(Gdx.graphics.getDeltaTime());
        fullnessBar.draw(batch, 1f);

        batch.draw (currentFrame, currentX, currentY, scaledWidth, scaledHeight, 0, 0, currentFrame.getWidth(),
                currentFrame.getHeight(), textureFlip, false);
    }

    private boolean moveTo(boolean run) {
        int stepSize = run ? 2 : 1;

        if((currentX != newX) || (currentY != newY)) {
            if (currentX < newX) {
                currentX += stepSize;

                if(currentX > newX) {
                    currentX = newX;
                }
                textureFlip = false;
            } else if(currentX > newX) {
                currentX -= stepSize;
                if(currentX < newX) {
                    currentX = newX;
                }
                textureFlip = true;
            }

            if (currentY < newY) {
                currentY += stepSize;
                if(currentY > newY) {
                    currentY = newY;
                }
            } else if(currentY > newY) {
                currentY -= stepSize;
                if(currentY < newY) {
                    currentY = newY;
                }
            }

        } else {
            changeCatState(CatState.STANDING);
            changeAnimState(AnimState.IDLE_ANIMATION);
        }
        return true;
    }

    private CatState oldCatState = CatState.STANDING;
    private void changeCatState(CatState newState) {
        oldCatState = catState;
        stateTime = 0;
        catState = newState;
    }


    private AnimState oldAnimState = AnimState.IDLE_ANIMATION;
    private void changeAnimState(AnimState newState) {
        oldAnimState = animState;
        stateTime = 0;
        animState = newState;
    }





    public void update() {
        if(System.currentTimeMillis() > timer + 1000) {
            timer = System.currentTimeMillis();
            if(fullness > 0) {
                int df = fullness - hungerSpeed;
                if(df > 0) {
                    fullness = df;
                } else {
                    fullness = 0;
                }
            }
        }


        switch(catState) {
            case STANDING:
                break;

            case WALK_TO:
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
                } else {
                    changeCatState(CatState.STANDING);
                    changeAnimState(AnimState.IDLE_ANIMATION);
                }
                break;

            case RUN_TO:

                break;

            case PUNCHED:
                if(hurtAnimation.isAnimationFinished(stateTime)) {
                    changeCatState(oldCatState);// CatState.GO_TO);
                    changeAnimState(oldAnimState);// AnimState.WALK_ANIMATION);
                }
                break;

            case EAT:
                break;

            case DEAD:
                break;
        }


        switch (animState) {
            case DEAD_ANIMATION:
                currentFrame = deadAnimation.getKeyFrame(stateTime, false);
                break;

            case FALL_ANIMATION:
                currentFrame = fallAnimation.getKeyFrame(stateTime, false);
                break;

            case HURT_ANIMATION:
                currentFrame = hurtAnimation.getKeyFrame(stateTime, false);
                break;

            case IDLE_ANIMATION:
                currentFrame = idleAnimation.getKeyFrame(stateTime, true);
                break;

            case JUMP_ANIMATION:
                currentFrame = jumpAnimation.getKeyFrame(stateTime, false);
                break;

            case RUN_ANIMATION:
                currentFrame = runAnimation.getKeyFrame(stateTime, true);
                break;

            case SLIDE_ANIMATION:
                currentFrame = slideAnimation.getKeyFrame(stateTime, true);
                break;

            case WALK_ANIMATION:
                currentFrame = walkAnimation.getKeyFrame(stateTime, true);
                break;
        }
    }

    public CatState getCatState() {
        return catState;
    }
}
