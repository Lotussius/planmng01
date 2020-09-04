package cn.edu.zucc.personplan.comtrol.example;

import cn.edu.zucc.personplan.itf.IUserManager;
import cn.edu.zucc.personplan.model.BeanUser;
import cn.edu.zucc.personplan.util.BaseException;
import cn.edu.zucc.personplan.util.DBUtil;
import cn.edu.zucc.personplan.util.DbException;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

public class ExampleUserManager  implements IUserManager {

	@Override
	public BeanUser reg(String userid, String pwd,String pwd2) throws BaseException {
		// TODO Auto-generated method stub

		Connection conn=null;

		if(userid.isEmpty())
			throw new BaseException("empty userid");
		if(!(pwd.equals(pwd2)))
			throw new BaseException("password inconsistency");

		try
		{
			conn= DBUtil.getConnection();
			conn.setAutoCommit(false);
			String sql="select user_id from tbl_user where user_id = ?";
			java.sql.PreparedStatement pst=conn.prepareStatement(sql);
			pst.setString(1,userid);
			java.sql.ResultSet rs=pst.executeQuery();
			if(rs.next())
				throw new BaseException("userid exsisted");

			sql="insert into tbl_user(user_id,user_pwd,register_time) values(?,?,?)";
			pst=conn.prepareStatement(sql);
			pst.setString(1,userid);
			pst.setString(2,pwd);
			pst.setTimestamp(3, new Timestamp(System.currentTimeMillis()));

			pst.execute();
			pst.close();
			conn.commit();
			System.out.println("userid: "+userid+" regist success");
		}catch(SQLException ex)
		{
			ex.printStackTrace();
			throw new DbException(ex);
		}
		finally
		{
			if(conn!=null)
				try {
					conn.rollback();
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return null;
	}

	
	@Override
	public BeanUser login(String userid, String pwd) throws BaseException {
		// TODO Auto-generated method stub
		Connection conn=null;
	 	BeanUser u=new BeanUser(userid);


		try
		{
			conn= DBUtil.getConnection();
			String sql="select user_id,user_pwd from tbl_user where user_id = ?";
			java.sql.PreparedStatement pst=conn.prepareStatement(sql);
			pst.setString(1,userid);

			java.sql.ResultSet rs=pst.executeQuery();
			if(!rs.next())
				throw new BaseException("non-existent id");

			if(rs.getString(2).equals(pwd))
				System.out.println("log in successfully");
			else
			{
				rs.beforeFirst();
				throw new BaseException("wrong password");
			}


			rs.close();
			pst.close();
		}catch(SQLException ex)
		{
			ex.printStackTrace();
			throw new DbException(ex);
		}
		finally
		{
			if(conn!=null)
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return null;

	}


	@Override
	public void changePwd(BeanUser user, String oldPwd, String newPwd,
			String newPwd2) throws BaseException {
		// TODO Auto-generated method stub
		
	}

}
