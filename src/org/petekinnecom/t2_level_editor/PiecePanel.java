package org.petekinnecom.t2_level_editor;

import java.awt.Color;
import java.awt.Graphics;


import javax.swing.JPanel;

import org.petekinnecom.t2_level_pieces.GenericLevelPiece;
import org.petekinnecom.t2_level_pieces.GenericTile;
import org.petekinnecom.t2_level_pieces.LevelPiece;
import org.petekinnecom.t2_level_pieces.Tile;

public class PiecePanel extends JPanel
{

	private GenericLevelPiece glevelPiece;
	private boolean selected;
	public PiecePanel(GenericLevelPiece gpiece)
	{
		super();
		this.glevelPiece = gpiece;
		this.setBackground(new Color(125, 0, 125));
		
	}
	public void paintComponent(Graphics g)
	{
		g.setColor(new Color(255, 255, 0));
		if(selected)
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
        g.drawImage(glevelPiece.png, 5, 5, null);
	}
	
	public LevelPiece getLevelPiece()
	{
		/*
		 * The tile panel holds a generic tile
		 * We will convert it to a regular tile
		 * before handing it over to the level
		 * editor so that it is editable.
		 */
		
		return glevelPiece.toLevelPiece();
	}
	
	/*
	 * The reason this function was hard to work 
	 * with is because it shouldn't exist.  Don't 
	 * copy the object and pass to an editor,
	 * tell the object to edit itself!
	 */
	
//	public GenericLevelPiece getGenericLevelPieceCopy()
//	{
//		/*
//		 * This is for the tile editor window.
//		 * Here, we want to edit the generic
//		 * tile so the vektors are changed 
//		 * in all instances.  We return
//		 * a copy of the tile so that changes 
//		 * are not permanent.  It will be saved
//		 * to database if needed.
//		 * 
//		 */
//		
//		
//		return (GenericLevelPiece) glevelPiece.clone();
//	}
	
	public void setGenericLevelPiece(GenericLevelPiece t)
	{
		this.glevelPiece = t;
	}
	public void select()
	{
		selected = true;
	}
	public void deSelect()
	{
		selected = false;
	}
	public void editPiece()
	{
		glevelPiece.editPiece(this);
		
	}
}
