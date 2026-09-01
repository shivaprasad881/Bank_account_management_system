const emp_token = sessionStorage.getItem("emp_token")

console.log(emp_token)

function send(){
    let type = document.getElementById('recipientType').value;
    let msg = document.getElementById("message").value;

     if (type === 'employeeid') {
        let empid = document.getElementById('empid').value;

        let url = `${API_BASE_URL}/send_msg?emp_token=${emp_token}&tar_empid=${empid}&msg=${msg}`;


                fetch(url, {
                    method: 'get'
                })
                .then(response => response.text())
                .then(data => {
                    handleResponse(data);
                })

                .catch(error => {
                    showToast("Error in sending msg !!");
                });

    }
    else {
       
        //send to specified category of employees
        let url = `${API_BASE_URL}/boardcast_msg?emp_token=${emp_token}&tar_dept=${type}&msg=${msg}`;


                fetch(url, {
                    method: 'get'
                })
                .then(response => response.text())
                .then(data => {
                    handleResponse(data);
                })

                .catch(error => {
                    showToast("Error in sending msg !!");
                });


    }

}
function handleResponse(data) {
    if(data == "success") {
        showToast("Message sent successfully !!");
        setTimeout(() => {
            window.location.href = "emp_dashboard.html";
        }, 1500);
    } else if(data == "msg_too_long") {
        showToast("Message exceeds 250 characters !!");
    } else if(data == "sensitive_data") {
        showToast("Message contains sensitive data !!");
    } else if(data == "emp_not_found") {
        showToast("Employee not found !!");
    } else if(data == "self_msg") {
        showToast("Cannot send message to yourself !!");
    } else if(data == "not_manager") {
        showToast("Only manager can broadcast messages !!");
    } else if(data == "invalid_token") {
        showToast("Session expired, please login again !!");
    }
}

function toggleInput() {
    let type = document.getElementById('recipientType').value;
    let inputDiv = document.getElementById('employeeIdInput');

    if (type === 'employeeid') {
        inputDiv.style.display = 'block';
    } else {
        inputDiv.style.display = 'none';
        document.getElementById('empid').value = '';
    }
}
toggleInput() 