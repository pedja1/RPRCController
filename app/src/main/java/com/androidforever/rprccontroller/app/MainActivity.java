package com.androidforever.rprccontroller.app;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.androidforever.rprccommon.lib.KryoCommand;
import com.androidforever.rprccommon.lib.KryoNet;
import com.androidforever.rprccommon.lib.Position;
import com.androidforever.rprccommon.lib.Power;
import com.androidforever.rprccommon.lib.Status;
import com.androidforever.rprccontroller.app.impl.SimpleOnSeekBarChangeListener;
import com.androidforever.rprccontroller.app.widget.JoystickView;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;


public class MainActivity extends AppCompatActivity
{
	public static final String INTENT_EXTRA_HOST = "host";

	Client client;
	String host;

	private ATConnect atConnect;
	ToggleButton tbPower;
	SeekBar sbPower;
	JoystickView joystick;
	boolean checkChangeEnabled = true;
	boolean seekChangeEnabled = true;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		host = getIntent().getStringExtra(INTENT_EXTRA_HOST);

		tbPower = (ToggleButton)findViewById(R.id.tbPower);
		sbPower = (SeekBar)findViewById(R.id.sbPower);
		joystick = (JoystickView)findViewById(R.id.joystick);
		tbPower.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(final CompoundButton buttonView, boolean isChecked)
			{
				if(!checkChangeEnabled)return;
				sbPower.setEnabled(isChecked);
				joystick.setEnabled(isChecked);
				buttonView.setEnabled(false);
				new ATSendCommand(new Status(false, isChecked), new SendCommandCallback()
				{
					@Override
					public void onCommandSent()
					{
						buttonView.setEnabled(true);
					}
				}).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			}
		});
		sbPower.setOnSeekBarChangeListener(new SimpleOnSeekBarChangeListener()
		{
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
			{
				if(!seekChangeEnabled)return;
				new ATSendCommand(new Power(false, progress), null).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			}
		});
		joystick.setOnJoystickMoveListener(new JoystickView.OnJoystickMoveListener()
		{
			@Override
			public void onValueChanged(int angle, int power, int direction)
			{
				System.out.println("Angle: '" + angle + "' Power: '" + power + "' Direction: '" + direction + "'");
				new ATSendCommand(new Position(false, angle, power), null).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			}
		});

		client = KryoNet.createClient();
		client.start();
		client.addListener(new Listener()
		{
			@Override
			public void connected(Connection connection)
			{

			}

			@Override
			public void disconnected(Connection connection)
			{
				runOnUiThread(new Runnable()
				{
					@Override
					public void run()
					{
						finish();
						Toast.makeText(MainActivity.this, R.string.disconnected, Toast.LENGTH_LONG).show();
					}
				});
			}

			@Override
			public void received(Connection connection, Object obj)
			{
				if (obj instanceof Status)
				{
					final Status status = (Status) obj;
					runOnUiThread(new Runnable()
					{
						@Override
						public void run()
						{
							checkChangeEnabled = false;
							tbPower.setChecked(status.power);
							checkChangeEnabled = true;
						}
					});
				}
				else if(obj instanceof Power)
				{
					final Power power = (Power) obj;
					runOnUiThread(new Runnable()
					{
						@Override
						public void run()
						{
							seekChangeEnabled = false;
							sbPower.setProgress(power.power);
							seekChangeEnabled = true;
						}
					});
				}

			}
		});

		atConnect = new ATConnect();
		atConnect.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		if(atConnect != null)atConnect.cancel(true);
	}

	public void setupViews()
	{
		sbPower.setEnabled(true);
		tbPower.setEnabled(true);
		joystick.setEnabled(true);
	}

	private class ATSendCommand extends AsyncTask<String, Integer, Integer>
	{
		KryoCommand command;
		SendCommandCallback callback;

		public ATSendCommand(KryoCommand command, SendCommandCallback callback)
		{
			this.command = command;
			this.callback = callback;
		}

		@Override
		protected Integer doInBackground(String[] params)
		{
			return client.sendUDP(command);
		}

		@Override
		protected void onPostExecute(Integer integer)
		{
			if(callback != null)callback.onCommandSent();
		}
	}

	private interface SendCommandCallback
	{
		void onCommandSent();
	}

	private class ATConnect extends AsyncTask<String, Integer, Boolean>
	{
		String mes;

		ProgressDialog pd;

		@Override
		protected void onPreExecute()
		{
			pd = new ProgressDialog(MainActivity.this);
			pd.setMessage(getString(R.string.please_wait));
			pd.setCancelable(false);
			pd.show();
		}

		@Override
		protected Boolean doInBackground(String[] params)
		{
			try
			{
				client.connect(5000, host, KryoNet.TCP_PORT, KryoNet.UDP_PORT);
				client.sendTCP(new Power(true, 0));
				client.sendTCP(new com.androidforever.rprccommon.lib.Status(true, false));
			}
			catch (IOException e)
			{
				e.printStackTrace();
				mes = e.getMessage();
				return false;
			}
			return true;
		}

		@Override
		public void onPostExecute(Boolean val)
		{
			pd.dismiss();
			if(!val)
			{
				finish();
				Toast.makeText(MainActivity.this, mes, Toast.LENGTH_LONG).show();
			}
			else
			{
				setupViews();
			}
		}
	}
}
