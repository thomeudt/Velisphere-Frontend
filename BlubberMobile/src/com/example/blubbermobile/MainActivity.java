package com.example.blubbermobile;


import java.io.UnsupportedEncodingException;

import com.example.blubbermobile.MessageConsumer.OnReceiveMessageHandler;

import android.os.Bundle;
import android.R.string;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity implements OnClickListener {

	private Button buttonSend;
	private static String history;
	private MessageConsumer mConsumer;
    private EditText mOutput;

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		buttonSend = (Button)findViewById(R.id.buttonSend);
		buttonSend.setOnClickListener(this);
	    
		//The output TextView we'll use to display messages
        mOutput =  (EditText) findViewById(R.id.txtMessageHistory);
 
        //Create the consumer
        mConsumer = new MessageConsumer(ServerParameters.bunny_ip,ServerParameters.my_queue_name);
 
        //Connect to broker
        mConsumer.connectToRabbitMQ();
 
        //register for messages
        
        mConsumer.setOnReceiveMessageHandler(new OnReceiveMessageHandler()
        {
 
            public void onReceiveMessage(String message) {
                 
                mOutput.setText("E"+message);
            }
        });
 
		
		// Altes verfahren mit Recv
		// Thread t = new Thread(new Recv(), "listener");
		// t.start();

		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		try {
			EditText sendMessageField = (EditText) findViewById(R.id.txtSendMessage);
			String sendMessage = sendMessageField.getText().toString();
			
			Send.main(sendMessage, "ute");
			//updateHistory("EEEEEEEEEEE");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void updateHistory(String toconcat)
	{	
			
			EditText historyField = (EditText)findViewById(R.id.txtMessageHistory);
			history = historyField.getText().toString();
				
			history = history.concat(toconcat);
			history = history.concat("\r\n");
			historyField.setText(history);
			// JScrollBar vertical = scpHistory.getVerticalScrollBar();
			// vertical.setValue( vertical.getMaximum());
			// txtHistory.setCaretPosition(txtHistory.getDocument().getLength());

	}
	
}
