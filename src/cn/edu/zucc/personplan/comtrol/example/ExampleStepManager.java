package cn.edu.zucc.personplan.comtrol.example;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cn.edu.zucc.personplan.itf.IStepManager;
import cn.edu.zucc.personplan.model.BeanPlan;
import cn.edu.zucc.personplan.model.BeanStep;
import cn.edu.zucc.personplan.model.BeanUser;
import cn.edu.zucc.personplan.util.BaseException;
import cn.edu.zucc.personplan.util.DBUtil;
import cn.edu.zucc.personplan.util.DbException;

public class ExampleStepManager implements IStepManager {

	@Override
	public void add(BeanPlan plan, String name, String planstartdate, String planfinishdate) throws BaseException {
		Connection conn=null;
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //时间输入的格式必须为"yyyy-MM-dd HH:mm:ss" 如"2020-9-5 10:10:10"

		try
		{
			conn= DBUtil.getConnection();

			String sql="select MAX(step_order) from tbl_step where plan_id=?";
			java.sql.PreparedStatement pst=conn.prepareStatement(sql);
			pst.setInt(1, plan.pid);
			java.sql.ResultSet rs=pst.executeQuery();

			sql="insert into tbl_step(plan_id,step_order,step_name,plan_begin_time,plan_end_time) value(?,?,?,?,?)";
			pst=conn.prepareStatement(sql);
			pst.setInt(1,plan.pid);
			pst.setInt(2,rs.next()?rs.getInt(1)+1:1);//已有step不存在时，初始plan_order为1
			pst.setString(3,name);
			pst.setTimestamp(4,new Timestamp(sdf.parse(planstartdate).getTime()));
			pst.setTimestamp(5,new Timestamp(sdf.parse(planfinishdate).getTime()));
			pst.execute();

			sql="update tbl_plan set step_count=step_count+1 where plan_id=?";
			pst=conn.prepareStatement(sql);
			pst.setInt(1,plan.pid);
			pst.execute();


			rs.close();
			pst.close();
			conn.close();
			System.out.println("step adding success");

		}catch(SQLException ex)
		{
			ex.printStackTrace();
			throw new DbException(ex);
		}
		catch(ParseException ex)
		{
			ex.printStackTrace();
			throw new BaseException("please enter with standard time format: \"yyyy-MM-dd HH:mm:ss\"");
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
		return;
	}

	@Override
	public List<BeanStep> loadSteps(BeanPlan plan) throws BaseException {
		List<BeanStep> result=new ArrayList<BeanStep>();
		BeanStep s=null;
		Connection conn=null;
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		try
		{
			conn= DBUtil.getConnection();

			String sql="select * from tbl_step where plan_id=?";
			java.sql.PreparedStatement pst=conn.prepareStatement(sql);
			pst.setInt(1, plan.pid);
			java.sql.ResultSet rs=pst.executeQuery();

			while(rs.next())
			{
				s=new BeanStep(rs.getInt(1),rs.getInt(2),rs.getInt(3),rs.getString(4),rs.getTimestamp(5),rs.getTimestamp(6),rs.getTimestamp(7),rs.getTimestamp(8));
				result.add(s);
			}



			rs.close();
			pst.close();
			conn.close();
			System.out.println("step loading success");

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
	public void deleteStep(BeanStep step) throws BaseException {
		// TODO Auto-generated method stub
		Connection conn=null;
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		try
		{
			conn= DBUtil.getConnection();

			String sql="delete from tbl_step where step_id=?";
			java.sql.PreparedStatement pst1=conn.prepareStatement(sql);
			pst1.setInt(1, step.step_id);

			sql="update tbl_plan set step_count=step_count-1 where plan_id=(select plan_id from tbl_step where step_id=?)";
			java.sql.PreparedStatement pst2=conn.prepareStatement(sql);
			pst2.setInt(1,step.step_id);

			pst2.execute();
			pst1.execute();


			pst1.close();
			pst2.close();
			conn.close();
			System.out.println("step deleting success");

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

	@Override
	public void startStep(BeanStep step) throws BaseException {
		// TODO Auto-generated method stub
		Connection conn=null;

		try
		{
			conn= DBUtil.getConnection();

			String sql="update tbl_step set real_begin_time=? where step_id=?";
			java.sql.PreparedStatement pst=conn.prepareStatement(sql);
			pst.setTimestamp(1,new Timestamp(System.currentTimeMillis()));
			pst.setInt(2,step.step_id);
			pst.execute();

			sql="update tbl_plan set start_step_count=start_step_count+1 where plan_id=(select plan_id from tbl_step  where step_id=?)";
			pst=conn.prepareStatement(sql);
			pst.setInt(1,step.step_id);
			pst.execute();

			pst.close();
			conn.close();
			System.out.println("step starts");

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
		return;
	}

	@Override
	public void finishStep(BeanStep step) throws BaseException {
		// TODO Auto-generated method stub
		Connection conn=null;

		try
		{
			conn= DBUtil.getConnection();

			String sql="update tbl_step set real_end_time=? where step_id=?";
			java.sql.PreparedStatement pst=conn.prepareStatement(sql);
			pst.setTimestamp(1,new Timestamp(System.currentTimeMillis()));
			pst.setInt(2,step.step_id);
			pst.execute();

			sql="update tbl_plan set start_step_count=start_step_count-1,finished_step_count=finished_step_count+1 where plan_id=(select  plan_id from tbl_step  where step_id=?)";
			pst=conn.prepareStatement(sql);
			pst.setInt(1,step.step_id);
			pst.execute();

			pst.close();
			conn.close();
			System.out.println("step starts");

		}catch(SQLException ex)
		{
			ex.printStackTrace();
			throw new DbException(ex);
		}
	}

	@Override
	public void moveUp(BeanStep step) throws BaseException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveDown(BeanStep step) throws BaseException {
		// TODO Auto-generated method stub
		
	}

}
