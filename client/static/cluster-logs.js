'use strict'

const logger = require('./logger');

var logs = {
    
    line: () => {
        console.log('-------------------------------------------------------------------');
    },
    
    masterPID: (pid) => {
        logs.line();
        console.log(`master with pid: ${pid}`);
    },
    
    listening: (pid, id, address) => {
        logs.line();
        console.log(`worker with pid: ${pid}, id: ${id} is listening on ${address.address}:${address.port}`);
    },
    
    online: (pid, id) => {
        logs.line();
        console.log(`worker with pid: ${pid}, id: ${id} is online`);
    },
    
    disconnect: (pid, id) => {
        logs.line();
        console.log(`worker with pid: ${pid}, id: ${id} has disconected`);
    },
    
    error: (pid, id, err) => {
        logs.line();
        logger.e(`worker with pid: ${pid}, id: ${id}` + err);
    },
    
    exit: {
        
        signal: (pid, id, signal) => {
            logs.line();
            console.log(`worker with pid: ${pid}, id: ${id} was killed by signal: ${signal}`);
        },
        
        error: (pid, id, code) => {
            logs.line();
            let msg = `worker with pid: ${pid}, id: ${id} exited with error code: ${code}`;
            console.log(msg);
            logger.e(msg);
        },
        
        startAgain: () => {
            logs.line();
            console.log('Starting a new worker');
        },
        
        ok: (pid, id) => {
            logs.line();
            console.log(`worker with pid: ${pid}, id: ${id} success!`);
        }
        
    },
    
    fileChanged: (event, filename) => {
        logs.line();
        console.log(`file ${filename} has been changed by event: ${event}`);
    },
    
    clearCacheErr: (err) => {
        logs.line();
        console.log(`clear cache failed: ${err}`);
    },
    
    requestMsg: (pid, id) => {
        logs.line();
        console.log(`worker with pid: ${pid}, id: ${id} has been invoked by request`);
    }
}

module.exports = logs;