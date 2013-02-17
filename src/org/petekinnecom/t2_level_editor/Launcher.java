package org.petekinnecom.t2_level_editor;

import java.sql.SQLException;

import javax.swing.JOptionPane;

public class Launcher
{
	

	public static void main(String args[])
	{
/*
		String s = (String)JOptionPane.showInputDialog(
		                    null,
		                    "Input the level_id of the level to load. ",
		                    "Level Selection",
		                    JOptionPane.PLAIN_MESSAGE,
		                    null,
		                    null,
		                    "0");

		//If a string was returned, say so.
		if ((s != null) && (s.length() > 0)) {
			new EditController(Integer.parseInt(s));
		    */
		new EditController(0);
		    return;
		}

		
		
	}


