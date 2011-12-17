package com.msynet.ld22;

import java.util.Random;

import javax.vecmath.Vector2f;

public class Entity {
	
	public String textureName;
	
	public Vector2f pos =  new Vector2f();
	
	public Vector2f dem = new Vector2f(64.0f, 64.0f);
	public Vector2f cdem = new Vector2f(64.0f, 64.0f); // collision dem
	
	public Vector2f velocity = new Vector2f();
	
	public float rotAngle = 0;
		
	public static Vector2f getRandomPoint(int xmax, int ymax) {
		Random random = new Random(); 
		return new Vector2f(random.nextInt(xmax), random.nextInt(ymax));		
	}
	
	public Entity() {
		
	}
	
	public void update(long stepMs) {		
		float fracSec = (float)((float)stepMs / 1000.0);

		pos.x += (velocity.x * fracSec);
		pos.y += (velocity.y * fracSec);
	}
		
	public void follow(Entity other) {	
		
		
	}
	
	public boolean intersects( Entity other )
	{		
	  float ulx = Math.max (pos.x, other.pos.x );
	  float uly = Math.max (pos.y, other.pos.y );
	  float lrx = Math.min (pos.x + cdem.x, other.pos.x + other.cdem.x );
	  float lry = Math.min (pos.y + cdem.y, other.pos.y + other.cdem.y );

	  return ulx <= lrx && uly <= lry;
	}
	
}
