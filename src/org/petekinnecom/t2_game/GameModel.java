package org.petekinnecom.t2_game;

import java.awt.Point;
import java.util.ArrayList;

import org.petekinnecom.t2_level_editor.*;
import org.petekinnecom.t2_level_pieces.Renderable;
public class GameModel
{
	private Level level;
	private Ball ball;
	
	GameModel(Level level)
	{
		this.level = level;
		ball = new Ball(level.ballStart, 32);
	}
	
	

	public Ball getBall()
	{
		//used for drawing
		
		return ball;
	}

	
	public void accelBall(float dx, float dy)
	{
		ball.accelerate(dx, dy);
	}
	
	public void tick(float deltaTick, Box zoomBox)
	{
		//ball.moveBall(deltaTick, getVektors(zoomBox));
		ball.move(deltaTick);
		level.moveBall(deltaTick, ball);
		ball.postMoveFix(deltaTick);
	}
	
	public Point getBallVelocity()
	{
		return ball.getVelocity();
	}
	
	public ArrayList<Renderable> getRenderables(Box zoomBox)
	{
		return level.getRenderables(zoomBox);
	}
	float x1, y1;
	Box temp = new Box(0,0,1,1);
	public Box getZoomBox(float width, float height)
	{
		x1 = (int) ball.x - (int) (width/3f);
		y1 = (int) ball.y - (int) (height/3f);


		temp.set((int)x1, (int) y1, (int)(x1 + width), (int) (y1 + height));
		//C.out("input size: "+width+", "+height);
		//C.out("output size: "+ temp.getWidth()+", "+temp.getHeight());
		
		return temp;
	}
}
