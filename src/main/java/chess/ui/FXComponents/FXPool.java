package chess.ui.FXComponents;

import chess.core.ChessmanType;
import chess.core.Team;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class FXPool extends FXCluster
{
	private int cursor;
	
	private final int[] lifetimes;
	
	private int currentLife;
	
	public FXPool(Stage primaryStage, GridPane gridPane, double width, double height, int collunms, int rows, double edgeWidth, double edgeHeight) {
		super(primaryStage, gridPane, width, height, collunms, rows, edgeWidth, edgeHeight, false);
		this.lifetimes = new int[collunms * rows];
		this.cursor = 0;
		this.currentLife = 1;
	}
	
	
	@Override
	void clickedOn(int x, int y)
	{
		//TODO respawn a Chessman
	}
	
	public void addChessman(Team team, ChessmanType type) {
		int x = cursor % 2;
		int y = cursor / 2;
		getChessCluster()[x][y].setChessman(team, type);
		lifetimes[cursor] = currentLife;
		cursor++;
	}
	
	
	/**
	 * Just decreases the cursor and sets it to null
	 */
	public void jumpBack(int backjumps) {
		currentLife -= backjumps;
		while (true) {
			if (cursor == 0) {
				break;
			} else if (lifetimes[cursor - 1] >= currentLife) {
				cursor--;
				int x = cursor % 2;
				int y = cursor / 2;
				getChessCluster()[x][y].setChessman(null, null);
			} else {
				break;
			}
		}
	}
	
	public void increaseLifetimes() {
		this.currentLife++;
	}
}
