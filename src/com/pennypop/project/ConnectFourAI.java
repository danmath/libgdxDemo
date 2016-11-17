package com.pennypop.project;

public class ConnectFourAI {
	public static int[] getMoveScore(ConnectFourBoard gameBoard, int depth, int level) {
		int[] ans = new int[gameBoard.getWidth()];
		for(int i = 0; i < gameBoard.getWidth(); i++) {
			ans[i] = 0;
		}
		ConnectFourBoard gameBoarded = new ConnectFourBoard(gameBoard);
		
		return minimax(gameBoarded, depth, level);
	}
	
	public static int[] minimax(ConnectFourBoard gameBoard, int depth, int level) {
		int[] ans = new int[gameBoard.getWidth()];
		boolean yellowsTurn = gameBoard.isYellowsTurn();
		for(int i = 0; i < gameBoard.getWidth(); i++) {
			//System.out.println("loading turn: " + i + "width: " + gameBoard.getWidth());
			if(gameBoard.makeMove(i)) {
				//System.out.println("made move with: " + gameBoard.boardStateString());
				if(yellowsTurn) {
					ans[i] = min(gameBoard, depth-1, level+1);
				}
				else {
					ans[i] = max(gameBoard, depth-1, level+1);
				}
				gameBoard.unmakeMove(i);
			}
			else {
				//System.out.println("failed move with: " + gameBoard.boardStateString());
				ans[i] = 0;
			}
		}
		return ans;
	}
	
	//red's turn
	public static int min(ConnectFourBoard gameBoard, int depth, int level) {
		//System.out.println("state: " + gameBoard.boardStateString());
		int score = 0;
		GameState curState = gameBoard.getGameState();
		if(curState != GameState.NOT_OVER) {
			score += gameBoard.evaluate();
		}
		else if(depth == 0) {
			return 0;
		}
		else {
			int lowestScore = Integer.MAX_VALUE;
			for(int i = 0; i < gameBoard.getWidth(); i++) {
				if(gameBoard.makeMove(i)) {
					int curScore = max(gameBoard, depth-1, level+1);
					if(curScore < lowestScore) {
						lowestScore = curScore;
					}
					gameBoard.unmakeMove(i);
				}
			}
		}
		return score;
	}
	
	//yellow's turn
	public static int max(ConnectFourBoard gameBoard, int depth, int level) {
		//System.out.println("state: " + gameBoard.boardStateString());
		int score = 0;
		GameState curState = gameBoard.getGameState();
		if(curState != GameState.NOT_OVER) {
			score += gameBoard.evaluate();
		}
		else if(depth == 0) {
			return 0;
		}
		else {
			int highestScore = Integer.MIN_VALUE;
			for(int i = 0; i < gameBoard.getWidth(); i++) {
				if(gameBoard.makeMove(i)) {
					int curScore = min(gameBoard, depth-1, level+1);
					if(curScore < highestScore) {
						highestScore = curScore;
					}
					gameBoard.unmakeMove(i);
				}
				
			}
		}
		return score;
	}
}
