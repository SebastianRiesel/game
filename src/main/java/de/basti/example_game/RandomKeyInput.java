package de.basti.example_game;

import java.util.Random;

import de.basti.game_framework.input.CustomKeyInput;
import javafx.scene.input.KeyCode;
import static javafx.scene.input.KeyCode.*;


public class RandomKeyInput extends CustomKeyInput {

	private Random random = new Random();
	private KeyCode[] keys = {A,W,S,D,SPACE};

	@Override
	public void update(long deltaMillis) {
		super.update(deltaMillis);
		
		if(random.nextDouble()<0.02) {
			long time = random.nextLong(300, 1000);
			KeyCode key = keys[random.nextInt(keys.length)];
			
			this.keyDownForDuration(time, key);
		}
		
		System.out.println(keysDown);
		
		
		
	}

}
