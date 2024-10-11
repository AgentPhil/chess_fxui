package chess.ui;

import chess.core.Game;
import chess.core.Move;
import chess.core.Player;

public class FXPlayer extends Player
{
	private final FXChessboard fxChessboard;
	
	
	public FXPlayer(Game game)
	{
		super(game);
		try {
			FXChess fxChess = (FXChess) game.getUi();
			fxChessboard = fxChess.getFxChessboard();
			
		} catch (ClassCastException e) {
			throw new IllegalArgumentException("ChessUserInterface is no instance of FXChess");
		}
	}
	
	@Override
	public Move makeMove()
	{
		return fxChessboard.waitPlayerMove(getGame());
	}
}
