
let stompClient = null;
function connect() {
    let socket = SockJS('/ws');
    stompClient = Stomp.over(socket)

    stompClient.connect({}, function(frame) {
        console.log('Connected '+frame);
        stompClient.subscribe('/topics/uproject',function(message) {
                console.log(message.body)
        })
    })
}

connect()

let pName = document.querySelector('.pName');
console.log(pName);
function notify() {
    const data = {
        "projectName":pName.innerText
    }
    console.log(data);
    if(stompClient != null) {
        stompClient.send('/app/update',{}, JSON.stringify(data));
        console.log('notified.')
    }
}

let interValID = setInterval(() => {
    if(stompClient.connected) {
        console.log('hello')
        notify()
        clearInterval(interValID);
    }
},1000)