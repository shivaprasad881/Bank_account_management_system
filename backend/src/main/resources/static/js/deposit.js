//acc number passed from dashboard
const params_depo = new URLSearchParams(window.location.search);
const token_depo = params_depo.get("token");

function deposit(redirect_page,passed_token,passed_amount) {

    if(redirect_page){
        //the call came from the frontend - so first validate the pin then deposit
        let amt = document.getElementById("numberInput").value;
        let int_amt = parseInt(amt);
        
        if(amt == "" ) {
            showToast("Please enter the amount");
        }
        else if( int_amt <= 0 ){
            showToast("Please enter the valid amount");
        }
        else {
            //now we had valid amount - redirect to pin page - validate - if correct - come here with values
            let action = "deposit"
            window.location.href = "pin.html?token=" + token_depo + "&action=" + action + "&amount=" + amt;
        }
    }
    else{

        //the call came from pin page  after validating the pin to deposit money
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
            showToast("error in depositing amount !!");
        });
    }
}