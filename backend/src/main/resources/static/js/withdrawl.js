
const params_with = new URLSearchParams(window.location.search);
const token_with = params_with.get("token");



function withdrawl() {

        
            //hoo not yet executed - let it executed the request

            let amt = document.getElementById("numberInput").value;
            let int_amt = parseInt(amt);
            
            if(amt == "" ) {
                showToast("Please enter the amount");
            }
            else if( int_amt <= 0 ){
                showToast("Please enter the valid amount");
            }
            else if( int_amt > 25000 ){
                showToast("Max withdrawl limit is 25000 /-");
            }
            else {
                //redirect to pin page 
                let action = "withdrawl"
                window.location.href = "pin.html?token=" + token_with + "&action=" + action + "&amount=" + amt;
            }

            // it executed successfully - restrict the re-execution
            
        
        //hoo its already executed - restrict the re-execution
        
        
    
   
}