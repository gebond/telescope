'use strict'

const fs = require('fs');

var logPattern = (prefix, message) => {
    return logger.getTime() + ' __________ ' + prefix + ': ' + message;
}

var logger = {
    
    i: (info) => {
        logger.w(logPattern('INFO', info));
    },
    
    e: (error) => {
        logger.w(logPattern('ERROR', error));
    },
    
    d: (debug) => {
        logger.w(logPattern('DEBUG', debug));
    },
    
    w: (message) => {
        let filePath = './logs/' + logger.getFileName();
        message += '\r\n';
        
        fs.access(filePath, fs.R_OK | fs.W_OK, (err) => {
            if (err) {
                logger.write(filePath, message);
            } else {
                logger.append(filePath, message);
            }
        });
    },
    
    write: (filePath, message) => {
        fs.writeFile(filePath, message,  'utf8', (err) => {
            if (err) {
                console.log(err);
                throw new Error('cant log message');
            }
        });
    },
    
    append: (filePath, message) => {
        fs.appendFile(filePath, message, 'utf8', (err) => {
            if (err) {
                throw new Error('cant log message');
            }
        });
    },
    
    getFileName: () => {
        let now = new Date();
        return [now.getDate(), now.getMonth(), now.getFullYear()].join('-');
    },
    
    getTime: () => {
        let now = new Date();
        return [now.getDate(), now.getMonth(), now.getFullYear()].join('-') 
            + ' '
            +  [now.getHours(), now.getMinutes(), now.getSeconds()].join(':');
    }
    
}

module.exports = logger;