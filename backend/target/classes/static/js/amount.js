function amount(){
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