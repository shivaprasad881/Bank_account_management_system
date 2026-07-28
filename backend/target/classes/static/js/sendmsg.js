const emp_token = sessionStorage.getItem("emp_token")

console.log(emp_token)

function send(){
    let empid = document.getElementById("empid").value;
    let msg = document.getElementById("text").value;

    //now send msg to empid
    
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