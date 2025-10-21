# java_http_server

## Purpose
<p>The purpose of this project is to get a deeper understanding of the underlying systems of a web server. The server will be built from scratch on a foundation of the TCP/IP protocol.</p>

## Table Of Contents
1. [Patch Notes](#patch-notes)
2. [How it works](#how-it-works)
3. [Closing Notes](#closing-notes)
4. [Links](#links)

### Patch Notes

#### Version 2.0.0 - 1ba84d5

This version converts the request response cycle to a more sustainable producer/consumer model. The major changes here involve replacing <code>Server.java</code> with <code>ReceiverThread.java</code> and replacing <code>ClientThread.java</code> with the private class <code>ConsumerThread</code> inside of the <code>ConsumerThreadPool.java</code> class. Rather than the server receiving a connection, spinning up a new thread, and handling the request with a new <code>ClientThread</code>, the <code>ReceiverThread</code> listens for a request, creates a <code>Request</code> object, adds it to a <code>BlockingQueue</code> and an available <code>ConsumerThread</code> will digest the request and create a response from it.

Summary:

What's New?
<br>1. Server -> ReceiverThread
<br>2. ClientThread -> ConsumerThreadPool > ConsumerThread
<br>3. Request object : Request(BufferedReader buffIn, OutputStream osOut, Socket cSocket)
<br>4. [How it works](#how-it-works) changed to new functionality.
<br>

What's next?
<br>This project is very far from being complete, but I have finished the initial things I had set out to accomplish in order to make a somewhat functional threaded http server from scratch. I do not have specific plans on what I will work on next, but I have indicated a few areas of improvement.
<br>
<br>1. Error handling on 404 fails on null body.
<br>2. Request METHOD is not utilitzed and simply returns whatever file is connected to the route.
<br>3. Threads do not shutdown gracefully. 
<br>4. Error handling needs work
<br>

This is just a small list of the current areas I think I could make much better with time. If you have any suggestions feel free to reach out to me at the email in the [Closing Notes](#closing-notes) of this README.



#### Version 1.0.0 - 770a87a

This version is actually a code refactor from the previous version. It should function standalone, so I am considering this 1.0.0. Features of this version are as follows:

1. Helper functions broken down into helper package. 
<br>A. DateHelper for date formatting functions
<br>B. ErrorPrinter for printing errors in my personal preferred format
<br>C. HttpRequest for breaking down request into hashtables for dealing with requests

2. Server functions broken down into httpserver package.
<br>A. ClientThread for handling client requests on a thread (currently closes after it completes single request)
<br>B. Router for routing requests for files via url request
<br>C. Server main functionality, what makes this an http server

This refactor changed the way I handled files and routing. Previously it was broken down into js/css/html folders and routed like that, but it was super inefficient. This way you choose where to route requests for files and all js/css/other supporting files get delivered from the static file directory. As it stands, I think this will remain moving forwards unless I find a better way to do it.<br><br>

I also changed the way I sent files. I have no idea why I was writing the file to a string then converting it back to bytes (poor choice or no sleep one or the other), but now it reads it directly to a byte[] and sends it like that. This allows to other types of files like images rather than only plan text files.<br><br>

This makes up the brunt of this update. I figured I would write out a longer explanation since this was a total refactor. If you have any comments let me know!

### How it works

1. Create URL router: <code>Router router = new Router()</code>
2. Create path route(s): <code>router.routePath(String urlPath, String filePath)</code>
3. Create static file path: <code>router.routeStaticFiles(String directoryPath)</code>
4. Create ThreadedServer object: <code>ThreadedServer threadedServer = new ThreadedServer(int PORT, int RequestQueueSize, int ThreadPoolSize, Router router);</code>
5. Run the server: <code> threadedServer.runServer()</code>

### Closing Notes

If you have made it this far, thank you! I hope you found this to be informative of the project thus far. If you have any comments/suggestions feel free to reach out to me at: trisstonprogramming@gmail.com, or raise an issue in this repository.

This is my first time writing this in-depth documentation on a project so I would like to know how it is. Too long? Too short? Not informative enough? Too much info? 

Regardless, I hope you follow along and keep up with the project. Thank you for your time. 

### Links

Portfolio: https://trisstonicenhower.github.io/portfolio<br>
Github: https://github.com/trisstonicenhower<br>
LinkedIn: https://linkedin.com/in/trisstonicenhower