package cn.edu.zucc.personplan.model;

public class BeanPlan {
	public static final String[] tableTitles={"���","����","������","�������"};
	/**
	 * �����и���javabean������޸ı��������룬col��ʾ�������е�����ţ�0��ʼ
	 */
	public BeanPlan(int pid,String uid,int order,String name,java.sql.Date time,int step,int start,int finish)
	{
		this.pid=pid;
		this.uid=uid;
		this.order=order;
		this.name=name;
		this.time=time;
		this.step=step;
		this.start=start;
		this.finish=finish;
	}
	public BeanPlan(int pid)
	{
		this.pid=pid;
	}

	public int pid,order,step,start,finish;
	public String uid,name;
	public java.sql.Date time;
	public String getCell(int col){
		if(col==0) return String.valueOf(order);
		else if(col==1) return name;
		else if(col==2) return String.valueOf(step);
		else if(col==3) return String.valueOf(finish);
		else return "";
	}

}
