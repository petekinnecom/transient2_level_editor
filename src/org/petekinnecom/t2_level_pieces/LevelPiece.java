package org.petekinnecom.t2_level_pieces;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.petekinnecom.t2_game.Ball;
import org.petekinnecom.t2_game.Box;

public abstract class LevelPiece extends Renderable
{
	
	public int id;
	public ArrayList<Line> lines = new ArrayList<Line>();

	/*
	 * Used for interaction with ball.
	 */
	public abstract boolean update(float deltaTick, Ball ball);
	public abstract LevelPiece clone();
	public abstract String getDBWriteString(int level_id);
	
	public LevelPiece(int x, int y, BufferedImage png)
	{
		super(x, y, png);
	}

	public LevelPiece(int x, int y, BufferedImage toRenderPNG, int reflect,
			int rotation)
	{
		super(x, y, toRenderPNG, reflect, rotation);
	}
	public LevelPiece(int id, BufferedImage png)
	{
		//TODO: 0,0 is position place holder.
		super(0,0,png);
		this.id = id;
	}
	

	/*
	 * used for editor only
	 */
	
	
	public void reflect()
	{
		reflect = -reflect;
	}

	public void rotate(int i)
	{
		rotation = (rotation + i * 45) % 360;
	}
	
	public void prep()
	{
		/*
		 * This translates collision vektors from their 
		 * tile space to world space.
		 * 
		 */
		for (Line v : lines)
		{
			v.prep(x, y, toRenderPNG.getWidth(), toRenderPNG.getHeight(), reflect, rotation);

		}
		
		
		/*
		 * Resize box so that it doesn't fits the vektors
		 * or image, whichever is bigger.
		 */
		fixBox();

	}

	public void fixBox()
	{

		float minx, miny, maxx, maxy;
		minx = x;
		miny = y;
		maxx = toRenderPNG.getWidth() + x;
		maxy = toRenderPNG.getHeight() + y;
		for (Line v : lines)
		{
			if (v.px > maxx)
				maxx = v.px;
			if (v.px < minx)
				minx = v.px;
			if (v.cx > maxx)
				maxx = v.cx;
			if (v.cx < minx)
				minx = v.cx;
			if (v.py > maxy)
				maxy = v.py;
			if (v.py < miny)
				miny = v.py;
			if (v.cy > maxy)
				maxy = v.cy;
			if (v.cy < miny)
				miny = v.cy;
		}
		this.box = new Box((int) minx, (int) miny, (int) maxx, (int) maxy);
	}

	public ArrayList<Line> copyLines()
	{
		ArrayList<Line> copy = new ArrayList<Line>();
		for (Line v : lines)
		{
			copy.add(v.copy());
		}
		return copy;
	}
	
	
}
