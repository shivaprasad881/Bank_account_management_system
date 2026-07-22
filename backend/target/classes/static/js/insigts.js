//first check whether manager or not - if manager then call all the relavant functions - ohterise reject

const emp_token = sessionStorage.getItem("emp_token")

console.log(emp_token)




function bank_balance(){
    url = "http://localhost:8080/total_bank_balance"

                fetch(url, {
                    method: 'get'
                })
                .then(response => response.text())
                .then(data => {
                    
                    document.getElementById('bankBalance').innerText = data
                })

                .catch(error => {
                    showToast("Error in fetching the bank-balance !!");
                });

}


function bank_transactions(){
    url = "http://localhost:8080/bank_transactions"

                fetch(url, {
                    method: 'get'
                })
                .then(response => response.text())
                .then(data => {

                    let arr = data.split(" ");

                    let outgoing_amt = arr[0];

                    let incoming_amt = arr[1];

                    let total_amt = arr[2];

                    document.getElementById("totalTransactions").innerHTML = total_amt;
                    document.getElementById("outgoing").innerHTML = outgoing_amt;
                    document.getElementById("incoming").innerHTML = incoming_amt;

                    
                    
                })

                .catch(error => {
                    showToast("Error in fetching the bank_transactions !!");
                });

}


function users_count_based_on_account_status(){
    url = "http://localhost:8080/users_count_based_on_account_status"

                fetch(url, {
                    method: 'get'
                })
                .then(response => response.text())
                .then(data => {

                    let arr = data.split(" ");

                    let active = arr[0];

                    let inactive = arr[1];

                    let total_acc = arr[2];

                    document.getElementById("totalAccounts").innerHTML = total_acc;
                    document.getElementById("active").innerHTML = active;
                    document.getElementById("inactive").innerHTML = inactive;

                    
                    
                })

                .catch(error => {
                    showToast("Error in fetching the Noof users !!");
                });
}


function newusers(){
    url = "http://localhost:8080/new_users"

                fetch(url, {
                    method: 'get'
                })
                .then(response => response.text())
                .then(data => {
                    
                    document.getElementById('newUsers').innerText = data
                })

                .catch(error => {
                    showToast("Error in fetching new-users !!");
                });

}




function ismanager(){
    //first check whether the employee had enough role or not - if not enogh roel then reject the request

    url = "http://localhost:8080/is_employee_manager?emp_token=" + emp_token

                fetch(url, {
                    method: 'get'
                })
                .then(response => response.text())
                .then(data => {
                    if(data=="true"){
                        document.body.style.display = "block";
                        console.log(data);
                        //hoo he is a manager - let him see the insiringts
                        bank_balance()
                        bank_transactions()
                        users_count_based_on_account_status()
                        newusers()
                    }
                    else{
                        //hoo he is not a namanger - rejecet the employee
                        showToast("Access Denied !!");
                        setTimeout(() => {
                            window.location.href = "emp_dashboard.html?token=" + emp_token;
                        }, 1500);
                        
                    }
                    
                })

                .catch(error => {
                    showToast("Error in validating the 'Employee is Manager' !!");
                });

    
}

ismanager()
