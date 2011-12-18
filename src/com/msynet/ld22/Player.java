package com.msynet.ld22;


public class Player extends Entity {
	
	public static final float Speed = 200.0f;
	
	public enum PlayerAction { Searching, Mining }
	
	public PlayerAction currentAction; 
	public long msMining = 0;
	public Treasure miningTreasure;
	public long treasureMined = 0;
	public boolean hasKey = false;
	
	public long numTreasureMined = 0;

	@Override
	public String getTextureName() {
		return TextureManager.PlayerTextureName;
	}
	
	
	@Override
	public void update(long stepMs) {
		super.update(stepMs);
		
		switch(currentAction) {
		case Mining:
			msMining += stepMs;
			break;
		case Searching:
			break;
		}
		
	}
	
	
}
