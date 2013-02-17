package org.petekinnecom.t2_tile_editor;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import org.petekinnecom.t2_level_editor.C;
import org.petekinnecom.t2_level_pieces.GenericTile;
import org.petekinnecom.t2_level_pieces.Tile;
import org.petekinnecom.t2_level_pieces.Line;

public class TileRender extends JPanel
{
	private BufferedImage tilePNG;
	public boolean drawGrid = true;
	private GenericTile tile;
	private RenderingHints hints;
	private Line tempVektor = null;
	
	TileRender(GenericTile t)
	{
		tilePNG = t.png;
		tile = t;
        hints = new RenderingHints(null);
        hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
       // hints.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
       // hints.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
       // hints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	}
	BufferedImage board;
	public void paintComponent(Graphics g)
	{
		board  = new BufferedImage(tilePNG.getWidth()+1, tilePNG.getHeight()+1,	BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = board.createGraphics();
		g2d.setRenderingHints(hints);
		g2d.setColor(new Color(240,240,240));
		g2d.fillRect(0, 0, board.getWidth(), board.getHeight());
		g2d.drawImage(tilePNG, 0, 1, tilePNG.getWidth(), tilePNG.getHeight()+1, 
				0,0 , tilePNG.getWidth(), tilePNG.getHeight(), null);
		
		g.drawImage(board, 0, 0, this.getWidth(), this.getHeight(), 0, 0, board.getWidth(), board.getHeight(),null);
		g2d.dispose();
		g2d = (Graphics2D) g;
		g2d.setRenderingHints(hints);
		if(drawGrid)
		{
			float scaleX = (float) this.getWidth() / (float) tilePNG.getWidth();
			g2d.setColor(new Color(125, 125, 125, 100));
			for (int i = 0; i < tilePNG.getWidth(); i++)
			{
				g2d.drawLine((int) (i * scaleX), 0, (int) (i * scaleX),
						this.getHeight());
			}

			float scaleY = (float) this.getHeight()
					/ (float) tilePNG.getHeight();
			for (int i = 0; i < tilePNG.getHeight(); i++)
			{
				g2d.drawLine(0, (int) (i * scaleY), this.getWidth(),
						(int) (i * scaleY));
			}
		}
		g2d.setColor(new Color(0, 0, 255));
		int x1, y1, x2, y2;
		float scaleX, scaleY;
		scaleX = (float) this.getWidth()/(float) tilePNG.getWidth();
		scaleY = (float) this.getHeight()/ (float) tilePNG.getHeight();
		g2d.setStroke(new BasicStroke(2.0f));
		Point p1, p2;
		for(Line v : tile.getLines())
		{
			
			g2d.setColor(new Color(255, 0, 0));
			p1 = convertToCanvasSpace(v.px, v.py);
			p2 = convertToCanvasSpace(v.cx, v.cy);

			g2d.setColor(v.color);
			g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
			
			
			p1 = convertToCanvasSpace(v.px+v.orth.x, v.py+v.orth.y);
			p2 = convertToCanvasSpace(v.cx+v.orth.x, v.cy+v.orth.y);

			g2d.setColor(new Color(120, 120, 120));
			if(v.killType!=C.KT_NONE)
				g2d.setColor(new Color(120, 0, 0));
			g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
		}
		if(tempVektor!=null)
		{
			Line v = tempVektor;
			g2d.setColor(new Color(255, 0, 0));
			p1 = convertToCanvasSpace(v.px, v.py);
			p2 = convertToCanvasSpace(v.cx, v.cy);

			g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
			
			
			p1 = convertToCanvasSpace(v.px+v.orth.x, v.py+v.orth.y);
			p2 = convertToCanvasSpace(v.cx+v.orth.x, v.cy+v.orth.y);

			g2d.setColor(new Color(120, 120, 120));

			g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
		}
	}

	public Point converToGridSpace(float x, float y)
	{
		float scaleX, scaleY;
		scaleX = (float) this.getWidth()/(float) tilePNG.getWidth();
		scaleY = (float) this.getHeight()/ (float) tilePNG.getHeight();
		int x1 = (int) (x/scaleX);
		int y1 = (int) ((this.getHeight()-y)/scaleY);
		return new Point(x1, y1);
	}
	public Point convertToCanvasSpace(float x, float  y)
	{
		float scaleX, scaleY;
		scaleX = (float) this.getWidth()/(float) tilePNG.getWidth();
		scaleY = (float) this.getHeight()/ (float) tilePNG.getHeight();
		int x1,y1;
		x1 = (int) (scaleX * x);
		y1 = this.getHeight() - (int) (scaleY * y);
		
		
		return new Point(x1, y1);
	}

	public void setTempVektor(Line v)
	{
		tempVektor = v;
		
	}
}
