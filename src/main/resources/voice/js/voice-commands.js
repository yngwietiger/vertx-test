var session_id = 1;
var host = '50.17.171.189';
// var host = 'localhost';


function init() {
    // loadCurrentPrice();
    registerHandlerForUpdateCurrentPriceAndFeed();
};

// function loadCurrentPrice() {
//     var xmlhttp = (window.XMLHttpRequest) ? new XMLHttpRequest() : new ActiveXObject("Microsoft.XMLHTTP");
//     xmlhttp.onreadystatechange = function () {
//         if (xmlhttp.readyState == 4) {
//             if (xmlhttp.status == 200) {
//                 document.getElementById('current_price').innerHTML = 'EUR ' + JSON.parse(xmlhttp.responseText).price.toFixed(2);
//             } else {
//                 document.getElementById('current_price').innerHTML = 'EUR 0.00';
//             }
//         }
//     };
//     xmlhttp.open("GET", "http://localhost:8080/api/auctions/" + auction_id);
//     xmlhttp.send();
// };

function registerHandlerForUpdateCurrentPriceAndFeed() {
    var eventBus = new EventBus('https://' + host + ':443/eventbus');
    eventBus.onopen = function () {
        eventBus.registerHandler('session.' + session_id, function (error, message) {
            document.getElementById('last_command').innerHTML = JSON.parse(message.body).command;
            document.getElementById('feed').value += 'Last Command: ' + JSON.parse(message.body).command + '\n';
        });
    }
};

function sendCommand() {

    var newCommand = document.getElementById('my_command').value;

    alert(newCommand);

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
    xmlhttp.open("POST", "https://" + host + ":443/api/voice/" + session_id);
    xmlhttp.setRequestHeader("Content-Type", "application/json");
    xmlhttp.send(JSON.stringify({command: newCommand}));
};
