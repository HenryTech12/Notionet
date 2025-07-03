
let stompClient = null;
let pName = document.querySelectorAll('.pName');
let oT = document.querySelectorAll('.ot');
let cT = document.querySelectorAll('.ct');
let iter = 0;
function connect() {
    let socket = SockJS("/ws");
    stompClient = Stomp.over(socket)

    stompClient.connect({}, function(frame) {
        console.log('Client Connected '+frame)
        stompClient.subscribe('/topics/len', function(message) {
            const response = JSON.parse(message.body);
            oT[iter].innerText = response.ot;
            cT[iter].innerText = response.ct;
            console.log(response)
            iter++;
        })
    })
}
connect()
function notifyForStatus() {
    if(stompClient != null) {
        pName.forEach((mydata) => {
            const data = {"projectName":mydata.innerText};
            stompClient.send('/app/getStatus',{}, JSON.stringify(data));
        });
        console.log('status data sent')
    }
}

let interValID = setInterval(() => {
    if(stompClient.connected) {
        console.log('hello')
        notifyForStatus()
        clearInterval(interValID);
    }
},1000)

