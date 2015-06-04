package com.androidforever.rprccommon.lib;

public class KN
{
	public static void registerClasses()
	{
		Kryo kryo = server.getKryo();
		kryo.register(SomeRequest.class);
		kryo.register(SomeResponse.class);
		Kryo kryo = client.getKryo();
		kryo.register(SomeRequest.class);
		kryo.register(SomeResponse.class);
	}
}
