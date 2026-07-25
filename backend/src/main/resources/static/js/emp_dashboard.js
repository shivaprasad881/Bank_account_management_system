
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

function fetchnotifications(){

    url = "http://localhost:8080/fetchnotifications?emp_token=" + emp_token

                fetch(url, {
                    method: 'get'
                })
                .then(response => response.text())
                .then(data => {
                    document.getElementById("notibox").innerHTML = data;
                    
                })

                .catch(error => {
                    showToast("Error in fetching notifications !!");
                });


}


fetchnotifications()
