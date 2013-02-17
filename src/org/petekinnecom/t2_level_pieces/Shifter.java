package org.petekinnecom.t2_level_pieces;

import java.awt.image.BufferedImage;

import org.petekinnecom.t2_game.Ball;
import org.petekinnecom.t2_level_editor.C;

public class Shifter extends Tile
{
	
	protected BufferedImage[] pngs = new BufferedImage[2];
	
	
	public Shifter(GenericShifter gShift)
	{
		super(gShift);
		this.pngs = gShift.pngs;
		
	}
	public Shifter(int id, int x, int y, BufferedImage toRenderPNG)
	{
		super(id, x, y, toRenderPNG);
	}
	@Override
	public boolean update(float deltaTick, Ball ball)
	{
		
		/*
		 * decide the correct color, then 
		 * collide as usual.  Vektors worry
		 * about correct color.
		 */
		if(ball.world == C.W_A)
			this.toRenderPNG = pngs[0];
		else
			this.toRenderPNG = pngs[1];
		return super.update(deltaTick, ball);
		
	}

	@Override
	public Shifter clone()
	{
		Shifter s = new Shifter(this.id, this.x, this.y, this.toRenderPNG);
		s.pngs = this.pngs;
		s.lines = this.copyLines();
		return s;
	}

	@Override
	public String getDBWriteString(int level_id)
	{
		return super.getDBWriteString(level_id);
	}

}
