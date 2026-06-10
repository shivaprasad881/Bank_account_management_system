const params_trans = new URLSearchParams(window.location.search);
const token_trans = params_trans.get("token");

function transfer() {


        let tar_acc = document.getElementById("taracc").value;
        let tar_amt = document.getElementById("useramount").value;

        let int_amt = parseInt(tar_amt);
        
        if(tar_acc == "" || tar_amt == "") {
            
            showToast("please fill the  details !!");

        }
        else if( tar_acc.length != 11  || tar_acc.substring(0,3)!="ACC"    ){

            showToast("Please enter valid tar_acc !!")

        }
        else if(int_amt <= 0){

            showToast("Please enter valid amount !!")

        }
        else if(int_amt > 100000){

            showToast("Cannot transfer more than 1 lakh !!")

        }
        else {
            
                    // url = "http://localhost:8080/check_accno"

                    // let userdata1 = {
                    //     accno: tar_acc
                    // };

                    // fetch(url, {
                    //     method: 'POST',
                    //      headers: {
                    //         "Content-Type": "application/json"
                    //     },
                    //     body: JSON.stringify(userdata1)

                    // })
                    // .then(response => response.text())
                    // .then(data => {
                    //     if(data == "true") {
                            
                            let action = "transfer"
                            window.location.href = "pin.html?token=" + token_trans + "&action=" + action   + "&tar_acc=" + tar_acc + "&amount=" + tar_amt;

                    //     }
                    //     else {
                    //         showToast("Destination account not existing !!");
                    //     }


                    // })

                    // .catch(error => {
                    //     showToast("Error in checking the presence of destination account !!");
                    // });

        }
    


}
