
function transferr(passed_target) {

 
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

            let action = "transfer"

            sessionStorage.setItem("action", "transfer");
            
            sessionStorage.setItem("target", target);
            sessionStorage.setItem("target_type", target_type);


            window.location.href = "amount.html"


        }
        else if( target.length == 11  && target.substring(0,3)=="ACC" &&  isNaN(target.substring(3))==false  ){

            //accno
            let target_type = "account"

            let action = "transfer"

            sessionStorage.setItem("action", "transfer");
            
            sessionStorage.setItem("target", target);
            sessionStorage.setItem("target_type", target_type);

            window.location.href = "amount.html"

        }
        else {
            
            showToast("Please enter valid 10-digit phone number or ACCXXXXXXXXX account number");
            

        }
    


}
