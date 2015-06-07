package com.androidforever.rprccommon.lib;

public class KryoCommand
{
	/**
	if this command is request (meaning we want some response)
		or just instruction to server*/
	public boolean request;

	public KryoCommand(boolean request)
	{
		this.request = request;
	}
	
	public KryoCommand()
	{}
}
