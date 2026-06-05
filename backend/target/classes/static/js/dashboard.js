const params = new URLSearchParams(window.location.search);
const token = params.get("token");

// this is the begin for any operation - like landing page - so if we validate token here - we would allow only valid tokens for further operations - only its valid we can allow further operations - further we dont need to fear as it is already valid - reject the invalid onces 



function username(){


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
        

        let element = document.getElementById("usermessage");
        if(element != null){
            element.innerHTML = `Hello ${data} !!`
        }
        //when element is null - which indicates we are in a page where we dont need to render the user name - so ignore it

    })

    .catch(error => {
        showToast("Error in fetching user name !!");
    });

    
}

username()

function logout(){
    // add the current token to the black list - so that we can reject next time
    console.log("Token from URL:", token); 

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
                showToast("Invalid token - no need to add to black list !!")
            }
            else{
                return response.text();
            }
        })
        .then(data => {
            // its a valid token - we successfully added it in the black list
            showToast("Successfully added token in the black_list",1500)

            //now redirect to login page

            // setTimeout(() => {
            //     window.location.href = "signin.html";
            // }, 1500);


        })

        .catch(error => {
            showToast("Error in adding the token to black_list !!");
        });



}

function history(){
    window.location.href = "history.html?token=" + token;
}

function update_pin_dash(redirect_page,passed_token){
    
    
 

    if(redirect_page){
        // first validate the token - if valid allow - if invalid reject

        let url = "http://localhost:8080/validate_user_token?token=" + token;

        fetch(url, {
            method: 'GET'
        })
        .then(response => {
            if(response.status == 200) {
                return response.text();
            } else {
                //its an expired token
                showToast("Invalid token !!");
            }
        })
        .then(data => { 
            //it is live token - now we would look whether it is valid or not

            if(data=="reject"){
                //hoo the user is using the expired_jwt_token - reject him
                showToast("token in the black_list  - u are rejected  !!")

                // through him in the login page

                setTimeout(() => {

                    window.location.href = "signin.html"
                    
                }, 1500);

            }
            else{
                
                showToast("valid token - u can proceed !!")

                //now we would allow the user for next operations


                setTimeout(() => {

                    let action = "updatepin"
                    window.location.href = "pin.html?token=" + token + "&action=" + action;
                    
                }, 1500);

                


            }

        })

        .catch(error => {
            showToast("Error in validating the user_token with black_list !!");
        });




        
    }
    else{
        window.location.href = "updatepin.html?token=" + passed_token;
    }


}

function profile() {
    window.location.href = "profile.html?token=" + token;
}

function check_balance(redirect_page,passed_token){


    if(redirect_page){
        let action = "checkbalance"
       
        window.location.href = "pin.html?token="+ token + "&action=" + action;
    }

    url = "http://localhost:8080/check_balance?token=" + passed_token;

    fetch(url, {
        method: 'get'
    })
    .then(response =>{
        if(response.status!=200){
            //invalid token
            showToast("Invalid token !!")
        }
        else{
            //valid token - send the response to the next level - then would handle the response
            return response.text()
        }
    })
    .then(data => {
        showToast(data,2000);

        //after showing balance on the pin page - we need to redirect ot the dashboard - as the user sees the balance only once
        setTimeout(() => {
           window.location.href = "dashboard.html?token=" + token;
        }, 2000);


    })
    .catch(error => {
        showToast("Error in fetching the balance !!");
    });
}

function deposit_dash() {
    window.location.href = "deposit.html?token=" + token;
}

function withdrawl_dash() {
    window.location.href = "withdrawl.html?token=" + token;
}

function transfer_dash() {
    window.location.href = "transfer.html?token=" + token;
}