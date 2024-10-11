package chess.ui.FXComponents;

import chess.ui.FXChessboard;
import chess.core.Chessboard;
import chess.core.Chessman;
import chess.core.ChessmanType;
import chess.core.Exceptions.ChessmanNullException;
import chess.core.Exceptions.NoKingFoundException;
import chess.core.Exceptions.NullTeamException;
import chess.core.Exceptions.PositionNotOnChessboardException;
import chess.core.Game;
import chess.core.Move;
import chess.core.Team;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class FXChessboardCluster extends FXCluster
{
	private final FXBox[][] chessCluster;
	
	private final FXChessboard fxChessboard;
	
	private Game game = null;
	
	private boolean moveValid;
	
	private boolean undooMove;
	
	private int selectedFirstX = -1, selectedFirstY = -1, selectedSecondX = -1, selectedSecondY = -1;
	
	@SuppressWarnings("SuspiciousNameCombination") // Height is passed as Width due to a 90 degree turn
	public FXChessboardCluster(FXChessboard fxChessboard, Stage primaryStage, GridPane gridPane, double width, double height, int collunms, int rows, double edgeWidth, double edgeHeight) {
		super(primaryStage, gridPane, width, height, collunms, rows, edgeWidth, edgeHeight, true);
		this.fxChessboard = fxChessboard;
		this.chessCluster = getChessCluster();
		FXQuitButton quitButton = new FXQuitButton(edgeHeight, edgeHeight, primaryStage);
		gridPane.add(quitButton, collunms + 1, 0);
		getEdges()[rows + 2* collunms + 2] = quitButton;
		FXUndooButton undooButton = new FXUndooButton(edgeHeight, edgeHeight, primaryStage, this);
		gridPane.add(undooButton, 0, 0);
		getEdges()[0] = undooButton;
	}
	
	public void  paint(Move move) throws PositionNotOnChessboardException
	{
		refresh();
		super.paint();
		if (game != null)
		{
			for (int x = 0; x < chessCluster.length; x++)
			{
				for (int y = 0; y < chessCluster[x].length; y++)
				{
					FXBox box = chessCluster[x][y];
					Chessman chessman = game.getChessboard().getChessman(x, y);
					if (chessman != null)
					{
						if (chessman.inDanger())
						{
							box.setColorOuterState(FXBox.OuterState.INDANGER);
						}
					}
					box.paint();
				}
			}
		}
		if (move != null) {
			move = move.makeStatic();
			//Is being painted directly
			chessCluster[move.x1][move.y1].setColorInnerState(FXBox.InnerState.MOVE);
			chessCluster[move.x1][move.y1].setColorOuterState(FXBox.OuterState.MOVE);
			chessCluster[move.x2][move.y2].setColorInnerState(FXBox.InnerState.MOVE);
			chessCluster[move.x2][move.y2].setColorOuterState(FXBox.OuterState.MOVE);
		}
	}
	
	/**
	 * Syncronises the ui with the actuall chessboard of .Chess
	 */
	public void refresh() throws PositionNotOnChessboardException
	{
		for (int x = 0; x < chessCluster.length; x++)
		{
			for (int y = 0; y < chessCluster[x].length; y++)
			{
				FXBox box = chessCluster[x][y];
				Chessman chessman = fxChessboard.getChessboard().getChessman(x, y);
				Team team;
				ChessmanType type;
				if (null == chessman)
				{
					team = null;
					type = null;
				}
				else
				{
					team = chessman.getTeam();
					type = chessman.getType();
				}
				box.setChessman(team, type);
			}
		}
	}
	/**
	 * handels the OnMouseClickedEvent of the boxes.
	 * If the box has no Chessman or a Chessman of the wrong team,
	 * -nothing happens
	 *
	 * If the box has a Chessman of the playing team,
	 * -the box is selected (Green)
	 * -possible moves without a Chessman on them are highlighted (blue)
	 * -possible moves with a Chessman on them are also highlighted (red)
	 * @param x, y coordinates of the FXBox
	 */
	void clickedOn(int x, int y) throws PositionNotOnChessboardException, ChessmanNullException, NoKingFoundException, NullTeamException
	{
		//a FXPlayer is on the move
		if (game != null) {
			if (selectedFirstY != -1 && selectedFirstX != -1) {
				if (chessCluster[x][y].getColorState() == FXBox.InnerState.POSSIBLE || chessCluster[x][y].getColorState() == FXBox.InnerState.POSSIBLEENEMY) {
					selectedSecondX = x;
					selectedSecondY = y;
					moveValid = true;
					return;
				} else {
					selectedSecondX = -1;
					selectedSecondY = -1;
					resetCluster();
				}
			}
			Chessboard chessboard = game.getChessboard();
			if (chessboard.getChessman(x, y) != null && chessboard.getChessman(x, y).getTeam() == game.getPlayingTeam()) {
				selectedFirstX = x;
				selectedFirstY = y;
				for (int xi = 0; xi < chessboard.getSize(); xi++) {
					for (int yi = 0; yi < chessboard.getSize(); yi++) {
						if (game.isMoveValid(new Move(x, y, xi, yi, Team.BLACK))) {
							if (chessboard.getChessman(xi, yi) == null)
							{
								chessCluster[xi][yi].setColorInnerState(FXBox.InnerState.POSSIBLE);
							} else {
								chessCluster[xi][yi].setColorInnerState(FXBox.InnerState.POSSIBLEENEMY);
							}
							if (chessboard.getChessman(selectedFirstX, selectedFirstY).inDangerAt(Move.convertToTeam(xi, game.getPlayingTeam()), Move.convertToTeam(yi, game.getPlayingTeam()))) {
								chessCluster[xi][yi].setColorOuterState(FXBox.OuterState.INDANGER);
							} else {
								chessCluster[xi][yi].setColorOuterState(FXBox.OuterState.SAFE);
							}
						}
					}
				}
				chessCluster[x][y].setColorInnerState(FXBox.InnerState.SELECTED);
				
			} else {
				selectedFirstX = -1;
				selectedFirstY = -1;
				for (int xi = 0; xi < chessCluster.length; xi++)
				{
					for (int yi = 0; yi < chessCluster[x].length; yi++)
					{
						FXBox box = chessCluster[xi][yi];
						Chessman chessman = game.getChessboard().getChessman(xi, yi);
						if (chessman != null)
						{
							if (chessman.inDanger())
							{
								box.setColorOuterState(FXBox.OuterState.INDANGER);
							}
						}
					}
				}
			}
		}
	}
	
	public ChessmanType getType(int x, int y) {
		return chessCluster[x][y].getType();
	}
	
	
	public Move waitPlayerMove(Game game)
	{
		this.game = game;
		try
		{
			while (!moveValid && !undooMove) {
				Thread.sleep(100);
			}
		}
		catch (InterruptedException ignored)
		{
			System.out.println("Interupted");
		}
		resetCluster();
		if (moveValid) {
			moveValid = false;
			return new Move(selectedFirstX, selectedFirstY, selectedSecondX, selectedSecondY, Team.BLACK);
		} else
		{
			undooMove = false;
			//Returns a Undoo Move
			return new Move();
		}
	}
	
	
	void undooMove() {
		this.undooMove = true;
	}
}
