const token = sessionStorage.getItem("token")

console.log("dashboard token is : ",token)

let action = sessionStorage.getItem("action");



function proceed(){
    let tar_amt = document.getElementById("useramount").value;

    let int_amt = parseInt(tar_amt);

        if(tar_amt == ""){
            showToast("please fill the  details !!");
        }
        else if(int_amt <= 0){

            showToast("Please enter valid amount !!") 

        }
        else if(int_amt > 100000){

            showToast("Cannot transfer more than 1 lakh !!")

        }
        else{
            // current detials

            sessionStorage.setItem("amount", tar_amt);


            window.location.href = "pin.html"
        }
}



if(action=="transfer"){
    //hoo as action is transfer , we need to fetch the tar acc details and show the user for confirmation

    //as we ar ein amt page , we only know about hte tar acc , so fetch his name

    //it takes (token,tar,type) , token for validatoin , type to check with , then tar for idenfiy who 

    //only for transafer we woul dehave thes two values , so first check whethe r it is transfer or not then only fetch these alues
    let target = sessionStorage.getItem("target");
    let target_type = sessionStorage.getItem("target_type");

    fetch_target_account_user_name(target,target_type);

}


function fetch_target_account_user_name(target,target_type){

    let url = `${API_BASE_URL}/fetch_the_target_account_user_name?token=${token}&target=${target}&target_type=${target_type}`;

    return fetch(url, {
        method: 'GET'
    })
    .then(response => response.text())
    .then(data => {
        
        //now we got the target users user name , display it inn the scrreen so that use can confirm to whom he sn=ending the amount 

        

        document.getElementById("targetName").innerHTML = data;
        document.getElementById("targetGroup").style.display = "flex";


        //also store the name so that we would use it in the pin page for final confirmmaiton

        sessionStorage.setItem("target_username",data);


    })
    .catch(error => {
        showToast("Error checking identity");
        return "false";
    });

}