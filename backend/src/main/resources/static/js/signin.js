function user_signin() {

    let usertype = document.getElementById("usertype").value;

    if(usertype=="customer"){

        let identity = document.getElementById("user_identity").value;
        let userpass = document.getElementById("password").value;

        identity = identity.trim();
        userpass = userpass.trim();

        let identity_type;
        
        if(identity == "" || userpass == "") {
            showToast("please fill the details");
        }
        else if( identity.length == 10  && isNaN(identity)==false     ){
            //phonenumber
            identity_type = "phonenumber"
            login(identity,identity_type,userpass)
        }
        else if( identity.length == 11  && identity.substring(0,3)=="ACC" &&  isNaN(identity.substring(3))==false  ){
            //accno
            identity_type = "account"
            login(identity,identity_type,userpass)
        }
        else {
            showToast("Please enter valid 10-digit phone number or ACCXXXXXXXXX account number");
        }

    }
    else{

        let empid = document.getElementById("user_identity").value;
        let emppass = document.getElementById("password").value;

        empid = empid.trim();
        emppass = emppass.trim();

        if(empid == "" || emppass == "") {
            showToast("please fill the details");
        }
        else if( empid.length == 11  && empid.substring(0,3)=="EMP" &&  isNaN(empid.substring(3))==false    ){
           
            login2(empid,emppass);
        }
        else{
            showToast("Please enter valid 10-digit Employee-ID");
        }
        

    }

    


}

function login2(empid,emppass){

    let url = `${API_BASE_URL}/validate_emp?empid=${empid}&password=${emppass}`;

                fetch(url, {
                    method: 'get'
                })
                .then(response => response.text())
                .then(data => {
                    if(data == "false") {

                        showToast("Invalid credentials !!");

                    }
                    else{
                        
                        showToast("successful login !!",1500);

                        setTimeout(() => {
                                sessionStorage.setItem("emp_token", data);
                                
                                window.location.href = "emp_dashboard.html"
                        }, 1500);

                    }
                
                })

                .catch(error => {
                    showToast("Error in validating the Employee credencials !!");
                });




}


function login(identity,identity_type,userpass){
        //consider user entered valid accno

        // check if he had attempts left - then only validate the user - otherwise reject
        let url = `${API_BASE_URL}/failure_authentication?identity=${identity}&identity_type=${identity_type}`

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

                url = `${API_BASE_URL}/validate_user?identity=${identity}&identity_type=${identity_type}&password=${userpass}`;

                fetch(url, {
                    method: 'get'
                })
                .then(response => response.text())
                .then(data => {
                    if(data == "false") {

                        showToast("Invalid credentials !!");

                        //hoo attempt failed - increment the count

                        url = `${API_BASE_URL}/failure_authentication`;

                        
                        let userdata = {
                            identity : identity,
                            identity_type : identity_type
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

                        url = `${API_BASE_URL}/reset_failure_attempts`;

                        
                        let userdata = {
                            identity : identity,
                            identity_type : identity_type
                        };
                        
                        fetch(url, {
                            method: 'PATCH',
                            headers: {
                                'Content-Type': 'application/json'
                            },
                            body: JSON.stringify(userdata)
                        })


                        .then(response =>{
                           
                            setTimeout(() => {
                                sessionStorage.setItem("token", data);
                                
                                window.location.href = "dashboard.html"
                            }, 1500);

                        })



                        .catch(error => {
                            showToast("Error in reseting the failure count !!");
                        });

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