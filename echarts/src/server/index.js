const express = require('express');
const app = express();

const mysql = require('mysql');
const models = require("./db");
const conn = mysql.createConnection(models.mysql);
conn.connect();

var colorMap = new Map();

app.all('*', function (req, res, next) {
    res.header("Access-Control-Allow-Origin", "*");
    res.header('Access-Control-Allow-Methods', 'PUT, GET, POST, DELETE, OPTIONS');
    res.header("Access-Control-Allow-Headers", "X-Requested-With");
    res.header('Access-Control-Allow-Headers', 'Content-Type');
    next();
});
app.get('/api', function (request, response) {
    var color;
    var tempArr = [];
    var re = [];
    var sql = "select a.project,a.author,sum(a.score) as score,format(sum(a.rate),2) as rate from(select project,if(rate<1,'其他',author) as author,score,rate from ?) as a group by a.project,a.author";
    handleDisconnect(conn);
    conn.query(sql.replaceAll('?', request.query.from), function (err, queryResult) {
        if (err) {
            console.log("error------------------" + err.message);
            throw err;
        }
        for (let i = 0; i < queryResult.length; i++) {
            //设置颜色
            if (colorMap.has(queryResult[i].author)) {
                color = colorMap.get(queryResult[i].author);
            } else {
                color = "rgb(" + Math.round(Math.random() * 255) + "," + Math.round(Math.random() * 255) + "," + Math.round(Math.random() * 255) + "," + 0.5 + ")";
                colorMap.set(queryResult[i].author, color);
            }
            //处理数据
            if (tempArr.indexOf(queryResult[i].project) === -1) {
                tempArr.push(queryResult[i].project);
                re.push({
                    title: queryResult[i].project,
                    items: [{
                        name: queryResult[i].author,
                        value: queryResult[i].rate,
                        itemStyle: {color: color}
                    }]
                })
            } else {
                for (let j = 0; j < re.length; j++) {
                    if (re[j].title == queryResult[i].project) {
                        re[j].items.push({
                            name: queryResult[i].author,
                            value: queryResult[i].rate,
                            itemStyle: {color: color}
                        });
                        break;
                    }
                }
            }
        }
        console.log("finally-----------" + request.query.from + "---------------" + queryResult.length + "----------" + tempArr);
        response.send(re);
    })
});
app.listen(3001, () => console.log('success'));


function handleDisconnect(connection) {
    //监听错误事件
    connection.on('error', function (err) {
        if (!err.fatal) {
            return;
        }
        if (err.code !== 'PROTOCOL_CONNECTION_LOST') {
            throw err;
        }
        console.log('Re-connecting lost connection: ' + err.stack);
        connection = mysql.createConnection(connection.config);
        handleDisconnect(connection);
        connection.connect();
    });
}