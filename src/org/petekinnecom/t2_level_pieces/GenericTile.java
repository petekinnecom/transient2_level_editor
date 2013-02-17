package org.petekinnecom.t2_level_pieces;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.petekinnecom.t2_level_editor.PiecePanel;
import org.petekinnecom.t2_tile_editor.TileEditController;

public class GenericTile extends GenericLevelPiece implements Cloneable
{
	
	
	
	public GenericTile(int id, BufferedImage png)
	{
		super(id, png);
		lines = new ArrayList<Line>();
	}
	
	public void addLine(Line v)
	{
		lines.add(v);
	}
	

	public ArrayList<Line> getLines()
	{
		return lines;
	}
	public void setLines(ArrayList<Line> v)
	{
		this.lines = v;
	}

	@Override
	public LevelPiece toLevelPiece()
	{
		return new Tile(this);
	}

	@Override
	public void editPiece(PiecePanel tp)
	{
		/*
		 * Make a copy so that changes 
		 * only take effect if saved.
		 */
		GenericTile gt = new GenericTile(this.id, this.png);
		gt.lines = this.copyLines();
		new TileEditController(tp, gt );
	}
}
