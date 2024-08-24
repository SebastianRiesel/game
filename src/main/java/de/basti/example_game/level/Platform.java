package de.basti.example_game.level;

import de.basti.example_game.GameEntity;
import de.basti.game_framework.collision.BoxCollider;
import de.basti.game_framework.collision.CollisionType;
import de.basti.game_framework.core.Entity;
import de.basti.game_framework.graphics.DrawableRectangle;
import de.basti.game_framework.math.Rectangle;
import de.basti.game_framework.math.Vector2D;
import javafx.scene.paint.Color;

public class Platform extends GameEntity<DrawableRectangle> {

	public Platform(Vector2D position, double width, double height) {
		super(position.clone(), new BoxCollider(new Rectangle(position.clone(), width, height)),
				new DrawableRectangle(new Rectangle(position.clone(), width, height)));
		this.getDrawable().setFillColor(Color.GRAY);
		this.getDrawable().setStrokeColor(Color.BLACK);
		this.getDrawable().setLineWidth(4);
	}

	@Override
	public void update(long deltaMillis) {
		// nothing
	}

	@Override
	public void onCollision(CollisionType type, Entity<?, ?> entity) {
		// nothing

	}

}
