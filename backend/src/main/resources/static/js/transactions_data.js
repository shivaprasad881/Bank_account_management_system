const emp_token = sessionStorage.getItem("emp_token")

console.log(emp_token)

let headers
let headerRow 
let alldata
function gettransdata(){

    //based on the emp role - render according user data

    let url = `${API_BASE_URL}/transactions_data?emp_token=${emp_token}`;

    fetch(url, {
        method: 'GET'
    })
    .then(response => {
        if(response.status == 200) {
            return response.json();
        } else if(response.status == 403) {
            showToast("Access denied !!");
            return;
        } else {
            showToast("Invalid token !!");
            return;
        }
    })
    .then(data => {
        if(!data) return;

        if(data.length == 0) {
            showToast("No employees found !!");
            return;
        }
        alldata = data
        // dynamically create headers from first object keys
        headers = Object.keys(data[0]);

        // render headers
        headerRow = "<th>S.no</th>"; 
        headers.forEach(key => {
            headerRow += `<th>${key}</th>`;
        });
        document.getElementById("tableHead").innerHTML = `<tr>${headerRow}</tr>`;

        // render rows

        //just render 9 records for reference - if requried then the user would search for the speficic one - so as we had all the data we would render that then 
        
        let count =0;

        data.forEach((row, index) => {

            if(count>=9){
                return
            }

            let rowHtml = `<td>${index + 1}</td>`;
            headers.forEach(key => {
                rowHtml += `<td>${row[key]}</td>`;
            });
            document.getElementById("tableBody").innerHTML += `<tr>${rowHtml}</tr>`;

            count++;
        });
    })
    .catch(error => {
        console.log("exact error: ", error);
        showToast("Error in fetching employees !!");
    });

}

gettransdata()


function search(){

        

        // render rows
        let searchTerm = document.getElementById("search").value.toLowerCase().trim();

        if(searchTerm==""){
            //render all
            document.getElementById("tableBody").innerHTML = "";

            alldata.forEach((row, index) => {
                

                let rowHtml = `<td>${index + 1}</td>`;
                headers.forEach(key => {
                    rowHtml += `<td>${row[key]}</td>`;
                });
                document.getElementById("tableBody").innerHTML += `<tr>${rowHtml}</tr>`;
            });
        }
        else if(searchTerm.length<3){
            showToast("input length should be atleast 3 !!");
            return
        }
        else{
            // first make the body empty then render the specific rows
            document.getElementById("tableBody").innerHTML = "";

            alldata.forEach((row, index) => {
                if(!(row.transaction_type && row.transaction_type.toLowerCase().includes(searchTerm))){
                    return; // skip rows that don't match the search term
                }

                let rowHtml = `<td>${index + 1}</td>`;
                headers.forEach(key => {
                    rowHtml += `<td>${row[key]}</td>`;
                });
                document.getElementById("tableBody").innerHTML += `<tr>${rowHtml}</tr>`;
            });
        }

        
    

}