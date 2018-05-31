package com.liumeo.jizhi;

/**
 * 消息类
 */
class Message
{
    /**
     * 较简短的标题
     */
	public String brief;
    /**
     * 消息的id
     */
    public int msgID;
    /**
     * 消息所属消息源名称
     */
    public String srcID;
    /**
     * 消息时间
     */
    public String time;
    
	Message(int msgID, String srcID, String brief, String time)
	{
		this.brief=brief;
		this.msgID=msgID;
		this.srcID=srcID;
		this.time=time;
	}
}
