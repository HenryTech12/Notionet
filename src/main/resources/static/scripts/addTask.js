
/* ADD CATEGORY*/
const listOfCategory = document.querySelector('.categories');
const categories = ['Marketing','Meeting','Sports','Development','Family','Urgent']
categories.forEach(data => {
    const buttons = document.createElement('section');
    buttons.value = data;
    buttons.onclick = 'chooseCategory(event)'
    buttons.innerText = data;
    buttons.classList.add('bg-gray-200','w-fit', 'h-auto', 'cBtn', 'hover:bg-purple-500','cursor-pointer', 'rounded-2xl', 'p-2');
    listOfCategory.appendChild(buttons)
});


let categoriesBtn = document.querySelectorAll('.cBtn')
let displayCategory = document.getElementById('displayCategory')
let chosenField = document.getElementById('chosenCategory');
if(categoriesBtn) {
    categoriesBtn.forEach((category) => {
        category.addEventListener('click', (event) => {
            event.preventDefault();
                chosenField.value = category.innerText;
                displayCategory.innerText = category.innerText;
                displayCategory.setAttribute('style', 'font-family: Work Sans')
                chosenField.innerText = category.innerText;
        });
    })
}

function removeCategory(event) {
    event.preventDefault()
    displayCategory.innerText = ''
    console.log(chosenField.value)
    chosenField.value = ''
}