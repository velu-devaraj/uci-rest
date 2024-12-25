# UCI REST API 

Spring Boot REST Web Application for a backend UCI engine

### UCI Specification 

UCI specifies the interaction format for a client with a chess engine. Many of the chess engines support UCI including Stockfish and Leela Chess Zero. See https://www.wbec-ridderkerk.nl/html/UCIProtocol.html for UCI.

### Spring Boot Open API implementation

This implementation is minimal and allows executing UCI commands on a backend engine. Command results are returned uninterpreted. uci.yaml under uci-api\src\main\resources specifies supported REST calls.

### Design

Chess engines accept input from stdin and write to stdin and stderr. The input commands and responses can be found in UCI documentation. The runtime behavior of the backend engines may not be clear and may need testing out from console interactions. There is no support for multiple sessions. Commands issued do not all return a response. An example is "position" which sets and remembers a position. But stateless behavior can be expected by a position followed by "go" with needed options. In this implementation the spring boot java server starts the backend engine, an executable that finally computes the chess moves.  The Spring boot server implements Open API REST calls that support UCI commands and can be accessed from a GUI front end. https://github.com/velu-devaraj/chess is a flutter, dart implementation of a minimal GUI. The actual engine's implementations may exploit modern CPU, GPU capabilities but are limited in interaction through process input, output and error streams of the engine executable. Pre-built executables can be downloaded from implementaions like Leela Chess for the operating system you have and their location has to configured as shown below. Command sequences issued and result sequences may come out of order in some cases. For this reason limit issuing one or two commands in a REST call. An additional "isready" is appended to in the REST call. 


### Server Configuration

Key Spring boot Server side properties can be set in config\application.properties. A sample is provided in config\application.properties. 

<div style="max-height: 200px; overflow-y: auto;"> 
<pre>
<code>


! application name 
spring.application.name=chess
! default https
server.port=443
! spring boot server host
server.host=[your-host]
server.servlet.context-path=/uci-api-0.0.1-SNAPSHOT
logging.file.name=./logs/chess-app.log
logging.level.root=debug
logging.level.org.springframework.web=warn
management.endpoints.jmx.exposure.exclude=*
spring.threads.virtual.enabled=true
! generate a key and certificate file using utils like Minica that can easily create a certification authority and the key
! see https://github.com/jsha/minica
server.ssl.certificate-private-key=file:./certs/[my-key.pem]
server.ssl.certificate=file:./certs/[my.pem]


</code>
</pre> 
</div>

### Backend Engine

This was minimally tested with Leela Chess Zero https://github.com/LeelaChessZero/lc0
The Engine parameters can be configured in engine-parameters.json under config. The spring.threads.virtual.enabled=true option enables virtual threads. Thread sleep calls may not be a performance issue. A pool of engine processes are maintained and engineProcessMaxCount sets the max count. Ensure that a neural net model is present in the executable location. See https://lczero.org/play/networks/bestnets/


<div>
<pre>
<code>

{
    "enginePath": "[LC0-ENGINE-PATH\\lc0-dnnl\\lc0-v0.31.1-windows-cpu-dnnl]",
    "engineExecutable": "lc0.exe",
    "workingDirectory": "[LC0-ENGINE-PATH\\lc0-dnnl\\lc0-v0.31.1-windows-cpu-dnnl]",
    "engineProcessMaxCount": 3,
    "retryCount" : 3,
    "milliSecWait" : 1
}
</code>
</pre>
</div>

### Build and Run the spring boot server

Install java 23, maven and set environment vars JAVA_HOME, PATH correctly. Build the server with mvn install. The executable jar packs the jetty server libbraries and can be executed with java -jar uci-api-0.0.1-SNAPSHOT.jar. The jar is created in target folder. A run-spring-boot-java.sh script can be used if config files are in config folder and jar in target folder.

### Swagger UI

You can check the server from the swagger UI. 
https://[your host:port/uci-api-0.0.1-SNAPSHOT/swagger-ui/index.html

 


