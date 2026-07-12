
function resetpin(){

    let new_pin = document.getElementById('new_pin').value;
    let confirm_pin = document.getElementById('confirm_pin').value;

    if(new_pin=="" || confirm_pin==""){
        showToast("please enter the details !!")
    }
    else if(new_pin != confirm_pin){
        showToast("please enter correct 'confirm pin' ")
    }
    else{
        //user input is correct - reset pin

        const useremail = sessionStorage.getItem("email")


                url = "http://localhost:8080/resetpin";
                
                let userdata = {
                    email : useremail,
                    newpin: new_pin
                };
                
                fetch(url, {
                    method: 'PATCH',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(userdata)
                })
                .then(response =>{
                    
                        return response.text();

                })
                .then(data => {
                    
                    if(data=="true"){
                        //successfull
                        showToast("pin reset successfull !!")

                        setTimeout(() => {

                            sessionStorage.removeItem("email");
                            

                            window.location.href = "dashboard.html"
                        }, 1500);

                    }
                    else{
                        showToast("unauthorized request !!")
                    }

                    

                })
                .catch(error => {
                    showToast("error in reseting the pin !!");
                });


    }

}