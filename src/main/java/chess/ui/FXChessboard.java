package chess.ui;

import chess.core.Chessboard;
import chess.core.ChessmanType;
import chess.core.Exceptions.PositionNotOnChessboardException;
import chess.core.Game;
import chess.core.HistoricChessboard;
import chess.core.Move;
import chess.core.Team;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

import chess.ui.FXComponents.FXChessboardCluster;
import chess.ui.FXComponents.FXPool;

public class FXChessboard extends Application
{
	private Stage primaryStage;
	
	private FXChessboardCluster chessboardCluster;
	
	private FXPool leftPool;
	
	private FXPool rightPool;
	
	private static FXChessboard instance;
	
	private static Thread gameThread;
	
	private static Chessboard chessboard = null;
	
	
	static FXChessboard getInstance() {
		if (instance == null){
			gameThread = Thread.currentThread();
			new Thread(() ->
					   {
						   System.out.println("\nLaunched FX Application");
					   		//creates new instance. Instance is being called bei JavaFX. instance is assigned in start()
						   launch();
						   System.out.println("\nClosed FX Aplication");
						   System.out.println("Stopping Game Thread");
						   //noinspection deprecation
						   gameThread.stop();
						   try
						   {
							   Thread.sleep(100);
						   }
						   catch (InterruptedException e)
						   {
							   e.printStackTrace();
						   }
						   if (gameThread.isAlive()) {
							   System.out.println("Stopping Game Thread failed: " + gameThread.getName() + " with ID " + gameThread.getId());
						   } else {
							   System.out.println("Game Thread stopped");
						   }
						   
					   }).start();
		}
		do {
			try
			{
				Thread.sleep(10);
			}
			catch (InterruptedException ignored)
			{
			
			}
		} while (instance == null);
		return instance;
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception
	{
		this.primaryStage = primaryStage;
		Parent root = FXMLLoader.load(getClass().getResource("FXChessboard.fxml"));
		primaryStage.setTitle("Chess");
		primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("icon.png")));
		primaryStage.setResizable(false);
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		primaryStage.setScene(new Scene(root));
		primaryStage.setWidth(64 * 12 + 50 + 40);
		primaryStage.setHeight(64 * 8 + 50);
		primaryStage.getScene().setFill(Color.TRANSPARENT);
		
		GridPane gridPaneChessboard = (GridPane) primaryStage.getScene().lookup("#Chessboard");
		gridPaneChessboard.setBackground(Background.EMPTY);
		gridPaneChessboard.setPrefWidth(64 * 8);
		gridPaneChessboard.setPrefHeight(64 * 8);
		
		GridPane gridPaneLeftPool = (GridPane) primaryStage.getScene().lookup("#LeftPool");
		gridPaneLeftPool.setBackground(Background.EMPTY);
		gridPaneLeftPool.setPrefWidth(64 * 2);
		gridPaneLeftPool.setPrefHeight(64 * 8);
		
		GridPane gridPaneRightPool = (GridPane) primaryStage.getScene().lookup("#RightPool");
		gridPaneRightPool.setBackground(Background.EMPTY);
		gridPaneRightPool.setPrefWidth(64 * 2);
		gridPaneRightPool.setPrefHeight(64 * 8);
		
		primaryStage.show();
		chessboardCluster = new FXChessboardCluster(this, primaryStage, gridPaneChessboard, gridPaneChessboard.getWidth(), gridPaneChessboard.getHeight(), 8, 8, 64, 25);
		leftPool = new FXPool(primaryStage, gridPaneLeftPool, gridPaneLeftPool.getWidth(), gridPaneLeftPool.getHeight(), 2, 8, 64, 10);
		rightPool = new FXPool(primaryStage, gridPaneRightPool, gridPaneRightPool.getWidth(), gridPaneRightPool.getHeight(), 2, 8, 64, 10);
		instance = this;
	}
	
	Move waitPlayerMove(Game game)
	{
		return chessboardCluster.waitPlayerMove(game);
	}
	
	void paint(Chessboard chessboard, Team playingTeam, Move move) throws PositionNotOnChessboardException
	{
		FXChessboard.chessboard = chessboard;
		if (move != null) {
			if (!move.isUndooMove()) {
				Move staticMove = move.makeStatic();
				ChessmanType type = chessboardCluster.getType(staticMove.x2, staticMove.y2);
				if (type != null) {
					if (playingTeam == Team.BLACK) {
						rightPool.addChessman(Team.WHITE, type);
					}
					else {
						leftPool.addChessman(Team.BLACK, type);
					}
				}
				rightPool.increaseLifetimes();
				leftPool.increaseLifetimes();
			} else {
				HistoricChessboard historic = (HistoricChessboard) chessboard;
				Move lastMoveBeforeUndoo = historic.getFollowingMove();
				move = lastMoveBeforeUndoo.makeStatic();
				leftPool.jumpBack(historic.getBackjumps());
				rightPool.jumpBack(historic.getBackjumps());
			}
		}
		chessboardCluster.refresh();
		Move finalMove = move;
		Platform.runLater(() ->
						  {
							  chessboardCluster.paint(finalMove);
							  leftPool.paint();
							  rightPool.paint();
						  });
	}
	
	public Chessboard getChessboard() {
		return FXChessboard.chessboard;
	}
	
	void won(Team winningTeam)
	{
		Platform.runLater(() -> {
			Canvas canvas = new Canvas(300, 100);
			Scene scene = new Scene(new AnchorPane(canvas));
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.setTitle(winningTeam + " won!");
			stage.initStyle(StageStyle.UNDECORATED);
			stage.getIcons().add(new Image(getClass().getResourceAsStream("icon.png")));
			GraphicsContext g2d = canvas.getGraphicsContext2D();
			g2d.setFill(Color.BROWN);
			g2d.fillRect(0, 0, 300, 100);
			g2d.setTextAlign(TextAlignment.CENTER);
			g2d.setFont(new Font(20));
			g2d.setFill(Color.GOLD);
			g2d.fillText(winningTeam + " won the match", 150, 50);
			stage.centerOnScreen();
			stage.show();
			scene.setOnMouseClicked(event ->
									{
										stage.close();
										primaryStage.close();
									});
		});
	}
	
	void showExceptionStackTrace(Exception e)
	{
		Platform.runLater(() -> {
			Parent root;
			try {
				root = FXMLLoader.load(getClass().getResource("FXExceptionScreen.fxml"));
			} catch (IOException ee) {
				e.printStackTrace();
				//not proud about that
				throw new RuntimeException("Exception showing Exception");
			}
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.setTitle(e.getMessage());
			TextArea textField = (TextArea) scene.lookup("#TextField");
			//textField.appendText(e.getCause());
			textField.appendText(e.toString() + "\n");
			for (StackTraceElement element : e.getStackTrace()) {
				textField.appendText("    " + element.toString() + "\n");
			}
			e.printStackTrace();
			stage.show();
			Button close = (Button) scene.lookup("#closeButton");
			close.setOnAction(event -> {
				stage.close();
				primaryStage.close();
			});
		});
	}
}
