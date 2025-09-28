# java_http_server

## Purpose
<p>The purpose of this project is to get a deeper understanding of the underlying systems of a web server. The server will be built from scratch on a foundation of the TCP/IP protocol.</p>

## Table Of Contents
1. [Patch Notes](#patch-notes)
2. [How it works](#how-it-works)
3. [Closing Notes](#closing-notes)
4. [Links](#links)

### Patch Notes

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

1. Create server object: <code>Server server = new Server(int PORT)</code>
2. Create URL router: <code>Router router = new Router()</code>
3. Create path route(s): <code>router.routePath(String urlPath, String filePath)</code>
4. Create static file path: <code>router.routeStaticFiles(String directoryPath)</code>
5. Assign router to server: <code>server.setRouter(router)</code>
6. Run server: <code> server.run()</code>

### Closing Notes

If you have made it this far, thank you! I hope you found this to be informative of the project thus far. If you have any comments/suggestions feel free to reach out to me at: trisstonprogramming@gmail.com, or raise an issue in this repository.

This is my first time writing this in-depth documentation on a project so I would like to know how it is. Too long? Too short? Not informative enough? Too much info? 

Regardless, I hope you follow along and keep up with the project. Thank you for your time. 

### Links

Portfolio: https://trisstonicenhower.github.io/portfolio<br>
Github: https://github.com/trisstonicenhower<br>
LinkedIn: https://linkedin.com/in/trisstonicenhower