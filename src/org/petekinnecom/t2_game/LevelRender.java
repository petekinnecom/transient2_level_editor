package org.petekinnecom.t2_game;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JComponent;

import org.petekinnecom.t2_level_editor.C;
import org.petekinnecom.t2_level_pieces.Renderable;


public class LevelRender extends JComponent
{
	
	private BufferedImage backgroundPNG, foregroundPNG;
	
	protected Box zoomBox;
	private float bgScaleX, bgScaleY, fgScaleX, fgScaleY;
	protected BufferedImage board;
	protected RenderingHints hints;

	private boolean drawGrid = true;
	private int gridWidth = C.GRID_DEFAULT;
	
	public LevelRender()
	{
		super();
		zoomBox = new Box(0, 0, 100, 100);
		board = new BufferedImage(zoomBox.getWidth(), zoomBox.getHeight(),
				BufferedImage.TYPE_INT_RGB);
		hints = new RenderingHints(null);
		 //hints.put(RenderingHints.KEY_ANTIALIASING,
		 //RenderingHints.VALUE_ANTIALIAS_ON);

		 //hints.put(RenderingHints.KEY_INTERPOLATION,
		 //RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		// hints.put(RenderingHints.KEY_INTERPOLATION,
		//RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		 //hints.put(RenderingHints.KEY_RENDERING,
		 //RenderingHints.VALUE_RENDER_QUALITY);

	}

	public void toggleGrid()
	{
		drawGrid = !drawGrid;
		if (drawGrid)
			gridWidth = C.GRID_DEFAULT;
		else
			gridWidth = 1;
	}
	

	public void setGrounds(Level level)
	{
		backgroundPNG = level.backgroundPNG;
		bgScaleX = (float) backgroundPNG.getWidth() / level.width;
		bgScaleY = (float) backgroundPNG.getHeight() / level.height;
		fgScaleX = (float) backgroundPNG.getWidth() / level.width;
		fgScaleX = (float) backgroundPNG.getHeight() / level.height;

	}

	public void paintComponent(Graphics g)
	{
		Graphics2D view = (Graphics2D) g;
		//view.setRenderingHints(hints);
		view.drawImage(board, 0, 0, this.getWidth(), this.getHeight(), 0,
				board.getHeight(), board.getWidth(), 0, null);
		

	}
	
	
	AffineTransform trans = new AffineTransform();
	Graphics2D g2d;
	int x1, y1, x2, y2;
	BufferedImage png;
	int bx1, by1, bx2, by2;
	BufferedImage lastFrame;
	public void updateBoard(Box zoomBox, ArrayList<Renderable> tiles, Ball ball)
	{
		/*
		 * set data first
		 */
		this.zoomBox = zoomBox;
		if(zoomBox.getWidth() != board.getWidth() || zoomBox.getHeight() != board.getHeight())
		{
			board = new BufferedImage(zoomBox.getWidth(), zoomBox.getHeight(),
				BufferedImage.TYPE_INT_RGB);
		}
		
		
		lastFrame = board.getSubimage(0, 0, board.getWidth(), board.getHeight());
		/* Now draw */
		g2d = board.createGraphics();
    	g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f)); // Set current alpha
		g2d.setRenderingHints(hints);
		// TODO:  200, the alpha is the 'dream effect' constant
		g2d.setColor(new Color(255, 255, 255, 230));
		g2d.fillRect(0, 0, board.getWidth(), board.getHeight());

		// Draw background
		/*
		 * PARALAX int bx1 = (int) ((zoomBox.x1-bgDepth) * bgScaleX); int by1 =
		 * backgroundPNG.getHeight() - (int)(zoomBox.y1 * bgScaleY); int bx2 =
		 * (int) ((zoomBox.x2+bgDepth) * bgScaleX); int by2 =
		 * backgroundPNG.getHeight() - (int)(zoomBox.y2 * bgScaleY);
		 */

		// NON_PARALAX
		bx1 = (int) ((zoomBox.x1) * bgScaleX);
		by1 = backgroundPNG.getHeight() - (int) (zoomBox.y1 * bgScaleY);
		bx2 = (int) ((zoomBox.x2) * bgScaleX);
		by2 = backgroundPNG.getHeight() - (int) (zoomBox.y2 * bgScaleY);

		g2d.drawImage(backgroundPNG, 0, 0, board.getWidth(), board.getHeight(),
				bx1, by1, bx2, by2, null);


		if (drawGrid)
		{
			g2d.setColor(new Color(240, 240, 240));
			for (int i = gridWidth - (zoomBox.x1 % gridWidth); i < zoomBox
					.getWidth(); i += gridWidth)
			{
				g2d.drawLine(i, 0, i, board.getHeight());
			}
			for (int i = gridWidth - (zoomBox.y1 % gridWidth); i < zoomBox
					.getHeight(); i += gridWidth)
			{
				g2d.drawLine(0, i, board.getWidth(), i);
			}
		}
		
		for (Renderable t : tiles)
		{
			png = t.getPNG();
			x1 = t.x - zoomBox.x1;
			y1 = t.y - zoomBox.y1;
			x2 = x1 + png.getWidth();
			y2 = y1 + png.getHeight();
			
			trans.setToIdentity();
			trans.translate(x1, y1);
			trans.translate(png.getWidth() / 2, png.getHeight() / 2);
			trans.scale(t.reflect, -1.0);
			trans.rotate(Math.toRadians(t.rotation));
			trans.translate(-png.getWidth() / 2, -png.getHeight() / 2);
			g2d.drawImage(png, trans, null);
		}
        g2d.setColor(ball.color);
        g2d.fillOval((int) (ball.x - ball.r) - zoomBox.x1, (int) (ball.y - ball.r)-zoomBox.y1, Math.round(2*ball.r), Math.round(2*ball.r));

		g2d.dispose();
	}

	public float getAspect()
	{
		return (float) getHeight() / (float) getWidth();
	}
	public Box convertToGridSpace(int x, int y)
	{
		y = this.getHeight() - y;
		int tx = zoomBox.x1
				+ (int) ((float) x / (float) this.getWidth() * (float) zoomBox
						.getWidth());
		int ty = zoomBox.y1
				+ (int) ((float) y / (float) this.getHeight() * (float) zoomBox
						.getHeight());
		tx = tx - (tx % gridWidth + gridWidth) % gridWidth;
		ty = ty - (ty % gridWidth + gridWidth) % gridWidth;

		return new Box(tx, ty, tx + gridWidth, ty + gridWidth);
	}
}
