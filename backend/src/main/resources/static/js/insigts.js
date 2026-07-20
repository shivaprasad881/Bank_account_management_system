
function bank_balance(){
    url = "http://localhost:8080/total_bank_balance"

                fetch(url, {
                    method: 'get'
                })
                .then(response => response.text())
                .then(data => {
                    
                    document.getElementById('bankBalance').innerText = data
                })

                .catch(error => {
                    showToast("Error in fetching the bank-balance !!");
                });

}
bank_balance()

function bank_transactions(){
    url = "http://localhost:8080/bank_transactions"

                fetch(url, {
                    method: 'get'
                })
                .then(response => response.text())
                .then(data => {

                    let arr = data.split(" ");

                    let outgoing_amt = arr[0];

                    let incoming_amt = arr[1];

                    let total_amt = arr[2];

                    document.getElementById("totalTransactions").innerHTML = total_amt;
                    document.getElementById("outgoing").innerHTML = outgoing_amt;
                    document.getElementById("incoming").innerHTML = incoming_amt;

                    
                    
                })

                .catch(error => {
                    showToast("Error in fetching the bank_transactions !!");
                });

}
bank_transactions()

function users_count_based_on_account_status(){
    url = "http://localhost:8080/users_count_based_on_account_status"

                fetch(url, {
                    method: 'get'
                })
                .then(response => response.text())
                .then(data => {

                    let arr = data.split(" ");

                    let active = arr[0];

                    let inactive = arr[1];

                    let total_acc = arr[2];

                    document.getElementById("totalAccounts").innerHTML = total_acc;
                    document.getElementById("active").innerHTML = active;
                    document.getElementById("inactive").innerHTML = inactive;

                    
                    
                })

                .catch(error => {
                    showToast("Error in fetching the Noof users !!");
                });
}


users_count_based_on_account_status()
