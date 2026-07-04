
function withdrawl(passed_token,passed_amount){

        url = "http://localhost:8080/withdrawl";

        let userdata = {
            token : passed_token,
            amount: passed_amount
        };

        fetch(url, {
            method: 'PATCH',
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(userdata)
        })
        .then(response => {
            if(response.status!=200){
                
                showToast("Unauthorized request!!")

            }
            else{
                return response.text();
            }
        })
        .then(data => {
            showToast(data);

            setTimeout(() => {
                //clear the extra data in the session before going back to dash
                sessionStorage.removeItem("action");
                sessionStorage.removeItem("amount");
                

                window.location.href = "dashboard.html"
            }, 1500);

        })
        .catch(error => {
            showToast("error in withdrawling amount !!");
        });

}

function deposit(passed_token,passed_amount){

     url = "http://localhost:8080/deposit" ;

        let userdata = {
            token: passed_token,
            amount: passed_amount
        };

        fetch(url, {
            method: 'PATCH',
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(userdata)
        })
        .then(response => {
            if(response.status!=200){
                showToast("Unauthorized request!!")
            }
            else{
                return response.text();
            }
        })
        .then(data => {
            showToast(data);

            setTimeout(() => {
                //clear extra data - go back to dash with its state
                sessionStorage.removeItem("action");
                sessionStorage.removeItem("amount");

                window.location.href = "dashboard.html"
            }, 1500);

        })
        .catch(error => {
            //console.log("deposit error: " + error)
            showToast("error in depositing amount !!");
        });

}


function transfer(target,target_type,tar_amt,token_passed){

    let url = "http://localhost:8080/transfer";

        let userdata2 = {
            token: token_passed,
            target: target,
            target_type:target_type,
            amount: tar_amt
        };

        fetch(url, {
            method: 'PATCH',
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(userdata2)
        })
        
        .then(response =>{
            if(response.status!=200){
                showToast("Unauthorized request!!")
            }
            else{
                return response.text()
            }
        })
        .then(data => {
            //transfer succesfull
            
            showToast(data)
            
            setTimeout(() => {

                sessionStorage.removeItem("action");
                sessionStorage.removeItem("amount");
                sessionStorage.removeItem("target");
                sessionStorage.removeItem("target_type");


                window.location.href = "dashboard.html"
            }, 1500);

        })

        .catch(error => {
            showToast("Error in money transfer  !!");
        }); 


}


function update_pin(token,newpin){

                url = "http://localhost:8080/updatepin";
                
                let userdata = {
                    token : token,
                    newpin: newpin
                };
                
                fetch(url, {
                    method: 'PATCH',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(userdata)
                })
                .then(response =>{
                    if(response.status!=200){
                        showToast("Unauthorized request!!")
                    }
                    else{
                        return response.text();

                    }
                })
                .then(data => {
                    showToast(data);

                    setTimeout(() => {

                        sessionStorage.removeItem("action");
                        sessionStorage.removeItem("newpin");

                        window.location.href = "dashboard.html"
                    }, 1500);
                })
                .catch(error => {
                    showToast("error in updating the pin !!");
                });

}

function check_balance(token){


     url = "http://localhost:8080/check_balance?token=" + token;

        fetch(url, {
            method: 'get'
        })
        .then(response =>{
            if(response.status!=200){
                //invalid token
                showToast("Unauthorized request!!")
            }
            else{
                //valid token - send the response to the next level - then would handle the response
                return response.text()
            }
        })
        .then(data => {
            showToast(data);

            //after showing balance on the pin page - we need to redirect ot the dashboard - as the user sees the balance only once
            setTimeout(() => {

                sessionStorage.removeItem("action");
                

            window.location.href = "dashboard.html"
            }, 1500);


        })
        .catch(error => {
            showToast("Error in fetching the balance !!");
        });


}


function register(){
    let uname = sessionStorage.getItem('uname');
    let age = sessionStorage.getItem('age');
    let city = sessionStorage.getItem('city');
    let phonenumber = sessionStorage.getItem('phonenumber');
    let email = sessionStorage.getItem('email');
    let password = sessionStorage.getItem('password');

    let url = "http://localhost:8080/register";

        let userData = {
            uname: uname, 
            age: age,
            city: city,
            phonenumber: phonenumber,
            email : email,
            password : password
        };

        let status_code;

        fetch(url, {
            method: 'post',
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(userData)
        })


        .then(response => {
            if(response.status == 200) {
                return response.text();
            } else {
                showToast("Unauthorized request !!")
            }
        })
        .then(data => {

            let userdata2 = data.split(",");
            
            
            showToast("Registration successfull !!",3500);
                

            let message = "Account Number : "+userdata2[0]+"\nPin : "+userdata2[1] +"\n\n*** Please update the pin after login ***"



            url = "http://localhost:8080/send_email";
                        
            let sendingdata = {
                email : email,
                subject : "Your Registration successfull !!",
                message: message,
                verifyemail: "false"
            };
                        
            fetch(url, {
                method: 'PATCH',
                headers: {
                'Content-Type': 'application/json'
                },
                body: JSON.stringify(sendingdata)
            })

            .then(response => {
                if(response.status == 200) {
                    return response.text();
                } else {
                    showToast("Unauthorized request !!")
                }
            })
            
            .then(data => {
                    //always true - because registration is completed successfully - we should not stop the notification to user

                    setTimeout(() => { showToast("Details mailed successfully !!");

                    },3500)

                    setTimeout(() => {

                        sessionStorage.removeItem('uname');
                        sessionStorage.removeItem('age');
                        sessionStorage.removeItem('city');
                        sessionStorage.removeItem('phonenumber');
                        sessionStorage.removeItem('email');
                        sessionStorage.removeItem('password');

                        window.location.href = "signin.html";
                    }, 5000)
               
            })

            .catch(error => {
                showToast("error in sending the email after Registration !!");
            });


            
            

        })
        .catch(error => {
            console.log("Error details:", error);
            console.log("Error message:", error.message);
            console.log("Error stack:", error.stack);
            showToast("Error in registration(all_functions): " + error.message);
        });
}
