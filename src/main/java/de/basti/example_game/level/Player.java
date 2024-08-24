package de.basti.example_game.level;

import de.basti.example_game.Game;
import de.basti.example_game.PhysicsEntity;
import de.basti.example_game.TouchingState;
import de.basti.game_framework.collision.BoxCollider;
import de.basti.game_framework.collision.CollisionType;
import de.basti.game_framework.core.Entity;
import de.basti.game_framework.core.Updatable;
import de.basti.game_framework.graphics.DrawableRectangle;
import de.basti.game_framework.math.Vector2D;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

public class Player extends PhysicsEntity<DrawableRectangle> implements Updatable {

	private final double maxWalkingSpeed = 0.3;
	private final double maxCrouchingSpeed = 0.15;

	private final double floorWalkAcceleration = 0.002;
	private final double floorStopAcceleration = 0.0008;
	private final double airWalkAcceleration = 0.0015;

	private final double jumpStrength = 0.4;

	private final double wallSlideSpeed = 0.1;
	private final double wallJumpXStrength = 0.4;

	private final int normalWidth = 20;
	private final int normalHeight = 30;
	private final int crouchHeight = 20;

	private boolean doubleJumpAvailable = true;
	private boolean currentlyCrouching = false;

	private Runnable onGoalTouch = () -> {
	};
	private Runnable onBarrierTouch = () -> {
	};

	public Player(Vector2D position) {
		super(position, null, null);
		DrawableRectangle d = new DrawableRectangle(position.clone(), normalWidth, normalHeight);
		d.setFillColor(Color.BLUE);
		d.setStrokeColor(Color.TRANSPARENT);

		this.setDrawable(d);

		this.setCollider(new BoxCollider(position.clone(), normalWidth, normalHeight));

	}

	@Override
	public void update(long deltaMillis) {
		
		boolean crouch = false;

		if ((this.getTouchingState() == TouchingState.FLOOR)) {
			this.doubleJumpAvailable = true;
			if (Game.input.getKeyData().isPressed(KeyCode.W)) {
				this.getMovement().setY(-jumpStrength);

			}

			if (Game.input.getKeyData().isDown(KeyCode.S)) {
				crouch = true;
			}
		} else {
			if (Game.input.getKeyData().isPressed(KeyCode.W) && this.doubleJumpAvailable) {

				this.getMovement().setY(-jumpStrength);

				this.doubleJumpAvailable = false;
			}
		}

		if (crouch) {
			if (!currentlyCrouching) {
				this.setHeight(crouchHeight);
				this.translate(new Vector2D(0, (normalHeight - crouchHeight) / 2.0));
				this.currentlyCrouching = true;
			}
		} else {
			if (currentlyCrouching) {
				this.setHeight(normalHeight);
				this.translate(new Vector2D(0, (crouchHeight - normalHeight) / 2.0));
				this.currentlyCrouching = false;
			}
		}

		this.setMaxGravitySpeed(0.5);

		boolean move = false;

		double targetSpeed = 0;
		double acceleration = 0;

		// movement
		int movementMultiplier = 0;

		if (Game.input.getKeyData().isDown(KeyCode.A)) {
			if (this.getTouchingState() == TouchingState.RIGHT && this.getMovement().getY() > 0) {
				this.setMaxGravitySpeed(wallSlideSpeed);
				this.getMovement().setY(wallSlideSpeed);

				if (Game.input.getKeyData().isPressed(KeyCode.W)) {
					this.getMovement().set(wallJumpXStrength, -jumpStrength);
					this.doubleJumpAvailable = true;
				}

			}
			movementMultiplier += -1;
			move = true;

		}
		if (Game.input.getKeyData().isDown(KeyCode.D)) {
			if (this.getTouchingState() == TouchingState.LEFT && this.getMovement().getY() > 0) {
				this.setMaxGravitySpeed(wallSlideSpeed);
				this.getMovement().setY(wallSlideSpeed);
				if (Game.input.getKeyData().isPressed(KeyCode.W)) {
					this.getMovement().set(-wallJumpXStrength, -jumpStrength);
					this.doubleJumpAvailable = true;
				}
			}

			movementMultiplier += 1;
			move = true;

		}
			
		if(movementMultiplier==0) {
			move = false;
		}
		
		if (this.getTouchingState() == TouchingState.FLOOR) {

			if (move) {
				if (currentlyCrouching) {
					targetSpeed = maxCrouchingSpeed * movementMultiplier;
				} else {
					targetSpeed = maxWalkingSpeed * movementMultiplier;
				}

				acceleration = floorWalkAcceleration;
			} else {
				targetSpeed = 0;
				acceleration = floorStopAcceleration;
			}
		} else {
			// in air and moving
			if (move) {
				if (movementMultiplier < 0) {

					targetSpeed = Math.min(-maxWalkingSpeed, this.getMovement().getX());

				} else if (movementMultiplier > 0) {
					targetSpeed = Math.max(maxWalkingSpeed, this.getMovement().getX());
				}
				acceleration = airWalkAcceleration;
			}

		}

		if (this.getMovement().getX() > targetSpeed) {
			this.getMovement().translate(-acceleration * deltaMillis, 0);
			if (this.getMovement().getX() < targetSpeed) {
				this.getMovement().setX(targetSpeed);
			}
		}

		if (this.getMovement().getX() < targetSpeed) {
			this.getMovement().translate(acceleration * deltaMillis, 0);
			if (this.getMovement().getX() > targetSpeed) {
				this.getMovement().setX(targetSpeed);
			}
		}

		super.update(deltaMillis);
	}

	private void setHeight(int newHeight) {
		this.getDrawable().setHeight(newHeight);
		this.getCollider().setHeight(newHeight);
	}

	@Override
	public void onCollision(CollisionType type, Entity<?, ?> entity) {

		if (entity instanceof Goal && type == CollisionType.BEGIN) {
			onGoalTouch.run();
			return;
		}
		if (entity instanceof DeathBarrier && type == CollisionType.BEGIN) {
			
			onBarrierTouch.run();
			return;
		}
		

		super.onCollision(type, entity);
	}

	public Runnable getOnGoalTouch() {
		return onGoalTouch;
	}

	public void setOnGoalTouch(Runnable onGoalTouch) {
		this.onGoalTouch = onGoalTouch;
	}

	public Runnable getOnBarrierTouch() {
		return onBarrierTouch;
	}

	public void setOnBarrierTouch(Runnable onBarrierTouch) {
		this.onBarrierTouch = onBarrierTouch;
	}
	
	

}
