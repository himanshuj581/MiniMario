package com.minimario.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Random;

public class MiniMario extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] mario;
	int marioState=0;
	int pause=0;
	float gravity = 0.2f;
	float velocity = 0f;
	int marioY;
	Random random;
	Rectangle marioRectangle;
	int score=0;
	BitmapFont font;
	int gameState;
	Texture dizzy;

	ArrayList<Integer> coinX = new ArrayList<>();
	ArrayList<Integer> coinY = new ArrayList<>();
	ArrayList<Rectangle> coinRectangle = new ArrayList<>();
	int coinCount=0;
	Texture coin;

	ArrayList<Integer> bombX = new ArrayList<>();
	ArrayList<Integer> bombY = new ArrayList<>();
	ArrayList<Rectangle> bombRectangle = new ArrayList<>();
	int bombCount=0;
	Texture bomb;

	
	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		coin = new Texture("coin.png");
		bomb = new Texture(("bomb.png"));
		mario = new Texture[4];
		mario[0] = new Texture("frame-1.png");
		mario[1] = new Texture("frame-2.png");
		mario[2] = new Texture("frame-3.png");
		mario[3] = new Texture("frame-4.png");
		marioY = Gdx.graphics.getHeight()/2;
		random = new Random();

		dizzy = new Texture("dizzy-1.png");

		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);

	}

	public void makeCoin(){
		float height = random.nextFloat()*Gdx.graphics.getHeight();
		coinY.add((int)height);
		coinX.add(Gdx.graphics.getWidth());
	}

	public void makeBomb(){
		float height = random.nextFloat()*Gdx.graphics.getHeight();
		bombY.add((int)height);
		bombX.add(Gdx.graphics.getWidth());
	}
	@Override
	public void render () {
		batch.begin();
		batch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

		if(gameState==1){
			//Game is live
			//bomb
			if(bombCount<250){
				bombCount++;
			}
			else{
				bombCount=0;
				makeBomb();
			}
			bombRectangle.clear();
			for(int i=0;i<bombX.size();i++){
				batch.draw(bomb,bombX.get(i),bombY.get(i));
				bombX.set(i,bombX.get(i)-12);
				bombRectangle.add(new Rectangle(bombX.get(i),bombY.get(i),bomb.getWidth(),bomb.getHeight()));
			}


			//coins
			if(coinCount<100){
				coinCount++;
			}
			else{
				coinCount=0;
				makeCoin();
			}
			coinRectangle.clear();
			for(int i=0;i<coinX.size();i++){
				batch.draw(coin,coinX.get(i),coinY.get(i));
				coinX.set(i,coinX.get(i)-8);
				coinRectangle.add(new Rectangle(coinX.get(i),coinY.get(i),coin.getWidth(),coin.getHeight()));
			}
			if(Gdx.input.justTouched()){
				velocity=-10;
			}

			if(pause<4){
				pause++;
			}
			else {
				pause = 0;
				if (marioState < 3) {
					marioState++;
				} else {
					marioState = 0;
				}
			}

			velocity+=gravity;
			marioY -= velocity;

			if(marioY<=0){
				marioY = 0;
			}
		}
		else if(gameState==0){
			//Waiting to start
			if(Gdx.input.justTouched())
				gameState=1;
		}
		else if(gameState==2){
			// Gameover Situtation
			if(Gdx.input.justTouched()) {
				gameState = 1;
				marioY =Gdx.graphics.getHeight()/2;
				score=0;
				velocity=0;
				coinX.clear();
				coinY.clear();
				coinRectangle.clear();
				coinCount=0;
				bombX.clear();
				bombY.clear();
				bombRectangle.clear();
				bombCount=0;
			}
		}

		if(gameState == 2){
			batch.draw(dizzy,Gdx.graphics.getWidth()/3-mario[0].getWidth()/2,marioY);
		}else{
			batch.draw(mario[marioState],Gdx.graphics.getWidth()/3-mario[0].getWidth()/2,marioY);
		}
		marioRectangle = new Rectangle(	Gdx.graphics.getWidth()/3-mario[0].getWidth()/2,marioY,mario[marioState].getWidth(),mario[marioState].getHeight());

		for(int i=0;i<coinRectangle.size();i++){
			if(Intersector.overlaps(marioRectangle,coinRectangle.get(i))){
				score++;
				coinRectangle.remove(i);
				coinX.remove(i);
				coinY.remove((i));
				break;
			}
		}

		for(int i=0;i<bombRectangle.size();i++){
			if(Intersector.overlaps(marioRectangle,bombRectangle.get(i))){
				gameState=2;
			}
		}

		font.draw(batch, String.valueOf(score),100,200);

		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();

	}
}
