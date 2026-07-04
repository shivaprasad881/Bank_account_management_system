function sendotp(useremail){
    
    let purpose = sessionStorage.getItem('purpose');

    let generated_otp = Math.floor(1000 + Math.random() * 9000);

    let otp_expiry_time = Date.now() + 120*1000;

    sessionStorage.setItem("email", useremail);
    sessionStorage.setItem("generated_otp", generated_otp);
    sessionStorage.setItem("otp_expiry_time", otp_expiry_time);


    let message = "Your OTP is : "+ generated_otp+"\n\nPlease dont share the OTP !!"

    

    let url = "http://localhost:8080/send_email";

    let userdata;


            if(purpose=="resetpassword"){
                userdata = {
                    email : useremail,
                    subject : "Your OTP for Password-Reset",
                    message: message,
                    verifyemail : "true"
                    
                };
            }
            else if(purpose=="registration"){
                userdata = {
                    email : useremail,
                    subject : "Your OTP for Password-Reset",
                    message: message,
                    verifyemail : "false"
                    
                };
            }
            else{
                showToast("'purpose' is none of the above !!")
                
            }
                
    
                
    return fetch(url, {
        method: 'PATCH',
        headers: {
        'Content-Type': 'application/json'
        },
        body: JSON.stringify(userdata)
    })

    .then(response => {
        if(response.status == 200) {
            return response.text();
        } else {
            showToast("Unauthorized request !!")
        }
    })

    .then(data => {
        if(data=="true"){
            showToast("OTP sent successfully !!");
        }
        else if(data=="false"){
            showToast("Email not registered*************** !!");
        }
        else{
            showToast("Other than true/false response from backend - sendotp() ");
        }
        
    })

    .catch(error => {
        showToast("error in sending the otp !!");
    });

}

function verifyotp(userotp){

    // let userotp = document.getElementById('userotp').value;

    const generated_otp = sessionStorage.getItem("generated_otp")

    //check whether the otp is expiried or not
    let otp_expiry_time = sessionStorage.getItem("otp_expiry_time")

    if(Date.now()<otp_expiry_time){
        //not yet expired - let the user continue
         if(userotp==generated_otp){
            return true;
         }
         else{
            showToast("Invalid OTP !!")
            return false;
         }
    }
    else{
        showToast("OTP expired !!")
        return false;
    }

}