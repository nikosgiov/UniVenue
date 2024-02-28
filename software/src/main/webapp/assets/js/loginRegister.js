function login(){
    $('#usernameError').text('');
    let username = document.getElementById("usernameField").value;
    let password = document.getElementById("passwordField").value;
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && (xhr.status === 200)) {
            localStorage.setItem("user",xhr.responseText);
            let user = JSON.parse(xhr.responseText);
            if (user.hasOwnProperty('money')) window.location.href = 'homepage.html';
            else window.location.href = 'admin.html';
        } else if (xhr.status !== 200) {
            $('#usernameError').text('Incorrect username or password.');
        }
    };
    xhr.open('POST', 'http://localhost:8080/351_PROJECT/Login?username='+username+'&password='+password);
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
    return false;
}

function signup(){
    $('#password2Error').text('');
    $('#usernameError').text('');
    $('#emailError').text('');
    $('#telephoneErr').text('');
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;
    const repassowrd = document.getElementById("confirmPassword").value;
    const firstname = document.getElementById("firstName").value;
    const lastname = document.getElementById("lastName").value;
    const email = document.getElementById("email").value;
    const birthdate = document.getElementById("birthdate").value;
    const tel = document.getElementById("telephone").value;
    const gender = document.querySelector('input[name="gender"]:checked').value;
    if (password !== repassowrd) {
        $('#password2Error').text('Mismatched password.');
        return false;
    }
    const data = {
        username: username,
        password: document.getElementById("password").value,
        money: 100,
        firstname: firstname,
        lastname: lastname,
        email: email,
        birthDate: birthdate,
        telephone: tel,
        gender: gender
    };
    const jsonData = JSON.stringify(data);
    // alert(jsonData);
    //return false;
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && (xhr.status === 200)) {
            //  alert(xhr.response);
            var successAlert = '<div class="alert alert-success alert-dismissible fade show" role="alert">' +
                '<strong>Signup successfull !</strong> You can now login with your credentials.' +
                '<button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>' +
                '</div>';
            localStorage.setItem('successAlert',successAlert);
            window.location.href = '../index.html';
        } else if (xhr.status !== 200) {
            if(xhr.responseText==='1') $('#usernameError').text('Not valid username.');
            if(xhr.responseText==='2')  $('#emailError').text('Not valid email.');
            if(xhr.responseText==='3') $('#telephoneErr').text('Not valid telephone.');
        }
    };
    xhr.open('POST', 'http://localhost:8080/351_PROJECT/Signup');
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send(jsonData);
    return false;
}

function prinAlert() {
    if (localStorage.getItem('successAlert')) {
        $('body').prepend(localStorage.getItem('successAlert'));
        localStorage.removeItem('successAlert');
    }
}