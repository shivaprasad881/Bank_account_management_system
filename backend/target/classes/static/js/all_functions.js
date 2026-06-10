
function withdrawl(passed_token,passed_amount){

        url = "http://localhost:8080/withdrawl";

        let userdata = {
            token : passed_token,
            amount: passed_amount
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
                //invalid token
                showToast("Invalid token !!")
            }
            else{
                return response.text();
            }
        })
        .then(data => {
            showToast(data,2000);

            // setTimeout(() => {
            //     window.location.href = "dashboard.html?token=" + token_with;
            // }, 2000);

        })
        .catch(error => {
            showToast("error in withdrawling amount !!");
        });

}

function deposit(passed_token,passed_amount){

     url = "http://localhost:8080/deposit" ;

        let userdata = {
            token: passed_token,
            amount: passed_amount
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
                showToast("Invalid token !!")
            }
            else{
                return response.text();
            }
        })
        .then(data => {
            showToast(data,2000);

            setTimeout(() => {
                window.location.href = "dashboard.html?token=" + token_depo;
            }, 2000);

        })
        .catch(error => {
            console.log("deposit error: " + error)
            showToast("error in depositing amount !!");
        });

}


function transfer(tar_acc,tar_amt,token_passed){

    let url = "http://localhost:8080/transfer";

        let userdata2 = {
            token: token_passed,
            tar_acc: tar_acc,
            amount: tar_amt
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
                return response.text()
            }
        })
        .then(data => {
            //transfer succesfull
            
            showToast(data,1500)
            
            setTimeout(() => {
                window.location.href = "dashboard.html?token=" + token_with;
            }, 1500);

        })

        .catch(error => {
            showToast("Error in money transfer  !!");
        }); 


}


function update_pin(token,newpin){

                url = "http://localhost:8080/updatepin";
                
                let userdata = {
                    token : token,
                    newpin: newpin
                };
                
                fetch(url, {
                    method: 'PATCH',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(userdata)
                })
                .then(response =>{
                    if(response.status!=200){
                        showToast("Invalid token !!")
                    }
                    else{
                        return response.text();

                    }
                })
                .then(data => {
                    showToast(data,2000);

                    setTimeout(() => {
                        window.location.href = "dashboard.html?token=" + token;
                    }, 2000);
                })
                .catch(error => {
                    showToast("error in updating the pin !!");
                });

}

function check_balance(token){


     url = "http://localhost:8080/check_balance?token=" + token;

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