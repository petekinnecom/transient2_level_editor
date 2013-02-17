package org.petekinnecom.t2_level_pieces;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.petekinnecom.t2_level_editor.PiecePanel;

public abstract class GenericLevelPiece implements Cloneable
{
	public int id;
	public BufferedImage png;
	public ArrayList<Line> lines;
	public int rotation, reflection;
	
	
	public abstract LevelPiece toLevelPiece();
	public abstract void editPiece(PiecePanel tp);
	public GenericLevelPiece(int id, BufferedImage png)
	{
		this.id = id;
		this.png = png;
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
