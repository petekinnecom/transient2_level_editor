package org.petekinnecom.t2_level_pieces;

import java.awt.image.BufferedImage;

import org.petekinnecom.t2_game.Box;


public abstract class Renderable
{
	public BufferedImage toRenderPNG;
	
	public int x, y;
	
	//-1 for horizontal reflection
	public int reflect = 1;
	
	//increments of 90
	public int rotation = 0;
	
	protected Box box;	
	
	
	public Renderable(int x, int y, BufferedImage png)
	{
		this.x = x;
		this.y = y;
		this.toRenderPNG = png;
		this.box = new Box(x, y, x+toRenderPNG.getWidth(), y+toRenderPNG.getHeight());
	}
	public Renderable(int x, int y, BufferedImage png, int reflect, int rotation)
	{
		this(x, y, png);
		this.reflect = reflect;
		this.rotation = rotation;
	}
	public BufferedImage getPNG()
	{
		return toRenderPNG;
	}
	
	public boolean inBox(Box zoomBox)
	{
		if(box.inBox(zoomBox))
			return true;
		return false;
	}

}
