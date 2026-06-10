// parameters passed from prev page - parameter chaining
const params = new URLSearchParams(window.location.search);
const token_upt = params.get("token");

function update_the_pin() {
    let newpin = document.getElementById("newpin").value;
    

    if(newpin == "") {
        showToast("please enter the pin !!");
    } 
    
    else if( newpin.length!=4 || parseInt(newpin)<0 ){
        showToast("Please enter the valid Pin !!")
    }
    
    else {

            let action = "updatepin"
            window.location.href = "pin.html?token=" + token_upt + "&newpin=" + newpin + "&action=" + action;

           
    }
}
