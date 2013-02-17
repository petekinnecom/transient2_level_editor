package org.petekinnecom.t2_level_editor;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JTextArea;

import org.petekinnecom.t2_level_pieces.GenericLevelPiece;
import org.petekinnecom.t2_level_pieces.GenericTile;
import org.petekinnecom.t2_level_pieces.Tile;

public class C
{
	public static JTextArea console = null;
	
	private static final int DEBUG_LEVEL = 1;
	
	public static int ZOOM_SPEED = 32;
	public static int MOVE_SPEED = 16;
	
	//This is what it resets to
	public static final int GRID_DEFAULT = 8;

	public static final float TICK_MULTIPLIER = 4f;
	
	public static final int W_SOLID = 0;
	public static final int W_A = 1;
	public static final int W_B = 2;
	public static final int W_LIMBO = 3;
	
	public static final int KT_NONE = 0;
	public static final int KT_EXPLODE = 1;
	
	public static ArrayList<GenericLevelPiece> piece_templates;
	public static GenericTile nullTile;
	public static GenericTile emptyTile;
	
	
	public static final Color BALL_ALIVE = new Color(0, 220, 0);
	public static final Color BALL_DEAD = new Color(220, 0, 0);
	
	public static void out(String s)
	{
		if(console!=null)
		{
			console.append(s + "\n");
			console.setCaretPosition(console.getDocument().getLength());
		}
		System.out.println(s);
	}
	public static void out(String s, int i)
	{
		if(C.DEBUG_LEVEL >= i)
		{
			out(s);
		}
	}
	public static void out(Object o)
	{
		System.out.println(o);
	}
	public static void loadGenericPieces()
	{
		piece_templates = new ArrayList<GenericLevelPiece>();
		try
		{
			piece_templates.addAll(DBHelper.readGenericTiles());
			piece_templates.addAll(DBHelper.readGenericShifters());
			
			for(GenericLevelPiece lp : piece_templates)
			{
				lp.lines = DBHelper.readLines(lp.id);
			}
			
		} catch (SQLException e)
		{
			
			e.printStackTrace();
		}
	}
	

	
	public static GenericLevelPiece getGenericPieceById(int tile_id)
	{
		for(GenericLevelPiece t : piece_templates)
		{
			if(t.id == tile_id)
				return t;
		}
		return nullTile;
	}
}
