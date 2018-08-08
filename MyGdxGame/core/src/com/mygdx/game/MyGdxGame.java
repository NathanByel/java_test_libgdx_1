package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Random;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Cat[] cats = new Cat[5];
	Random rand = new Random();
	@Override
	public void create () {
		batch = new SpriteBatch();

		for (int i = 0; i < cats.length; i++) {
			cats[i] = new Cat(rand.nextInt(400), rand.nextInt(400) );

			cats[i].goTo(rand.nextInt(400), rand.nextInt(400));
		}



		while(true) {
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
		}
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		for (Cat cat: cats) {
			cat.render(batch);
		}

		//batch.draw(img, 0, 0);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
