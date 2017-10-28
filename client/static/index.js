'use strict'

const fs = require('fs');
const cluster = require('cluster');
const numCPUs = require('os').cpus().length;
const logs = require('./cluster-logs');

const config = require('./app-config');
const updatingFiles = config.updatingFiles;
const env = config[process.env.NODE_ENV];
const App = require('./app');

var cpusCount = 0;

var fork = () => {
    for (let i = 0; i < numCPUs; i++) {
        cluster.fork();
    }
    handleMaster();
    handleWorkers();
    watchFiles();
}

var loadApp = () => {
    new App(env.host, env.port).run();
};

var handleMaster = () => {
    
    fs.writeFileSync("/tmp/node.guseyn.me.cluster.pid", process.pid);
    
    cluster.on('exit', (worker, code, signal) => {
        logs.exit.startAgain();
        handleWorker(cluster.fork());
    });
    
    tuneProcess();
    
}

var tuneProcess = () => {
    
    process.on('SIGUSR2', () => {
        disconnectAllWorkers();
    });
    
}

var watchFiles = () => {
    Object.keys(updatingFiles).forEach((id) => {
        fs.watch(updatingFiles[id], (event, filename) => {
            logs.fileChanged(event, filename);
        });
    });
}

var handleWorkers = () => {
    Object.keys(cluster.workers).forEach((id) => {
        handleWorker(cluster.workers[id]);
    });
}

var handleWorker = (worker) => {
    
    let pid = worker.process.pid;
    let id = worker.id;
    
    worker.on('online', () => {
        logs.online(pid, id);
    });
    
    worker.on('listening', (address) => {
        logs.listening(pid, id, address);
        cpusCount += 1;
        if (cpusCount === numCPUs) {
            logs.line();
        }
    });
    
    worker.on('disconnect', () => {
        logs.disconnect(pid, id);
    });
    
    worker.on('error', (err) => {
        logs.error(pid, id, err);
    });
    
    worker.on('exit', (code, signal) => {
        if (signal) {
            logs.exit.signal(pid, id, signal);
        } else if (code !== 0) {
            logs.exit.error(pid, id, code);
        } else {
            logs.exit.ok(pid, id);
        }
    });
    
}

var disconnectAllWorkers = () => {
    Object.keys(cluster.workers).forEach((id) => {
        cluster.workers[id].process.disconnect();
    });
}

if (cluster.isMaster) {
    fork();
} else {
    loadApp();
}