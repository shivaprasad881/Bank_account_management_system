const params = new URLSearchParams(window.location.search);
const token = params.get("token");

let size_his;
let tar_page_his;

let total_pages;

function fetch_transactions(size,tar_page){
    console.log("fetch_transactions called with size:", size, "page:", tar_page);
    let url = "http://localhost:8080/user_transactions?token=" + token  + "&size=" + size + "&page=" + tar_page ;

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

        let data = jsondata.content;

        total_pages = jsondata.totalPages;

        size_his = size;
        tar_page_his = tar_page;

        display(data,tar_page_his,size_his)
        
    })
    .catch(error => {
        showToast("error in fetching the history !!");
    });

}

fetch_transactions(10,0)


function nextpage(){

    if((tar_page_his + 2 ) > total_pages){
        showToast("Already at ending !!",1500)
    }
    else{
        fetch_transactions(size_his,tar_page_his+1)
    }
}


function prevpage(){

    if(tar_page_his==0){
        showToast("Already at Beginning !!",1500)
    }
    else{
        fetch_transactions(size_his,tar_page_his-1)
    } 
}


function display(data,current_page,page_size){

        document.getElementById("tableBody").innerHTML  = `` 

        document.getElementById("heading").innerHTML = `Transactions (page - ${current_page+1})`
        

        for(let i=0;i<data.length;i++) {
            let tar_acc_value = data[i].tarAcc;
            let amount_value = data[i].amount;
            let transaction_type_value = data[i].transactionType;
            let transaction_date_value = data[i].transactionDate;

            let updated = transaction_date_value.substring(0,10) + "   " + transaction_date_value.substring(11,19);


            if(transaction_type_value=="credit"){
                //green colour

                document.getElementById("tableBody").innerHTML += `
                <tr>
                    <td>${(i+1) + page_size*(current_page) } </td>
                    <td>${tar_acc_value}</td>
                    <td   style="color: green"  >+ ${amount_value}</td>
                    <td>${transaction_type_value}</td>
                    <td>${updated}</td>
                </tr>
                `;

            }
            else{

                document.getElementById("tableBody").innerHTML += `
                <tr>
                    <td>${(i+1) + page_size*(current_page) } </td>
                    <td>${tar_acc_value}</td>
                    <td   style="color: red"  >- ${amount_value}</td>
                    <td>${transaction_type_value}</td>
                    <td>${updated}</td>
                </tr>
                `;

            }

        }       
}

function download() {
    
    const doc = new jspdf.jsPDF();

    // x should remain 10 but y would be increment by 10
    doc.text("Transaction records ", 65, 10);
    
    let y = 20;
    
    for(let i = start_pointer; i>= Math.max(0,end_pointer) ; i--) {
        //each json object
        let str = JSON.stringify(data[i]); // each json object converted into string
        
        //each line can hold 80 characters only
        str = (data.length-i) + "].   " + str;
        
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
