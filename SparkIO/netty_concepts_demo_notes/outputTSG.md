Given the logs you've shared, it seems the Netty setup is initializing correctly, as evidenced by the detailed debug logs from both the server and client sides. The absence of specific logs related to your application logic (such as messages being sent or received) suggests that the interaction between your client and server might not be occurring as expected. 

Here are a few steps to troubleshoot and ensure the communication is happening as intended:

1. **Imported the wrong libraries**
   
   1. `com.sun.org.slf4j.internal.LoggerFactory;` 'sun' has slf4j too. 

2. **Check Channel Handlers**: Ensure that your server's and client's channel handlers (e.g., `ChannelInitializer`) are properly set up to handle incoming connections and data. Verify that the pipeline configuration includes your custom handlers for processing messages.

3. **Verify Channel Active**: In your handlers, override the `channelActive` method to log when a connection is established. This can help verify that your client is indeed connecting to the server.
   
   ```java
   @Override
   public void channelActive(ChannelHandlerContext ctx) throws Exception {
       super.channelActive(ctx);
       System.out.println("Channel is active");
   }
   ```

4. **Implement Exception Handling**: Override the `exceptionCaught` method in your handlers to catch and log any exceptions that might occur during message handling.
   
   ```java
   @Override
   public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
       cause.printStackTrace();
       ctx.close();
   }
   ```

5. **Send a Test Message**: After establishing a connection, try sending a simple test message from the client to the server and vice versa to confirm that the data exchange mechanism is working. Ensure your message is correctly encoded/decoded if you're using custom encoders/decoders.

6. **Check Firewall Settings**: Ensure there are no firewall or network settings that might be blocking the connections between your client and server.

7. **Simplify the Test**: Temporarily simplify your test case to a minimal client-server interaction without the chunk fetch logic. Once you confirm that basic connectivity works, incrementally add your application logic.

8. **Logging**: Since you've enabled detailed Netty logging, you should see logs related to the actual message exchange. If you're not seeing these, it suggests the message send/receive code might not be executed. Double-check the logic that triggers these operations.

If you're still facing issues after following these steps, consider isolating the problem by creating a minimal reproducible example of your Netty client-server setup. This approach can help identify whether the issue lies in the Netty configuration, the application logic, or elsewhere.
