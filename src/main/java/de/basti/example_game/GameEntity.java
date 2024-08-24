package de.basti.example_game;

import de.basti.game_framework.collision.BoxCollider;
import de.basti.game_framework.core.Entity;
import de.basti.game_framework.graphics.Drawable;
import de.basti.game_framework.math.Vector2D;

public abstract class GameEntity<D extends Drawable> extends Entity<D, BoxCollider>{

	public GameEntity(Vector2D position, BoxCollider collider, D drawable) {
		super(position, collider, drawable);
	}
	

}
