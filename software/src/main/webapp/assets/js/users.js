function loadPageFunctions(webpage){
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            localStorage.setItem("user",xhr.responseText);
            let user = JSON.parse(xhr.responseText);
            if (window.location.href === 'http://localhost:8080/351_PROJECT/pages/admin.html'
            || window.location.href === 'http://localhost:8080/351_PROJECT/pages/admin.html#') {
                if (user.hasOwnProperty('money')) window.location.href = 'homepage.html';
            }

            $('#navBalance').text(`Balance: ${user.money}$`);
            const imgSrc = `../ProfileImage?costumerId=${user.costumerId}`;
            $('#navAnatar').attr('src', imgSrc);
            $("#navUsername").text(`${user.username}`);

            if (webpage==="homepage"){
                $('#welcomeHeader').text('Welcome back '+user.firstname);
                fetchNotifications();
            }
            if (webpage==="history"){
                fetchHistory();
            }
            if (webpage==="profile"){
                $('#profile_img').attr('src', imgSrc);
                $("#profile_name").text(`${user.firstname} ${user.lastname}`);
                $("#profile_email").text(`${user.email}`);
                $("#profile_username").text(`${user.username}`);
                $("#profile_tel").text(`${user.telephone}`);
                $("#profile_birthdate").text(`${user.birthDate}`);
                $("#profile_gender").text(`${user.gender}`);
                $("#profile_balance").text(`Balance: ${user.money}$`);
            }
            if (webpage==="roomPreview"){
                loadRoom();
            }
            if (webpage==="rooms"){
                fetchRooms();
            }

        } else if (xhr.status !== 200) {
            window.location.href = '../index.html';
        }
    };
    xhr.open('GET', 'http://localhost:8080/351_PROJECT/Login');
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
}

function logout(){
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            window.location.href = '../index.html';
            localStorage.clear();
        } else if (xhr.readyState === 4 && xhr.status !== 200) {
            alert('Request failed. Returned status of ' + xhr.status);
        }
    };
    xhr.open('POST', 'http://localhost:8080/351_PROJECT/Logout',false);
    xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
    xhr.send();
}
