package chess.ui.FXComponents;

import chess.ui.FXImages.FXChessmanImageLoader;
import chess.core.ChessmanType;
import chess.core.Team;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

class FXBox extends Canvas
{
	public enum InnerState
	{
		NORMAL, SELECTED, POSSIBLE, POSSIBLEENEMY, MOVE
	}
	public enum OuterState
	{
		NORMAL, INDANGER, SAFE, MOVE
	}
	private final FXCluster cluster;
	
	private final int x,y;
	
	private final Color normalColor, selectedColor, possibleColor, possibleEnemyColor, moveColor, dangerColor, safeColor;
	
	private Color currentInnerColor;
	
	private Color currentOuterColor;
	
	private ChessmanType chessmanType;
	
	private Team chessmanTeam;
	
	private InnerState innerState;
	
	private OuterState outerState;
	
	
	/**
	 * One Boxy thingy on a Chessboard, weather Black or White.
	 * The highlighting Colors are applied with a bit of Transparency to keep a visual difference between
	 * selected White and Black Boxes
	 * @param cluster it is part in
	 * @param x Position of the Box, startng with zer0
	 * @param y Position of the Boc, starting with zer0
	 * @param normalColor Standard Color
	 * @param selectedColor when being clicked on
	 * @param possibleColor when a move to this box is possible
	 * @param possibleEnemyColor when its possible to defeat a Chessman there
	 * @param moveColor Color of the Last Move
	 * @param dangerColor
	 * @param safeColor
	 * @param width
	 * @param height
	 */
	FXBox(FXCluster cluster, int x, int y, Color normalColor, Color selectedColor, Color possibleColor, Color possibleEnemyColor, Color moveColor, Color dangerColor, Color safeColor,
		  double width, double height) {
		super(width, height);
		this.cluster = cluster;
		this.x = x;
		this.y = y;
		this.currentInnerColor = normalColor;
		this.currentOuterColor = normalColor;
		this.normalColor = normalColor;
		this.selectedColor = selectedColor;
		this.possibleColor = possibleColor;
		this.possibleEnemyColor = possibleEnemyColor;
		this.moveColor = moveColor;
		this.dangerColor = dangerColor;
		this.safeColor = safeColor;
		this.innerState = InnerState.NORMAL;
		this.outerState = OuterState.NORMAL;
		this.setOnMouseEntered(event ->
							   {
								   currentInnerColor = currentInnerColor.brighter();
								   currentOuterColor = currentOuterColor.brighter();
								   paint();
							   });
		this.setOnMouseExited(event ->
							  {
								  setColorInnerState(this.innerState);
								  setColorOuterState(this.outerState);
							  });
		this.setOnMouseClicked(event -> this.cluster.clickedOn(this.x, this.y));
	}
	
	void setColorInnerState(InnerState innerState)
	{
		this.innerState = innerState;
		Color newColor;
		currentInnerColor = normalColor;
		switch (innerState) {
			case NORMAL: newColor = normalColor; break;
			case SELECTED: newColor = selectedColor; break;
			case POSSIBLE: newColor = possibleColor; break;
			case POSSIBLEENEMY: newColor = possibleEnemyColor; break;
			case MOVE: newColor = moveColor; break;
			default: newColor = normalColor; break;
		}
		this.currentInnerColor = this.currentInnerColor.interpolate(newColor, 0.9);
		paint();
	}
	
	InnerState getColorState()
	{
		return this.innerState;
	}
	
	void setColorOuterState(OuterState outerState)
	{
		this.outerState = outerState;
		Color newColor;
		currentOuterColor = normalColor;
		switch (outerState) {
			case NORMAL: newColor = normalColor; break;
			case INDANGER: newColor = dangerColor; break;
			case SAFE: newColor = safeColor; break;
			case MOVE: newColor = moveColor; break;
			default: newColor = normalColor; break;
		}
		this.currentOuterColor = this.currentOuterColor.interpolate(newColor, 0.9);
		paint();
	}

	
	void paint() {
		GraphicsContext g2d = this.getGraphicsContext2D();
		g2d.setFill(this.currentInnerColor);
		g2d.setStroke(this.currentOuterColor);
		g2d.setLineWidth(5);
		g2d.fillRect(0, 0, getWidth(), getHeight());
		g2d.strokeRect(0, 0, getWidth(), getHeight());
		g2d.drawImage(FXChessmanImageLoader.getInstance().getImage(this.chessmanType, this.chessmanTeam), 0, 0, getWidth(), getHeight());
	}
	
	void setChessman(Team team, ChessmanType type) {
		this.chessmanTeam = team;
		this.chessmanType = type;
	}
	
	public ChessmanType getType()
	{
		return this.chessmanType;
	}
	
}
