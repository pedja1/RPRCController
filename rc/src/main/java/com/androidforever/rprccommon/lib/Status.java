package com.androidforever.rprccommon.lib;

public class Status extends KryoCommand
{
	public boolean power;

	public Status(boolean request,  boolean power)
	{
		super(request);
		this.power = power;
	}
	
	public Status()
	{}
	
}
