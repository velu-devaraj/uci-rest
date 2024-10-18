# UCI Playwright Tests 

Spring Boot REST Web Application for a backend UCI engine

### Installation

Install npm, node from https://nodejs.org/en/download/package-manager

The open a console in uci-tsn and execute these commands. Choose Typescript and do not oberwrite existing files.

npm init playwright@latest

npm install request --save

### UCI Server

Start the spring boot Server under uci-api. For that do a maven build, deploy the backend chess engine and modify properties as described in uci-api. and execute the "java -jar uci-api-0.0.1-SNAPSHOT.jar"

### Open API Generated Client 

Executing the maven install in uci.client.cpi and copy the generated code under uci.client.api\target\generated-sources-typescript to this project.

### Testing

Test files are placed under tests folder. 

Here is an example test execution. Only a couple of tests are implemented.

npx playwright test MovesAPI.spec.ts --repeat-each=3

npx playwright show-report

The results are shown in a browser

