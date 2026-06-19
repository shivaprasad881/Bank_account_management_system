
function transfer() {


        let target = document.getElementById("target").value;
        let tar_amt = document.getElementById("useramount").value;

        target = target.trim() // removing the leading and trailing spaces (user may unfortunetely include spaces at the ends)

        let int_amt = parseInt(tar_amt);
        
        if(target == "" || tar_amt == "") {
            
            showToast("please fill the  details !!");

        }
        else if(int_amt <= 0){

            showToast("Please enter valid amount !!") 

        }
        else if(int_amt > 100000){

            showToast("Cannot transfer more than 1 lakh !!")

        }
        //now the input amount is valid - check for target

        else if( target.length == 10  && isNaN(target)==false     ){

            //phonenumber

            let target_type = "phonenumber"

            let action = "transfer"

            sessionStorage.setItem("action", "transfer");
            sessionStorage.setItem("amount", tar_amt);
            sessionStorage.setItem("target", target);
            sessionStorage.setItem("target_type", target_type);


            window.location.href = "pin.html"


        }
        else if( target.length == 11  && target.substring(0,3)=="ACC" &&  isNaN(target.substring(3))==false  ){

            //accno
            let target_type = "account"

            let action = "transfer"

            sessionStorage.setItem("action", "transfer");
            sessionStorage.setItem("amount", tar_amt);
            sessionStorage.setItem("target", target);
            sessionStorage.setItem("target_type", target_type);

            window.location.href = "pin.html"

        }
        else {
            
            showToast("Please enter valid 10-digit phone number or ACCXXXXXXXXX account number");
            

        }
    


}
