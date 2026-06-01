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
        else {
            
                    url = "http://localhost:8080/check_accno"

                    let userdata1 = {
                        accno: tar_acc
                    };

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

        }
    }
    else{

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

    }//else

}
