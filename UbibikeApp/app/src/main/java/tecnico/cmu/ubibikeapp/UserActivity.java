package tecnico.cmu.ubibikeapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class UserActivity extends AppCompatActivity //implements View.OnClickListener{
{
/*
    EditText editText=(EditText)findViewById(R.id.message);
    ImageButton imageButton=(ImageButton)findViewById(R.id.send_button);
       MessageAdapter messageAdapter; */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        /*imageButton.setOnClickListener(this);


        messageAdapter = new MessageAdapter(this, new ArrayList<Message>());
        final ListView messagesView = (ListView) findViewById(R.id.messages_view);
        messagesView.setAdapter(messageAdapter); */

    }
/*
    @Override
    public void onClick(View v) {
        postMessage();
    }


    private void postMessage()  {
        String text = editText.getText().toString();

        if(text.equals(""))
            return;

    }

    @Override
    public void onClick(View v) {

    } */
}
