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
	private Button buttonLogin;
	private MessageConsumer mConsumer;
    private EditText mOutput;

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		buttonSend = (Button)findViewById(R.id.buttonSend);
		buttonSend.setEnabled(false);
		buttonSend.setOnClickListener(this);
		
		buttonLogin = (Button)findViewById(R.id.buttonLogin);
		buttonLogin.setOnClickListener(this);
	    
		
		
		/* 
		 * 
		 * Depracted Out Login with Start, now changed to button click
		
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
                 
                mOutput.append(message);
                mOutput.append("\r\n");
                
            }
        });
 
        
		**/
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		
		switch(v.getId()){
        	case R.id.buttonSend:
        	
        	EditText sendMessageField = (EditText) findViewById(R.id.txtSendMessage);
        	String sendMessage = sendMessageField.getText().toString();
		
        	EditText sendToQueueField = (EditText) findViewById(R.id.txtReceiverQueue);
        	String sendToQueue = sendToQueueField.getText().toString();
		
        	try {
        		EditText myQueueField = (EditText) findViewById(R.id.txtLoginName);
                String myQueueName = myQueueField.getText().toString();
        		Send.main(sendMessage, sendToQueue, myQueueName);
        		mOutput.append(sendMessage);
                mOutput.append("\r\n");
        		sendMessageField.setText("");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        	break;
        	
        	case R.id.buttonLogin:
        		//The output TextView we'll use to display messages
                mOutput =  (EditText) findViewById(R.id.txtMessageHistory);
         
                //Create the consumer
                EditText myQueueField = (EditText) findViewById(R.id.txtLoginName);
                String myQueueName = myQueueField.getText().toString();
                mConsumer = new MessageConsumer(ServerParameters.bunny_ip, myQueueName);
         
                //Connect to broker
                mConsumer.connectToRabbitMQ();
         
                //register for messages
                
                mConsumer.setOnReceiveMessageHandler(new OnReceiveMessageHandler()
                {
         
                    public void onReceiveMessage(String message) {
                         
                        mOutput.append(message);
                        mOutput.append("\r\n");
                        
                    }
                });
                // Make send button clickable
                Button btnSend=(Button)findViewById(R.id.buttonSend);
                btnSend.setEnabled(true);
                
                
                
            	
            	
            break;
        	
		}
        
		
		
	}


	
}
