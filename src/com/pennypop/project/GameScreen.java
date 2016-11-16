package com.pennypop.project;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class GameScreen implements Screen {
	
	private Game game;
	private Screen parent;
	
	private SpriteBatch spriteBatch;
	
	private ShapeRenderer shapeRenderer;
	
	private ConnectFourBoard gameBoard;
	
	private BitmapFont font;
	
	private int boardX = (Gdx.graphics.getWidth())/4;
	private int boardY = (Gdx.graphics.getHeight())/4;
	private int boardPixelWidth = ((Gdx.graphics.getWidth())/2);
	private int boardPixelHeight = ((Gdx.graphics.getHeight())/2);
	
	private final int boardWidth = 6;
	private final int boardHeight = 7;
	private final int boardWinningSize = 4;
	
	public GameScreen() {
		setDimensions();
	}
	
	public GameScreen(Game game, Screen parent) {
		this.game = game;
		this.parent = parent;
		
		font = new BitmapFont(Gdx.files.internal("font.fnt"),Gdx.files.internal("font.png"),false);
		
		spriteBatch = new SpriteBatch();
		
		shapeRenderer = new ShapeRenderer();
		
		gameBoard = new ConnectFourBoard(boardWidth, boardHeight, boardWinningSize);
		setDimensions();
	}
	
	public void setDimensions() {
		boardX = (Gdx.graphics.getWidth())/4;
		boardY = (Gdx.graphics.getHeight())/8;
		boardPixelWidth = ((Gdx.graphics.getWidth())/2);
		boardPixelHeight = ((2*Gdx.graphics.getHeight())/3);
	}

	@Override
	public void dispose() {
		spriteBatch.dispose();
		shapeRenderer.dispose();
		parent.dispose();
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float arg0) {
		drawSprites();
		handleInput();
	}
	
	public void handleInput() {
		if(Gdx.input.justTouched()) {
			int x = Gdx.input.getX();
			int y = Gdx.input.getY();
			GameState gameState = gameBoard.getGameState();
			if(x > boardX && y > boardY &&
					x < boardX+boardPixelWidth && y < boardY+boardPixelHeight) {
				if(gameState == GameState.NOT_OVER) {
					int slotTouched = (int)Math.floor((gameBoard.getWidth())*((float)(x-(boardX))/(float)(boardPixelWidth)));
					if(gameBoard.legalMove(slotTouched)) {
						gameBoard.makeMove(slotTouched);
					}
					else {
						System.out.println("Illegal Move!");
					}
				}
			}
			if(gameState != GameState.NOT_OVER) {
				game.setScreen(parent);
			}
		}
	}
	
	public void drawSprites() {
		spriteBatch.begin();
		gameBoard.renderSprites(boardX, boardY, boardPixelWidth, boardPixelHeight, spriteBatch);
		
		font.setColor(com.badlogic.gdx.graphics.Color.BLACK);
		font.draw(spriteBatch, "Connect Four Game", (Gdx.graphics.getWidth()/2)-(font.getBounds("Connect Four Game").width/2), (9*Gdx.graphics.getHeight()/10));
		
		GameState gameState = gameBoard.getGameState();
		
		switch(gameState) {
		case RED_WON:
			font.draw(spriteBatch, "Red Won!", 50, 100);
			break;
		case YELLOW_WON:
			font.draw(spriteBatch, "Yellow Won!", 50, 100);
			break;
		case TIE:
			font.draw(spriteBatch, "Tie!", 50, 100);
			break;
		case NOT_OVER:
			break;
		}
		
		if(gameState != GameState.NOT_OVER) {
			font.draw(spriteBatch, "Click to return to the Main Screen", (Gdx.graphics.getWidth()/2)-(font.getBounds("Click to return to the Main Screen").width/2), (17*Gdx.graphics.getHeight()/20));
		}
		else {
			if(gameBoard.isYellowsTurn()) {
				font.draw(spriteBatch, "Yellow's Turn", 50, 500);
			}
			else {
				font.draw(spriteBatch, "Red's Turn", 50, 500);
			}
		}
		
		spriteBatch.end();
		
		shapeRenderer.begin(ShapeType.Line);
		gameBoard.renderShapes(boardX, boardY, boardPixelWidth, boardPixelHeight, shapeRenderer);
		shapeRenderer.end();
	}

	@Override
	public void resize(int arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}
}
