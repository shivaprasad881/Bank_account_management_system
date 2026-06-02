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
            url = "http://localhost:8080/updatepin";
                
                let userdata = {
                    token : token_upt,
                    newpin: newpin
                };
                
                fetch(url, {
                    method: 'PATCH',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(userdata)
                })
                .then(response =>{
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
                        window.location.href = "dashboard.html?token=" + token_upt;
                    }, 2000);
                })
                .catch(error => {
                    showToast("error in updating the pin !!");
                });
    }//else
}
