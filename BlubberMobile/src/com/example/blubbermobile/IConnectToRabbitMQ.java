package com.example.blubbermobile;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

/**
 * Base class for objects that connect to a RabbitMQ Broker
 */
public abstract class IConnectToRabbitMQ {
      public String mServer;
      public String mQueueName;
 
      protected Channel mModel = null;
      protected Connection  mConnection;
 
      protected boolean Running ;
 
 
 
      /**
       *
       * @param server The server address
       * @param exchange The named exchange
       * @param exchangeType The exchange type name
       */
      public IConnectToRabbitMQ(String server, String queueName)
      {
          mServer = server;
          mQueueName = queueName;
          
      }
 
      

	public void Dispose()
      {
          Running = false;
 
            try {
                if (mConnection!=null)
                    mConnection.close();
                if (mModel != null)
                    mModel.abort();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
 
      }
 
      public boolean connectToRabbitMQ()
      {
          if(mModel!= null && mModel.isOpen() )//already declared
              return true;
          try
          {
              ConnectionFactory connectionFactory = new ConnectionFactory();
              connectionFactory.setHost(mServer);
              mConnection = connectionFactory.newConnection();
              mModel = mConnection.createChannel();
              // mModel.queueDeclare(mQueueName, false, false, false, null);
              System.out.println(mQueueName);
              return true;
          }
          catch (Exception e)
          {
              e.printStackTrace();
              return false;
          }
      }
}