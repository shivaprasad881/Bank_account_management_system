//acc number passed from dashboard
const params_depo = new URLSearchParams(window.location.search);
const token_depo = params_depo.get("token");

function deposit(redirect_page,passed_token,passed_amount) {

    if(redirect_page){
        //hoo the call came from the frontend - so first validate the pin then deposit
        let amt = document.getElementById("numberInput").value;
        let int_amt = parseInt(amt);
        
        if(amt == "" || int_amt <= 0) {
            showToast("Please enter valid amount");
        }
        else {
            //now we had valid amount - redirect to pin page - validate - if correct - come here with values
            let action = "deposit"
            window.location.href = "pin.html?token=" + token_depo + "&action=" + action + "&amount=" + amt;
        }
    }
    else{

        
        //hoo the call came from pin page  after validating the pin to deposti money
        url = "http://localhost:8080/deposit" ;

        //as we are updating a attribute - this is patch - pass input in body
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
                //hoo i got the status other than 200 p - so may be invalid tone
                showToast("Invalid token !!")
            }
            else{
                //hoo i got the stauts as 200 so it is valid toke and i got a valid response i shoudl render thantg in tnext ithem
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
            showToast("error in depositing amount !!");
        });
    }
}