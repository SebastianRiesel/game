package de.basti.example_game.level;

import java.util.List;

import de.basti.example_game.Game;
import de.basti.game_framework.graphics.GameGraphics;

public class Level {
	private List<Platform> platforms;
	private Player player;
	private Goal goal;
	private List<DeathBarrier> deathBarriers;

	private LevelEndListener levelEndListener = (a) -> {
	};

	public Level(List<Platform> platforms,List<DeathBarrier> deathBarriers, Player player, Goal goal) {
		super();
		this.platforms = platforms;
		this.deathBarriers = deathBarriers;
		this.player = player;
		this.goal = goal;
	}

	public void start() {
		Game.engine.addEntity(GameGraphics.FORE_MIDDLE, this.getPlayer());

		player.setOnGoalTouch(() -> {
			this.end();
			levelEndListener.onLevelEnd(LevelEndType.WIN);
		});
		
		player.setOnBarrierTouch(() -> {
			this.end();
			levelEndListener.onLevelEnd(LevelEndType.LOSS);
		});

		for (Platform platform : this.getPlatforms()) {
			Game.engine.addEntity(GameGraphics.FOREGROUND, platform);

		}
		
		for(DeathBarrier db: this.getDeathBarriers()) {
			Game.engine.addEntity(GameGraphics.FOREGROUND, db);
		}
		Game.engine.addEntity(GameGraphics.FOREGROUND, this.getGoal());

		Game.engine.stickCameraTo(this.getPlayer());
	}

	public void end() {
		System.out.println("Player:"+Game.engine.removeEntity(this.getPlayer()));

		for (Platform platform : this.getPlatforms()) {
			System.out.println("Platform:"+Game.engine.removeEntity(platform));

		}
		
		for (Platform platform : this.getPlatforms()) {
			System.out.println("Platform:"+Game.engine.removeEntity(platform));

		}
		
		for (DeathBarrier deathBarrier: this.getDeathBarriers()) {
			System.out.println("Barrier:"+Game.engine.removeEntity(deathBarrier));

		}
		
		System.out.println("Goal:"+Game.engine.removeEntity(this.getGoal()));
		
		
		
		Game.engine.stickCameraTo(null);

		
	}

	public List<Platform> getPlatforms() {
		return platforms;
	}

	public void setPlatforms(List<Platform> platforms) {
		this.platforms = platforms;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Goal getGoal() {
		return goal;
	}

	public void setGoal(Goal goal) {
		this.goal = goal;
	}

	public LevelEndListener getLevelEndListener() {
		return levelEndListener;
	}

	public void setLevelEndListener(LevelEndListener levelEndListener) {
		this.levelEndListener = levelEndListener;
	}

	public List<DeathBarrier> getDeathBarriers() {
		return deathBarriers;
	}

	public void setDeathBarriers(List<DeathBarrier> deathBarriers) {
		this.deathBarriers = deathBarriers;
	}
	
	

}
