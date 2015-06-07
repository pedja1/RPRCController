package com.androidforever.rprccommon.lib;

public class Position extends KryoCommand
{
	public int angle, power;

	public Position(boolean request, int angle, int power)
	{
		super(request);
		this.angle = angle;
		this.power = power;
	}

	public Position()
	{}
}
