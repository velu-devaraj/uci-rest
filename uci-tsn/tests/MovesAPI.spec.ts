import { test, expect } from '@playwright/test';

import { CommandResponse } from '../model/commandResponse.ts';
import { Command } from '../model/models.ts';
import { MovesApi } from '../api/movesApi.ts';
const { execute } = require('../api/movesApi.ts');


async function pause(duration) {
  return new Promise(resolve => setTimeout(resolve, duration));
}


test('execute', async () => {

  var moves = new MovesApi();
  moves.basePath = 'http://localhost:8080/uci-api-0.0.1-SNAPSHOT';

  var command1 =  new Command();

  command1.commandString = ["uci",
    "position startpos moves e2e4 e7e5",
    "go depth 3"];


  await moves.execute(command1).then((value) => {
    var ar = value;

    //console.log(value.body.engineResult);

    if(value.body.error){
      console.log("Error: ");
      console.log(value.body.errorString);
      return;
    }
    
    
    expect(value.body.engineResult).toBeTruthy();
    expect(value.body.engineResult).toContain("bestmove g1f3 ponder g8f6");
    expect(value.body.engineResult).toContain("readyok");
    

  });


});


