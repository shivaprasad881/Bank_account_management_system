const params_trans = new URLSearchParams(window.location.search);
const token_trans = params_trans.get("token");

function transfer(redirect_page,tar_acc_pin,tar_amt_pin,token_passed) {


    if(redirect_page){
        // the call came from the frontend - first redirect and validate the pin then transfer

        let tar_acc = document.getElementById("taracc").value;
        let tar_amt = document.getElementById("useramount").value;

        let int_amt = parseInt(tar_amt);
        
        if(tar_acc == "" || tar_amt == "" || int_amt <= 0) {
            
            showToast("please enter valid details!!");

        }
        // else if(useracc_trans == tar_acc) {

        //     showToast("Cannot transfer to your own account!!");

        // }
        else {
            // valid data

            // // now get the user balance
            // let url = "http://localhost:8080/check_balance?token="+ token_trans;
        
            // fetch(url, {
            //     method: 'get'
            // })
            // .then(response => {
            //     if(response.status!=200){
            //         showToast("Invalid token !!")
            //     }
            //     else{
            //         // valid token - proceed
            //         return response.text()
            //     }
            // })
            // .then(data => {
            //     //succesfully fetched user balance
            //     let bal = parseFloat(data);
            //     if(bal >= tar_amt) {
            //         //user had enough bal to transfer

            //         //now check whether dest user exists or not - ****change it to post request for security concern



                    //no token required
                    url = "http://localhost:8080/check_accno"

                    let userdata1 = {
                        accno: tar_acc
                    };

                    //get method - pass acc within url
                    fetch(url, {
                        method: 'POST',
                         headers: {
                            "Content-Type": "application/json"
                        },
                        body: JSON.stringify(userdata1)

                    })
                    .then(response => response.text())
                    .then(data => {
                        if(data == "true") {
                            //dest acc is present - now we can start the transaction
                            
                            let action = "transfer"
                            window.location.href = "pin.html?token=" + token_trans + "&action=" + action   + "&tar_acc=" + tar_acc + "&amount=" + tar_amt;

                        }
                        else {
                            showToast("Destination account not existing !!");
                        }


                    })

                    .catch(error => {
                        showToast("error in checking the presence of destination user !!");
                    });



            //     }
            //     else{
            //         showToast("Insufficient balance !!");
            //     }

            // })

            // .catch(error => {
            //     showToast("error in fetching the user balance !!");
            // });

        }
    }
    else{ // redirect_page == False
        //hoo the call came from the pin page after succesffull validateion - now no reidrection needed - only thins is to trranfer
        //all the detatils for transfer is passed by pin page which are passed by ours

        //*** now do the transaction without any token validation - as we done it in the pin security gate 
        //*** */ by not using the token here , it would restrict the transaction rejection in the middel due to token expiry
        // ** here we are doing the transfer in request but not in two differnt requests (debit and credit ) - this make sure the atomicity

        let url = "http://localhost:8080/transfer";



        let userdata2 = {
            token: token_passed,
            tar_acc: tar_acc_pin,
            amount: tar_amt_pin
        };

        fetch(url, {
            method: 'PATCH',
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(userdata2)
        })
        
        .then(response =>{
            if(response.status!=200){
                showToast("Invalid token !!")
            }
            else{
                // valid token - pass the response
                return response.text()
            }
        })
        .then(data => {
            
            showToast(data)

        })

        .catch(error => {
            showToast("Error in money transfer  !!");
        });           


    }//else


}
