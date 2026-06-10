const params_pin = new URLSearchParams(window.location.search);
const token_pin = params_pin.get("token");
const action = params_pin.get("action");


function check_pin(){
    // check whether user pin is valid or not
    const userpin = document.getElementById("userpin").value

    if(userpin=="" ){
        showToast("Please enter the  Pin !!")
    }
    else if( userpin.length!=4   ||    isNaN( userpin )   ||   parseInt(userpin)<0 ){
        showToast("Please enter the valid Pin !!")
    }
    else{
            // 1.blacklist validation

            check_token_in_blacklist(token_pin).then(is_valid => {

                if(is_valid){
                    
                    // 2.pin validation

                    let url = "http://localhost:8080/validate_pin?token=" + token_pin + "&userpin=" + userpin;

                    //console.log("pin page ->  token: " + token_pin);
                    
                    fetch(url, {
                        method: 'get'
                    })
                    .then(response => {
                        if(response.status!=200){
                            // hoo invalid token 
                            showToast("Invalid token !!")
                            return null;
                        }
                        else{
                            return response.text();
                        }
                    })

                    .then(data => {
                        if(data=="true"){
                            if(action=="withdrawl"){
                                const amt = params_pin.get("amount");

                                withdrawl(token_pin,amt);
                            }
                            else if(action=="checkbalance"){
                                
                                check_balance(token_pin);
                            }
                            else if(action=="updatepin"){
                                const newpin = params_pin.get("newpin");
                                update_pin(token_pin,newpin);
                            }
                            else if(action=="deposit"){

                                const amt = params_pin.get("amount");

                                deposit(token_pin,amt);
                            }
                            
                            else if(action=="transfer"){

                                const tar_acc = params_pin.get("tar_acc");
                                const amt = params_pin.get("amount");

                                transfer(tar_acc,amt,token_pin)

                            }
                            else{
                                console.log("the action is : "+action)
                                showToast("The action is not matched with any option !!")
                            } 
                            
                        }
                        else{
                            showToast("Invalid pin !!")
                        }
                    })

                    .catch(error => {
                        console.log("pin error: " + error)
                        showToast("Error in validating the pin !!");
                    });



                }
                else{
                    showToast("It's not a valid token !!")
                }

            });

    }//else  
}



