var config = {
    
    updatingFiles: {
        1: './css',
        2: './html',
        3: './js'
    },
    
    prod: {
        host: '5.187.7.231',
        port: 80
    },
    
    dev: {
        host: '5.187.7.231',
        port: 3100
    },
    
    local: {
        host: '127.0.0.1',
        port: 8080
    }
    
}

module.exports = config;

//-- restart server
//kill -SIGUSR2 `cat /tmp/node.guseyn.me.cluster.pid`