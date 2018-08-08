package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

import java.util.Random;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	//Cat[] cats = new Cat[5];
	Random rand = new Random();

	Array<Cat> cats = new Array<Cat>();


	long timer = 0;

    // Пул для пуль.
    private final Pool<Cat> catPool = new Pool<Cat>() {
        @Override
        protected Cat newObject() {
            return new Cat();
        }
    };

    HealthBar healthBar;
	@Override
	public void create () {
        healthBar = new HealthBar(100, 3);
        healthBar.setPosition(10, Gdx.graphics.getHeight() - 20);
        //healthBar.setRange(1f, 100f);
        healthBar.setValue(1f);

		batch = new SpriteBatch();

		for (int i = 0; i < 5; i++) {
            catPool.obtain();
			cats.add( new Cat(rand.nextInt(400), rand.nextInt(400)) );

			//cats[i].goTo(rand.nextInt(400), rand.nextInt(400));
		}



		/*while(true) {
			try {
				Thread.sleep(1000);     //1000-задержка  на 1000 милисекуннд =1 сек
			} catch (InterruptedException e) {
				System.err.println(e.getMessage());
			}

			for (int i = 0; i < cats.length; i++) {
				if (cats[i].getCatState() == Cat.CAT_STATE_STANDING) {
					cats[i].goTo(rand.nextInt(400), rand.nextInt(400));
				}

			}
		}*/
	}

	long lastUpdate;
	@Override
	public void render () {

        // Убиваем котов
        if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
            for (int i = 0; i < cats.size; i++) {
                if( cats.get(i).isCat(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY()) ) {
                    cats.get(i).setState(Cat.CAT_STATE_DEAD);
                }
            }
        }

        // Создаем котов
        if(Gdx.input.isButtonPressed(Input.Buttons.RIGHT)){
            cats.add( new Cat(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY()) );
        }

        if(System.currentTimeMillis() > timer + 2000) {
            timer = System.currentTimeMillis();

            for (int i = 0; i < cats.size; i++) {
                if (cats.get(i).getCatState() == Cat.CAT_STATE_STANDING) {
                    cats.get(i).goTo(rand.nextInt(400), rand.nextInt(400));
                }
            }

            healthBar.setValue(healthBar.getValue() - 0.1f);
        }



		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		for (Cat cat: cats) {
			cat.render(batch);
		}

		long dt = System.currentTimeMillis() - lastUpdate;
        lastUpdate = System.currentTimeMillis();

        healthBar.act(dt);
        healthBar.draw(batch, 1f);

		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
