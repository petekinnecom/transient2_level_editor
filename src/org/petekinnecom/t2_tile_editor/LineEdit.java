package org.petekinnecom.t2_tile_editor;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.petekinnecom.t2_level_editor.C;
import org.petekinnecom.t2_level_pieces.Line;

public class LineEdit extends JFrame
{
	private JComboBox worldCombo, dummyCombo, ktCombo;
	private JTextField frictionText;
	private JPanel panel;
	private JButton okButton;
	protected final Line v;

	LineEdit(final Line vek)
	{
		this.v = vek;
		this.setSize(400, 300);

		panel = new JPanel(new GridLayout(5, 2));
		worldCombo = new JComboBox();
		worldCombo.setEditable(false);
		worldCombo.addItem("Solid");
		worldCombo.addItem("A (green)");
		worldCombo.addItem("B (blue)");

		if (v.world == C.W_SOLID)
			worldCombo.setSelectedIndex(0);
		else if (v.world == C.W_A)
			worldCombo.setSelectedIndex(1);
		else if (v.world == C.W_B)
			worldCombo.setSelectedIndex(2);

		worldCombo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				int s = worldCombo.getSelectedIndex();
				if (s == 0)
					v.world = C.W_SOLID;
				else if (s == 1)
					v.world = C.W_A;
				else if (s == 2)
					v.world = C.W_B;

			}
		});

		dummyCombo = new JComboBox();
		dummyCombo.addItem("not dummy");
		dummyCombo.addItem("dummy");

		if (v.dummy)
			dummyCombo.setSelectedIndex(1);
		else
			dummyCombo.setSelectedIndex(0);

		dummyCombo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				int s = dummyCombo.getSelectedIndex();
				if (s == 0)
					v.dummy = false;
				else
					v.dummy = true;
			}
		});

		ktCombo = new JComboBox();
		ktCombo.addItem("No killType");
		ktCombo.addItem("Explode");

		if(v.killType == C.KT_NONE)
			ktCombo.setSelectedIndex(0);
		else if(v.killType == C.KT_EXPLODE)
			ktCombo.setSelectedIndex(1);
		
		ktCombo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				int s = ktCombo.getSelectedIndex();
				if (s == 0)
					v.killType = C.KT_NONE;
				else if (s == 1)
					v.killType = C.KT_EXPLODE;

			}
		});

		frictionText = new JTextField(v.friction + "");
		frictionText.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{

			}
		});

		okButton = new JButton("ok");
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				C.out("BUTTON PRESSED!");
				String s = frictionText.getText();
				if ((s != null) && (s.length() > 0))
				{
					try
					{
						Float f = Float.parseFloat(s);
						v.friction = f;
					} catch (NumberFormatException ef)
					{
					} finally
					{
					}
				}
				dispose();
			}
		});

		panel.add(new JLabel("World: "));
		panel.add(worldCombo);
		panel.add(new JLabel("Dummy: "));
		panel.add(dummyCombo);
		panel.add(new JLabel("Kill Type: "));
		panel.add(ktCombo);
		panel.add(new JLabel("Friction: "));
		panel.add(frictionText);
		panel.add(new JLabel(""));
		panel.add(okButton);
		this.add(panel);

	}
}
