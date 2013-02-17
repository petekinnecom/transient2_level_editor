package org.petekinnecom.t2_level_pieces;

import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.petekinnecom.t2_game.Ball;
import org.petekinnecom.t2_game.Box;
import org.petekinnecom.t2_level_editor.C;

public class Tile extends LevelPiece
{


	/*
	 * INITIATOR FUNCTIONS
	 */
	public Tile(int id, int x, int y, BufferedImage png)
	{
		super(x, y, png);
		this.id = id;
	}

	public Tile(GenericTile gt)
	{
		super(gt.id, gt.png);
		this.lines = gt.copyLines();
	}
	
	

	
	/*
	 * Called when reading levels.
	 * TODO: Should prep vektors here?  !
	 */
	public Tile(GenericLevelPiece template, int x, int y, int rotation, int reflect)
	{
		super(x, y, template.png, reflect, rotation);
		this.id = template.id;
		this.lines = template.copyLines();
	}

	/* Deep copy */
	public Tile(Tile t)
	{
		super(t.x, t.y, t.toRenderPNG, t.reflect, t.rotation);
		this.id = t.id;
		this.lines = t.copyLines();
	}

	/*
	 * GETTER FUNCTIONS
	 */





	public String toString()
	{
		return "Tile id: " + id;
	}



	Line _L;
	int _i;
	int collided;
	@Override
	public boolean update(float deltaTick, Ball ball)
	{

		collided = 0;
		// hookVektors.clear();
		
		for (_i=0;_i<lines.size();_i++)
		{
			_L = lines.get(_i);
			collided += _L.collide(ball);

		}

		if(collided>0)
			return true;
		return false;

	}


	@Override
	public Tile clone()
	{
		return new Tile(this);
	}

	@Override
	public String getDBWriteString(int level_id)
	{
		return "INSERT INTO level_data " + "("
		+ "level_id, piece_id, x, y, rotation, reflect) VALUES "
		+ "(" + level_id + ", " + id + ", " + x + ", " + y
		+ ", " + rotation + ", " + reflect + ");";
	}
}
