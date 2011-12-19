package com.msynet.ld22;

import org.lwjgl.util.vector.Vector3f;

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
	
	public boolean isMining() {
		return currentAction == PlayerAction.Mining;		
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
	
	@Override
	public Vector3f getColor() {
		if(currentAction == PlayerAction.Mining) {
			float comp = 1.0f - (float)(msMining / 1250.0f);
			Vector3f res = new Vector3f(comp, comp, comp);
			return res;
		}
		
		return new Vector3f(1.0f, 1.0f, 1.0f);
	}

}
