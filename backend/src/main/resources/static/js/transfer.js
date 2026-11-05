const tokennn = sessionStorage.getItem("token")

console.log("dashboard token is : ",tokennn)



async function transferr(passed_target) {

 
        let element = document.getElementById("target");

        let target;

        if(element!=null){
            //we are withing the same page - use the fronted value
            target = document.getElementById("target").value;
        }
        else{
            //we are not in the same page -use the value passed from another page

            target = passed_target
        }
        

        target = target.trim() // removing the leading and trailing spaces (user may unfortunetely include spaces at the ends)

        
        
        if(target == "") {
            
            showToast("please fill the  details !!");
            

        }
        
        //now the input amount is valid - check for target

        else if( target.length == 10  && isNaN(target)==false     ){

            //phonenumber

            let target_type = "phonenumber"




             //now check whether an user exits with this target phonenumber

            if(await check_the_existance_of_identity(target,target_type)){
                //yes a user exits with these details 

                //so continue the transfer flow
                

                sessionStorage.setItem("action", "transfer");
                
                sessionStorage.setItem("target", target);
                sessionStorage.setItem("target_type", target_type);

                window.location.href = "amount.html"

            }
            else{
                //hoo no user exits with this identity - indicate no user found
                showToast("No user found with that Phone Number")
            }


        }
        else if( target.length == 11  && target.substring(0,3)=="ACC" &&  isNaN(target.substring(3))==false  ){

            //accno
            let target_type = "account"


            //now check whether an user exits with this target accno 

            if(await check_the_existance_of_identity(target,target_type)){
                //yes a user exits with these details 

                //so continue the transfer flow
                

                sessionStorage.setItem("action", "transfer");
                
                sessionStorage.setItem("target", target);
                sessionStorage.setItem("target_type", target_type);

                window.location.href = "amount.html"

            }
            else{
                //hoo no user exits with this identity - indicate no user found
                showToast("No user found with that Account Number")
            }


            

        }
        else {
            
            
            showToast("Please enter valid 10-digit phone number or ACCXXXXXXXXX account number: " + target,3000);
            

        }
    


}


function check_the_existance_of_identity(target,target_type){

    let url = `${API_BASE_URL}/check_the_existance_of_identity?target=${target}&target_type=${target_type}&token=${tokennn}`;

    return fetch(url, {
        method: 'GET'
    })
    .then(response => response.text())
    .then(data => {
        return data === "true";
    })
    .catch(error => {
        showToast("Error checking the existance of target Account");
        return false;
    });
    

}
