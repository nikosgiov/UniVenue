function fetchRooms(){
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            let rooms = JSON.parse(xhr.responseText);
            for (let i = 0; i < rooms.length; i++) {
                var room = '                <div class="col">\n' +
                    '                    <div class="card">\n' +
                    '                        <img src="../assets/img/' + rooms[i].image + '" alt="' + rooms[i].name + '" class="card-img-top" height="250">\n' +
                    '                        <div class="card-body">\n' +
                    '                            <h4 class="card-title">' + rooms[i].name + '</h4>\n' +
                    '                            <p class="card-text">' + rooms[i].slogan + '</p>\n' +
                    '                            <p class="text-muted mb-0">Capacity: ' + rooms[i].capacity + ' | Amount: ' + rooms[i].pricePerHour + '$/h</p>\n' +
                    '                            <a href="#" class="btn auth-btn mt-2 mb-4" onclick="bookRoom(' + rooms[i].roomId + ')">Book Now</a>\n' +
                    '                        </div>\n' +
                    '                    </div>\n' +
                    '                </div>';
                $('.worksCards').append(room);
            }

        } else if (xhr.status !== 200) {
            alert('Servlet Failed!');
        }
    };
    xhr.open('GET', 'http://localhost:8080/351_PROJECT/FetchRooms');
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
}

function bookRoom(roomId){
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            localStorage.setItem('roomToPreview', xhr.responseText);
            window.location.href = 'roomPreview.html';
        } else if (xhr.status !== 200) {
            alert('Servlet Failed!');
        }
    };
    xhr.open('GET', 'http://localhost:8080/351_PROJECT/FetchRoom?roomId='+roomId);
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
}

function loadRoom(){
    if (localStorage.getItem('roomToPreview')) {
        let room = JSON.parse(localStorage.getItem('roomToPreview'));
        $("#roomTitle").text(room.name);
        $("#imgPreview").attr("src", "../assets/img/"+room.image);
        $("#amountPerHour").text(room.pricePerHour+"$");
        $("#location").text(room.location);
        $("#capacity").text(room.capacity);
        $("#description").text(room.description);
        loadReviews(room.roomId);
    }
    if (localStorage.getItem('BookingAlert')){
        $('#addAlertHere').prepend(localStorage.getItem('BookingAlert'));
        localStorage.removeItem('BookingAlert');
    }
}

function loadReviews(roomId){
    let index=0;
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            let reviews = JSON.parse(xhr.responseText);
            for (let i = 0; i < reviews.length; i++) {
                var reviewHtml = `
                  <div class="review${index+1}">
                    <div class="col d-flex">
                      <img src="../ProfileImage?costumerId=${reviews[i].costumerId}" alt="hugenerd" width="50" height="50" class="rounded-circle me-3">
                      <div class="col">
                        <h6>${reviews[i].username}</h6>
                        <h8>Rating: ${reviews[i].rating}</h8>
                      </div>
                    </div>
                    <div class="my-3">
                      <p>${reviews[i].comment}</p>
                    </div>
                  </div><hr>
                `;
                $('#reviews').prepend(reviewHtml);
            }
        } else if (xhr.status !== 200) {
            alert('Servlet Failed!');
        }
    };
    xhr.open('GET', 'http://localhost:8080/351_PROJECT/Reviews?roomId='+roomId);
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
}

function loadSlots(){
    var selectedDate = $('#datepicker').val();
    const dateParts = selectedDate.split('-');
    const formattedDate = dateParts[1] + '/' + dateParts[2] + '/' + dateParts[0];
    var xhr = new XMLHttpRequest();
    let room = JSON.parse(localStorage.getItem('roomToPreview'));
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            // Array of disabled slots
            var disabledSlots = JSON.parse(xhr.responseText);
            // Enable all radio buttons
            $("#radioBtns input[type='radio']").prop("disabled", false);
            // Disable corresponding radio buttons
            for (var i = 0; i < disabledSlots.length; i++) {
                var slotIndex = disabledSlots[i] - 1; // Index starts from 0, slot number starts from 1
                $("#radioBtns input[type='radio']").eq(slotIndex).prop("disabled", true);
            }

        } else if (xhr.status !== 200) {
            alert('Servlet Failed!');
        }
    };
    xhr.open('GET', 'http://localhost:8080/351_PROJECT/FetchSlots?roomId='+room.roomId+'&date='+formattedDate);
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
}

function makeReservation() {
    let costumerId = JSON.parse(localStorage.getItem('user')).costumerId;
    let room = JSON.parse(localStorage.getItem('roomToPreview'));
    var selectedDate = $('#datepicker').val();
    const dateParts = selectedDate.split('-');
    const formattedDate = dateParts[1] + '/' + dateParts[2] + '/' + dateParts[0];
    const slot = document.querySelector('input[name="options"]:checked').value;
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            var successAlert = '<div class="alert alert-success alert-dismissible fade show" role="alert">' +
                '<strong>Successfull booking!</strong> You can check now check your booking history to see the progress of your order.' +
                '<button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>' +
                '</div>';
            localStorage.setItem('BookingAlert',successAlert);
            window.location.reload();

        } else if (xhr.status !== 200) {
            window.location.reload();
            var successAlert = '<div class="alert alert-danger alert-dismissible fade show" role="alert">' +
                '<strong>Booking Failed!</strong> Your balance is not sufficient to make this booking.' +
                '<button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>' +
                '</div>';
            localStorage.setItem('BookingAlert',successAlert);
        }
    };
    let url='http://localhost:8080/351_PROJECT/Book?costumerId='+costumerId+'&date='+
        formattedDate+'&slot='+slot+'&roomId='+room.roomId+'&amount='+room.pricePerHour;
    xhr.open('POST', url);
    xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
    xhr.send();
    window.scrollTo(0, 0);
    return false;
}

function makeReview(){
    let costumerId = JSON.parse(localStorage.getItem('user')).costumerId;
    let room = JSON.parse(localStorage.getItem('roomToPreview'));
    let roomId = room.roomId;
    var comment = document.getElementById('new-review').value;
    var rating = document.querySelector('input[name="rating"]:checked').value;
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            window.location.href = 'roomPreview.html';
        } else if (xhr.status !== 200) {
            var errorAlert = '<div class="alert alert-danger alert-dismissible fade show" role="alert">' +
                '<strong>Review Failed!</strong> You have not used this room yet to be able to make a review for it.' +
                '<button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>' +
                '</div>';
            $('#addAlertHere').prepend(errorAlert);
            window.scrollTo(0, 0);
        }
    };
    let url = 'http://localhost:8080/351_PROJECT/Reviews?costumerId='+costumerId+'&roomId='+roomId+'&comment='+comment+'&rating='+rating;
    xhr.open('POST', url);
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
    return false;
}
