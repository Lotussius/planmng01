package cn.edu.zucc.personplan.model;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class BeanStep {
	public static final String[] tblStepTitle={"���","����","�ƻ���ʼʱ��","�ƻ����ʱ��","ʵ�ʿ�ʼʱ��","ʵ�����ʱ��"};
	/**
	 * �����и���javabean������޸ı��������룬col��ʾ�������е�����ţ�0��ʼ
	 */
	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public int step_id,plan_id,step_order;
	public String step_name;
	public java.sql.Timestamp plan_start_time,plan_finish_time,real_start_time,real_finish_time;

	public BeanStep(int step_id, int plan_id, int step_order, String step_name, Timestamp plan_start_time,Timestamp plan_finish_time,Timestamp real_start_time,Timestamp real_finish_time)
	{
		this.step_id=step_id;
		this.plan_id=plan_id;
		this.step_order=step_order;
		this.step_name=step_name;
		this.plan_start_time=plan_start_time;
		this.plan_finish_time=plan_finish_time;
		this.real_start_time=real_start_time;
		this.real_finish_time=real_finish_time;
	}

	public String getCell(int col){
		if(col==0) return String.valueOf(step_id);
		else if(col==1) return step_name;
		else if(col==2) return sdf.format(plan_start_time);
		else if(col==3) return sdf.format(plan_finish_time);
		else if(col==4) return real_start_time==null?"":sdf.format(real_start_time);
		else if(col==5) return real_finish_time==null?"":sdf.format(real_finish_time);
		
		else return "";
	}
}
