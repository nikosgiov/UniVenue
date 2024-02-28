function fetchNotifications(){
    let costumerId = JSON.parse(localStorage.getItem('user')).costumerId;
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            let notifications = JSON.parse(xhr.responseText);
            for (let i = 0; i < notifications.length; i++) {
                const alertString = `<div class="alert alert-success alert-dismissible fade show" role="alert">
                        <strong>REMINDER</strong> ${notifications[i]}
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                      </div>`;
                $('#homepageAlertHere').prepend(alertString);
            }
        } else if (xhr.status !== 200) {
            alert('Servlet Failed!');
        }
    };
    xhr.open('GET', 'http://localhost:8080/351_PROJECT/Book?costumerId='+costumerId);
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
}