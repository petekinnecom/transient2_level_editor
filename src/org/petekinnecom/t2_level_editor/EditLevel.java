package org.petekinnecom.t2_level_editor;

import java.awt.Point;
import java.util.ArrayList;

import org.petekinnecom.t2_game.Box;
import org.petekinnecom.t2_game.Level;
import org.petekinnecom.t2_level_pieces.LevelPiece;
import org.petekinnecom.t2_level_pieces.Tile;

public class EditLevel extends Level
{
	
	/*
	 * Set the defaults for the editor will need to change for game.
	 */
	public EditLevel(int id)
	{
		pieces = new ArrayList<LevelPiece>();
		this.id = id;
	}

	public ArrayList<LevelPiece> getPieces()
	{
		return pieces;
	}
	
	public void addPiece(LevelPiece t)
	{
		pieces.add(t);
	}

	public void removePiece(Box gridBox)
	{

		ArrayList<LevelPiece> delTiles = getLevelPieces(gridBox);
		for(LevelPiece t:delTiles)
		{
			pieces.remove(t);
		}
	}

}
