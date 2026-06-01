const params = new URLSearchParams(window.location.search);
const token = params.get("token");

let fetched  = false;

function username(){


    let url = "http://localhost:8080/user_name?token=" + token;

    fetch(url,{
        method : 'get'
    })

    .then(response =>{
        if(response.status!=200){
            showToast("Invalid token !!")
            return null;
        }
        else{
            return response.text()
        }
    })

    .then(data =>{
        

        let element = document.getElementById("usermessage");
        if(element != null){
            element.innerHTML = `Hello ${data} !!`
        }
        //when element is null - which indicates we are in a page where we dont need to render the user name - so ignore it

    })

    .catch(error => {
        showToast("Error in fetching user name !!");
    });

    
}

username()

function logout(){
    alert("logout !!")
}

function history(){
    window.location.href = "history.html?token=" + token;
}

function update_pin_dash(redirect_page,passed_token){
    // first use pin page to check the pin 
    
    if(redirect_page){
        let action = "updatepin"
        window.location.href = "pin.html?token=" + token + "&action=" + action;
    }
    else{
        window.location.href = "updatepin.html?token=" + passed_token;
    }

}

function profile() {
    window.location.href = "profile.html?token=" + token;
}

function check_balance(redirect_page,passed_token){

    //hoo i came here to check the blanace only - i dont know what is redirection and stuff -i just need blance

    if(redirect_page){
        let action = "checkbalance"
        //hoo the functions is being called from the frontend - so we would to redirect to pin page for pin validation
        window.location.href = "pin.html?token="+ token + "&action=" + action;
    }

    // when the function call is made from the pin page , we would pass the redirectpage as false - so the page would not get repeated redirection

    // if we came here means in the pin page the pin is declared as valid - only then this function is called with redirectpage as false then only this line would executed

    
    
    url = "http://localhost:8080/check_balance?token=" + passed_token;

    fetch(url, {
        method: 'get'
    })
    .then(response =>{
        if(response.status!=200){
            //invalid token
            showToast("Invalid token !!")
        }
        else{
            //valid token - send the response to the next level - then would handle the response
            return response.text()
        }
    })
    .then(data => {
        showToast(data,2000);

        //after showing balance on the pin page - we need to redirect ot the dashboard - as the user sees the balance only once
        setTimeout(() => {
           window.location.href = "dashboard.html?token=" + token;
        }, 2000);


    })
    .catch(error => {
        showToast("Error in fetching the balance !!");
    });
}

function deposit_dash() {
    window.location.href = "deposit.html?token=" + token;
}

function withdrawl_dash() {
    window.location.href = "withdrawl.html?token=" + token;
}

function transfer_dash() {
    window.location.href = "transfer.html?token=" + token;
}