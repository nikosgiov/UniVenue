function showDatabase() {
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            let tables = JSON.parse(xhr.responseText);
            const costomersData = JSON.parse(tables[0]);
            const transactionsData = JSON.parse(tables[1]);
            const reservationsData = JSON.parse(tables[2]);
            const roomsData = JSON.parse(tables[3]);

            const customersTable = generateTable('customers', ['costumerId', 'money', 'firstname', 'lastname', 'username', 'password', 'email', 'gender', 'birthDate', 'telephone'], costomersData);
            const transactionsTable = generateTable('transactions', ['transactionId', 'date', 'amount', 'paymentType'], transactionsData);
            const reservationsTable = generateTable('reservations', ['reservationId', 'roomId', 'transactionId', 'costumerId', 'date', 'slot', 'state'], reservationsData);
            const roomsTable = generateTable('rooms', ['name','roomId', 'capacity', 'image', 'pricePerHour', 'description', 'slogan', 'location'], roomsData);

            $('#content-area').replaceWith(`
            <div class="col py-3 d-flex flex-column justify-content-center align-items-center" id="content-area">
              ${customersTable}
              ${transactionsTable}
              ${reservationsTable}
              ${roomsTable}
            </div>
          `);
        } else if (xhr.status !== 200) {
            alert('Servlet Failed!');
        }
    };
    xhr.open('GET', 'http://localhost:8080/351_PROJECT/AdminTables',false);
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
}

function generateTable(id, headers, data) {
    let tableHeaders = '';
    for (let i = 0; i < headers.length; i++) {
        tableHeaders += `<th scope="col">${headers[i]}</th>`;
    }
    let tableRows = '';
    for (let i = 0; i < data.length; i++) {
        let tableData = '';
        const values = Object.values(data[i]);
        for (let j = 0; j < values.length; j++) {
            tableData += `<td>${values[j]}</td>`;
        }
        tableRows += `<tr>${tableData}</tr>`;
    }
    return `
        <table class="table table-dark" id="${id}">
            <h7>${id}</h7>
            <thead>
                <tr>
                    ${tableHeaders}
                </tr>
            </thead>
            <tbody>
                ${tableRows}
            </tbody>
        </table>
    `;
}

function showWelcome() {
    $('#content-area').replaceWith(`
<div class="col py-3 d-flex flex-column justify-content-center align-items-center" id="content-area">
    <div class="col py-3 d-flex flex-column justify-content-center align-items-center" id="content-area">
        <img src="../assets/img/logo.png" justify-content-start class="univenue" alt="...">
        <div class="row justify-content-center align-items-center flex-grow-1">
          <div class="col-auto text-center my-auto">
            <h1 class="mb-4">Welcome back Admin</h1>
            <p class="text-muted mb-4">It's always a pleasure to have you here with us!</p>
          </div>
        </div>
        <div class="row justify-content-center align-items-end flex-grow-1">
          <div class="col-auto">
            <p class="text-center"></p>
          </div>
        </div>
      </div>
      </div>
</div>
    `);
}

function reservationToHTML(reservation) {
    const slots = ["10:00-13:00","13:00-16:00","16:00-19:00","19:00-22:00","22:00-24:00"];
    const roomName = reservation.name;
    const reservationId = reservation.reservationId;
    const username = reservation.username;
    const fullName = reservation.fullName;
    const date = reservation.date;
    const slot = slots[reservation.slot - 1];
    return `
    <div class="alert alert-primary " role="alert">
      <h4 class="alert-heading">A user made a purchase</h4>
      <p>Username: ${username}</p>
      <p>Fullname: ${fullName}</p>
      <p>Room: ${roomName}</p>
      <p>Date: ${date}</p>
      <p>Timeslot: ${slot}</p>
      <hr>
      <button class="btn auth-btn mt-2 mb-4" onclick="approveReservation(${reservationId})">Approve</button>
    </div>
  `;
}


function showPending(){
    $('#content-area').replaceWith(`
    <div class="col py-3 d-flex flex-column justify-content-center align-items-center" id="content-area">
    </div>
    `);
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            let reservations = JSON.parse(xhr.responseText);
            for (let i = 0; i < reservations.length; i++) {
                $('#content-area').append(reservationToHTML(reservations[i]));
            }
        } else if (xhr.status !== 200) {
            alert('Servlet Failed!');
        }
    };
    xhr.open('GET', 'http://localhost:8080/351_PROJECT/Pending');
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
}

function approveReservation(reservationId){
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            showPending();
        } else if (xhr.status !== 200) {
            alert('Servlet Failed!');
        }
    };
    xhr.open('POST', 'http://localhost:8080/351_PROJECT/Pending?reservationId='+reservationId);
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
}

function loadAdmin(){
    if(!localStorage.getItem('user')){
        window.location.href = '../index.html';
        return;
    }
    let user = JSON.parse(localStorage.getItem("user"));
    if (!user.hasOwnProperty('money') && window.location.href !== 'http://localhost:8080/351_PROJECT/pages/admin.html'
        && window.location.href !== 'http://localhost:8080/351_PROJECT/pages/admin.html#'){
        window.location.href = 'admin.html';
    }
    const imgSrc = `../AdminProfileImage?admin_id=${user.admin_id}`;
    $('#adminAvatar').attr('src', imgSrc);
    $("#adminUsername").text(`${user.username}`);
}