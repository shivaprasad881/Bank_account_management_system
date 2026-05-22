const params = new URLSearchParams(window.location.search);
const token = params.get("token");

let data;
let url = "http://localhost:8080/user_transactions?token=" + token ;

fetch(url, {
    method: 'GET'
})
.then(response => {
    if(response.status == 200) {
        return response.json();
    } else {
        showToast("Invalid token !!");
    }
})

.then(jsondata => {
    
        data = jsondata;

        for(let i = jsondata.length-1; i>=0; i--) {
            let tar_acc_value = jsondata[i].tarAcc;
            let amount_value = jsondata[i].amount;
            let transaction_type_value = jsondata[i].transactionType;
            let transaction_date_value = jsondata[i].transactionDate;

            let updated = transaction_date_value.substring(0,10) + "   " + transaction_date_value.substring(11,19);
            console.log(transaction_date_value)

            document.getElementById("tableBody").innerHTML += `
            <tr>
                <td>${tar_acc_value}</td>
                <td>${amount_value}</td>
                <td>${transaction_type_value}</td>
                <td>${updated}</td>
            </tr>
            `;
        

        }

    
})
.catch(error => {
    showToast("error in fetching the history !!");
});

function download() {
    // we need to include all the transactions of the user in a pdf and download it
    const doc = new jspdf.jsPDF();

    // x should remain 10 but y would be increment by 10
    doc.text("Transaction records ", 45, 10);
    
    let y = 20;

    
    for(let i = data.length-1; i>=0; i--) {
        //each json object
        let str = JSON.stringify(data[i]); // each json object converted into string
        
        //each line can hold 80 characters only
        str = (i + 1) + "].   " + str;
        
        let len = str.length;
        let start = 0;

        while(len > 70) {
            doc.text(str.substring(start, start + 70), 10, y);
            len -= 70;
            y += 8;
            start += 70;
        }

        if(len != 0) {
            //still something left - add it in new line
            doc.text(str.substring(start), 10, y);
        }
        
        y += 11;
    }

    doc.save("first.pdf");
}
