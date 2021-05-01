package com.r001.game1.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Random;

public class Game1 extends ApplicationAdapter {

	SpriteBatch batch;
	Texture background;
	Texture main;
	Texture obstacle;
	int gamestate = 0;
	float velocity = 20;
	float mainX ;
	Viewport viewport;
	int numberofobstacles = 4;
	float[] obsX = new float[numberofobstacles];
	float[] obsY = new float[numberofobstacles];
	float gap ;
	float distanceBetweenObstacles;
	Random randomGenerator;
	float obstacleVelocity = 25;
	float temp = 3.5f;
	boolean last = false;
	Rectangle[] obstacleRectangle;
	Rectangle mainRectangle;
	Circle mainCircle;
	long score  = 0;
	//ShapeRenderer shapeRenderer ;
    long startTime;
    long elapsedTime;
    BitmapFont font;
    BitmapFont highScore;
    BitmapFont font2 , toast;
    long setHighScore = 0;
    Texture gameOver;
    //Texture startGame;


	@Override
	public void create () {

		batch = new SpriteBatch();
		background = new Texture("bg.jpg");
		main = new Texture("Main.png");
		viewport = new StretchViewport(Gdx.graphics.getWidth() , Gdx.graphics.getHeight());
		mainX = viewport.getWorldWidth()/2;
		obstacle = new Texture("obstacle.png");
		gameOver = new Texture("gameover1.png");
		distanceBetweenObstacles = Gdx.graphics.getWidth() * 1/4 - 0.5f;

		randomGenerator = new Random();

		gap = main.getWidth() + 100;
		obsX[0] = 100;
		obsX[1] = 500;
		obsX[2] = 900;
		obsX[3] = 1400;

		mainCircle = new Circle();
		mainRectangle = new Rectangle();
		obstacleRectangle = new Rectangle[numberofobstacles];

        font = new BitmapFont();
        font.setColor(com.badlogic.gdx.graphics.Color.WHITE);
        font.getData().setScale(10);

        highScore = new BitmapFont();
        highScore.setColor(com.badlogic.gdx.graphics.Color.WHITE);
        highScore.getData().setScale(3);

        font2 = new BitmapFont();
        font2.setColor(com.badlogic.gdx.graphics.Color.WHITE);
        font2.getData().setScale(7);

        toast = new BitmapFont();
		toast.setColor(Color.BLACK);
		toast.getData().setScale(3);

		//shapeRenderer = new ShapeRenderer();
		for(int i = 0 ;i<numberofobstacles ; i++){

			obsY[i] = Gdx.graphics.getHeight() + 100;

			obstacleRectangle[i] = new Rectangle();
		}

         startTime = System.currentTimeMillis()- 1000;

	}


	public void startgame(){

		if(Gdx.input.justTouched()) {

			gamestate = 1;
			mainX = Gdx.graphics.getWidth() / 2 - main.getWidth() / 2;
			obsX[0] = 100;
			obsX[1] = 500;
			obsX[2] = 900;
			obsX[3] = 1400;

			for (int i = 0; i < 4; i++) {

				obsY[i] = Gdx.graphics.getHeight() + 100;
				obstacleRectangle[i] = new Rectangle();
			}
			startTime = System.currentTimeMillis();
		}

	}


	@Override
	public void render () {
		//Gdx.gl.glClearColor(1, 0, 0, 1);
		//Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();

		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.draw(main, mainX, 60);

		if (gamestate == 1) {

			obstacleVelocity = 25;

			elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
			Gdx.app.log("Time", String.valueOf(elapsedTime));
			score = elapsedTime;

			for (int i = 0; i < numberofobstacles; i++) {

				batch.draw(obstacle, obsX[i], obsY[i]);
				obsY[i] -= obstacleVelocity;

				if (obsY[i] < 0) {

					obsY[i] += Gdx.graphics.getHeight() + 100 + randomGenerator.nextFloat() * 1000;
				}
				if (obsX[3] >= Gdx.graphics.getWidth()) {

					last = true;
				} else if (obsX[0] <= 0) {

					last = false;
				}
				if (!last) {

					obsX[i] += temp;
				} else {

					obsX[i] -= temp;
				}
				obstacleRectangle[i] = new Rectangle(obsX[i], obsY[i], obstacle.getWidth(), obstacle.getHeight());
			}


			if (Gdx.input.isTouched()) {

				if (mainX <= 0 || mainX > Gdx.graphics.getWidth() - 50)

					gamestate = 2;

				else {

					if (Gdx.input.getX() < Gdx.graphics.getWidth() / 2) {

						mainX -= velocity;
					} else {

						mainX += velocity;
					}
				}


			}

		} else if (gamestate == 2) {

			batch.draw(gameOver, Gdx.graphics.getWidth() / 2 - gameOver.getWidth() / 2, Gdx.graphics.getHeight() / 2 - gameOver.getHeight() / 2);

			toast.draw(batch, "Tap to Play Again", Gdx.graphics.getWidth() / 2 - gameOver.getWidth() / 2 + 200 , Gdx.graphics.getHeight() / 2 - gameOver.getHeight() / 2 );

			if (setHighScore <= score) {

				setHighScore = score;
			}
			if (Gdx.input.justTouched()) {

				score = 0;
				obstacleVelocity = 0;
				startgame();
			}
		} else if (gamestate == 0) {

			score = 0;

			if (Gdx.input.isTouched()) {

				gamestate = 1;
			}
		}

		font.draw(batch, String.valueOf(score), 100, Gdx.graphics.getHeight() - 100);

		highScore.draw(batch, "High Score", Gdx.graphics.getWidth() - 250, Gdx.graphics.getHeight() - 50);

		font2.draw(batch, String.valueOf(setHighScore), Gdx.graphics.getWidth() - 150, Gdx.graphics.getHeight() - 120);

		mainCircle.set(mainX + main.getWidth() / 2, 60 + main.getHeight() / 2, main.getWidth() / 2);


        //mainRectangle.set(mainX,60,main.getWidth(),main.getHeight());
        /*shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(com.badlogic.gdx.graphics.Color.RED);
		//shapeRenderer.rect(mainRectangle.x , mainRectangle.y , mainRectangle.getWidth() , mainRectangle.getHeight());
		shapeRenderer.circle(mainCircle.x, mainCircle.y , mainCircle.radius);*/

		for (int i = 0; i < numberofobstacles; i++) {

			//shapeRenderer.rect(obsX[i], obsY[i], obstacle.getWidth(), obstacle.getHeight());

			if (Intersector.overlaps(mainCircle, obstacleRectangle[i])) {

				gamestate = 2;

			}
		}

		batch.end();

		//shapeRenderer.end();


	}
}
