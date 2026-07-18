
const token = sessionStorage.getItem("token")

console.log("dashboard token is : ",token)

// this is the begin for any operation - like landing page - so if we validate token here - we would allow only valid tokens for further operations - only its valid we can allow further operations - further we dont need to fear as it is already valid - reject the invalid onces 



function username(){


    let element = document.getElementById("usermessage");
    if(element != null){
        //only when we are in dashboard - then only fetch for the user name - in other pages we dont need the username

        
        check_token_in_blacklist(token).then(is_valid => {

            if(is_valid){
                // continue next operation - fetch the user name
                
                let url = "http://localhost:8080/user_name?token=" + token;

                fetch(url,{
                    method : 'get'
                })

                .then(response =>{
                    if(response.status!=200){
                        showToast("Invalid token !!")
                        return null;
                    }
                    else{
                        return response.text()
                    }
                })

                .then(data =>{

                    element.innerHTML = `Hello ${data} !!`
                    //when element is null - which indicates we are in a page where we dont need to render the user name - so ignore it

                })

                .catch(error => {
                    showToast("Error in fetching user name !!");
                });


            }
            else{
                // user trying to use the black list token - reject him
                showToast("Unauthorized request!!")
            }

        });
        
    }

 
}//username

username()

function logout(){
    // add the current token to the black list - so that we can reject next time

    // logout only when the user is valid - otherwise we dont need about it

    // valid user - logout , invalid user - ignore him

    // first validate the user then logout functinality

    check_token_in_blacklist(token).then(is_valid => {

        if(is_valid){
            // continue next operation - he is valid user
           

            let url = "http://localhost:8080/new_black_list_token" ;

                let userdata = {
                    token: token,
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
                    // its a valid token - we successfully added it in the black list
                    showToast("Successfully added token in the black_list")

                    //now redirect to login page

                    setTimeout(() => {

                        //in dashboard we would only have token with us , so clear it before logout - so that the session would become fresh as like we entered earlier o this would maintain the state of the page 

                        sessionStorage.removeItem("token");

                        window.location.href = "signin.html";
                    }, 1500);


                })

                .catch(error => {

                    showToast("Error in adding the token to black_list !!");
                });


        }
        else{
            //token in blacklist
            showToast("Unauthorized request!!")
        }

    });


}//logout



function history(){
    check_token_in_blacklist(token).then(is_valid => {

        if(is_valid){
            // continue next operation - he is valid user
            window.location.href = "history.html"
        }
        else{
            showToast("Unauthorized request!!")
        }

    });
    
}

function update_pin_dash(){
    
    
        check_token_in_blacklist(token).then(is_valid => {

            if(is_valid){
                // continue next operation - he is valid user
                window.location.href = "updatepin.html"

                
            }
            else{
                showToast("Unauthorized request!!")
            }

        });
  
}


function update_pass_dash(){
    check_token_in_blacklist(token).then(is_valid => {

            if(is_valid){
                // continue next operation - he is valid user
                window.location.href = "updatepassword.html"

                
            }
            else{
                showToast("Unauthorized request!!")
            }

        });
}

function profile() {

    
        // This waits for the background fetch to complete
        // Then executes the redirect logic


    check_token_in_blacklist(token).then(is_valid => {

        if(is_valid){
            // continue next operation - he is valid user
            window.location.href = "profile.html"
        }
        else{
            showToast("Unauthorized request!!")
        }

    });
}

function check_balance(){

   
        check_token_in_blacklist(token).then(is_valid => {

            if(is_valid){
                
                
                sessionStorage.setItem("action", "checkbalance");

                window.location.href = "pin.html"

            }
            else{
                showToast("Unauthorized request!!")
            }

        });
  
    
    
}

function deposit_dash() {
    check_token_in_blacklist(token).then(is_valid => {

        if(is_valid){
            // continue next operation - he is valid user
            window.location.href = "deposit.html"
        }
        else{
            showToast("Unauthorized request!!")
        }

    });
    
}

function withdrawl_dash() { 
    check_token_in_blacklist(token).then(is_valid => {

        if(is_valid){
            // redirect to withdrawl page
            window.location.href = "withdrawl.html"
        }
        else{
            showToast("Unauthorized request!!")
        }

    });
    
}

function transfer_dash() {
    check_token_in_blacklist(token).then(is_valid => {

        if(is_valid){
            // continue next operation - he is valid user
            window.location.href = "transfer.html"
        }
        else{
            showToast("Unauthorized request!!")
        }

    });
    
}


function check_transfered_amt(){
    //now we would fetch the transfered amt in the last 24 hours by the user and display it

    let url = "http://localhost:8080/check_transfered_amount?token=" + token;

    fetch(url,{
        method : 'get'
    })

    .then(response =>{
        if(response.status!=200){
            showToast("Invalid token !!")
            return null;
        }
        else{
            return response.text()
        }
        })

        .then(data =>{

            showToast(data);
        })


        .catch(error => {
            showToast("Error in fetching user's last 24-hour transfered amount !!");
        });
}   


function forgot_pin(){
    document.getElementById('loader').style.display = 'flex';

            

    check_token_in_blacklist(token).then(is_valid => {

        if(is_valid){
            // continue next operation - he is valid user
            
            sessionStorage.setItem('purpose', 'resetpin')

            send_otp();

            

        }
        else{
            showToast("Unauthorized request!!")
        }

    });
}

function temp(){
    fetch('http://localhost:8080/generate', 
        {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ data: 'hello' })
    })

    .then(response => response.blob())

    .then(blob => {
        const url = URL.createObjectURL(blob);
        window.open(url);  // Opens QR image in new tab
    })

    .catch(error => console.error('Error:', error));
}

let scanner;

function startScanner() {
    if (scanner) {
        return;
    }

    // Show overlay
    document.getElementById('scanner-overlay').style.display = 'flex';

    scanner = new Html5Qrcode("reader");
    scanner.start(
        { facingMode: "environment" },
        { fps: 10, qrbox: { width: 250, height: 250 } },
        (decodedText) => {
            // Store the result (no alert, no display)
            
            transferr(decodedText)


            
            stopScanner();
        },
        (error) => {}
    ).catch(err => {
        console.error(err);
        stopScanner();
    });
}

function stopScanner() {
    if (scanner) {
        scanner.stop().then(() => {
            scanner = null;
            document.getElementById('scanner-overlay').style.display = 'none';
        }).catch(err => console.error(err));
    } else {
        document.getElementById('scanner-overlay').style.display = 'none';
    }
}