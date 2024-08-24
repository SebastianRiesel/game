package de.basti.example_game;

import de.basti.game_framework.collision.BoxCollider;
import de.basti.game_framework.collision.CollisionType;
import de.basti.game_framework.core.Entity;
import de.basti.game_framework.graphics.Drawable;
import de.basti.game_framework.math.Vector2D;

public class PhysicsEntity<D extends Drawable> extends GameEntity<D> {

	private Vector2D movement = new Vector2D(0, 0);
	private double weight = 1;

	private static double gravity = 0.001;

	private TouchingState touchingState = TouchingState.NONE;
	private double maxGravitySpeed = 0.5;

	public PhysicsEntity(Vector2D position, BoxCollider collider, D drawable) {
		super(position, collider, drawable);
	}

	public PhysicsEntity(Vector2D position, Vector2D movement, BoxCollider collider, D drawable) {
		super(position, collider, drawable);
		this.movement = movement;
	}

	public PhysicsEntity(Vector2D position, Vector2D movement, double weight, double maxGravitySpeed,
			BoxCollider collider, D drawable) {
		super(position, collider, drawable);
		this.movement = movement;
		this.weight = weight;
		this.maxGravitySpeed = maxGravitySpeed;

	}

	@Override
	public void update(long deltaMillis) {
		if (this.movement.getY() < maxGravitySpeed) {
			this.movement.translate(new Vector2D(0, gravity * deltaMillis));
			if ((this.movement.getY() > maxGravitySpeed)) {
				this.movement.setY(maxGravitySpeed);
			}
		}

		double speed = this.movement.length();
		Vector2D movementVector = this.movement.normalized();
		movementVector.scale(speed * deltaMillis);
		this.translate(movementVector);

		this.setTouchingState(TouchingState.NONE);
	}

	public Vector2D getMovement() {
		return movement;
	}

	public void setMovement(Vector2D movement) {
		this.movement = movement;

	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public TouchingState getTouchingState() {
		return touchingState;
	}

	public void setTouchingState(TouchingState touchingState) {
		this.touchingState = touchingState;
		
	}

	public double getMaxGravitySpeed() {
		return maxGravitySpeed;
	}

	public void setMaxGravitySpeed(double maxGravitySpeed) {
		this.maxGravitySpeed = maxGravitySpeed;
	}

	@Override
	public void onCollision(CollisionType type, Entity<?, ?> entity) {
		switch (type) {
		case BEGIN:
		case ONGOING:
			if (entity instanceof GameEntity<?>) {
				this.collisionWithGameEntity((GameEntity<?>) entity);
			}else if(entity instanceof PhysicsEntity<?>) {
				this.collisionWithPhysicsEntity((PhysicsEntity<?>) entity);
			}

			break;
		case END:
			break;
		default:
			break;

		}

	}

	private void collisionWithGameEntity(GameEntity<?> gE) {

		BoxCollider pCollider = this.getCollider();
		BoxCollider gCollider = gE.getCollider();

		Vector2D pEPos = pCollider.getPosition();
		Vector2D gEPos = gCollider.getPosition();

		double xCenterDiff = pEPos.getX() - gEPos.getX();
		double yCenterDiff = pEPos.getY() - gEPos.getY();

		double xMinDiff = (pCollider.getWidth() + gCollider.getWidth()) / 2;
		double yMinDiff = (pCollider.getHeight() + gCollider.getHeight()) / 2;

		double xDisplacement = xMinDiff - Math.abs(xCenterDiff);
		double yDisplacement = yMinDiff - Math.abs(yCenterDiff);

		if (xDisplacement < yDisplacement) {

			double translation = Math.signum(xCenterDiff) * xDisplacement;
			this.translate(new Vector2D(translation, 0));
			this.getMovement().setX(0);

			if (translation < 0) {
				this.setTouchingState(TouchingState.LEFT);
			} else if (translation > 0) {
				this.setTouchingState(TouchingState.RIGHT);
			}
		} else {

			double translation = Math.signum(yCenterDiff) * yDisplacement;
			this.translate(new Vector2D(0, translation));
			this.getMovement().setY(0);

			if (translation < 0) {
				this.setTouchingState(TouchingState.FLOOR);
			} else if (translation > 0) {
				this.setTouchingState(TouchingState.CEILING);
			} 
		}

	}
	
	
	
	private void collisionWithPhysicsEntity(PhysicsEntity<?> e) {
		System.out.println("Two Physics Entities Collided, behaviour not implemented yet");
		
	}

}
