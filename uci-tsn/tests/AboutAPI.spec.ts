import { test, expect } from '@playwright/test';
import { AboutApi } from '../api/aboutApi.ts';
import { AboutResponse } from '../model/aboutResponse.ts';
const { aboutApp } = require('../api/aboutApi.ts');


test('aboutApp', async () => {

  var api = new AboutApi();
  api.basePath = 'http://localhost:8080/uci-api-0.0.1-SNAPSHOT';
  await api.aboutApp().then((value) => {
    var ar = value;

    console.log(value.body.about?.description);
    console.log(value.body.about?.engines);
    
    expect('Chess Application').toBe(value.body.about?.description);
    expect(value.body.about?.engines).toBeTruthy();
    expect(["LC0"]).toEqual(value.body.about?.engines);
    

  });


});


