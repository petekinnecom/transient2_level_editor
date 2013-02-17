package org.petekinnecom.t2_level_pieces;

import java.awt.image.BufferedImage;

import org.petekinnecom.t2_level_editor.PiecePanel;

public class GenericShifter extends GenericTile
{
	public BufferedImage[] pngs;
	
	
	public GenericShifter(int id, BufferedImage png_a, BufferedImage png_b)
	{
		/*
		 * The first png will be displayed in the editor.
		 */
		super(id, png_a);
		pngs = new BufferedImage[2];
		pngs[0] = png_a;
		pngs[1] = png_b;
	}
	@Override
	public Shifter toLevelPiece()
	{
		return new Shifter(this);
	}

	@Override
	public void editPiece(PiecePanel tp)
	{
		super.editPiece(tp);
		
	}
	

}
