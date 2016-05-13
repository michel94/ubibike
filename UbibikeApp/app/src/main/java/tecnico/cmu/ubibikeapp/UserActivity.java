package tecnico.cmu.ubibikeapp;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import tecnico.cmu.ubibikeapp.model.Message;
import tecnico.cmu.ubibikeapp.network.DataHandler;
import tecnico.cmu.ubibikeapp.network.Peer;
import tecnico.cmu.ubibikeapp.network.RequestCallback;
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
    int availabe=0,ValueClinteside=0;

    private DataHandler dataHandler;

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
        dataHandler.unbind();
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

        // Fredy
         final TextView qtdpts = (TextView)findViewById(R.id.qtdpts);
         String s=String.valueOf(Utils.getUserStats().getScore());
         qtdpts.setText(s);
        //

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




        //fredy
        final ImageButton trofeu = (ImageButton) findViewById(R.id.tropID);

         trofeu.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText editText = (EditText) findViewById(R.id.editponts);
                final ListView listView =(ListView)findViewById(R.id.messages_view);
                final Button button = (Button)findViewById(R.id.ok);

                // TODO: Aqui

                editText.setVisibility(EditText.VISIBLE);
                listView.setVisibility(ListView.INVISIBLE);
                button.setVisibility(Button.VISIBLE);
                chatText.setVisibility(EditText.INVISIBLE);
                buttonSend.setVisibility(ImageButton.INVISIBLE);
            }
        });


        final Button sendclose = (Button) findViewById(R.id.ok);
        sendclose.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText getpontos = (EditText) findViewById(R.id.editponts);
                final ListView listView =(ListView)findViewById(R.id.messages_view);
                final Button button = (Button)findViewById(R.id.ok);

              if(getpontos.getText().length()!=0) {

                   availabe=Integer.parseInt(qtdpts.getText().toString());
                   ValueClinteside=Integer.parseInt(getpontos.getText().toString());

                            if((ValueClinteside<availabe)) {

                                //TODO Enviar pontos

                            //TODO: Enviar pontos
                                String remaining = String.valueOf(availabe-ValueClinteside);
                                qtdpts.setText(remaining);
                                getpontos.setText(" ");

                                getpontos.setVisibility(EditText.INVISIBLE);
                                listView.setVisibility(ListView.VISIBLE);
                                button.setVisibility(Button.INVISIBLE);
                                chatText.setVisibility(EditText.VISIBLE);
                                buttonSend.setVisibility(ImageButton.VISIBLE);

                            }else {

                                Toast toast = Toast.makeText(getApplicationContext(), "Pontos insuficientes !", Toast.LENGTH_SHORT);
                                toast.show();

                            }
                } else {
                    Toast toasts = Toast.makeText(getApplicationContext(), "Inserir pontos a enviar !", Toast.LENGTH_SHORT);
                    toasts.show();
                }

           }
        });
        // fredy
    }

    private boolean sendChatMessage() {

        wdservice.sendMessage(userID, chatText.getText().toString(), new RequestCallback() {
            @Override
            public void onFinish(boolean success) {
                Message m = new Message(true, chatText.getText().toString(), Utils.getUserID(), userID);
                chatArrayAdapter.add(m);
                chatText.setText("");
            }
        });
        return true;
    }

    private void updateStatus(){
        if(onlineStatus) {
            TextView statusIndic = (TextView) findViewById(R.id.statusIndic);
            statusIndic.setText("Online");
        }else{
            TextView statusIndic = (TextView) findViewById(R.id.statusIndic);
            statusIndic.setText("Offline");
        }
    }

    private ServiceConnection serviceConn = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder serviceBinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            WDService.LocalBinder binder = (WDService.LocalBinder) serviceBinder;
            wdservice = binder.getService();

            onlineStatus = wdservice.isUserAvailable(userID);
            updateStatus();

            Log.d(TAG, "Binding data handler");
            dataHandler = new DataHandler(wdservice){
                @Override
                public boolean onMessage(String text) {
                    Message m = new Message(false, text, userID, Utils.getUserID());
                    chatArrayAdapter.add(m);
                    wdservice.getLocalStorage().putMessage(m);

                    return true;
                }
                @Override
                public boolean onStatusChanged(boolean online, Peer peer){
                    onlineStatus = online;
                    updateStatus();

                    return true;
                }
            };
            dataHandler.bind(userID);

        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Log.d(TAG, "Unbinding dataHandler");
            dataHandler.unbind();
            wdservice = null;
        }
    };

 }
