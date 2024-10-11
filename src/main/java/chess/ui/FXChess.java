package chess.ui;

import chess.core.ChessUserInterface;
import chess.core.Chessboard;
import chess.core.Move;
import chess.core.Team;

public class FXChess implements ChessUserInterface
{
	private final FXChessboard fxChessboard;
	
	public FXChess() {
		//launches the Application
		fxChessboard = FXChessboard.getInstance();
		System.out.println("Initilized FX Application");
	}
	
	@Override
	public void paint(Chessboard chessboard, Team playingTeam, Move move)
	{
		this.fxChessboard.paint(chessboard, playingTeam, move);
	}
	
	@Override
	public void won(Team winningTeam)
	{
		this.fxChessboard.won(winningTeam);
	}
	
	
	@Override
	public void showExceptionStackTrace(Exception e)
	{
		this.fxChessboard.showExceptionStackTrace(e);
	}
	
	FXChessboard getFxChessboard()
	{
		return fxChessboard;
	}
}
