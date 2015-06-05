package com.androidforever.rprccommon.lib;

public class Power extends KryoCommand
{
	public int power;//0-100

	public Power(int id, boolean request,  int power)
	{
		super(id, request);
		this.power = power;
	}
}
