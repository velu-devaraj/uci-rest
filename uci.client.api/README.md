# UCI Client API 

Client API Generators for UCI Open API server under uci-api

### UCI Specification 

UCI specifies the interaction format for a client with a chess engine. Many of the chess engines support UCI including Stockfish and Leela Chess Zero. See https://www.wbec-ridderkerk.nl/html/UCIProtocol.html for UCI.

### Code Generation

The pom configures openapi-generator-maven-plugin for client generation on src/main/resources/uci.yaml. Currently configured for typescript-node under target\generated-sources-typescript. Check https://openapi-generator.tech/docs/generators/ for more languages.

