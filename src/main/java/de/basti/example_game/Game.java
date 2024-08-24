package de.basti.example_game;

import de.basti.example_game.level.Level;
import de.basti.example_game.level.LevelEndType;
import de.basti.example_game.level.LevelGenerator;
import de.basti.example_game.level.SimpleLevelGenerator;
import de.basti.game_framework.core.Engine;
import de.basti.game_framework.input.FXKeyInput;
import de.basti.game_framework.input.FXMouseInput;
import de.basti.game_framework.input.Input;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class Game extends Application {

	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;

	// javafx
	private static Canvas canvas = new Canvas(WIDTH, HEIGHT);
	private static Group root = new Group(canvas);
	private static Scene scene = new Scene(root);

	private static GraphicsContext gc = canvas.getGraphicsContext2D();

	// game-framework

	public static Engine<GameEntity<?>> engine = new Engine<>(scene, gc,
			new Input(new RandomKeyInput(), new FXMouseInput(scene)));

	public static Input input = engine.getInput();

	private static final long START_SEED = (long) (Math.random() * Long.MAX_VALUE);

	private long seed = START_SEED;
	LevelGenerator generator = new SimpleLevelGenerator();

	@Override
	public void start(Stage stage) {

		engine.start();
		stage.setScene(scene);
		stage.show();

		this.nextLevel();

	}

	private void nextLevel() {
		Level level = generator.generate(seed);
		level.setLevelEndListener((type) -> {
			if (type == LevelEndType.WIN) {
				seed++;
			} else {
				seed = START_SEED;
				
			}
			
			nextLevel();
		});

		level.start();
	}

	public static void main(String[] args) {
		launch();
	}

}