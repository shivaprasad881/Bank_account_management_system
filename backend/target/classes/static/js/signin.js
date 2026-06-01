function user_signin() {
    let useracc = document.getElementById("user_acc").value;
    let userpass = document.getElementById("password").value;
    
    if(useracc == "" || userpass == "" || useracc.length!=11 || "ACC" != useracc.substring(0,3)) {
        showToast("please fill the valid details");
    }
    else {
        //consider user entered valid accno

        // check if he had attempts left - then only validate the user - otherwise reject
        let url = "http://localhost:8080/failure_authentication?accno=" + useracc

        fetch(url, {
            method: 'get'
        })
        .then(response => response.text())
        .then(data => {
            if(data.length>0){

                showToast(data,2000)
                      
            }
            else{
                //he had enough attempts - validate user - if failure - then increment the count

                url = "http://localhost:8080/validate_user?accno=" + useracc + "&password=" + userpass;

                fetch(url, {
                    method: 'get'
                })
                .then(response => response.text())
                .then(data => {
                    if(data == "false") {

                        showToast("Invalid credentials !!",1500);

                        //hoo attempt failed - increment the count

                        url = "http://localhost:8080/failure_authentication";

                        
                        let userdata = {
                            accno : useracc
                        };
                        
                        fetch(url, {
                            method: 'PATCH',
                            headers: {
                                'Content-Type': 'application/json'
                            },
                            body: JSON.stringify(userdata)
                        })
                        .then(response =>{})

                        .catch(error => {
                            showToast("Error in incrementing the failure count !!");
                        });


                        
                    } else {
                        
                    
                        showToast("successful login !!",1500);

                        //reset the failure attempts to 0 - as user logged in 

                        url = "http://localhost:8080/reset_failure_attempts";

                        
                        let userdata = {
                            accno : useracc
                        };
                        
                        fetch(url, {
                            method: 'PATCH',
                            headers: {
                                'Content-Type': 'application/json'
                            },
                            body: JSON.stringify(userdata)
                        })
                        .then(response =>{
                           
                        })

                        .catch(error => {
                            showToast("Error in reseting the failure count !!");
                        });

                        
                        setTimeout(() => {
                            window.location.href = "dashboard.html?token=" + data;
                        }, 1500);

                    }
                })
                .catch(error => {
                    showToast("Error in validating the user credencials !!");
                });


            }//else

        })

        .catch(error => {
            showToast("Error in Fetching the failure count !!");
        });

    }
}
