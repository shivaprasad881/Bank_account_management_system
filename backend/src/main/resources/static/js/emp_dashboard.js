
const emp_token = sessionStorage.getItem("emp_token")

console.log(emp_token)


function insigts(){
    //first check whether the employee had enough role or not - if not enogh roel then reject the request

    url = "http://localhost:8080/is_employee_manager?emp_token=" + emp_token

                fetch(url, {
                    method: 'get'
                })
                .then(response => response.text())
                .then(data => {
                    if(data=="true"){
                        //he is a manager - let him allow 
                        window.location.href = "insigts.html"

                    }
                    else{
                        //he is not a manager - reject
                        showToast("Only manager can access it !!");
                        
                    }
                    
                })

                .catch(error => {
                    showToast("Error in validating the 'Employee is Manager' !!");
                });

    
}

function getusers(){
    //fetch the user data - based on the emp role 

    window.location.href = "users_data.html"
    

}

function getemployees(){
    //only when manager clicks this then only allow him - for other employees restrict it

    url = "http://localhost:8080/is_employee_manager?emp_token=" + emp_token

                fetch(url, {
                    method: 'get'
                })
                .then(response => response.text())
                .then(data => {
                    if(data=="true"){
                        // hoo he is a manager - let him see the emp data
                        window.location.href = "employees_data.html"

                    }
                    else{
                        //he is not a manager - reject
                        showToast("Only manager can access it !!");
                        
                    }
                    
                })

                .catch(error => {
                    showToast("Error in validating the 'Employee is Manager' !!");
                });

}

function gettransactions(){
    //first check whether the employee had enough role or not - if not enogh roel then reject the request

    url = "http://localhost:8080/is_employee_manager?emp_token=" + emp_token

                fetch(url, {
                    method: 'get'
                })
                .then(response => response.text())
                .then(data => {
                    if(data=="true"){
                        //he is a manager - let him allow 
                        window.location.href = "transactions_data.html"

                    }
                    else{
                        //he is not a manager - reject
                        showToast("Only manager can access it !!");
                        
                    }
                    
                })

                .catch(error => {
                    showToast("Error in validating the 'Employee is Manager' !!");
                });

    
}


function sendmsg(){
    window.location.href = "sendmsg.html"
}

function fetchnotifications() {
    let url = "http://localhost:8080/fetchnewnotifications?emp_token=" + emp_token;

    fetch(url)
    .then(response => response.json())
    .then(data => {
        let html = "";
        data.forEach(n => {
            let time = new Date(n.createdAt);
            let timeStr = time.toLocaleTimeString('en-IN', { hour: '2-digit', minute: '2-digit', hour12: true });
            html += `<div class="noti-item">
                        <span><strong>${n.sender}:</strong> ${n.message}</span>
                        <span style="font-size: 0.8rem; color: #64748b;">${timeStr}</span>
                    </div>`;
        });
        document.getElementById("notiList").innerHTML = html || "No notifications";
    })
    .catch(() => showToast("Error fetching notifications"));
}


fetchnotifications()


function markAsRead() {
    let url = "http://localhost:8080/make_noti_read?emp_token=" + emp_token;

    fetch(url)
    .then(response => response.text())
    .then(data => {
        showToast(data);
        fetchnotifications()
    })
    .catch(() => showToast("Error making noti read"));
}

function fetchallnoti(){
     window.location.href = "emp_notifications.html"
}