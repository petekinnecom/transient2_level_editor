package org.petekinnecom.t2_game;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.petekinnecom.t2_level_editor.C;
import org.petekinnecom.t2_level_editor.EditLevel;
import org.petekinnecom.t2_level_pieces.LevelPiece;
import org.petekinnecom.t2_level_pieces.Renderable;
import org.petekinnecom.t2_level_pieces.Line;

public class Level
{
	public static final float G_ACCEL = 300f;
	public static final float MAX_VX = 1000f;
	public static final float MAX_VY = 627f;
	public static final float JUMP_FORCE = 400;
	public static final int MOVE_FORCE = 30;

	public int id;
	protected ArrayList<LevelPiece> pieces;
	public Point ballStart = new Point(32, 32);

	public BufferedImage backgroundPNG, foregroundPNG;
	public int bgDepth, fgDepth;
	public int width, height;

	/*
	 * we need a deep copy, so that original vektors stay intact.
	 */
	public Level()
	{
	}

	public Level(EditLevel l)
	{
		this.id = l.id;
		this.backgroundPNG = l.backgroundPNG;
		this.foregroundPNG = l.foregroundPNG;
		this.bgDepth = l.bgDepth;
		this.fgDepth = l.fgDepth;
		this.width = l.width;
		this.height = l.height;
		pieces = new ArrayList<LevelPiece>();
		for (LevelPiece t : l.getPieces())
		{
			pieces.add(t.clone());
		}
		this.ballStart = l.ballStart;
	}

	/*
	 * Here because the current level is held by the editor, then passed to this
	 * controller.
	 * 
	 * On the phone, this should be processed as things are read by the
	 * database.
	 */

	public void prepPieces()
	{
		for (LevelPiece p : pieces)
		{

			p.prep();
		}

	}

	ArrayList<Renderable> temp = new ArrayList<Renderable>();

	public ArrayList<Renderable> getRenderables(Box zoomBox)
	{
		temp.clear();

		for (_i=0;_i<pieces.size();_i++)
		{
			_lp = pieces.get(_i);
			if (_lp == null)
			{
				C.out("null piece :(");
				System.exit(1);
			}
			if (_lp.inBox(zoomBox))
			{
				temp.add(_lp);
			}
		}

		return temp;
	}

	ArrayList<LevelPiece> tempLP = new ArrayList<LevelPiece>();

	public ArrayList<LevelPiece> getLevelPieces(Box gridBox)
	{
		tempLP.clear();

		for (_i=0; _i<pieces.size();_i++)
		{
			_lp = pieces.get(_i);
			if (_lp.inBox(gridBox))
			{
				tempLP.add(_lp);
			}
		}

		return tempLP;
	}

	Box ballBox = new Box(0, 0, 1, 1);
	ArrayList<LevelPiece> nearPieces;
	ArrayList<Line> nearLines = new ArrayList<Line>();
	int _i, _j;
	Line _v;
	LevelPiece _lp;
	
	public void moveBall(float deltaTick, Ball ball)
	{
		/*
		 * Make a ball box and grab potential vektors. Then do two passes: first
		 * for dummy vektors. Second for non-dummy vektors.
		 */
		ballBox.x1 = (int) (ball.x - 4 * ball.r);
		ballBox.y1 = (int) (ball.y - 4 * ball.r);
		ballBox.x2 = (int) (ballBox.x1 + 8 * ball.r);
		ballBox.y2 = (int) (ballBox.y1 + 8 * ball.r);

		nearPieces = getLevelPieces(ballBox);

		nearLines.clear();
		for (_i=0; _i<nearPieces.size();_i++)
		{
			// p.update(deltaTick, ball);
			nearLines.addAll(nearPieces.get(_i).lines);
		}

		for (_i=0; _i<nearLines.size();_i++)
		{
			_v = nearLines.get(_i);
			if (_v.dummy)
				_v.collide(ball);
		}
		for (_i = 0; _i < 2 && !ball.isStuck; _i++)
		{
			for (_j=0; _j<nearLines.size();_j++)
			{
				_v = nearLines.get(_j);
				if (!_v.dummy)
					_v.collide(ball);
			}
		}

	}
}
