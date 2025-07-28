# java_http_server

<h2>Purpose</h2>
<p>The purpose of this project is to get a deeper understanding of the underlying systems of a server. The server will be built from scratch on a foundation of the TCP/IP protocol.</p>
<p>The project is a hobby project I am working on, so I am unsure how extensive the capabilities will be, but the following bulleted list are what I intent to, or have achieved. I will update it as I progress, so the goals are more of a rolling list of features than a set in stone list.</p>

<h2>Goals</h2>
<ul>
    <b>Complete</b>
    <li>✔️Implement TCP/IP communication over sockets.</li>
    <li>✔️Implement socket threads to handle client communication.</li>
    <li>
        ✔️Respond to client with a generic html file.
    </li>
    <b>Incomplete</b>
    <li>
        ⭕Utilize 1 socket per client to send all resources requested (stylesheets, js, icons, etc.) Currently spins up a new thread per request.
    </li>
    <li>
        ⭕Digest HTTP request and build response from request.
    </li>
    <li>
        ⭕Set up functions for building paths rather than hardcoding them in.
    </li>
</ul>

<h2>How it works</h2>

<p>The following section will go over the basics of this server and how it functions to send and receive data from a browser. I will try to remember to update this section as it evolves, but if any of this information appears to be out of date, please raise an issue and I will address it.</p>

<h3>TCP/IP Communication - ServerMain.java</h3>
Starting from the bottom, the basis of communication is the TCP/IP protocol. The server opens a socket on a specified <code>PORT</code> and prints that it has been initialized if successful.

<code>try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port " + PORT);</code>

From there, it enters a <code>while(true)</code> loop to listen for connections while the server is active. It initializes a client socket connection <code>Socket clientSocket = null</code> to prepare to receive a connection.

Once a connection is made, the server socket attempts to accept the connection <code>clientSocket = serverSocket.accept()</code>

Then, a buffered reader is made to read the input stream from the client socket, and a output stream is made to handle output to the client socket. 

Buffered Reader
<code>BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));</code>
<br>
Output Stream
<code>OutputStream out = clientSocket.getOutputStream();</code>

Finally, a thread is spun up to handle the client interactions. The clientSocket, BufferedReader, and the OutputStream are passed as parameters, and the main socket then returns to listen for the next connection.