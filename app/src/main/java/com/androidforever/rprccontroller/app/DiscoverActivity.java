package com.androidforever.rprccontroller.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.androidforever.rprccommon.lib.KryoNet;
import com.esotericsoftware.kryonet.Client;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pedja on 6.6.15..
 */
public class DiscoverActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener
{
    public static final int MENU_ITEM_ID_ADD = 0;
    Client client;
    ListView mListView;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView tvEmptyView;

    private ATDiscover atDiscover;
    private ArrayAdapter<Host> mListAdapter;
    MenuItem menuItemAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover);

        mListView = (ListView)findViewById(R.id.list);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        tvEmptyView = (TextView)findViewById(R.id.tvEmptyView);

        mListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        mListView.setAdapter(mListAdapter);
        mListView.setOnItemClickListener(this);

        client = KryoNet.createClient();

        mListView.post(new Runnable()
        {
            @Override
            public void run()
            {
                onRefresh();
            }
        });
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if(atDiscover != null)atDiscover.cancel(true);
        if(client != null) try
        {
            client.dispose();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        menuItemAdd = menu.add(0, MENU_ITEM_ID_ADD, 0, R.string.add).setIcon(R.drawable.ic_action_add);
        menuItemAdd.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return super.onCreateOptionsMenu(menu);
    }

    public void showAddHostDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.add_host);
        final EditText editText = new EditText(this);
        editText.setHint(R.string.host);
        builder.setView(editText);
        builder.setPositiveButton(R.string.add, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Host host = new Host(editText.getText().toString());
                MainApp.get().getDaoSession().getHostDao().insertOrReplaceInTx(host);
                MainApp.get().getDaoSession().clear();
                mListAdapter.add(host);
                mListAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton(R.string.cancel, null);
        builder.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case MENU_ITEM_ID_ADD:
                showAddHostDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupView(List<Host> inetAddresses)
    {
        swipeRefreshLayout.setRefreshing(false);
        tvEmptyView.setVisibility(inetAddresses.isEmpty() ? View.VISIBLE : View.GONE);
        mListAdapter.clear();
        mListAdapter.addAll(inetAddresses);
        mListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh()
    {
        if(atDiscover != null)atDiscover.cancel(true);
        atDiscover = new ATDiscover();
        atDiscover.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        startActivity(new Intent(this, MainActivity.class).putExtra(MainActivity.INTENT_EXTRA_HOST, mListAdapter.getItem(position).getHost()));
    }

    private class ATDiscover extends AsyncTask<String, Integer, List<Host>>
    {
        @Override
        protected void onPreExecute()
        {
            swipeRefreshLayout.setRefreshing(true);
            tvEmptyView.setVisibility(View.GONE);
            mListAdapter.clear();
            mListAdapter.notifyDataSetChanged();
            menuItemAdd.setEnabled(false);
        }

        @Override
        protected List<Host> doInBackground(String... params)
        {
            List<Host> discoverHosts = Host.fromInetAddressList(client.discoverHosts(KryoNet.UDP_PORT, 5000));
            List<Host> userHosts = new ArrayList<>(MainApp.get().getDaoSession().getHostDao().loadAll());

            for (Host x : userHosts){
                if (!discoverHosts.contains(x))
                    discoverHosts.add(x);
            }
            return discoverHosts;
        }

        @Override
        protected void onPostExecute(List<Host> inetAddresses)
        {
            setupView(inetAddresses);
            menuItemAdd.setEnabled(true);
        }
    }
}
