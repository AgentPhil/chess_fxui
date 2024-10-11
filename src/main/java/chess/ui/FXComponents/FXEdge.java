package chess.ui.FXComponents;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class FXEdge extends Canvas
{
	private final String text;
	
	private final double directionOfText;
	
	private double xOffset;
	
	private double yOffset;
	
	private final Stage primaryStage;
	
	FXEdge(double width, double height, String text, double orientation, Stage primaryStage) {
		super(width, height);
		this.text = text;
		this.directionOfText = orientation;
		this.primaryStage = primaryStage;
		this.setOnMousePressed(event -> {
			xOffset = event.getSceneX();
			yOffset = event.getSceneY();
		});
		this.setOnMouseDragged(event -> {
			this.primaryStage.setX(event.getScreenX() - xOffset);
			this.primaryStage.setY(event.getScreenY() - yOffset);
		});
	}
	
	public void paint() {
		GraphicsContext g2d = this.getGraphicsContext2D();
		g2d.setFill(Color.BROWN);
		g2d.fillRect(0, 0, getWidth(), getHeight());
		g2d.translate(getWidth() / 2, getHeight() / 2);
		g2d.rotate(directionOfText);
		g2d.setFill(Color.GOLD);
		g2d.setTextAlign(TextAlignment.CENTER);
		g2d.fillText(this.text, 0, 0);
		g2d.rotate(0 - directionOfText);
		g2d.translate(0 - getWidth() / 2, 0 - getHeight() / 2);
	}
}
