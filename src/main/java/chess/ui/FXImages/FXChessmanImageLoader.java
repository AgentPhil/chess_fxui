package chess.ui.FXImages;

import chess.core.ChessmanType;
import chess.core.Team;
import javafx.scene.image.Image;
import java.util.HashMap;

public class FXChessmanImageLoader
{
	private static FXChessmanImageLoader instance;
	
	private final HashMap<String, Image> images = new HashMap<>();
	
	public static FXChessmanImageLoader getInstance() {
		if (instance == null) {
			instance = new FXChessmanImageLoader();
		}
		return instance;
	}
	
	public synchronized Image getImage(ChessmanType type, Team team) {
		String key;
		if (type == null || team == null) {
			key = "EMPTY.png";
		} else {
			key = team.toString() + "_" + type.toString() + ".png";
		}
		if (images.containsKey(key)) {
			return images.get(key);
		} else {
			Image image = new Image(getClass().getResourceAsStream(key));
			images.put(key, image);
			return image;
		}
		
	}
}
