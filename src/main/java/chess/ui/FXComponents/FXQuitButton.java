package chess.ui.FXComponents;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class FXQuitButton extends FXEdge
{
	
	private Color color;
	
	FXQuitButton(double width, double height, Stage primaryStage)
	{
		super(width, height, "", 45, primaryStage);
		this.color = Color.DARKRED;
		setOnMouseClicked(event -> primaryStage.close());
		setOnMouseEntered(event -> {
			this.color = Color.RED;
			paint();
		});
		setOnMouseExited(event -> {
			this.color = Color.DARKRED;
			paint();
		});
	}
	
	
	@Override
	public void paint()
	{
		GraphicsContext g2d = this.getGraphicsContext2D();
		g2d.setFill(this.color);
		g2d.fillRect(0, 0, getWidth(), getHeight());
		g2d.setStroke(Color.GOLD);
		g2d.setLineWidth(2);
		g2d.strokeLine(getWidth() * (7/25d), getHeight() * (7/25d), getWidth() * (18/25d), getHeight() * (18/25d));
		g2d.strokeLine(getWidth() * (7/25d), getHeight() * (18/25d), getWidth() * (18/25d), getHeight() * (7/25d));
	}
}
