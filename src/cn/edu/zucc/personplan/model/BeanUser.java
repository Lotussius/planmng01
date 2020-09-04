package cn.edu.zucc.personplan.model;

public class BeanUser {
	public static BeanUser currentLoginUser=null;
	public String userid=null;
	public BeanUser(String uid)
	{
		userid=uid;
	}
}
