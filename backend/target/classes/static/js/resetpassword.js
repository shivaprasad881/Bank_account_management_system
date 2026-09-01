
function resetpassword(){

    let new_password = document.getElementById('new_password').value;
    let confirm_password = document.getElementById('confirm_password').value;

    if(new_password=="" || confirm_password==""){
        showToast("please enter the details !!")
    }
    else if(new_password != confirm_password){
        showToast("please enter correct 'confirm password' ")
    }
    else{
        //user input is correct - reset password

        const useremail = sessionStorage.getItem("email")


                let url = `${API_BASE_URL}/resetpassword`;
                
                let userdata = {
                    email : useremail,
                    newpassword: new_password
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
                        showToast("password reset successfull !!")

                        setTimeout(() => {

                            sessionStorage.removeItem("email");
                            

                            window.location.href = "signin.html"
                        }, 1500);

                    }
                    else{
                        showToast("unauthorized request !!")
                    }

                    

                })
                .catch(error => {
                    showToast("error in reseting the password !!");
                });


    }

}