

function verifyotp1(){
    let userotp = document.getElementById('userotp').value;

    let useremail = sessionStorage.getItem('email');

    let url = `${API_BASE_URL}/verify_otp`;

        let userdata = {
            email : useremail,
            otp:userotp
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
                
                //based on purponse - redirect to approapte page


                setTimeout(() => {
                    let purpose = sessionStorage.getItem('purpose');

                    sessionStorage.removeItem('purpose');


                    if(purpose=="resetpassword"){
                          // Clear after use
                        //as the purpose is reseting the password - next page is resetpassword page
                        window.location.href = "resetpassword.html";
                    }
                    else if(purpose=="resetpin"){
                        
                        
                        window.location.href = "resetpin.html";
                    }
                    else if(purpose=="registration"){
                        
                        
                        //as the purpose is registration - next page is login page
                        register();
                    }
                    else{
                        showToast("'purpose' is none of the above !! (verifyotp.js) ")
                    }
                
                }, 1500)
                
            
            }
            else{
                showToast("invalid otp / expired otp / no otp ")
            }
        })

        .catch(error => {
            showToast("error in verifying otp");
        });



    
}


