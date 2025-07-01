
let vertIcon = document.querySelectorAll('.vertI');
let crudLayer = document.querySelectorAll('.crud');
let delPopup = document.querySelector('.del-popup');
for(let j = 0; j < vertIcon.length;j++) {
    vertIcon[j].addEventListener('click', (event)=> {
            event.preventDefault()
            vertIcon[j].style.display='none';
            crudLayer[j].style.display = 'flex';

            console.log('hello')
     })
}
function closeCrud(event) {
    event.preventDefault()

    for(let j = 0; j < vertIcon.length;j++) {
        event.preventDefault()
        vertIcon[j].style.display='flex';
        crudLayer[j].style.display = 'none';

        console.log('hello')
    }
}

function showDeletePopup(event) {
    event.preventDefault()
    delPopup.style.display='block'
}

function closeDeletePopup(event) {
    event.preventDefault()
    delPopup.style.display='none'
}


let navBar = document.querySelector('.navBar');
let menuIcon = document.querySelector('.menuIcon')
let i = 0;
function showNavBar(event) {
    event.preventDefault();
    if(i%2 == 0) {
        navBar.style.display = 'block';
        menuIcon.innerText='close';
        i++;
    }
    else {
        navBar.style.display = 'none';
        menuIcon.innerText='menu';
        i++;
    }
}

/* OPEN PROJECT*/

const cards = document.querySelectorAll('.card-1');
let projectIds = document.querySelector('.projectId');

for(let i = 0; i < cards.length; i++) {
    cards[i].addEventListener('click', (event) => {
         event.preventDefault()
         let projectId = document.querySelectorAll('.projectId')[i];
         event.preventDefault();
         console.log('clicked!!')
         window.location.href=`http://localhost:8080/app/task/view/${projectId.innerText}`
    })
}

// UPDATE DATE
let card_date = document.getElementById('card-date')
const rawDate = card_date.innerText;
console.log(card_date)
const date = new Date(rawDate.replace(" ", "T"));

const options = {
  year: "numeric",
  month: "long",
  day: "numeric",
  hour: "2-digit",
  minute: "2-digit",
  second: "2-digit",
};

const readable = date.toLocaleString("en-US", options);
console.log(readable);
card_date.innerText = readable;