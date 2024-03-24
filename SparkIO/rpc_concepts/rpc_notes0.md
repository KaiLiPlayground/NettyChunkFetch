#### [Why use threads?](https://timilearning.com/posts/mit-6.824/lecture-2-rpc-and-threads/#why-use-threads%3F)

- Threads enable *concurrency*, which is important in distributed systems. Concurrency allows us to schedule multiple tasks on a single processor. These tasks are run in an interleaved manner and essentially share CPU time between themselves. For example: with I/O concurrency, instead of waiting for an I/O operation to complete before continuing execution (thereby rendering the CPU idle), threads allow us to perform other tasks while we wait.
- *Parallelism*: We can perform multiple tasks in parallel on several cores. Unlike with just concurrency where only one task is making progress at a time (depending on which has its share of CPU time at that instant), parallelism allows multiple tasks to make process at the same time since they are executing on different CPU cores.
- *Convenience*: Threads provide a convenient way to execute short-lived tasks in the background e.g. a master node continuously polling a worker to check if it's alive.

Here's a breakdown of Remote Procedure Calls (RPC) and how they work:

**What is a Remote Procedure Call (RPC)?**

- **Distributed Computing:** RPC is a fundamental concept in distributed computing, where multiple computers work together on a task. It lets a program on one computer execute a procedure (a block of code, like a function) that lives on a different computer in a network.
- **Feels Like Local Execution:** The magic of RPC is that it makes this remote execution feel transparent to the programmer. They write their code as if the procedure were running locally, but RPC handles all the networking and communication details under the hood.

**How RPC Works (Simplified)**

1. **Client-side:**
   
   - **Call Stub:** The client program calls a local "stub" procedure, which acts as a stand-in for the real, remote procedure.
   - **Marshalling:** The stub gathers the procedure's name and parameters and packages them up ("marshals" them) into a message format.
   - **Transmission:** The stub hands this message to the underlying transport layer (e.g., TCP/IP) for sending over the network.

2. **Server-side:**
   
   - **Receive Message:** The transport layer on the server receives the message.
   - **Unmarshalling:** A server-side stub unpacks ("unmarshals") the message, extracting the desired procedure's name and parameters.
   - **Execute Procedure:** The server-side stub calls the actual remote procedure, passing in the parameters.

3. **Return Trip:**
   
   - **Repeat Steps:** The procedure does its work, and the results are 'marshaled' into a new message and sent back through the network the same way, in reverse.
   - **Client Unblocks:** The client's stub receives the results, 'unmarshals' them, and returns them to the client program, as if the remote procedure was called locally.

**Why Use RPC?**

- **Abstraction:** Makes distributed computing much simpler for developers by hiding complex network interactions.
- **Modularity:** Breaks down applications into smaller services that can live on different machines, promoting scalability and flexibility.
- **Resource Sharing:** Allows access to procedures or resources that might not be available on the local machine.

**Examples of RPC**

- **Traditional RPC Systems:** SUN RPC (which later became Open Network Computing RPC or ONC RPC), CORBA, DCOM
- **Modern RPC:** gRPC, REST (representational state transfer) – while REST isn't strictly RPC, it's often used for similar purposes.

**Let me know if you want a deeper dive into any part of this!**

Sure, here's a textual graph representing the RPC procedure:

```
           +---------+      +---------+      +---------+
           | Client  | ---- |  Stub   | ---- | Network |
           +---------+      +---------+      +---------+
                     |                       | (Transport Layer)
                     V                       V
           +---------+      +---------+      +---------+
           |  Stub   | ---- | Procedure|      | Server  |
           +---------+      +---------+      +---------+
                     | (Marshalling)          |
                     V                       | (Execution)
                     +---------+              V
                     | Result  |              +---------+
                     +---------+              | Server  |
                             |                  +---------+
                             V                  | (Unmarshalling)
                             +---------+--------->+---------+
                             | Client  |          | Client  |
                             +---------+          +---------+
                                     | (Return result)
```

**Explanation:**

- The graph starts with the **Client** program calling the local **Stub**.
- The **Stub** marshals the procedure call and arguments into a message.
- The message travels over the **Network** (using the transport layer).
- On the server side, the message is received and unmarshalled by the server-side **Stub**.
- The **Stub** calls the actual **Procedure**.
- The **Procedure** executes and generates a result.
- The result is marshalled into a message on the server.
- The message travels back over the **Network**.
- The client-side **Stub** receives the message, unmarshals it, and extracts the **Result**.
- Finally, the **Result** is returned to the **Client** program.

Certainly, including the transport layer explicitly in the RPC process graph will give a clearer picture of how data moves between the client and server. Here's the revised textual graph with the transport layer highlighted:

```
[Client Machine]                  [Network]                 [Server Machine]
     |                                       |                             |
1. Client invokes client stub --------------->                             
     |                                       |                             |
2. Client stub marshalls parameters                                       
     |                                       |                             |
3. Marshalled message passed to transport layer                     
     |                                       |                             |
     ----------------------------------------->
     |                                       |                             |
     |                Transport layer sends message to server             |
     |                                       |                             |
     <-----------------------------------------
     |                                       |                             |
4. Server transport layer receives message                                
     |                                       |                             |
5. Message passed to server stub                                           
     |                                       |                             |
6. Server stub demarshalls message                                         
     |                                       |                             |
7. Server executes routine and marshalls return values                     
     |                                       |                             |
8. Marshalled return value passed to server transport layer                
     |                                       |                             |
     ----------------------------------------->
     |                                       |                             |
     |               Transport layer sends response to client              |
     |                                       |                             |
     <-----------------------------------------
     |                                       |                             |
9. Client transport layer receives response                              
     |                                       |                             |
10. Client stub demarshalls return values                                 
     |                                       |                             |
11. Execution returns to the caller                                       
     |                                       |                             |
```

### Detailed Steps Including the Transport Layer:

1-2. **Invocation & Marshalling:** The client invokes the client stub, which marshalls the parameters into a standard format suitable for network transmission.

3. **To Transport Layer:** The marshalled data is handed to the client's transport layer, emphasizing the role of this layer in managing network communications.

4-5. **Server Receipt & Demarshalling:** The server's transport layer receives the message and passes it to the server stub, which then demarshalls the data to retrieve the original parameters.

6-7. **Execution & Response Preparation:** The server processes the request, and the return values are marshalled by the server stub.

8. **Back to Transport Layer:** The marshalled response is given to the server's transport layer, ready to be sent back to the client.

9-10. **Client Reception & Processing:** The client's transport layer receives the response, and the client stub demarshalls the return values.

11. **Completion:** The process completes with the execution returning to the original caller on the client side.

This graph more explicitly showcases the transport layer's pivotal role in RPC communication, managing the sending and receiving of messages between the client and server.
