# UCI REST API 

Spring Boot REST Web Application for a backend UCI engine

### UCI Specification 

UCI specifies the interaction format for a client with a chess engine. Many of the chess engines support UCI including Stockfish and Leela Chess Zero. See https://www.wbec-ridderkerk.nl/html/UCIProtocol.html for UCI.

### Spring Boot Open API implementation

This implementation is minimal and allows executing UCI commands on a backend engine. Command results are returned uninterpreted. uci.yaml under uci-api\src\main\resources specifies supported REST calls.

### Design

The UCI specification is not clear on runtime behavior of the engines. There is no support for multiple sessions. Commands issued do not all return a response. An example is "position" which sets and remembers a position. But stateless behavior can be expected by a position followed by go with needed options. The backend engine is an executable that takes UCI commands from Java Open API REST calls. The actual engines implementations may exploit modern CPU, GPU capabilities but are limited in interaction through process input, output and error streams. Command sequences issued and result sequences may come out of order in some cases. For this reason limit issuing one or two commands in a REST call. An additional "isready" is appended to in the REST call. While this design allows multiple sessions, a better design is to implement them directly in the backend code which is typically in C++.

### Backend Engine

This was minimally tested with Leela Chess Zero https://github.com/LeelaChessZero/lc0
The Engine and other parameters can be configured in application.properties and engine-parameters.json under /uci-api/src/main/resources/. The spring.threads.virtual.enabled=true option enables virtual threads. Thread sleep calls may not be a performance issue.




