package org.petekinnecom.t2_tile_editor;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.petekinnecom.t2_level_editor.*;
import org.petekinnecom.t2_level_pieces.GenericTile;
import org.petekinnecom.t2_level_pieces.Tile;
import org.petekinnecom.t2_level_pieces.Line;

public class TileEditController implements MouseListener, MouseMotionListener
{
	private JFrame frame;
	private TileRender tRender;
	private GenericTile workingTile;
	private PiecePanel tp;
	
	public TileEditController(PiecePanel tp, GenericTile gt)
	{
		this.tp = tp;
		workingTile = gt;
		//tile = t;
		tRender = new TileRender(workingTile);

		frame = new JFrame("weee");
		frame.setSize(600, 600);
		frame.add(tRender);

		tRender.addMouseListener(this);
		tRender.addMouseMotionListener(this);
		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu("File");
		JMenuItem saveItem = fileMenu.add("Save");

		saveItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae)
			{
				saveTile();

			}
		});

		menuBar.add(fileMenu);

		JMenu actionMenu = new JMenu("Actions");
		JMenuItem eraseItem = fileMenu.add("Erase All");

		eraseItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae)
			{
				workingTile.setLines(new ArrayList<Line>());
				tRender.repaint();
			}
		});

		JMenuItem gridItem = fileMenu.add("Toggle Grid");

		gridItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae)
			{
				tRender.drawGrid = !tRender.drawGrid;
				tRender.repaint();

			}
		});

		actionMenu.add(gridItem);
		actionMenu.add(eraseItem);
		menuBar.add(fileMenu);
		menuBar.add(actionMenu);
		frame.setJMenuBar(menuBar);

		frame.setVisible(true);

	}

	private void saveTile()
	{
		try
		{
			DBHelper.writeLines(workingTile);
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		tp.setGenericLevelPiece(workingTile);
	}

	int x1 = -1;
	int y1 = -1;
	int x2, y2;

	@Override
	public void mousePressed(MouseEvent e)
	{
		for (Line tmp : workingTile.getLines())
		{
			tmp.setColor();
		}

		if (e.getButton() == MouseEvent.BUTTON1)
		{
			Point p = tRender.converToGridSpace(e.getX(), e.getY());
			if (x1 == -1)
			{
				x1 = p.x;
				y1 = p.y;
			} else
			{
				x2 = p.x;
				y2 = p.y;
				workingTile.addLine(new Line(x1, y1, x2, y2));
				x1 = x2;
				y1 = y2;
			}
		} else if (e.getButton() == MouseEvent.BUTTON2)
		{

			x1 = -1;
			y1 = -1;
			tRender.setTempVektor(null);

		} else if (e.getButton() == MouseEvent.BUTTON3)
		{
			Line v = getVektor(e.getX(), e.getY());
			if (v != null)
			{
				editVektor(v);

			}
		}
		tRender.repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{

	}

	@Override
	public void mouseEntered(MouseEvent e)
	{

	}

	@Override
	public void mouseExited(MouseEvent e)
	{

	}

	@Override
	public void mouseReleased(MouseEvent e)
	{

	}

	@Override
	public void mouseDragged(MouseEvent arg0)
	{

	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		Point p = tRender.converToGridSpace(e.getX(), e.getY());
		if (x1 != -1)
		{
			tRender.setTempVektor(new Line(x1, y1, p.x, p.y));
		}
		tRender.repaint();
	}

	private Line getVektor(float x, float y)
	{
		Point p1, p2;
		for (Line v : workingTile.getLines())
		{
			p1 = tRender.convertToCanvasSpace(v.px, v.py);
			p2 = tRender.convertToCanvasSpace(v.cx, v.cy);
			//C.out(p1 + ", "+p2 + " , ("+x+", "+y+")");
			if (Math.abs(p1.x - p2.x) - (Math.abs(p1.x - x) + Math.abs(x
					- p2.x))>-10)
			{
				

				if (Math.abs(p1.y - p2.y) - (Math.abs(p1.y - y) + Math.abs(y
						- p2.y))>-10)
				{

					v.color = new Color(255, 255, 255);
					return v;
				}
			}
		}
		return null;
	}

	private void editVektor(Line v)
	{
		LineEdit veFrame = new LineEdit(v);
		veFrame.setVisible(true);

	}
}
