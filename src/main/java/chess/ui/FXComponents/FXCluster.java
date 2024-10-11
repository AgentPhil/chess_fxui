package chess.ui.FXComponents;

import chess.core.Exceptions.ChessmanNullException;
import chess.core.Exceptions.NoKingFoundException;
import chess.core.Exceptions.NullTeamException;
import chess.core.Exceptions.PositionNotOnChessboardException;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public abstract class FXCluster
{
	private final FXBox[][] chessCluster;
	
	private final FXEdge[] edges;
	
	@SuppressWarnings("SuspiciousNameCombination")
	FXCluster(Stage primaryStage, GridPane gridPane, double width, double height, int collunms, int rows, double edgeWidth, double edgeHeight, boolean text)
	{
		chessCluster = new FXBox[collunms][rows];
		edges = new FXEdge[collunms * 2 + rows * 2 + 4];
		Color c1 = Color.SADDLEBROWN;
		Color c2 = Color.SANDYBROWN;
		Color color = c1;
		int edgeCounter = 0;
		String[] xTexts = new String[]{"A", "B", "C", "D", "E", "F", "G", "H"};
		String[] yTexts = new String[] {"8", "7", "6", "5", "4", "3", "2", "1"};
		for (int x = -1; x < collunms + 1; x++) {
			for (int y = -1; y < rows + 1; y++) {
				String xText = "";
				String yText = "";
				if (text && x != -1 && x != collunms) {
					xText = xTexts[x];
				}
				if (text && y != -1 && y != rows) {
					yText = yTexts[y];
				}
				if ((x == -1 || x == collunms) && (y == -1 || y == rows)) {
					edges[edgeCounter] = new FXEdge(edgeHeight, edgeHeight,"", 45, primaryStage);
					gridPane.add(edges[edgeCounter++], x + 1, y + 1); }
				else {
					if (y == -1) {
						edges[edgeCounter] = new FXEdge(edgeWidth, edgeHeight, xText, 180, primaryStage);
						gridPane.add(edges[edgeCounter++], x + 1, y + 1); }
					else if (y == rows) {
						edges[edgeCounter] = new FXEdge(edgeWidth, edgeHeight, xText, 0, primaryStage);
						gridPane.add(edges[edgeCounter++], x + 1, y + 1); }
					else if (x == -1) {
						edges[edgeCounter] = new FXEdge(edgeHeight, edgeWidth, yText, 0, primaryStage);
						gridPane.add(edges[edgeCounter++], x + 1, y + 1); }
					else if (x == collunms) {
						edges[edgeCounter] = new FXEdge(edgeHeight, edgeWidth, yText, 180, primaryStage);
						gridPane.add(edges[edgeCounter++], x + 1, y + 1); }
					else {
						chessCluster[x][y] = new FXBox(this, x, y, color, Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW, Color.RED, Color.GREEN, width / collunms, height / rows);
						gridPane.add(chessCluster[x][y], x + 1, y + 1);
						color = color == c1 ? c2 : c1;
					}
				}
			}
			color = color == c1 ? c2 : c1;
		}
	}
	
	
	public void paint()
	{
		this.resetCluster();
		for (FXEdge edge : edges)
		{
			edge.paint();
		}
		for (FXBox[] chessClusterRow : chessCluster)
		{
			for (FXBox box : chessClusterRow)
			{
				box.paint();
			}
		}
	}
	
	void resetCluster()
	{
		for (FXBox[] column : chessCluster) {
			for (FXBox box : column) {
				box.setColorInnerState(FXBox.InnerState.NORMAL);
				box.setColorOuterState(FXBox.OuterState.NORMAL);
			}
		}
	}
	
	abstract void clickedOn(int x, int y) throws PositionNotOnChessboardException, ChessmanNullException, NoKingFoundException, NullTeamException;
	
	FXBox[][] getChessCluster() {
		return this.chessCluster;
	}
	
	FXEdge[] getEdges() {
		return this.edges;
	}
	

}