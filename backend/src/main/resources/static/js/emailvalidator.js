

function send_otp_forgot(){
    let useremail = document.getElementById('useremail').value;

    sendotp(useremail);

}

function verify_otp_forgot(){
    let userotp = document.getElementById('userotp').value;

    if(verifyotp(userotp)){
        //valid otp
        setTimeout(() => {
            let purpose = sessionStorage.getItem('purpose');


            if(purpose=="resetpassword"){
                sessionStorage.removeItem('purpose');  // Clear after use
                //as the purpose is reseting the password - next page is resetpassword page
                 window.location.href = "resetpassword.html";
            }
            else if(purpose=="registration"){
                sessionStorage.removeItem('purpose');
                
                //as the purpose is registration - next page is login page
                register();
            }
            else{
                showToast("'purpose' is none of the above !!")
            }
           
        }, 1500)
    }
}


