const emp_token = sessionStorage.getItem("emp_token")

console.log(emp_token)

function send(){
    let type = document.getElementById('recipientType').value;
    let msg = document.getElementById("message").value;

     if (type === 'employeeid') {
        let empid = document.getElementById('empid').value;

        let url = "http://localhost:8080/send_msg?emp_token=" + emp_token + "&tar_empid=" + empid + "&msg=" + msg ;


                fetch(url, {
                    method: 'get'
                })
                .then(response => response.text())
                .then(data => {

                    if(data=="true"){
                        showToast("msg sent successfull !!")

                        setTimeout(() => {
            
                            window.location.href = "emp_dashboard.html"
                        }, 1500);
                    }
                    else{
                        showToast("Error in sending msg !!");
                    }
                    
                    

                })

                .catch(error => {
                    showToast("Error in sending msg !!");
                });

    }
    else {
       
        //send to specified category of employees
        let url = "http://localhost:8080/boardcast_msg?emp_token=" + emp_token + "&tar_dept=" + type + "&msg=" + msg ;


                fetch(url, {
                    method: 'get'
                })
                .then(response => response.text())
                .then(data => {

                    if(data=="true"){
                        showToast("msg sent successfull !!")

                        setTimeout(() => {
            
                            window.location.href = "emp_dashboard.html"
                        }, 1500);
                    }
                    else{
                        showToast("Only manager can boardcast the messages !!");
                    }
                    
                    

                })

                .catch(error => {
                    showToast("Error in sending msg !!");
                });


    }

    //now send msg to empid
    
    



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