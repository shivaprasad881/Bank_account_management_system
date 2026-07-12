const tokenn = sessionStorage.getItem("token")

function send_otp(){

    


    
        let element = document.getElementById('useremail');

        let useremail;
        
        if (element!=null) {
            useremail = element.value;
        }
       
        
    
    
    let purpose = sessionStorage.getItem('purpose');




    
    if(purpose=="registration"){
        sessionStorage.setItem("email", useremail);
        //no verification - just send otp

        let url = "http://localhost:8080/send_otp_no_verification";

        let userdata = {
            email : useremail 
        }

        fetch(url, {
            method: 'PATCH',
            headers: {
            'Content-Type': 'application/json'
            },
            body: JSON.stringify(userdata)
        })

        .then(response => response.text())  

        .then(data => {
            if(data==="true"){
                
                showToast("OTP sent successfully !!");

                setTimeout(() => {
                    window.location.href = "verifyotp.html";
                }, 1500);
            
            }
            else{
                showToast("backend sent other than 'true' ")
            }
        })

        .catch(error => {
            showToast("error in sending the otp !!");
        });


    }
    else if(purpose=="resetpassword"){
        sessionStorage.setItem("email", useremail);
        //verify the existance of the email in the database - send otp

        
        let url = "http://localhost:8080/verify_email_send_otp";

        let userdata = {
            email : useremail
        }

        fetch(url, {
            method: 'PATCH',
            headers: {
            'Content-Type': 'application/json'
            },
            body: JSON.stringify(userdata)
        })

        .then(response => response.text())  

        .then(data => {
            if(data==="true"){
                
                showToast("OTP sent successfully !!");

                setTimeout(() => {
                    window.location.href = "verifyotp.html";
                }, 1500);
            
            }
            else{
                showToast("Email not existing in the database !!!")
            }
        })

        .catch(error => {
            showToast("error in sending the otp !!");
        });



    }
    else if(purpose=="resetpin"){
        //valid the user token - send otp

         
        let url = "http://localhost:8080/verify_user_send_otp";

        let userdata = {
            token:tokenn
        }

        fetch(url, {
            method: 'PATCH',
            headers: {
            'Content-Type': 'application/json'
            },
            body: JSON.stringify(userdata)
        })

        .then(response => {
            if (response.status == 200) {
                return response.text();
            } else {
                throw new Error("Unauthorized");
            }
        }) 

        .then(data => {
            
            sessionStorage.setItem("email", data);

            showToast("OTP sent successfully !!");

            setTimeout(() => {
                window.location.href = "verifyotp.html";
            }, 1500);
            
           
        })

        .catch(error => {
            
            console.log(error.message);
            showToast("Unauthorized request !!");
        });
        

    }
    else{
        showToast(" purpose is none of the above (emailvalidator.js)  ")
    }
    

    

}




