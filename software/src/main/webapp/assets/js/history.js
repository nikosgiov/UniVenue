function fetchHistory(){
    let costumerId = JSON.parse(localStorage.getItem('user')).costumerId;
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            let reservation = JSON.parse(xhr.responseText);
            for (let i = 0; i < reservation.length; i++) {
                const slots = ["10:00-13:00","13:00-16:00","16:00-19:00","19:00-22:00","22:00-24:00"];
                let timeslot = slots[reservation[i].slot - 1];
                let progress = reservation[i].state === "COMPLETED" ? "95" : "50";
                let removeButton = reservation[i].state === "COMPLETED" ? "" : `<a href="#" onclick="cancelReservation(${reservation[i].reservationId})"><img src="../assets/img/x.svg" alt="Price" width="24" height="24"></a>`;
                let html = `<div class="card shadow-0 border mb-4">
                              <div class="card-body">
                                <div class="row">
                                  <div class="col-md-2">
                                    <img src="../assets/img/${reservation[i].image}" class="img-fluid rounded" alt="Room">
                                  </div>
                                  <div class="col-md-2 text-center d-flex justify-content-center align-items-center">
                                    <p class="text-muted mb-0">${reservation[i].roomName}</p>
                                  </div>
                                  <div class="col-md-2 text-center d-flex justify-content-center align-items-center">
                                    <p class="text-muted mb-0 small">Purchased: ${reservation[i].transactionDate}</p>
                                  </div>
                                  <div class="col-md-2 text-center d-flex justify-content-center align-items-center">
                                    <p class="text-muted mb-0 small">Date: ${reservation[i].reservationDate}</p>
                                  </div>
                                  <div class="col-md-2 text-center d-flex justify-content-center align-items-center">
                                    <p class="text-muted mb-0 small">Timeslot: ${timeslot}</p>
                                  </div>
                                  <div class="col-md-2 text-center d-flex justify-content-center align-items-center">
                                    <p class="text-muted mb-0 small">$${reservation[i].amount} (${reservation[i].paymentType})</p>
                                  </div>
                                  <div class="col-md-2 text-center d-flex justify-content-center align-items-center">
                                    ${removeButton}
                                  </div>                                    
                                </div>
                                <hr class="mb-4" style="background-color: black; opacity: 1;">
                                <div class="row d-flex align-items-center">
                                  <div class="col-md-2">
                                    <p class="text-muted mb-0 small">Track Order: UVT${reservation[i].reservationId}</p>
                                  </div>
                                  <div class="col-md-10">
                                    <div class="progress" style="height: 6px; border-radius: 16px;">
                                      <div class="progress-bar" role="progressbar" style="width: ${progress}%; border-radius: 16px; background-color: #66fcf1;" aria-valuenow="${progress}" aria-valuemin="0" aria-valuemax="100"></div>
                                    </div>
                                    <div class="d-flex justify-content-around mb-1">
                                      <p class="text-muted mt-1 mb-0 small ms-xl-5">Pending</p>
                                      <p class="text-muted mt-1 mb-0 small ms-xl-5">Completed</p>
                                    </div>
                                  </div>
                                </div>
                              </div>
                            </div>`;
                $('#history').append(html);
            }
        } else if (xhr.status !== 200) {
            alert('Servlet Failed!');
        }
    };
    xhr.open('GET', 'http://localhost:8080/351_PROJECT/Reservations?costumerId='+costumerId);
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
}

function cancelReservation(reservationId){
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            window.location.reload(); // reload the page
        } else if (xhr.status !== 200) {
            alert('Servlet Failed!');
        }
    };
    xhr.open('POST', 'http://localhost:8080/351_PROJECT/Reservations?reservationId='+reservationId);
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
}