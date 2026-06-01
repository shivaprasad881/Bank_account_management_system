
const params_with = new URLSearchParams(window.location.search);
const token_with = params_with.get("token");

function withdrawl(redirect_page,passed_token,passed_amount) {

    if(redirect_page){
        //the call came from the frontend - so first validate the pin then withdrawl
        let amt = document.getElementById("numberInput").value;
        let int_amt = parseInt(amt);
        
        if(amt == "" || int_amt <= 0) {
            showToast("Please enter valid amount");
        }
        else {
            //redirect to pin page - validate - if correct - come here with values
            let action = "withdrawl"
            window.location.href = "pin.html?token=" + token_with + "&action=" + action + "&amount=" + amt;
        }
    }
    else{

        
        // the call came from pin page  after validating the pin to withdraw money
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

            setTimeout(() => {
                window.location.href = "dashboard.html?token=" + token_with;
            }, 2000);

        })
        .catch(error => {
            showToast("error in withdrawling amount !!");
        });
    }
}