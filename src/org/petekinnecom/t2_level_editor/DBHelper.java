package org.petekinnecom.t2_level_editor;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.petekinnecom.t2_level_pieces.GenericLevelPiece;
import org.petekinnecom.t2_level_pieces.GenericShifter;
import org.petekinnecom.t2_level_pieces.GenericTile;
import org.petekinnecom.t2_level_pieces.LevelPiece;
import org.petekinnecom.t2_level_pieces.Tile;
import org.petekinnecom.t2_level_pieces.Line;

/*
 * To use these sqlite methods, add the sqlite driver .jar to 
 * the build path of the project, using project->properties->add external jar
 */
public class DBHelper
{

	public static void writeLevel(EditLevel level) throws SQLException
	{
		C.out("Saving level (id: " + level.id + " )");
		try
		{
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Connection conn = DriverManager.getConnection("jdbc:sqlite:levels.db");
		Statement stat = conn.createStatement();

		ResultSet rs = stat
				.executeQuery("SELECT * FROM level_data WHERE level_id='"
						+ level.id + "';");
		if (rs.next())
		{
			C.out("Level is already saved in DB.  Grab it's id.", 1);
			stat.executeUpdate("DELETE FROM level_data WHERE level_id='"
					+ level.id + "';");
			C.out("Deleted from DB.  Now save level.", 1);
		}
		rs.close();

		for (LevelPiece p : level.getPieces())
		{
			stat.addBatch(p.getDBWriteString(level.id));
			C.out(p.getDBWriteString(level.id)+"\n");
			
		}
		C.out("Finished adding tile data.", 1);
		conn.setAutoCommit(false);
		stat.executeBatch();
		conn.setAutoCommit(true);
		
		
		stat.executeUpdate("UPDATE level_info  SET ballStartX="+level.ballStart.x+" WHERE level_id='"
				+ level.id + "';");

		stat.executeUpdate("UPDATE level_info  SET ballStartY="+level.ballStart.y+" WHERE level_id='"
				+ level.id + "';");

	
		C.out("Changes committed.  Closing connection.", 1);
		conn.close();
	}

	
	/** 
	 * For TILE EDITOR.  Okay that it's specific to tiles.
	 */
	public static void writeLines(GenericLevelPiece tile) throws SQLException
	{
		C.out("Saving lines for tile (id: " + tile.id + " )");
		try
		{
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Connection conn = DriverManager.getConnection("jdbc:sqlite:levels.db");
		Statement stat = conn.createStatement();

		ResultSet rs = stat
				.executeQuery("SELECT * FROM lines WHERE piece_id='"
						+ tile.id + "';");
		if (rs.next())
		{
			C.out("Tile already has lines in DB.  Deleting them.", 1);
			stat.executeUpdate("DELETE FROM lines WHERE piece_id='"
					+ tile.id + "';");
			C.out("Deleted from DB.  Now save lines.", 1);
			
			
		}
		rs.close();
		
		int x1, y1, x2, y2;
		int world;
		int dummy;
		int killType;
		float friction;
		for (Line v : tile.lines)
		{
			x1 = (int)  v.px;
			y1 = (int) v.py;
			x2 = (int) v.cx;
			y2 = (int) v.cy;
			world = v.world;
			if(v.dummy)
				dummy = 1;
			else
				dummy = 0;
			killType = v.killType;
			friction = v.friction;
			
			stat.addBatch("INSERT INTO lines " + "("
					+ "piece_id, x1, y1, x2, y2, world, dummy, killType, friction) VALUES "
					+ "(" + tile.id + ", " + x1 + ", " + y1 + ", " + x2
					+ ", " + y2  +", "+world+", "+dummy+", "+killType+", "+friction+");");

		}
		C.out("Finished adding line data.", 1);
		conn.setAutoCommit(false);
		stat.executeBatch();
		conn.setAutoCommit(true);
		C.out("Changes committed.  Closing connection.", 1);
		conn.close();
	}

	public static EditLevel readLevel(int level_id) throws SQLException
	{

		C.out("Reading level from db.");
		/*
		 * GENERIC INIT STUFF
		 */
		try
		{
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Connection conn = DriverManager.getConnection("jdbc:sqlite:levels.db");
		Statement stat = conn.createStatement();
		/*
		 * GENERIC INIT STUFF END
		 */
		EditLevel level = new EditLevel(level_id);
		C.out("SELECT * FROM level_data WHERE level_id='" + level_id + "';", 1);
		ResultSet rs = stat
				.executeQuery("SELECT * FROM level_data WHERE level_id='"
						+ level_id + "';");
		while (rs.next())
		{
			int x = rs.getInt("x");
			int y = rs.getInt("y");
			int tile_id = rs.getInt("piece_id");
			int rotation = rs.getInt("rotation");
			int reflect = rs.getInt("reflect");

			LevelPiece lp = C.getGenericPieceById(tile_id).toLevelPiece();
			lp.x = x;
			lp.y = y;
			lp.rotation = rotation;
			lp.reflect = reflect;
			lp.fixBox();
			level.addPiece(lp);

		}
		rs.close();
		C.out("Finished reading level layout.");
		C.out("Reading background/foreground data.");

		rs = stat.executeQuery("SELECT * FROM level_info WHERE level_id='"
				+ level_id + "';");
		level.width = rs.getInt("level_width");
		level.height = rs.getInt("level_height");
		level.backgroundPNG = getFile(rs.getString("backgroundPNG"));
		level.foregroundPNG = getFile(rs.getString("foregroundPNG"));
		level.bgDepth = rs.getInt("bgDepth");
		level.fgDepth = rs.getInt("fgDepth");
		level.ballStart = new Point(rs.getInt("ballStartX"), rs.getInt("ballStartY"));

		conn.close();

		C.out("Finished reading level");
		return level;

	}

	/*
	 * Used for loading the generic tile data into the static globals class.
	 */
	public static ArrayList<GenericTile> readGenericTiles() throws SQLException
	{
		C.out("Reading generic tiles from db.");
		/*
		 * GENERIC INIT STUFF
		 */
		try
		{
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Connection conn = DriverManager.getConnection("jdbc:sqlite:levels.db");
		Statement stat = conn.createStatement();
		/*
		 * GENERIC INIT STUFF END
		 */

		/*
		 * To enforce unique id's for all pieces, we must connect 
		 * a tile_id to its piece_id.  First, grab all pairs from 
		 * the pieces table.
		 */
		ArrayList<Point> pairs = new ArrayList<Point>();
		ResultSet r = stat.executeQuery("SELECT * FROM pieces WHERE type='tile';");
		while (r.next())
		{

			pairs.add(new Point(r.getInt("foreign_id"), r.getInt("_id")));

		}
		
		
		
		
		ArrayList<GenericTile> tiles = new ArrayList<GenericTile>();
		String png;
		int id;
		BufferedImage tilePNG;
		ResultSet rs = stat.executeQuery("SELECT * FROM tiles;");
		C.out("entering loop");
		while (rs.next())
		{
			C.out("Parsing tile record: " + rs.getInt("_id"));

			id = -1;
			/*
			 * we now have the foreign id.  Convert it
			 * to the unique id.
			 */
			for(Point p : pairs)
			{
				if(p.x == rs.getInt("_id"))
				{
					id = p.y;
				}
			}
			if(id==-1)
				C.out("ERROR in ReadGenericTiles: can't find unique id for tile_id: "+rs.getInt("_id"));
			png = rs.getString("png");
			

			tilePNG = getFile(png);
			
			GenericTile gtile = new GenericTile(id, tilePNG);

			tiles.add(gtile);
		}
		rs.close();
		conn.close();
		return tiles;
		

//
//		C.out("Finished reading tiles");
//
//		return tiles;
	}
	/*
	 * Used for loading the generic shifter data into the static globals class.
	 * Lotsa copy and paste here...oh well.
	 */
	public static ArrayList<GenericShifter> readGenericShifters() throws SQLException
	{
		C.out("Reading generic shifters from db.");
		/*
		 * GENERIC INIT STUFF
		 */
		try
		{
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Connection conn = DriverManager.getConnection("jdbc:sqlite:levels.db");
		Statement stat = conn.createStatement();
		/*
		 * GENERIC INIT STUFF END
		 */

		/*
		 * To enforce unique id's for all pieces, we must connect 
		 * a tile_id to its piece_id.  First, grab all pairs from 
		 * the pieces table.
		 */
		ArrayList<Point> pairs = new ArrayList<Point>();
		ResultSet r = stat.executeQuery("SELECT * FROM pieces WHERE type='shifter';");
		while (r.next())
		{

			pairs.add(new Point(r.getInt("foreign_id"), r.getInt("_id")));

		}
		
		
		ArrayList<GenericShifter> shifters = new ArrayList<GenericShifter>();
		String png_a, png_b;
		int id;
		BufferedImage shifterPNGA, shifterPNGB;
		ResultSet rs = stat.executeQuery("SELECT * FROM shifters;");
		C.out("entering loop");
		while (rs.next())
		{
			C.out("Parsing shifter record: " + rs.getInt("_id"));

			id = -1;
			/*
			 * we now have the foreign id.  Convert it
			 * to the unique id.
			 */
			for(Point p : pairs)
			{
				if(p.x == rs.getInt("_id"))
				{
					id = p.y;
				}
			}
			if(id==-1)
				C.out("ERROR in ReadGenericShifters: can't find unique id for piece_id: "+rs.getInt("_id"));
			png_a = rs.getString("png_a");
			png_b = rs.getString("png_b");

			shifterPNGA = getFile(png_a);
			shifterPNGB = getFile(png_b);
			
			GenericShifter gshift = new GenericShifter(id, shifterPNGA, shifterPNGB);

			shifters.add(gshift);
		}
		rs.close();
		
		conn.close();
		C.out("Finished reading generic shifters");
		return shifters;
		

	}

	public static ArrayList<Line> readLines(int piece_id) throws SQLException
	{
		
		/*
		 * GENERIC INIT STUFF
		 */
		try
		{
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Connection conn = DriverManager.getConnection("jdbc:sqlite:levels.db");
		Statement stat = conn.createStatement();
		ResultSet lineSet = stat.executeQuery("SELECT * FROM lines WHERE piece_id='"+piece_id+"';");
		
		ArrayList<Line> lines = new ArrayList<Line>();
		int x1, y1, x2, y2;
		
		int world;
		int dummy;
		int killType;
		float friction;
		
		while(lineSet.next())
		{
			
			x1 = lineSet.getInt("x1");
			y1 = lineSet.getInt("y1");
			x2 = lineSet.getInt("x2");
			y2 = lineSet.getInt("y2");
			world = lineSet.getInt("world");
			dummy = lineSet.getInt("dummy");
			killType = lineSet.getInt("killType");
			friction = lineSet.getFloat("friction");
			Line v = new Line(x1, y1, x2, y2, world, dummy, killType, friction);

			C.out("Read line for "+piece_id+" : \n "+v);
			lines.add(v);
		}
		lineSet.close();
		
		
		conn.close();
		return lines;
	}
	
	public static BufferedImage getFile(String s)
	{

		try
		{
			C.out("Reading file: " + s);
			return ImageIO.read(new File(s));

		} catch (IOException e)
		{
			C.out(e.getMessage());
		}
		return null;

	}
}
