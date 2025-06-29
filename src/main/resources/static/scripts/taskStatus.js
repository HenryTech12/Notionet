
let stompClient2 = null;
let pName2 = document.querySelector('.pName');
const notifBox = document.getElementById('notif-box');
const bellBtn = document.getElementById('bell-btn');
const notifDot = document.getElementById('notif-dot');


function connect() {
    let socket2 = SockJS("/ws");
    stompClient2 = Stomp.over(socket2)

    stompClient2.connect({}, function(frame) {
        console.log('Client Connected '+frame)
        stompClient2.subscribe('/topics/finished', function(message) {
            const response = JSON.parse(message.body);
            console.log(response.message)
            const field = document.createElement('span');
            field.innerText = response.message;
            field.classList.add('fields','border', 'w-full', 'h-auto', 'text-center');
            notifBox.appendChild(field);
            notifDot.classList.remove('hidden'); // Hide red dot on view

        })
    })
}
connect()
function notifyForStatus() {
    if(stompClient2 != null) {
        const data2 = {"projectName":pName2.innerText};
        stompClient2.send('/app/getLimit',{}, JSON.stringify(data2));
        console.log('status data sent')
    }
}

let interValID2 = setInterval(() => {
    if(stompClient2.connected) {
        console.log('hello')
        notifyForStatus()
        clearInterval(interValID2);
    }
},1000)


bellBtn.addEventListener('click', (event) => {
  event.preventDefault();
  notifBox.classList.toggle('hidden');
  notifDot.classList.add('hidden'); // Hide red dot on view

});