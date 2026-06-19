

function update_the_pin() {
    let newpin = document.getElementById("newpin").value;
    

    if(newpin == "") {
        showToast("please enter the pin !!");
    } 
    
    else if( newpin.length!=4 || parseInt(newpin)<0 ){
        showToast("Please enter the valid Pin !!")
    }
    
    else {

           
            sessionStorage.setItem("action", "updatepin");
            sessionStorage.setItem("newpin", newpin);


            window.location.href = "pin.html"

           
    }
}
