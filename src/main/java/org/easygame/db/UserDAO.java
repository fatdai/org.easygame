package org.easygame.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.easygame.vo.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserDAO {

	private static Logger logger = LoggerFactory.getLogger(UserDAO.class);

	private static UserDAO userDAO = new UserDAO();

	public static UserDAO getInstance() {
		return userDAO;
	}

	public boolean isUserExist(String username, String password) {
		String sql = "select * from userinfo where username='" + username + "' and password = '" + password + "'";
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
			logger.error("isUserExist error : {}", e.getMessage());
			e.printStackTrace();
		}
		return false;
	}

	public User getUser(String username, String password) {
		String sql = "select * from userinfo where username='" + username + "' and password = '" + password + "'";
		try {
			Connection conn = DBManager.getConn();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			User user = null;
			while (rs.next()) {
				user = new User();
				user.setId(rs.getInt("id"));
				user.setUsername(rs.getString("username"));
				user.setPassword(rs.getString("password"));
				user.setLevel(rs.getInt("level"));
				user.setAttack(rs.getFloat("attack"));
				user.setDefence(rs.getFloat("defence"));
				user.setX(rs.getInt("x"));
				user.setY(rs.getInt("y"));
				break;
			}
			stmt.close();
			DBManager.closeConn(conn);
			return user;
		} catch (SQLException e) {
			logger.error("isUserExist error : {}", e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	public void insertUser(User user){
		StringBuffer sb = new StringBuffer();
		sb.append("insert into userinfo (username,password,level,attack,defence,x,y) values ('");
		sb.append(user.getUsername()).append("','").append(user.getPassword()).append("',");
		sb.append(user.getLevel()).append(",").append(user.getAttack()).append(",").append(user.getDefence());
		sb.append(",").append(user.getX()).append(",").append(user.getY());
		sb.append(")");
		
		Connection conn = DBManager.getConn();
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(sb.toString());
			stmt.close();
			DBManager.closeConn(conn);
		} catch (SQLException e) {
			logger.error("insert user error : {}",user);
			e.printStackTrace();
		}
	}

}
