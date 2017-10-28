'use strict'

const fs = require('fs');
const path = require('path');
const url = require('url');

const indexHtmlPage = '/html/index.html';
const notFoundHtmlPage = '/html/404.html';
const staticBasePath = './../static';

const mimeTypes = {
    html: 'text/html',
    css: 'text/css',
    js: 'application/javascript',
    json: 'application/json',
    png: 'image/png',
    jpeg:'image/jpeg',
    jpg:'image/jpeg',
    gif:'image/gif',
    pdf:'application/pdf',
    ttf:'application/x-font-ttf',
    svg: 'image/svg+xml'
}

class Router {
    
    constructor({inputData, request, response}) {
        this.pathName = url.parse(request.url).pathname;
        this.inputData = inputData;
        this.request = request;
        this.response = response;
    }
    
    run() {
        var filePath = path.resolve(staticBasePath);
        let parts = this.pathName.split('/');
        if (parts.join('') === '') {
            filePath = path.join(filePath, indexHtmlPage);
            this.sendFile(filePath, 'html'); 
        } else {
            filePath = path.join(filePath, this.pathName);
            let type;
            let typeParts = parts[parts.length - 1].split('.');
            if (typeParts.length !== 1) {
                type = typeParts[1];
                this.sendFile(filePath, type);
            } else {
                type = 'html';
                this.sendFile(filePath + '.html', type);
            }
        }
    }
    
    sendFile(filePath, type) {
        let parts = this.pathName.split('/');
        fs.open(filePath, 'r', (err, fd) => {
            if (err) {
                this._404();
            } else {
                this.response.writeHead(200, {
                    'Content-Type': this.getResTypeByMimeType(type)
                });
                let stream = fs.createReadStream(null, {fd: fd});
                stream.pipe(this.response);
            }
        });
    }
    
    _404(type) {
        var filePath = path.resolve(staticBasePath);
        filePath = path.join(filePath, notFoundHtmlPage);
        fs.open(filePath, 'r', (err, fd) => {
            if (err) {
                this._500();
            } else {
                this.response.writeHead(404, {
                    'Content-Type': this.getResTypeByMimeType('html')
                });
                let stream = fs.createReadStream(null, {fd: fd});
                stream.pipe(this.response);
            }
        });
    }

    _500() {
        this.response.writeHead(500, {
            'Content-Type': 'text/plain'
        });
        this.response.end('Server error');
    }

    getResTypeByMimeType(mimeType) {
        console.log(mimeTypes[mimeType]);
        return mimeTypes[mimeType] || 'text/plain';
    }
    
}

module.exports = Router;
