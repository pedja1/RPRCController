package com.androidforever.rprccommon.lib;

public class Status extends KryoCommand
{
	public boolean power;

	public Status(int id, boolean request,  boolean power)
	{
		super(id, request);
		this.power = power;
	}
	
	public Status()
	{}
	
}
