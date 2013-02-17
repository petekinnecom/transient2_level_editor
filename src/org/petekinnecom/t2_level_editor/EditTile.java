package org.petekinnecom.t2_level_editor;

import java.util.ArrayList;

import org.petekinnecom.t2_level_pieces.GenericTile;
import org.petekinnecom.t2_level_pieces.Tile;
import org.petekinnecom.t2_level_pieces.Line;

public class EditTile extends Tile
{
	public EditTile(GenericTile gt)
	{
		super(gt);
	}
	
	/*
	 * EDITOR FUNCTIONS
	 */


	public void addVektor(Line v)
	{
		lines.add(v);
	}

	public void setVektors(ArrayList<Line> v)
	{
		lines = v;
	}

}
