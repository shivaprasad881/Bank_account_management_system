//acc number passed from dashboard


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

            sessionStorage.setItem("action", "deposit");
            sessionStorage.setItem("amount", amt);

            
            window.location.href = "pin.html"
        }
   
}