package com.pennypop.project;

import java.awt.Point;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class ConnectFourBoard {
	private int turnNumber = 1;
	
	private PieceState[][] pieceStates;
	
	private int gameWidth;
	private int gameHeight;
	private int winningSize;
	private GameState gameState;
	
	private Texture yellowPiece;
	private Texture redPiece;
	
	public ConnectFourBoard(int width, int height, int winningSize) {
		gameWidth = width;
		gameHeight = height;
		this.winningSize = winningSize;
		pieceStates = new PieceState[width][height];
		reset();
		
		yellowPiece = new Texture(Gdx.files.local("yellow.png"));
		redPiece = new Texture(Gdx.files.local("red.png"));
	}
	
	public ConnectFourBoard(ConnectFourBoard gameBoard) {
		turnNumber = gameBoard.turnNumber;
		
		gameWidth = gameBoard.gameWidth;
		gameHeight = gameBoard.gameHeight;
		winningSize = gameBoard.winningSize;
		gameState = gameBoard.gameState;
		
		pieceStates = new PieceState[gameWidth][gameHeight];
		for(int i = 0; i < gameWidth; i++) {
			for(int j = 0; j < gameHeight; j++) {
				pieceStates[i][j] = gameBoard.pieceStates[i][j];
			}
		}
	}
	
	public void reset() {
		gameState = GameState.NOT_OVER;
		for(int i = 0; i < gameWidth; i++) {
			for(int j = 0; j < gameHeight; j++) {
				pieceStates[i][j] = PieceState.EMPTY;
			}
		}
	}
	
	//returns true if it is a legal move
	public boolean legalMove(int slot) {
		return (slot >= 0 && slot < gameWidth && pieceStates[slot][gameHeight-1] == PieceState.EMPTY);
	}
	
	
	//returns true if the move is success (not successful if the spot is full)
	public boolean makeMove(int slot) {
		if(gameState != GameState.NOT_OVER) {
			return false;
		}
		for(int i = 0; i < gameHeight; i++) {
			if(pieceStates[slot][i] == PieceState.EMPTY) {
				if(isYellowsTurn()) {
					pieceStates[slot][i] = PieceState.YELLOW;
				}
				else {
					pieceStates[slot][i] = PieceState.RED;
				}
				break;
			}
			else if(i+1 == gameHeight) {
				return false;
			}
		}
		turnNumber++;
		gameState = winnerFromLastMove(slot);
		return true;
	}
	
	public boolean unmakeMove(int slot) {
		for(int i = gameHeight-1; i >= 0; i--) {
			if(pieceStates[slot][i] != PieceState.EMPTY) {
				pieceStates[slot][i] = PieceState.EMPTY;
				break;
			}
			else if(0 == gameHeight) {
				return false;
			}
		}
		turnNumber--;
		gameState = winnerFromLastMove(slot);
		return true;
	}
	
	//returns true if it is red's turn
	public boolean isYellowsTurn() {
		return (turnNumber%2) != 0;
	}
	
	public void renderShapes(int x, int y, int screenWidth, int screenHeight, ShapeRenderer shapeRenderer) {
		shapeRenderer.setColor(com.badlogic.gdx.graphics.Color.BLACK);
		for(int i = 0; i < gameWidth; i++) {
			for(int j = 0; j < gameHeight; j++) {
				shapeRenderer.line((((i)*screenWidth)/gameWidth)+x, (((j)*screenHeight)/gameHeight)+y,
						(((i+1)*screenWidth)/gameWidth)+x, (((j)*screenHeight)/gameHeight)+y);
				
				shapeRenderer.line((((i)*screenWidth)/gameWidth)+x, (((j)*screenHeight)/gameHeight)+y,
						(((i)*screenWidth)/gameWidth)+x, (((j+1)*screenHeight)/gameHeight)+y);
			}
		}
		shapeRenderer.line(x, (screenHeight)+y,
				(screenWidth)+x, (screenHeight)+y);
		
		shapeRenderer.line((screenWidth)+x, y,
				(screenWidth)+x, (screenHeight)+y);
	}
	
	public void renderSprites(int x, int y, int screenWidth, int screenHeight, SpriteBatch spriteBatch) {		
		float spriteOffsetX = (screenWidth/(gameWidth*4));
		float spriteOffsetY = (screenHeight/(gameHeight*4));
		float spriteWidth = (screenWidth/(gameWidth*2));//how much space each sprite takes up in pixels
		float spriteHeight = (screenHeight/(gameHeight*2));
		for(int i = 0; i < gameWidth; i++) {
			for(int j = 0; j < gameHeight; j++) {
				if(pieceStates[i][j] == PieceState.YELLOW) {
					spriteBatch.draw(yellowPiece, spriteOffsetX+x+((i*screenWidth)/gameWidth), spriteOffsetY+y+((j*screenHeight)/gameHeight), spriteWidth, spriteHeight);
				}
				else if(pieceStates[i][j] == PieceState.RED) {
					spriteBatch.draw(redPiece, spriteOffsetX+x+((i*screenWidth)/gameWidth), spriteOffsetY+y+((j*screenHeight)/gameHeight), spriteWidth, spriteHeight);
				}
				else {
					break;
				}
			}
		}
	}
	
	public boolean boardFull() {
		for(int i = 0; i < gameWidth; i++) {
			if(pieceStates[i][gameHeight-1] == PieceState.EMPTY) {
				return false;
			}
		}
		return true;
	}
	
	//determine the winner from the last move
	public GameState winnerFromLastMove(int lastMoveSlot) {
		for(int i = gameHeight-1; i >= 0; i--) {
			if(pieceStates[lastMoveSlot][i] != PieceState.EMPTY) {
				PieceState lastMoveState = pieceStates[lastMoveSlot][i];
				//check for matching pieces below
				int belowMatches = 0;
				for(int j = i-1; j >= 0; j--) {
					if(pieceStates[lastMoveSlot][j] == lastMoveState) {
						belowMatches++;
					}
					else {
						break;
					}
				}
				//System.out.println("below: " + belowMatches);
				//check for matching pieces to the side
				int sideMatches = 0;
				for(int j = lastMoveSlot-1; j >= 0; j--) {
					if(pieceStates[j][i] == lastMoveState) {
						sideMatches++;
					}
					else {
						break;
					}
				}
				for(int j = lastMoveSlot+1; j < gameWidth; j++) {
					if(pieceStates[j][i] == lastMoveState) {
						sideMatches++;
					}
					else {
						break;
					}
				}
				//System.out.println("side: " + sideMatches);
				//check for matching pieces down and left
				int downLeftMatches = 0;
				for(int j = i-1; j >= 0 && lastMoveSlot-(i-j) >= 0; j--) {
					if(pieceStates[lastMoveSlot-(i-j)][j] == lastMoveState) {
						downLeftMatches++;
					}
					else {
						break;
					}
				}
				//System.out.println("downLeft: " + downLeftMatches);
				//check for matching pieces to up and right
				int upRightMatches = 0;
				for(int j = i+1; j < gameHeight && lastMoveSlot+(j-i) < gameWidth; j++) {
					if(pieceStates[lastMoveSlot+(j-i)][j] == lastMoveState) {
						upRightMatches++;
					}
					else {
						break;
					}
				}
				//System.out.println("upRight: " + upRightMatches);
				//check for matching pieces to the down and right
				int downRightMatches = 0;
				for(int j = i-1; j >= 0 && lastMoveSlot+(i-j) < gameWidth; j--) {
					if(pieceStates[lastMoveSlot+(i-j)][j] == lastMoveState) {
						downRightMatches++;
					}
					else {
						break;
					}
				}
				//System.out.println("downRight: " + downRightMatches);
				//check for matching pieces up and left
				int upLeftMatches = 0;
				for(int j = i+1; j < gameHeight && lastMoveSlot-(j-i) >= 0; j++) {
					if(pieceStates[lastMoveSlot-(j-i)][j] == lastMoveState) {
						upLeftMatches++;
					}
					else {
						break;
					}
				}
				//System.out.println("upLeft: " + upLeftMatches);
				//check if the highest pieces matches ends the game
				if(sideMatches+1 >= winningSize || belowMatches+1 >= winningSize ||
						upRightMatches+downLeftMatches+1 >= winningSize ||
						upLeftMatches+downRightMatches+1 >= winningSize) {
					if(lastMoveState == PieceState.YELLOW) {
						return GameState.YELLOW_WON;
					}
					else {
						return GameState.RED_WON;
					}
				}
				else if(i+1 == gameHeight && boardFull()) {
					return GameState.TIE;
				}
				break;
			}
		}
		return GameState.NOT_OVER;
	}
	
	public int getWidth() {
		return gameWidth;
	}
	
	public GameState getGameState() {
		return gameState;
	}
	
	public String boardStateString() {
		String out = "board state:\n";
		for(int i = gameHeight-1; i >= 0; i--) {
			for(int j = 0; j < gameWidth ; j++) {
				switch(pieceStates[j][i]) {
				case RED:
					out += "2";
					break;
				case YELLOW:
					out += "1";
					break;
				case EMPTY:
					out += "0";
					break;
				}
			}
			out += "\n";
		}
		return out;
	}
	
	//evaluate who is winning on the board and by how much
	//Yellow = position, Red = negative
	public int evaluate() {
		if(gameState != GameState.NOT_OVER) {
			switch(gameState) {
			case YELLOW_WON:
				return Integer.MAX_VALUE;
			case RED_WON:
				return Integer.MIN_VALUE;
			case TIE:
				return 0;
			default:
				return 0;
			}
		}
		else {
			ArrayList<Point> visited = new ArrayList<Point>();
			int score = 0;
			for(int i = 0; i < gameWidth; i++) {
				for(int j = 0; j < gameHeight; j++) {
					int subscore = 0;
					if(pieceStates[i][j] != PieceState.EMPTY &&
							!visited.contains(new Point(i, j))) {
						visited.add(new Point(i, j));
						PieceState lastMoveState = pieceStates[i][j];
						//check for matching pieces below
						int belowMatches = 0;
						for(int k = j-1; k >= 0; k--) {
							if(pieceStates[i][k] == lastMoveState) {
								belowMatches++;
								visited.add(new Point(i, k));
							}
							else {
								break;
							}
						}
						subscore += Math.pow(belowMatches+1, 2);
						//System.out.println("below: " + belowMatches);
						//check for matching pieces to the side
						boolean openside = false;
						int sideMatches = 0;
						for(int k = i-1; k >= 0; k--) {
							if(pieceStates[k][j] == lastMoveState) {
								sideMatches++;
								visited.add(new Point(k, j));
							}
							else if(pieceStates[k][j] == PieceState.EMPTY) {
								openside = true;
							}
							else {
								break;
							}
						}
						for(int k = i+1; k < gameWidth; k++) {
							if(pieceStates[k][j] == lastMoveState) {
								sideMatches++;
								visited.add(new Point(k, j));
							}
							else if(pieceStates[k][j] == PieceState.EMPTY) {
								openside = true;
							}
							else {
								break;
							}
						}
						if(openside) {
							subscore += Math.pow(sideMatches+1, 2);
						}
						//System.out.println("side: " + sideMatches);
						openside = false;
						//check for matching pieces down and left
						int downLeftMatches = 0;
						for(int k = j-1; k >= 0 && i-(j-k) >= 0; k--) {
							if(pieceStates[i-(j-k)][k] == lastMoveState) {
								downLeftMatches++;
								visited.add(new Point(i-(j-k), k));
							}
							else if(pieceStates[i-(j-k)][k] == PieceState.EMPTY) {
								openside = true;
							}
							else {
								break;
							}
						}
						//System.out.println("downLeft: " + downLeftMatches);
						//check for matching pieces to up and right
						int upRightMatches = 0;
						for(int k = j+1; k < gameHeight && i+(k-j) < gameWidth; k++) {
							if(pieceStates[i+(k-j)][k] == lastMoveState) {
								upRightMatches++;
								visited.add(new Point(i+(k-j), k));
							}
							else if(pieceStates[i-(j-k)][k] == PieceState.EMPTY) {
								openside = true;
							}
							else {
								break;
							}
						}
						if(openside) {
							subscore += Math.pow(upRightMatches+downLeftMatches+1, 2);
						}
						//System.out.println("upRight: " + upRightMatches);
						//check for matching pieces to the down and right
						int downRightMatches = 0;
						openside = false;
						for(int k = j-1; k >= 0 && i+(j-k) < gameWidth; k--) {
							if(pieceStates[i+(j-k)][k] == lastMoveState) {
								downRightMatches++;
								visited.add(new Point(i+(j-k), k));
							}
							else if(pieceStates[i+(j-k)][k] == PieceState.EMPTY) {
								openside = true;
							}
							else {
								break;
							}
						}
						//System.out.println("downRight: " + downRightMatches);
						//check for matching pieces up and left
						int upLeftMatches = 0;
						for(int k = j+1; k < gameHeight && i-(k-j) >= 0; k++) {
							if(pieceStates[i-(k-j)][k] == lastMoveState) {
								upLeftMatches++;
								visited.add(new Point(i-(k-j), k));
							}
							else if(pieceStates[i-(k-j)][k] == PieceState.EMPTY) {
								openside = true;
							}
							else {
								break;
							}
						}
						if(openside) {
							subscore += Math.pow(upLeftMatches+downRightMatches+1, 2);
						}
						//System.out.println("upLeft: " + upLeftMatches);
						//check if the highest pieces matches ends the game
						if(sideMatches+1 >= winningSize || belowMatches+1 >= winningSize ||
								upRightMatches+downLeftMatches+1 >= winningSize ||
								upLeftMatches+downRightMatches+1 >= winningSize) {
							if(lastMoveState == PieceState.YELLOW) {
								return Integer.MAX_VALUE;
							}
							else {
								return Integer.MIN_VALUE;
							}
						}
						else if(i+1 == gameHeight && boardFull()) {
							return 0;
						}
						if(lastMoveState == PieceState.YELLOW) {
							score += subscore;
						}
						else if(lastMoveState == PieceState.RED) {
							score -= subscore;
						}
						//System.out.println("subscore: " + subscore);
					}
				}
			}
			return score;
		}
	}
}
