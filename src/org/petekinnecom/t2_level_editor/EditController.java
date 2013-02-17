package org.petekinnecom.t2_level_editor;


import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import org.petekinnecom.t2_game.Ball;
import org.petekinnecom.t2_game.Box;
import org.petekinnecom.t2_game.GameController;
import org.petekinnecom.t2_level_pieces.GenericLevelPiece;
import org.petekinnecom.t2_level_pieces.GenericTile;
import org.petekinnecom.t2_level_pieces.LevelPiece;
import org.petekinnecom.t2_level_pieces.Tile;
import org.petekinnecom.t2_tile_editor.TileEditController;

public class EditController implements MouseListener, KeyListener,
		MouseWheelListener, MouseMotionListener
{
	private EditRender editRender;

	private PiecePanel selected;
	private JFrame levelFrame, tileFrame;
	private EditLevel level;
	private Ball ball;
	private Box zoomBox;
	
	private boolean flagPlacingBall = false;

	
	LevelPiece hoverPiece = null;
	Box hoverBox = null;
	public EditController(int level_id)
	{

		/*
		 * Init vars
		 */
		editRender = new EditRender();
		levelFrame = new JFrame("Fun!");
		/*
		 * Must load tiles before initing the layout, because the tile selection
		 * panel depends on the files.
		 */
		C.loadGenericPieces();
		try
		{
			level = DBHelper.readLevel(level_id);
			editRender.setGrounds(level);
		} catch (SQLException e)
		{
			level = new EditLevel(level_id);
			C.out("Level not found in DB.  Created new level with id: "
					+ level_id);
		}


		initLayout();

		// Init unimportant
		ball = new Ball(level.ballStart, 32);
		zoomBox = new Box(0, 0, 600, (int) (600 * editRender.getAspect()));
		editRender.updateBoard(zoomBox, level.getRenderables(zoomBox), ball);
		repaint();

	}

	/*
	 * Shove away the GUI code to make things easier to read.
	 */
	private void initLayout()
	{

		/*
		 * Table experiment
		 */

		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new WrapLayout());

		for (GenericLevelPiece t : C.piece_templates)
		{

			PiecePanel inside = new PiecePanel(t);
			inside.addMouseListener(this);

			inside.setPreferredSize(new Dimension(t.png.getWidth() + 10, t
					.png.getHeight() + 10));
			rightPanel.add(inside);
		}
		JScrollPane scrollPane = new JScrollPane(rightPanel);
		scrollPane
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		JTextArea console = new JTextArea();
		console.setEditable(false);
		JScrollPane consolePane = new JScrollPane(console);

		JPanel container = new JPanel(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.weightx = 1d;
		c.weighty = 0.8d;
		c.gridwidth = 1;
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;
		container.add(editRender, c);
		c = new GridBagConstraints();
		c.weightx = 1d;
		c.weighty = 0.2d;
		c.gridwidth = 1;
		c.gridy = 1;
		c.fill = GridBagConstraints.BOTH;
		// container.add(consolePane, c);
		// container.add(gameRender);

		// container.add(consolePane);

		levelFrame.add(container);
		levelFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		levelFrame.setSize(820, 540);
		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu("File");
		JMenuItem saveItem = fileMenu.add("Save");

		saveItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae)
			{
				saveLevel();

			}
		});

		JMenuItem loadItem = fileMenu.add("Load");
		loadItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				loadLevel();

			}
		});
		menuBar.add(fileMenu);

		JMenu optionsMenu = new JMenu("Options");
		JMenuItem toggleGridItem = optionsMenu.add("Grid Toggle");

		toggleGridItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				toggleGrid();

			}
		});

		JMenuItem testPlayItem = optionsMenu.add("Test Play");
		testPlayItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				testPlay();

			}
		});

		JMenuItem placeBallItem = optionsMenu.add("Place Ball");
		placeBallItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				flagPlacingBall = true;
				selected = null;
			}
		});

		
		menuBar.add(optionsMenu);
		levelFrame.setJMenuBar(menuBar);
		levelFrame.setVisible(true);

		tileFrame = new JFrame("tiles");
		tileFrame.add(scrollPane);
		tileFrame.setSize(300, 600);
		tileFrame.setVisible(true);

		/*
		 * Set up listeners.
		 */
		editRender.addMouseListener(this);
		editRender.addMouseWheelListener(this);
		editRender.addMouseMotionListener(this);
		editRender.addKeyListener(this);
		levelFrame.addKeyListener(this);
		tileFrame.addKeyListener(this);
		console.addKeyListener(this);

		levelFrame.addComponentListener(new ComponentListener() {
			// This method is called after the component's size changes
			public void componentResized(ComponentEvent evt)
			{
				// TODO: FIX THIS
				// Get new size
				zoomBox.x2 = zoomBox.x1 + editRender.getWidth();
				zoomBox.y2 = (int) (zoomBox.y1 + editRender.getHeight());
				repaint();
				C.out("Set gameRender size to (" + editRender.getWidth() + ", "
						+ editRender.getHeight() + ")");
			}

			@Override
			public void componentHidden(ComponentEvent arg0)
			{

			}

			@Override
			public void componentMoved(ComponentEvent arg0)
			{

			}

			@Override
			public void componentShown(ComponentEvent arg0)
			{

			}
		});

		/*
		 * Set up our little console
		 */

		C.console = console;
	}

	private void toggleGrid()
	{
		editRender.toggleGrid();

		repaint();
	}

	private void testPlay()
	{
		new GameController(level);
	}
	

	private void repaint()
	{
		editRender.updateBoard(zoomBox, level.getRenderables(zoomBox), ball);
		levelFrame.repaint();
		tileFrame.repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{

	}

	@Override
	public void mouseEntered(MouseEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		C.out("mouse button: " + e.getButton(), 1);

		try
		{
			/*
			 * try to grab clicked panel. If we clicked a panel, deselect old
			 * one.
			 */
			PiecePanel temp = (PiecePanel) e.getComponent();
			if (selected != null)
				selected.deSelect();
			selected = temp;
			C.out("Clicked: " + selected.getLevelPiece());
			selected.select();
			//hoverTile = new Tile(selected.getTile(), hoverBox);

			hoverPiece = selected.getLevelPiece();
			
			if (e.getButton() == MouseEvent.BUTTON3)
			{
				//new TileEditController(selected);
				selected.editPiece();
			}

		} catch (ClassCastException ex)
		{
			/*
			 * We have clicked on the level board. decide what to do...
			 */
			if(flagPlacingBall)
			{
				Box bs = editRender.convertToGridSpace(e.getX(), e.getY());
				level.ballStart = new Point(bs.x1, bs.y1);
				ball.x = bs.x1;
				ball.y = bs.y1;
				flagPlacingBall = false;
				
			}
			
			if (selected != null)
			{
				Box gridBox = editRender.convertToGridSpace(e.getX(), e.getY());

				if (e.getButton() == MouseEvent.BUTTON1)
				{
					// level.removeTile(gridBox);
					//LevelPiece lp = new LevelPiece(hoverPiece, gridBox);
					hoverPiece.x = gridBox.x1;
					hoverPiece.y = gridBox.y1;
					level.addPiece(hoverPiece.clone());
					C.out("added levelPiece at: " + gridBox);
				} else if (e.getButton() == MouseEvent.BUTTON3)
				{
					/*
					 * Give one point of free space, since textures overlap
					 * their last point.
					 */
					gridBox.x1++;
					gridBox.y1++;
					gridBox.x2--;
					gridBox.y2--;
					level.removePiece(gridBox);

					// hoverTile = null;
					// gameRender.setHoverTile(hoverTile);
					// gameRender.updateHoverBoard();
					// levelFrame.repaint();
				}
			}
		} finally
		{
			repaint();
		}

	}



	@Override
	public void mouseReleased(MouseEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		switch (e.getKeyCode())
		{

		case KeyEvent.VK_E:
		{
			zoomBox.x2 -= C.ZOOM_SPEED;
			zoomBox.y2 -= (int) (C.ZOOM_SPEED * editRender.getAspect());
			repaint();
			break;
		}
		case KeyEvent.VK_Q:
		{
			zoomBox.x2 += C.ZOOM_SPEED;
			zoomBox.y2 += (int) (C.ZOOM_SPEED * editRender.getAspect());
			repaint();
			break;
		}
		case KeyEvent.VK_W:
		{
			zoomBox.move(0, C.MOVE_SPEED);
			break;
		}
		case KeyEvent.VK_S:
		{
			zoomBox.move(0, -C.MOVE_SPEED);
			break;
		}
		case KeyEvent.VK_A:
		{
			zoomBox.move(-C.MOVE_SPEED, 0);
			break;
		}
		case KeyEvent.VK_D:
		{
			zoomBox.move(C.MOVE_SPEED, 0);
			break;
		}
		case KeyEvent.VK_P:
		{
			// saveLevel();
			break;
		}
		case KeyEvent.VK_L:
		{
			// loadLevel();
			break;
		}
		}
		repaint();
	}

	@Override
	public void keyReleased(KeyEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		/*
		 * When the wheel scrolls, grab the x,y and then move to the next tile.
		 */
		int notches = e.getWheelRotation();
		if (notches < 0)
			hoverPiece.rotate(notches);
		else
			hoverPiece.reflect();
		editRender.updateHoverBoard();
		levelFrame.repaint();
	}

	private void loadLevel()
	{
		try
		{
			String s = (String) JOptionPane.showInputDialog(null,
					"Input the level_id to load. ", "Level Load",
					JOptionPane.PLAIN_MESSAGE, null, null, level.id);

			// If a string was returned, say so.
			if ((s != null) && (s.length() > 0))
			{
				level = DBHelper.readLevel(Integer.parseInt(s));
				editRender.setGrounds(level);
			}
		} catch (SQLException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		repaint();
	}

	private void saveLevel()
	{
		try
		{
			String s = (String) JOptionPane.showInputDialog(null,
					"Input the _id of the level to save. "
							+ "(Warning: Automatic overwrite.)", "Level Save",
					JOptionPane.PLAIN_MESSAGE, null, null, level.id);

			// If a string was returned, say so.
			if ((s != null) && (s.length() > 0))
			{
				level.id = Integer.parseInt(s);
				DBHelper.writeLevel(level);
			}
		} catch (SQLException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		repaint();
	}

	@Override
	public void mouseDragged(MouseEvent arg0)
	{
		// TODO Auto-generated method stub

	}



	@Override
	public void mouseMoved(MouseEvent e)
	{
		hoverBox = editRender.convertToGridSpace(e.getX(), e.getY());
		if (selected == null)
		{
		} else if (hoverPiece != null)
		{
			//hoverPiece = new LevelPiece(hoverPiece, hoverBox);
			hoverPiece.x = hoverBox.x1;
			hoverPiece.y = hoverBox.y1;
			editRender.setHoverPiece(hoverPiece);
			editRender.updateHoverBoard();
			levelFrame.repaint();
		}

	}
}
