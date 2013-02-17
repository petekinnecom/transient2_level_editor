package org.petekinnecom.t2_level_pieces;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;

import org.petekinnecom.t2_game.Ball;
import org.petekinnecom.t2_level_editor.C;

public class Line
{

	public float px, py, cx, cy;
	public Vektor orth, v;
	public Color color;
	double mag;
	public int world;
	public boolean dummy;
	public int killType;
	public float friction;

	public Line()
	{
	}

	
	/*
	 * defaults here are used by tileEditController
	 */
	public Line(float px, float py, float cx, float cy)
	{
		this.px = px;
		this.py = py;
		this.cx = cx;
		this.cy = cy;
		
		setOrth();

		this.color = new Color(0, 0, 0);

		/* defaults */
		this.world = C.W_SOLID;
		this.dummy = false;
		this.killType = C.KT_NONE;
		this.friction = 1f;
		setColor();
	}

	public Line(float px, float py, float cx, float cy, int world, int dummyI,
			int killType, float friction)
	{
		this(px,py,cx,cy,world,false,killType,friction);
		if(dummyI!=0)
			this.dummy = true;

	}

	public Line(float px, float py, float cx, float cy, int world,
			boolean dummy, int killType, float friction)
	{
		this(px,py,cx,cy);
		
		this.world = world;
		this.dummy = dummy;
		this.killType = killType;
		this.friction = friction;

		setColor();

	}




	public void setOrth()
	{
		this.v = new Vektor(cx - px, cy-py);
		orth = new Vektor();

		mag = Math.sqrt(v.x*v.x+v.y*v.y);

		orth.x = (float) (v.y / mag);
		orth.y = (float) (-v.x / mag);
	}

	public String toString()
	{
		return "(" + px + " , " + py + ") to (" + cx + " , " + cy
				+ ")  length: " + v.mag() + "\n" + "world: " + world
				+ " killType: " + killType + " dummy: " + dummy;
	}

	public void setColor()
	{
		if (this.world == C.W_SOLID)
			color = new Color(200, 0, 0);
		else if (this.world == C.W_A)
			color = new Color(0, 200, 0);
		else if (this.world == C.W_B)
			color = new Color(0, 0, 200);
		else
			color = new Color(200, 200, 0);
	}

	public Line copy()
	{
		return new Line(px, py, cx, cy, world, dummy, killType, friction);
	}

	public void prep(int x, int y, int width, int height, int reflect,
			int rotation)
	{
		AffineTransform trans = new AffineTransform();
		trans.setToIdentity();
		trans.translate(x, y);
		trans.translate(width / 2, height / 2);
		trans.scale(reflect, 1.0);
		trans.rotate(Math.toRadians(-rotation));
		trans.translate(-width / 2, -height / 2);

		Point start = new Point((int) px, (int) py);

		Point left = new Point();
		trans.transform(start, left);
		start.x = (int) cx;
		start.y = (int) cy;
		Point right = new Point();
		trans.transform(start, right);
		if (reflect == 1)
		{
			this.px = left.x;
			this.py = left.y;
			this.cx = right.x;
			this.cy = right.y;
		} else
		{
			this.px = right.x;
			this.py = right.y;
			this.cx = left.x;
			this.cy = left.y;
		}
		setOrth();
	}

	int collided;
	float bpx, bpy;
	Vektor leftHook = new Vektor();
	Vektor rightHook = new Vektor();
	Vektor vel = new Vektor();
	Vektor addVektor = new Vektor();
	Vektor leftSide = new Vektor();
	float offSet;

	public int collide(Ball ball)
	{
		if (this.world != C.W_SOLID && this.world != ball.world)
			return 0;

		int collided = 0;
		bpx = (ball.x + ball.r * this.orth.x);
		bpy = (ball.y + ball.r * this.orth.y);
		leftHook.set(bpx, bpy, this.px, this.py);
		rightHook.set(bpx, bpy, this.cx, this.cy);
		vel.set(0, 0, ball.vx, ball.vy);
		/*
		 * If both are negative or positive, then we are out of range. So we
		 * multiply them to find out whether they are opposites.
		 */

		/*
		 * Added vel.normalDot(v)>0 :
		 * 
		 * This ensures that the vektor we're colliding with is less than
		 * parallel to our ball's velocity. This reduces the chances of the ball
		 * 'jumping' through a collision vektor:
		 * 
		 * theta(v,n)<90 -> sign(cos(theta)) = sign(v dot n) >0
		 */
		if (leftHook.dot(this.v) * rightHook.dot(this.v) <= 0
				&& vel.dot(this.orth) > 0)
		{

			/*
			 * Determines whether correction vector is telling us we're below
			 */

			if (leftHook.dot(this.orth) <= 0)
			{

				/*
				 * check to see how far under the ball is
				 */
				// TODO: What is the 4 for? ( [2r]^2 = d^2 ) Seems to
				// provide
				// a maximum range for correction? Should
				// be a constant?
				if (this.orth.x * leftHook.dot(this.orth) * this.orth.x
						* leftHook.dot(this.orth) + this.orth.y
						* leftHook.dot(this.orth) * this.orth.y
						* leftHook.dot(this.orth) < 4 * ball.r * ball.r)
				{

					/*
					 * If the vektor is a dummy, the ball is inside a shifter
					 * and cannot move.
					 * 
					 * Else, we fix its location and continue
					 */
					if (this.dummy == true)
					{
						ball.setStuck(true);
						return 0;
					}
					/*
					 * fix location
					 */
					ball.x = (ball.x + this.orth.x * leftHook.dot(this.orth));
					ball.y = (ball.y + this.orth.y * leftHook.dot(this.orth));

					/*
					 * the 1.f is a bounce factor
					 */
					ball.velCorrectionVks.add(new Vektor(-this.orth.x
							* vel.dot(this.orth) * 1.f, -this.orth.y
							* vel.dot(this.orth) * 1.f));

					if (this.killType == C.KT_EXPLODE)
					{
						ball.isAlive = false;
						ball.color = C.BALL_DEAD;
					}
					collided++;
				}
			}
		}

		else
		{
			// TODO: this should not nullify the velocity as it currently
			// does
			// in other words, the ball gets sucked in to the corner, even
			// if it's just passing.

			/*
			 * the ball is out of range. See if it's close to either corner.
			 */
			leftSide.set(ball.x, ball.y, this.px, this.py);
			if (leftSide.mag() < ball.r)
			{
				/*
				 * It is, correct it by that distance in the
				 */

				offSet = ball.r - leftSide.mag();
				leftSide.normalize();
				ball.x = (ball.x - offSet * (leftSide.x));
				ball.y = (ball.y - offSet * (leftSide.y));
				collided++;

			}

		}
		// hookVektors.add(leftHook);
		// hookVektors.add(rightHook);
		return collided;
	}
}
