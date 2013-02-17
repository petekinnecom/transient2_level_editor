package org.petekinnecom.t2_game;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.Timer;

import org.petekinnecom.t2_level_editor.*;

public class GameController implements WindowListener, KeyListener
{

	private GameModel gameModel;
	private LevelRender levelRender;
	private Level level;
	private JFrame frame;

	private Box zoomBox;

	public float time = 0f;
	Timer tickTimer;

	public GameController(EditLevel l)
	{
		level = new Level(l);
		/*
		 * The level editor (and/or database) will give us tiles that have
		 * generic collision vectors. The first thing we must do is have each
		 * tile fix its vectors. This function also fixes it's bounding box.
		 */
		level.prepPieces();

		gameModel = new GameModel(level);
		levelRender = new LevelRender();

		/* Init layout */
		frame = new JFrame("Game!");
		frame.setSize(800, 480);
		frame.add(levelRender);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.addWindowListener(this);
		frame.addKeyListener(this);
		levelRender.addKeyListener(this);
		frame.setVisible(true);

		/* Init view Window */
		zoomBox = new Box(0, 0, 800, (int) (800 * levelRender.getAspect()));
		levelRender.setGrounds(level);
		levelRender.toggleGrid();
		levelRender.updateBoard(zoomBox, level.getRenderables(zoomBox),
				gameModel.getBall());
		repaint();

		tickTimer = new Timer(10, gameTick);
		tickTimer.start();
	}

	Point velocity;

	private void repaint()
	{

		/*
		 * move zoombox
		 */

		levelRender.updateBoard(zoomBox, gameModel.getRenderables(zoomBox),
				gameModel.getBall());
		levelRender.paintImmediately(0, 0, levelRender.getWidth(),
				levelRender.getHeight());
	}
	int frames = 0;
	long framesStart = 0;
	long framesEnd;
	Action gameTick = new AbstractAction() {
		long lastTime = System.currentTimeMillis();
		long thisTime = 0;
		float deltaTick;

		public void actionPerformed(ActionEvent e)
		{
			thisTime = System.currentTimeMillis();
			deltaTick = (thisTime - lastTime) / 1000f;
			lastTime = thisTime;
			time += deltaTick;

			gameModel.tick(C.TICK_MULTIPLIER * deltaTick, zoomBox);
		
			zoomBox = gameModel.getZoomBox(zoomBox.getWidth(),
					zoomBox.getHeight());

			repaint();
			// C.out("tick: "+deltaTick+" time: "+time);
			frames++;
			if(frames%60 == 0)
			{
				framesEnd = System.currentTimeMillis();
				C.out(60000f/(framesEnd - framesStart));
				framesStart = framesEnd;
			}
		}

	};

	@Override
	public void windowActivated(WindowEvent arg0)
	{

	}

	@Override
	public void windowClosed(WindowEvent arg0)
	{

	}

	@Override
	public void windowClosing(WindowEvent arg0)
	{
		tickTimer.stop();
	}

	@Override
	public void windowDeactivated(WindowEvent arg0)
	{

	}

	@Override
	public void windowDeiconified(WindowEvent arg0)
	{

	}

	@Override
	public void windowIconified(WindowEvent arg0)
	{

	}

	@Override
	public void windowOpened(WindowEvent arg0)
	{

	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		switch (e.getKeyCode())
		{

		case KeyEvent.VK_W:
		{

			gameModel.accelBall(0, Level.JUMP_FORCE);
			break;
		}
		case KeyEvent.VK_A:
		{
			gameModel.accelBall(-Level.MOVE_FORCE, 0);
			break;
		}
		case KeyEvent.VK_D:
		{
			gameModel.accelBall(Level.MOVE_FORCE, 0);
			break;
		}
		case KeyEvent.VK_E:
		{
			gameModel.getBall().shift(C.W_B);
			break;
		}
		case KeyEvent.VK_Q:
		{
			gameModel.getBall().shift(C.W_A);
			break;
		}
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0)
	{

	}

	@Override
	public void keyTyped(KeyEvent arg0)
	{

	}
}
