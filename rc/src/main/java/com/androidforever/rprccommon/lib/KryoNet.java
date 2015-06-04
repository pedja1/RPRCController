package com.androidforever.rprccommon.lib;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.EndPoint;
import com.esotericsoftware.kryonet.Server;

public class KryoNet
{
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
		kryo.register(KryoCommand.class);
		//kryo.register(SomeResponse.class);
	}
}
