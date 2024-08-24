package de.basti.example_game.level;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.basti.game_framework.math.Vector2D;

public class SimpleLevelGenerator implements LevelGenerator {

	private static final int MAX_PLATFORM_COUNT = 20;
	private static final int MIN_PLATFORM_COUNT = 5;

	private static final int PLATFORM_WIDTH = 50;
	private static final int PLATFORM_HEIGHT = 30;

	private static final int GOAL_WIDTH = 20;
	private static final int GOAL_HEIGHT = 30;

	private static final int MIN_PLATFORM_X_DISTANCE = 50;
	private static final int MAX_PLATFORM_X_DISTANCE = 200;

	private static final int MAX_PLATFORM_Y_DISTANCE = 30;

	private Random random;

	@Override
	public Level generate(long seed) {

		random = new Random(seed);
		
		
		//player
		Vector2D playerStartPos = new Vector2D(0, 0);
		Player player = new Player(playerStartPos);

		
		//platforms
		List<Platform> platforms = new ArrayList<>();

		platforms.add(newPlatform(0, 15 + PLATFORM_HEIGHT / 2, PLATFORM_WIDTH, PLATFORM_HEIGHT));
		
		int platformCount = random.nextInt(MIN_PLATFORM_COUNT, MAX_PLATFORM_COUNT);
		
		int i = 0;
		Vector2D position;
		Vector2D lastPosition;
		
		Platform platform;
		while(i<platformCount) {
			position = new Vector2D(random.nextInt(MIN_PLATFORM_X_DISTANCE, MAX_PLATFORM_X_DISTANCE),random.nextInt(-MAX_PLATFORM_Y_DISTANCE, MAX_PLATFORM_Y_DISTANCE));
			lastPosition = platforms.get(i).getPosition();
			position.translate(lastPosition);
			position.translate(PLATFORM_WIDTH,0);
			platform = new Platform(position, PLATFORM_WIDTH, PLATFORM_HEIGHT);
			platforms.add(platform);
			i++;
		}
		
		//goal
		Vector2D goalPosition = platforms.get(i).getPosition().translated(0, -PLATFORM_HEIGHT/2 -GOAL_HEIGHT/2);
		Goal goal = new Goal(goalPosition, GOAL_WIDTH, GOAL_HEIGHT);
		
		double barrierXOffset = 1000;
		
		double barrierWidth = platforms.get(i).getEnclosingBounds().getBottomRight().getX()+barrierXOffset*2;
		
		List<DeathBarrier> barriers = List.of(new DeathBarrier(new Vector2D(-barrierXOffset+barrierWidth/2,500), barrierWidth, 50.0));
		
		return new Level(platforms,barriers, player, goal);
	}

	private Platform newPlatform(double x, double y, double w, double h) {
		return new Platform(new Vector2D(x, y), w, h);
	}

}
