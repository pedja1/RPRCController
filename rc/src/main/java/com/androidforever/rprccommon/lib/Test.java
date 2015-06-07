package com.androidforever.rprccommon.lib;

import com.esotericsoftware.kryonet.Client;

import java.net.InetAddress;
import java.util.Arrays;
import java.util.List;

/**
 * Created by pedja on 6.6.15..
 */
public class Test
{
    public static void main(String[] args)
    {
        Client client = KryoNet.createClient();
        client.start();
        List<InetAddress> list = client.discoverHosts(KryoNet.UDP_PORT, 5000);
        System.out.println(Arrays.toString(list.toArray(new InetAddress[list.size()])));
    }
}
