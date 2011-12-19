package com.msynet.ld22;

import org.lwjgl.util.vector.Vector3f;

public class Miner extends Entity {
	
	public enum MinerAction { Searching, Mining }
	
	public MinerAction currentAction; 
	public Treasure miningTreasure = null;
	public long elapsedMiningTime;
	
	public long msMined = 0;
	public long treasureMined = 0;
	public boolean hasKey = false;
	
	
	@Override
	public void update(long stepMs) {
		
		switch(this.currentAction) {

		case Mining:
			this.speed = 0.0f;
			msMined += stepMs;
			break;

		case Searching:
			this.speed = 180.0f;
			int changeDir = LD22.random.nextInt(256);
			if(changeDir == 128) {
				this.heading = LD22.random.nextFloat() * 360.0f;				
			}
			
			break;

		default:
			break;
		}		
		
		super.update(stepMs);
	}



	@Override
	public String getTextureName() {
		if(alive) {
			return TextureManager.OtherMinerTexture;			
		} else {
			return TextureManager.DeadMinerTexture;
		}
	}


	@Override
	public Vector3f getColor() {
		if(currentAction == MinerAction.Mining) {
			float comp = 1.0f - (float)(msMined / 1250.0f);
			Vector3f res = new Vector3f(comp, comp, comp);
			return res;
		}
		
		return new Vector3f(1.0f, 1.0f, 1.0f);
	}
	
}
