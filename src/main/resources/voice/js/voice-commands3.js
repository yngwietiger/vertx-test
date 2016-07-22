var session_id = 1;
 //var host = '50.17.171.189';
var host = 'localhost';
//var host = '10.20.3.92';

function init() {
    // loadCurrentPrice();
    //registerHandlerForUpdateCurrentPriceAndFeed();
};


function registerHandler() {

    //host = document.getElementById('my_host').value;
    session_id = document.getElementById('my_session').value;

    var options = {};
    //options.protocols_whitelist = ["websocket"];

    //var eventBus = new vertx.EventBus('http://' + host + ':8080/eventbus', options);
    var eventBus = new vertx.EventBus('/eventbus', options);

    eventBus.onopen = function () {
        eventBus.registerHandler('session.' + session_id, function (message, replyTo) {

            console.log("got message from vertx: " + message);

            document.getElementById('last_command').innerHTML = JSON.parse(message).command;
            document.getElementById('feed').value += 'Last Command: ' + JSON.parse(message).command + '\n';
        });
    }
};

function sendCommand() {

    var newCommand = document.getElementById('my_command').value;

    //alert(newCommand);

    var xmlhttp = (window.XMLHttpRequest) ? new XMLHttpRequest() : new ActiveXObject("Microsoft.XMLHTTP");
    xmlhttp.onreadystatechange = function () {
        if (xmlhttp.readyState == 4) {
            if (xmlhttp.status == 200) {
                document.getElementById('error_message').innerHTML = '';
            } else {
                document.getElementById('error_message').innerHTML = 'Invalid command!';
            }
        }
    };
    //xmlhttp.open("POST", "http://" + host + ":8080/api/voice/" + session_id);
    xmlhttp.open("POST", "/api/voice/" + session_id);
    xmlhttp.setRequestHeader("Content-Type", "application/json");
    xmlhttp.send(JSON.stringify({command: newCommand}));
};

function setHost() {
    host = document.getElementById('my_host').value;
};

function setSessionId() {
    session_id = document.getElementById('my_session').value;
};

