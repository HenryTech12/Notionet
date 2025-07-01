
let stompClient = null;
let pID = document.querySelector('.pID');
const notifBox = document.getElementById('notif-box');
const bellBtn = document.getElementById('bell-btn');
const notifDot = document.getElementById('notif-dot');


function connect() {
    let socket = SockJS('/ws');
    stompClient = Stomp.over(socket)

    stompClient.connect({}, function(frame) {
        console.log('Connected '+frame);
        stompClient.subscribe('/topics/uproject',function(message) {
                console.log(message.body)
                const response = JSON.parse(message.body);
                console.log(response.message)
                const listOfResponse = response.message;
                listOfResponse.forEach((data) => {
                       const field = document.createElement('span');
                       field.innerText = data;
                       field.classList.add('fields','border', 'w-full', 'h-auto', 'text-center');
                       notifBox.appendChild(field);

                });
                notifDot.classList.remove('hidden'); // Hide red dot on view
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


bellBtn.addEventListener('click', (event) => {
  event.preventDefault();
  notifBox.classList.toggle('hidden');
  notifDot.classList.add('hidden'); // Hide red dot on view

});