package cn.edu.zucc.personplan.comtrol.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import cn.edu.zucc.personplan.itf.IPlanManager;
import cn.edu.zucc.personplan.model.BeanPlan;
import cn.edu.zucc.personplan.model.BeanUser;
import cn.edu.zucc.personplan.ui.FrmMain;
import cn.edu.zucc.personplan.util.BaseException;
import cn.edu.zucc.personplan.util.DBPool;
import cn.edu.zucc.personplan.util.DbException;

public class ExamplePlanManager implements IPlanManager {
	DBPool DBUtil=new DBPool(); //Á¬½Ó³ØDBPoolÌæ»»DBUtil
	@Override
	public BeanPlan addPlan(String name) throws BaseException {
		// TODO Auto-generated method stub
		Connection conn=null;
		BeanPlan p=null;

		try
		{
			conn= DBUtil.getConnection();

			String  sql="select MAX(plan_order) from tbl_plan where user_id=?";
			java.sql.PreparedStatement pst=conn.prepareStatement(sql);
			pst.setString(1,BeanUser.currentLoginUser.getId());
			java.sql.ResultSet rs=pst.executeQuery();
			rs.next();

			sql="insert into tbl_plan(user_id,plan_order,plan_name,create_time,step_count,start_step_count,finished_step_count) value(?,?,?,?,?,?,?)";
			pst=conn.prepareStatement(sql);
			pst.setString(1,BeanUser.currentLoginUser.getId());
			pst.setInt(2,rs.getInt(1)+1);
			pst.setString(3,name);
			pst.setTimestamp(4,new Timestamp(System.currentTimeMillis()));
			pst.setInt(5,0);
			pst.setInt(6,0);
			pst.setInt(7,0);

			pst.execute();

			rs.close();
			pst.close();
			conn.close();
			System.out.println("plan:"+name+" adding success");

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
	public List<BeanPlan> loadAll() throws BaseException {
		List<BeanPlan> result=new ArrayList<BeanPlan>();
		Connection conn=null;
		BeanPlan p=null;

		try
		{
			conn= DBUtil.getConnection();

			String sql="select * from tbl_plan where user_id=?";
			java.sql.PreparedStatement pst=conn.prepareStatement(sql);
			pst.setString(1,BeanUser.currentLoginUser.getId());
			java.sql.ResultSet rs=pst.executeQuery();

			while(rs.next())
			{
				p=new BeanPlan(rs.getInt(1),rs.getString(2),rs.getInt(3),rs.getString(4),rs.getDate(5),rs.getInt(6),rs.getInt(7),rs.getInt(8));
				result.add(p);
			}
			rs.close();
			pst.close();
			conn.close();
			System.out.println("plans loading success");

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

		return result;
	}

	@Override
	public void deletePlan(BeanPlan plan) throws BaseException {
		Connection conn=null;

		try
		{
			conn= DBUtil.getConnection();

			String sql="select * from tbl_step where plan_id=?";
			java.sql.PreparedStatement pst=conn.prepareStatement(sql);
			pst.setInt(1,plan.pid);
			java.sql.ResultSet rs=pst.executeQuery();

			if(rs.next())
				throw new BaseException("step existed in this plan, it can't be delete");

			sql="delete from tbl_plan where plan_id=?";
			pst=conn.prepareStatement(sql);
			pst.setInt(1,plan.pid);
			pst.execute();
			System.out.println("delete successfully");

			rs.close();
			pst.close();
			conn.close();

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
	}

}
