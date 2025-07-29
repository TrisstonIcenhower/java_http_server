# java_http_server

<h2>Purpose</h2>
<p>The purpose of this project is to get a deeper understanding of the underlying systems of a web server. The server will be built from scratch on a foundation of the TCP/IP protocol.</p>
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
        ⭕Utilize 1 thread per client to send all resources requested (stylesheets, js, icons, etc.) Currently spins up a new thread per request.
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

Finally, a thread <code>Thread clientThread = new ClientHandler(in, out, clientSocket);</code> is spun up to handle the client interactions. The clientSocket, BufferedReader, and the OutputStream are passed as parameters, and the main socket then returns to listen for the next connection.

<h3>Client HTTP Request - ClientHandler.java</h3>

Once the connection has been made and the thread has started, the HTTP request on the input stream is handled by the ClientHandler.

The <code>ClientHandler</code> is a class that attempts to read the lines of data from the input stream <code>while ((line = in.readLine()) != null && !line.isEmpty())</code>. 

<h3>Server HTTP Response - ClientHandler.java</h3>

In its current iteration it does not actually respond based off the request, but instead uses a hardcoded path to files. This will be fixed and updated later once I have implemented it.

For now, it grabs the file and converts it to a string using my <code>FileSender</code> class. This class takes a file path as a an input string, reads the file, and returns its contents as a string. <code>FileSender.fileToString(String path)</code>

Once the file has been converted to a string, the header is compiled with the information from the file, and using <code>HelperFunctions.getRfc1123Format()</code> it creates a proper response header.

Finally, the header and body are both converted to byte arrays, and the <code>Client Handler</code> attepts to write the data to the <code>OutputStream</code>. If successful, the thread will then close. This currently does not support <code>connection: keep-alive</code> for multiple requests, but I will attempt to fix that at a later date. 

<h3>Closing Notes</h3>

If you have made it this far, thank you! I hope you found this to be informative of the project thus far. If you have any comments/suggestions feel free to reach out to me at: trisstonprogramming@gmail.com, or raise an issue in this repository.

This is my first time writing this in-depth documentation on a project so I would like to know how it is. Too long? Too short? Not informative enough? Too much info? 

Regardless, I hope you follow along and keep up with the project. Thank you for your time. 
<h3>Links</h3>

Portfolio: https://trisstonicenhower.github.io/portfolio<br>
Github: https://github.com/trisstonicenhower<br>
LinkedIn: https://linkedin.com/in/trisstonicenhower