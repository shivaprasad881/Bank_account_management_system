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
                    
                    if(action=="checkbalance"){
                        
                        check_balance(false,token_pin);// false parameter restricts the reexecution of redirection to pin page
                    }
                    else if(action=="updatepin"){
                        update_pin_dash(false,token_pin);
                    }
                    else if(action=="deposit"){

                        const amt = params_pin.get("amount");

                        deposit(false,token_pin,amt);
                    }
                    else if(action=="withdrawl"){

                        const amt = params_pin.get("amount");

                        withdrawl(false,token_pin,amt);
                    }
                    else if(action=="transfer"){

                        const tar_acc = params_pin.get("tar_acc");
                        const amt = params_pin.get("amount");

                        transfer(false,tar_acc,amt,token_pin)

                    }
                    else{
                        showToast("The action is not matched with any option !!")
                    } 
                    
                }
                else{
                    showToast("Invalid pin !!")
                }
            })

        .catch(error => {
            showToast("Error in validating the pin !!");
        });

    }//else  
}



