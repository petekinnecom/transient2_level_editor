package org.petekinnecom.t2_game;

public class Box
{
	public int x1, y1, x2, y2;
	
	public Box(int x1, int y1, int x2, int y2)
	{
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}
	public void set(int x1, int y1, int x2, int y2)
	{
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		
	}
	public int getWidth()
	{
		return x2 - x1;
	}
	public int getHeight()
	{
		return y2 - y1;
	}
	public void resize(int dx, int dy)
	{
		x2 += dx;
		y2 += dy;

		/* Make sure we have a box 
		 * of at least length/width one.
		 */
		if(x2 <= x1)
			x2 = x1 + 1;
		if(y2 <= y1)
			y2 = y1 + 1;
	}
	public void move(int dx, int dy)
	{
		x1 += dx;
		y1 += dy;
		x2 += dx;
		y2 += dy;
	}
	public String toString()
	{
		return "("+x1+", "+y1+") to ("+x2+", "+y2+")";
	}
	public boolean inBox(Box zoomBox)
	{
		if(this.x1>zoomBox.x2)
			return false;
		if(this.x2<zoomBox.x1)
			return false;
		if(this.y1>zoomBox.y2)
			return false;
		if(this.y2<zoomBox.y1)
			return false;
		return true;
	}
}
