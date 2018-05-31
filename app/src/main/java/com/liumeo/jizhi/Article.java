package com.liumeo.jizhi;

class Article
{
	public String brief;
    public int msgID;
    public String srcID;
    public String time;
	Article(int msgID, String srcID, String brief, String time)
	{
		this.brief=brief;
		this.msgID=msgID;
		this.srcID=srcID;
		this.time=time;
	}
}
