package com.androidforever.rprccommon.lib;

public class KryoCommand
{
	/**
	Unique id of this command*/
	public int id;
	
	/**
	if this command is request (meaning we want some response)
		or just instruction to server*/
	public boolean request;

	public KryoCommand(int id, boolean request)
	{
		this.id = id;
		this.request = request;
	}
	
	public KryoCommand()
	{}
}
