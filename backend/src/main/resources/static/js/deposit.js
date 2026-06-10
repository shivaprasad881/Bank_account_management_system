//acc number passed from dashboard
const params_depo = new URLSearchParams(window.location.search);
const token_depo = params_depo.get("token");

function deposit() {

    
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