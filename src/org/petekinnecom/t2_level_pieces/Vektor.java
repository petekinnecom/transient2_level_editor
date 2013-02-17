package org.petekinnecom.t2_level_pieces;

public class Vektor
{
	public float x, y;

	public Vektor()
	{}
	
	public Vektor(float x, float y)
	{
		this.x = x;
		this.y = y;
	}

	public Vektor(float px, float py, float cx, float cy)
	{
		this.x = cx - px;
		this.y = cy - py;
	}
	public void set(float px, float py, float cx, float cy)
	{
		this.x = cx - px;
		this.y = cy - py;
	}
	public float dot(Vektor v)
	{
		return this.x * v.x + this.y * v.y;
	}

	public float mag()
	{
		return (float) Math.sqrt(x * x + y * y);
	}

	public void normalize()
	{
		/*
		 * can't make function call while scaling, because scaling x will affect
		 * the scaling of the y. So, save the magnitude.
		 */
		float mag = mag();
		x = x / mag ;
		y = y / mag;
	}
}
