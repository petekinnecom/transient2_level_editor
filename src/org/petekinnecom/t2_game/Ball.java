package org.petekinnecom.t2_game;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

import org.petekinnecom.t2_level_editor.C;
import org.petekinnecom.t2_level_pieces.Line;
import org.petekinnecom.t2_level_pieces.Vektor;

public class Ball
{
	public float x, y, r;
	public float lastX, lastY;
	public float vx, vy;
	public float ax, ay;
	public ArrayList<Vektor> velCorrectionVks = new ArrayList<Vektor>();

	public Color color = C.BALL_ALIVE;
	public boolean isAlive;
	public boolean isStuck;

	public int world = C.W_A;

	public Ball(Point bs, float r)
	{
		this.x = bs.x;
		this.y = bs.y;
		this.r = r;
		vy = 0;
		vx = 0;
		ay = -Level.G_ACCEL;
		ax = 0;
		lastX = x;
		lastY = y;
		isAlive = true;
		isStuck = false;
	}

	public void move(float deltaTick)
	{
		if (isStuck)
			return;
		lastX = x;
		lastY = y;

		/*
		 * 
		 * Limit the movement to one diameter per tick. This will stop the nasty
		 * effects of pauses as well as eliminate many collision problems.
		 */
		if (Math.abs((double) deltaTick * vx) > 2 * r)
		{
			if (vx > 0)
			{
				x += 2 * r;
			} else
			{
				x -= 2 * r;
			}
		} else
		{
			x += deltaTick * vx;
		}
		if (Math.abs((double) deltaTick * vy) > r)
		{
			if (vy > 0)
			{
				y += 2 * r;
			} else
			{
				y -= 2 * r;
			}
		} else
		{
			y += deltaTick * vy;
		}
	}

	float dx, dy;
	int _i;
	Vektor _v;
	public void postMoveFix(float deltaTick)
	{

		dx = dy = 0f;
		for (_i=0; _i<velCorrectionVks.size();_i++)
		{
			_v = velCorrectionVks.get(_i);
			dx += _v.x / (float) velCorrectionVks.size();
			dy += _v.y / (float) velCorrectionVks.size();
		}
		velCorrectionVks.clear();
		vx = (vx + dx);
		vy = (vy + dy);

		vy += deltaTick * ay;
		vx += deltaTick * ax;
		ay = -Level.G_ACCEL;

		x = Math.round(x);
		y = Math.round(y);

	}

	public void accelerate(float dx, float dy)
	{
		vx += dx;
		vy += dy;

		/*
		 * SPEED LIMITER
		 */
		if (Math.abs((double) vx) > Level.MAX_VX)
		{
			if (vx < 0)
				vx = -Level.MAX_VX;
			else
				vx = Level.MAX_VX;
		}
		if (Math.abs((double) vy) > Level.MAX_VY)
		{
			if (vy < 0)
				vy = -Level.MAX_VY;
			else
				vy = Level.MAX_VY;
		}
	}

	public Point getVelocity()
	{
		return new Point((int) vx, (int) vy);
	}

	public void setStuck(boolean b)
	{
		isStuck = b;
	}

	public void shift(int toWorld)
	{
		this.world = toWorld;
		this.isStuck = false;

	}
}
