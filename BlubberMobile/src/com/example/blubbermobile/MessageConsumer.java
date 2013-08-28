package com.example.blubbermobile;

import java.io.IOException;

import android.os.Handler;

import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

/**
 *Consumes messages from a RabbitMQ broker
 *
 */
public class MessageConsumer extends  IConnectToRabbitMQ{
 
    public MessageConsumer(String server, String queueName) {
        super(server, queueName);
    }
 
    //The Queue name for this consumer
    private String mQueue;
    private QueueingConsumer MySubscription;
 
    //last message to post back
    private String mLastMessage;
 
    // An interface to be implemented by an object that is interested in messages(listener)
    public interface OnReceiveMessageHandler{
        public void onReceiveMessage(String mLastMessage);
    };
 
    //A reference to the listener, we can only have one at a time(for now)
    private OnReceiveMessageHandler mOnReceiveMessageHandler;
 
    /**
     *
     * Set the callback for received messages
     * @param handler The callback
     */
    public void setOnReceiveMessageHandler(OnReceiveMessageHandler handler){
        mOnReceiveMessageHandler = handler;
    };
 
    private Handler mMessageHandler = new Handler();
    private Handler mConsumeHandler = new Handler();
 
    // Create runnable for posting back to main thread
    
    final Runnable mReturnMessage = new Runnable() {
        public void run() {
            mOnReceiveMessageHandler.onReceiveMessage(mLastMessage);
        }
    };
 
    final Runnable mConsumeRunner = new Runnable() {
        public void run() {
            Consume();
        }
    };
 
    /**
     * Create Exchange and then start consuming. A binding needs to be added before any messages will be delivered
     */
    @Override
    public boolean connectToRabbitMQ()
    {
    	
    	System.out.println("CONNECTING");
       if(super.connectToRabbitMQ())
       {
 
           try {
               
          	   mModel.queueDeclare(mQueueName, false, false, false, null);
          	   MySubscription = new QueueingConsumer(mModel);
               mModel.basicConsume(mQueueName, true, MySubscription);
               System.out.println("DECLARED");
                        
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
             
            Running = true;
            mConsumeHandler.post(mConsumeRunner);
 
           return true;
       }
       return false;
    }
 
    
    
    
    private void Consume()
    {
        Thread thread = new Thread()
        {
 
             @Override
                public void run() {
               
            	 while(Running){
                	 System.out.println("Waiting");
            		 QueueingConsumer.Delivery delivery;
					try {
						delivery = MySubscription.nextDelivery();
						
						String message = new String(delivery.getBody());
						mLastMessage = message;
						mMessageHandler.post(mReturnMessage);
						System.out.println("WAITER"+mLastMessage);
					} catch (ShutdownSignalException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ConsumerCancelledException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
					 
             	                         
                 }
             }
        };
        thread.start();
 
    }
 
    public void dispose(){
        Running = false;
    }
}