package com.androidforever.rprccommon.lib;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.EndPoint;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;

public class KryoNet
{
	public static final int TCP_PORT = 6056;
	public static final int UDP_PORT = 6057;
	
	static
	{
		Log.set(Log.LEVEL_TRACE);
	}
	
	public static Server createServer()
	{
		Server server = new Server();
		registerClasses(server);
		return server;
	}

	public static Client createClient()
	{
		Client client = new Client();
		registerClasses(client);
		return client;
	}
	
	public static void registerClasses(EndPoint endPoint)
	{
		Kryo kryo = endPoint.getKryo();
		kryo.register(Power.class);
		kryo.register(Status.class);
		kryo.register(Position.class);
	}
}
