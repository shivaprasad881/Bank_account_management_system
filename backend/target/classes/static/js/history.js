const params = new URLSearchParams(window.location.search);
const token = params.get("token");

let data;
let start_pointer;
let end_pointer;


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

        // both are inclusive - defines current rendering pages
        data = jsondata

    start_pointer = data.length-1 // 21
    end_pointer = data.length - 10 // 12 
        
    display()
    
})
.catch(error => {
    showToast("error in fetching the history !!");
});

function nextpage(){

    if(start_pointer <= 9){
        //there are no records left to display
        showToast("Alreadt at Ending !!",1000)
    }
    else{
        start_pointer -= 10;// 11 //1 // 9    // 1
        end_pointer -=10;// 2 // -8   // 0     // 0
        document.getElementById("tableBody").innerHTML  = `` //first empty the page then render the data
        display();
    }
  
}


function prevpage(){

    if(start_pointer == (data.length -1 )){
        // hoo the start pointer is again pointing to the its start which is the bottom of the index
        showToast("Already at Beginning !!",1000)
    }
    else{
        start_pointer += 10; // 11    21   31
        end_pointer += 10;   // 2     12   22
        document.getElementById("tableBody").innerHTML  = `` //first empty the page then render the data
        display();
    }

    
}




function display(){
        
    console.log(start_pointer +" "+ end_pointer)
        

        for(let i = start_pointer; i>= Math.max(0,end_pointer) ; i--) {
            let tar_acc_value = data[i].tarAcc;
            let amount_value = data[i].amount;
            let transaction_type_value = data[i].transactionType;
            let transaction_date_value = data[i].transactionDate;

            let updated = transaction_date_value.substring(0,10) + "   " + transaction_date_value.substring(11,19);
            

            document.getElementById("tableBody").innerHTML += `
            <tr>
                <td>${data.length-i}</td>
                <td>${tar_acc_value}</td>
                <td>${amount_value}</td>
                <td>${transaction_type_value}</td>
                <td>${updated}</td>
            </tr>
            `;
        

        }

        
}

function download() {
    // we need to include all the transactions of the user in a pdf and download it
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

    doc.save("first.pdf");
}
