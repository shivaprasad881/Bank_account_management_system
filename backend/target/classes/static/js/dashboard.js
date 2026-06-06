const params = new URLSearchParams(window.location.search);
const token = params.get("token");

// this is the begin for any operation - like landing page - so if we validate token here - we would allow only valid tokens for further operations - only its valid we can allow further operations - further we dont need to fear as it is already valid - reject the invalid onces 



function username(){


    let element = document.getElementById("usermessage");
    if(element != null){
        //only when we are in dashboard - then only fetch for the user name - in other pages we dont need the username

        
        check_token(token).then(is_valid => {

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
                showToast("It's not a valid token !!")
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

    check_token(token).then(is_valid => {

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
        else{
            showToast("It's not a valid token !!")
        }

    });


}//logout

function check_token(tokenn){

    let url = "http://localhost:8080/validate_user_token?token=" + tokenn;

        return fetch(url, {
            method: 'GET'
        })
        .then(response => {
            if(response.status == 200) {
                return response.text();
            } else {
                //its an expired token
                //console.log("its not a valid token - caused for rejection")
                return "reject";
            }
        })
        .then(data => { 
            //it is live token - now we would look whether it is valid or not

            if(data=="reject"){

                //console.log("the token is present in the blacklist - caused for rejection")

                return false;


            }
            else{
                //only when

                return true;
            }
        
        })

        .catch(error => {
            //showToast("Error in validating the user_token with black_list !!");
            return false
        });



}//check token

function history(){
    check_token(token).then(is_valid => {

        if(is_valid){
            // continue next operation - he is valid user
            window.location.href = "history.html?token=" + token;
        }
        else{
            showToast("It's not a valid token !!")
        }

    });
    
}

function update_pin_dash(redirect_page,passed_token){
    
    if(redirect_page){
        // first validate the token - if valid allow - if invalid reject


        check_token(token).then(is_valid => {

            if(is_valid){
                // continue next operation - he is valid user
                let action = "updatepin"
                window.location.href = "pin.html?token=" + token + "&action=" + action;
            }
            else{
                showToast("It's not a valid token !!")
            }

        });
  
    }
    else{
        window.location.href = "updatepin.html?token=" + passed_token;
    }

}

function profile() {

    
        // This waits for the background fetch to complete
        // Then executes the redirect logic


    check_token(token).then(is_valid => {

        if(is_valid){
            // continue next operation - he is valid user
            window.location.href = "profile.html?token=" + token;
        }
        else{
            showToast("It's not a valid token !!")
        }

    });
}

function check_balance(redirect_page,passed_token){

    if(redirect_page){

        check_token(token).then(is_valid => {

            if(is_valid){
                let action = "checkbalance"

                window.location.href = "pin.html?token="+ token + "&action=" + action;

            }
            else{
                showToast("It's not a valid token !!")
            }

        });
  
    }
    else{

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

    
}//check balance

function deposit_dash() {
    check_token(token).then(is_valid => {

        if(is_valid){
            // continue next operation - he is valid user
            window.location.href = "deposit.html?token=" + token;
        }
        else{
            showToast("It's not a valid token !!")
        }

    });
    
}

function withdrawl_dash() {
    check_token(token).then(is_valid => {

        if(is_valid){
            // continue next operation - he is valid user
            window.location.href = "withdrawl.html?token=" + token;
        }
        else{
            showToast("It's not a valid token !!")
        }

    });
    
}

function transfer_dash() {
    check_token(token).then(is_valid => {

        if(is_valid){
            // continue next operation - he is valid user
            window.location.href = "transfer.html?token=" + token;
        }
        else{
            showToast("It's not a valid token !!")
        }

    });
    
}