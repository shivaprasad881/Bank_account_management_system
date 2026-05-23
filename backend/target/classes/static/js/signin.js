function user_signin() {
    let useracc = document.getElementById("user_acc").value;
    let userpass = document.getElementById("password").value;
    
    if(useracc == "" || userpass == "" ) {
        showToast("please fill the details");
    }
    else {
        let url = "http://localhost:8080/validate_user?accno=" + useracc + "&password=" + userpass;

        fetch(url, {
            method: 'get'
        })
        .then(response => response.text())
        .then(data => {
            if(data == "false") {

                showToast("Invalid credentials !!");
                
            } else {
                
               
                showToast("successful login !!",1000);


                // logged in sucessfully - hoo we got a jwt token for our successfull login

                //now where should i store this jwt token - so that i would send for every request
                
                
                setTimeout(() => {
                    window.location.href = "dashboard.html?token=" + data;
                }, 1000);

            }
        })
        .catch(error => {
            showToast("Error in validating the user credencials !!");
        });
    }
}
