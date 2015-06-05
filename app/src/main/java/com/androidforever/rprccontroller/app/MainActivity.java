package com.androidforever.rprccontroller.app;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;
import com.androidforever.rprccommon.lib.KryoNet;
import com.esotericsoftware.kryonet.Client;
import java.io.IOException;
import com.androidforever.rprccommon.lib.Status;


public class MainActivity extends Activity
{
	Client client;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		
		client = KryoNet.createClient();
		client.start();
		connect();
    }
	
	private void connect()
	{
		new AsyncTask<Void, Void, Boolean>()
		{
			String mes;
			@Override
			protected Boolean doInBackground(Void[] p1)
			{
				try
				{
					client.connect(5000, "127.0.0.1", KryoNet.TCP_PORT, KryoNet.UDP_PORT);
					client.sendTCP(new com.androidforever.rprccommon.lib.Status(0, false, true));
				}
				catch (IOException e)
				{
					e.printStackTrace();
					mes = e.getMessage();
					return false;
				}
				return true;
			}
			
			public void onPostExecute(Boolean val)
			{
				if(!val)Toast.makeText(MainActivity.this, mes, Toast.LENGTH_LONG).show();
			}
			
		}.execute();
	}
}
