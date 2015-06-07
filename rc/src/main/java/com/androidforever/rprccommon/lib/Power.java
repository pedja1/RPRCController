package com.androidforever.rprccommon.lib;

public class Power extends KryoCommand
{
	public int power;//0-100

	public Power(boolean request,  int power)
	{
		super(request);
		this.power = power;
	}

	public Power()
	{}
}
