package org.easygame.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.easygame.logic.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerDAO {
	private static Logger logger = LoggerFactory.getLogger(PlayerDAO.class);
	private static PlayerDAO playerDAO = new PlayerDAO();

	public static PlayerDAO getInstance() {
		return playerDAO;
	}

	public boolean isPlayerExist(String name, String pwd) {
		String sql = "select * from player where name ='" + name + "' and pwd = '" + pwd + "'";
		try {
			Connection conn = DBManager.getConn();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			boolean exist = false;
			while (rs.next()) {
				exist = true;
				break;
			}
			stmt.close();
			DBManager.closeConn(conn);
			return exist;
		} catch (SQLException e) {
			logger.error("isPlayerExist error : {}", e.getMessage());
			e.printStackTrace();
		}
		return false;
	}

	public Player getPlayer(String name, String pwd) {
		String sql = "select * from player where name='" + name + "' and pwd = '" + pwd + "'";
		try {
			Connection conn = DBManager.getConn();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			Player player = null;
			while (rs.next()) {
				player = new Player(rs.getInt("id"));
				player.setGx(rs.getInt("gx"));
				player.setGy(rs.getInt("gy"));
				player.setName(rs.getString("name"));
				player.setPwd(rs.getString("pwd"));
				break;
			}
			stmt.close();
			DBManager.closeConn(conn);
			return player;
		} catch (SQLException e) {
			logger.error("getPlayer error : {}", e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

//	public void insertPlayer(Player player) {
//		StringBuffer sb = new StringBuffer();
//		sb.append("insert into player (name,gx,gy,pwd) values ('");
//		sb.append(player.getName()).append("',").append(player.getGx()).append(",");
//		sb.append(player.getGy()).append(",'").append(player.getPwd()).append("'");
//		sb.append(")");
//
//		Connection conn = DBManager.getConn();
//		try {
//			Statement stmt = conn.createStatement();
//			stmt.executeUpdate(sb.toString());
//			stmt.close();
//			DBManager.closeConn(conn);
//		} catch (SQLException e) {
//			logger.error("insertPlayer : {}", player);
//			e.printStackTrace();
//		}
//	}

	public Player insertPlayer(String name, String pwd,int gx,int gy) {
		
		StringBuffer sb = new StringBuffer();
		sb.append("insert into player (name,gx,gy,pwd) values ('");
		sb.append(name).append("',").append(gx).append(",");
		sb.append(gy).append(",'").append(pwd).append("'");
		sb.append(")");
		
		Connection conn = DBManager.getConn();
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(sb.toString());
			DBManager.closeConn(conn);
		} catch (SQLException e) {
			logger.error("insertPlayer : {}",name);
			e.printStackTrace();
		}
		
		return getPlayer(name, pwd);
	}

}
