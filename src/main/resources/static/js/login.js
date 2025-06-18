const pwToggleButton = document.getElementById('togglePw');
const pwInput = document.getElementById('password');

initPwToggle = function() {
    pwToggleButton.classList.add("js");
}

if(pwToggleButton) {
    pwToggleButton.addEventListener('click', function() {
        if (pwInput.type === 'password') {
            pwInput.type = 'text';
            pwToggleButton.classList.add('active');
        } else {
            pwInput.type = 'password';
            pwToggleButton.classList.remove('active');
        }
    });
}

initPwToggle();