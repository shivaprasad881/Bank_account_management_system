
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