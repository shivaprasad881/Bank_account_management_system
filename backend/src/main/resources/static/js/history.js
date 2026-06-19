
const token = sessionStorage.getItem("token")

console.log("dashboard token is : ",token)


// global variables
let current_page = 0;
let page_size = 9;

let total_pages;
let data;

function fetch_transactions(){

    
        console.log("fetch_transactions called with size:", page_size, "page:", current_page);

        let url = "http://localhost:8080/user_transactions?token=" + token  + "&size=" + page_size + "&page=" + current_page ;

        fetch(url, {
            method: 'GET'
        })


        .then(response => {
            if(response.status == 200) {
                return response.json();
            } else {
                showToast("Unauthorized request!!")
            }
        })



        .then(jsondata => {

            data = jsondata.content;//global
            total_pages = jsondata.totalPages;


            display()
            
        })



        .catch(error => {
            showToast("error in fetching the history !!");
        });

}

fetch_transactions()


function nextpage(){

    if((current_page + 2 ) > total_pages){
        showToast("Already at ending !!",1500)
    }
    else{
        current_page += 1;
        fetch_transactions()
    }
}


function prevpage(){

    if(current_page==0){
        showToast("Already at Beginning !!",1500)
    }
    else{
        current_page -= 1;
        fetch_transactions()
    } 
}


function display(){

        document.getElementById("tableBody").innerHTML  = `` 

        document.getElementById("heading").innerHTML = `Transactions (page - ${current_page+1})`

        

        for(let i=0;i<data.length;i++) {
            let tar_acc_value = data[i].tarAcc;
            let amount_value = data[i].amount;
            let transaction_type_value = data[i].transactionType;
            let transaction_date_value = data[i].transactionDate;
            let available_balance = data[i].availableBalance;
            
            transaction_type_value = transaction_type_value.trim();
            let updated = transaction_date_value.substring(0,10) + "   " + transaction_date_value.substring(11,19);

            

            if(transaction_type_value=="credit"){
                //green colour

                document.getElementById("tableBody").innerHTML += `
                <tr>
                    <td>${(i+1) + page_size*(current_page) } </td>
                    <td>${tar_acc_value}</td>
                    <td   style="color: green"  >₹ ${amount_value}</td>

                    <td>${transaction_type_value}</td>
                    <td style="font-weight: 500">${available_balance.toFixed(2)}</td>
                    <td>${updated}</td>
                </tr>
                `;

            }
            else{

                document.getElementById("tableBody").innerHTML += `
                <tr>
                    <td>${(i+1) + page_size*(current_page) } </td>
                    <td>${tar_acc_value}</td>
                    <td   style="color: red"  >₹ ${amount_value}</td>

                    <td>${transaction_type_value}</td>
                    <td style="font-weight: 500">${available_balance.toFixed(2)}</td>
                    <td>${updated}</td>
                </tr>
                `;

            }

        } 
        
        
}



function download() {
    
    const doc = new jspdf.jsPDF();

    // x should remain 10 but y would be increment by 10
    doc.text("Transaction records(page - "+ (current_page + 1)+") ", 65, 10);
    
    let y = 20;
    
    for(let i = 0; i<data.length ; i++) {
        //each json object
        let str = JSON.stringify(data[i]); // each json object converted into string
        
        //each line can hold 80 characters only
        str = ( current_page*page_size + i + 1 ) + "].   " + str;
        
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

    doc.save("transaction_records.pdf");
}
