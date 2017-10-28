'use strict'

const http = require('http');
const StringDecoder = require('string_decoder').StringDecoder;
const decoder = new StringDecoder('utf8');
const Router = require('./router');

const logger = require('./logger');

class App {
    
    constructor(host, port) {
        this.host = host;
        this.port = port;
        this.dbClient = null;
    }
    
    run() {
        http.createServer((request, response) => {
            
            let inputData = [];
            
            request.addListener('data', ((chunk) => {
                this.dataFunc(inputData, chunk);
            }).bind(this));
            
            request.addListener('end', (() => {
                this.endFunc(inputData, request, response);
            }).bind(this));
            
        }).listen(this.port, this.host, () => {});
    }
    
    dataFunc(inputData, chunk) {
        if (chunk) {
            inputData.push(chunk);
        }
    }
        
    endFunc(inputData, request, response) {
        if (inputData.length != 0) {
            let buffer = Buffer.concat(inputData);
            let data;
            try {
                data = JSON.parse(decoder.write(buffer));
            } catch (e) {
                console.log(e);
            }
            new Router({inputData: data, request, response}).run();
        } else {
            new Router({request, response}).run();
        }
    }

}

module.exports = App;