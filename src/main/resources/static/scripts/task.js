
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

let delPopup = document.querySelector('.del-popup');

function showDeletePopup(event) {
    event.preventDefault()
    delPopup.style.display='block'
}

function closeDeletePopup(event) {
    event.preventDefault()
    delPopup.style.display='none'
}