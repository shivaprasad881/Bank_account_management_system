const params_pin = new URLSearchParams(window.location.search);
const token_pin = params_pin.get("token");
const action = params_pin.get("action");


function check_pin(){
    // check whether user pin is valid or not - get request - pass userpin in url
    const userpin = document.getElementById("userpin").value

    if(userpin=="" || userpin.length!=4){
        showToast("Please enter the valid pin !!")
    }
    else{
        //valid pin format 


            let url = "http://localhost:8080/validate_pin?token=" + token_pin + "&userpin=" + userpin;

            console.log("pin page ->  token: " + token_pin);
            
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
                    //valid pin - now fetch the user bal and display - now what task should i do - should i check the bla or deposit orupdate pinor else something - the one who called me to check the pin need to specify the task to do when the p;in is valid 
                    if(action=="checkbalance"){
                        //the task is to chekc blanace - call that funtion with the useracc
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
                        //hoo i done with pin validation - my next task it to transfer the money
                        //for this i need a user acc and tar acc and the mat to transfer
                        //these all requirements are passed by my caller for smooth transaction
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



