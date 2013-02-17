package org.petekinnecom.t2_level_editor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import org.petekinnecom.t2_game.LevelRender;
import org.petekinnecom.t2_level_pieces.LevelPiece;
import org.petekinnecom.t2_level_pieces.Tile;

public class EditRender extends LevelRender
{
	BufferedImage hoverBoard;
	LevelPiece hoverPiece;
	EditRender()
	{
		super();
		hoverBoard = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
 
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		Graphics2D view = (Graphics2D) g;
		view.setRenderingHints(hints);
		view.drawImage(hoverBoard, 0, 0, this.getWidth(), this.getHeight(), 0,
				hoverBoard.getHeight(), hoverBoard.getWidth(), 0, null);
	}
	public void setHoverPiece(LevelPiece t)
	{
		hoverPiece = t;
	}
	public void updateHoverBoard()
	{
		hoverBoard = new BufferedImage(zoomBox.getWidth(), zoomBox.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = hoverBoard.createGraphics();
        g2d.setRenderingHints(hints);
		
		BufferedImage png;
		int x1,y1,x2,y2;
		if(hoverPiece!=null)
		{
			png = hoverPiece.getPNG();
			x1 = hoverPiece.x - zoomBox.x1;
			y1 = hoverPiece.y - zoomBox.y1;
			x2 = x1 + png.getWidth();
			y2 = y1 + png.getHeight();
			AffineTransform trans = new AffineTransform();
			trans.setToIdentity();
			trans.translate(x1, y1);
			trans.translate(png.getWidth()/2, png.getHeight()/2);
			trans.scale( hoverPiece.reflect, -1.0 );
			trans.rotate(Math.toRadians(hoverPiece.rotation));
			trans.translate(-png.getWidth()/2, -png.getHeight()/2);
			
			g2d.drawImage(png, trans, null);
			//g2d.drawImage(png, x1, y1, x2, y2, 0, png.getHeight(), png.getWidth(), 0,null);
			g2d.setColor(new Color(255,255,0));
			g2d.drawRect(x1, y1, png.getWidth(), png.getHeight());

		}
		
	}
}
