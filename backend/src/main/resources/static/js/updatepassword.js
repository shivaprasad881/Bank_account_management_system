const token = sessionStorage.getItem("token")

console.log("dashboard token is : ",token)


function updatepass(){
    let oldpass = document.getElementById("oldpass").value;
    let newpass1 = document.getElementById("newpass1").value;
    let newpass2 = document.getElementById("newpass2").value;


    if(oldpass == "" || newpass1 == "" || newpass2 == ""  ) {
        showToast("Please enter the details !!");
    }
    else if(newpass1!=newpass2){ 
        showToast("Please enter correct 'confirm password' ");
    }
    else{
        //the inputs are correct 

        //1.now validate the old pass - if correct update else reject

        let url = `${API_BASE_URL}/update_password`;
                
                let userdata = {
                    token : token,
                    password: oldpass,
                    newpass : newpass1
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
                        showToast("Unauthorized request!!")
                    }
                    else{
                        return response.text();

                    }
                })
                .then(data => {
                    
                        showToast("password updated successfully")
                        // setTimeout(() => {window.location.href = "dashboard.html"
                        // }, 1500);
                    

                   
                })
                .catch(error => {
                    showToast("error in updating the password !!");
                });


    }
    
}