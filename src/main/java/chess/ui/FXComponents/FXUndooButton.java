package chess.ui.FXComponents;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.stage.Stage;

public class FXUndooButton extends FXEdge
{
	private Color color;
	
	FXUndooButton(double width, double height, Stage primaryStage, FXChessboardCluster cluster)
	{
		super(width, height, "", 45, primaryStage);
		this.color = Color.DARKBLUE;
		setOnMouseClicked(event -> {
			cluster.undooMove();
			paint();
		});
		setOnMouseEntered(event -> {
			this.color = Color.BLUE;
			paint();
		});
		setOnMouseExited(event -> {
			this.color = Color.DARKBLUE;
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
		g2d.strokeArc(getWidth() * (7/25d), getHeight() * (7/25d), getWidth() * 0.5, getHeight() * 0.5, -90, 270, ArcType.OPEN);
		g2d.strokeLine(getWidth() * (7/25d), getHeight() * 0.5, getWidth() * (7/25d) - 1, getHeight() * 0.5 - 5);
		g2d.strokeLine(getWidth() * (7/25d), getHeight() * 0.5, getWidth() * (7/25d) + 5, getHeight() * 0.5 - 1);
	}
}
