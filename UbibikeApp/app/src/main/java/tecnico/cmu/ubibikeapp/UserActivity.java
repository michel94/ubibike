package tecnico.cmu.ubibikeapp;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import tecnico.cmu.ubibikeapp.model.Message;
import tecnico.cmu.ubibikeapp.network.WDService;

public class UserActivity extends Activity {

    private static final String TAG = "ChatActivity";

    private MessageAdapter chatArrayAdapter;
    private ListView listView;
    private EditText chatText;
    private ImageButton buttonSend;
    private boolean side = false;
    private WDService wdservice;
    private String userID, username;
    private boolean onlineStatus;

    @Override
    public void onResume(){
        super.onResume();

        /*if(**.isUserAvailable(userID)){
            TextView statusIndic = (TextView) findViewById(R.id.statusIndic);
            statusIndic.setText("Online"); // also, subscribe to changes
        }*/

    }

    public void onStart(){
        super.onStart();
        Intent intent = new Intent(getApplicationContext(), WDService.class);
        bindService(intent, serviceConn, Context.BIND_AUTO_CREATE);
    }

    protected void onStop(){
        super.onStop();
        unbindService(serviceConn);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extra = getIntent().getExtras();
        username = extra.getString("username");
        userID = extra.getString("userID");
        Log.d(TAG, "username: " + username + ", userID: " + userID);

        setContentView(R.layout.activity_user);

        buttonSend = (ImageButton) findViewById(R.id.send_button);
        listView = (ListView) findViewById(R.id.messages_view);


        chatArrayAdapter = new MessageAdapter(getApplicationContext(), R.layout.activity_right);
        listView.setAdapter(chatArrayAdapter);

        chatText = (EditText) findViewById(R.id.message);
        chatText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    return sendChatMessage();
                }
                return false;
            }
        });
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendChatMessage();
            }
        });

        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setAdapter(chatArrayAdapter);

        //to scroll the list view to bottom on data change
        chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(chatArrayAdapter.getCount() - 1);
            }
        });
    }

    private boolean sendChatMessage() {
        chatArrayAdapter.add(new Message(side, chatText.getText().toString()));
        chatText.setText("");
        side = !side;
        return true;
    }

    private ServiceConnection serviceConn = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder serviceBinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            WDService.LocalBinder binder = (WDService.LocalBinder) serviceBinder;
            wdservice = binder.getService();
            wdservice.testMethod("On User Activity");

            onlineStatus = wdservice.isUserAvailable(userID); // also, subscribe to changes
            if(onlineStatus) {
                TextView statusIndic = (TextView) findViewById(R.id.statusIndic);
                statusIndic.setText("Online");
            }else{
                TextView statusIndic = (TextView) findViewById(R.id.statusIndic);
                statusIndic.setText("Offline");
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            wdservice = null;
        }
    };

 }
