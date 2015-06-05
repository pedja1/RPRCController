package com.androidforever.rprccommon.lib;

public class Position extends KryoCommand
{
	public int angle, power, distance;

	public Position(int id, boolean request, int angle, int power, int distance)
	{
		super(id, request);
		this.angle = angle;
		this.power = power;
		this.distance = distance;
	}
}
