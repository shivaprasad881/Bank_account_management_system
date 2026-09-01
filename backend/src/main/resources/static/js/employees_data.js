
const emp_token = sessionStorage.getItem("emp_token")

console.log(emp_token)

let headers
let headerRow 
let alldata
function getusersdata(){

    //based on the emp role - render according user data

    let url = `${API_BASE_URL}/employees_data?emp_token=${emp_token}`;

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
        data.forEach((row, index) => {
            let rowHtml = `<td>${index + 1}</td>`;
            headers.forEach(key => {
                rowHtml += `<td>${row[key]}</td>`;
            });
            document.getElementById("tableBody").innerHTML += `<tr>${rowHtml}</tr>`;
        });
    })
    .catch(error => {
        console.log("exact error: ", error);
        showToast("Error in fetching employees !!");
    });

}

getusersdata()

function search(){
    // let inputname = document.getElementById("search").value;

    // //empid  ename  age  salary   dept
    // inputname = inputname.trim()

    //consider only city

    // dynamically create headers from first object keys
        document.getElementById("tableBody").innerHTML = "";
        
        
        

        // render rows
        let searchTerm = document.getElementById("search").value.toLowerCase().trim();

        

        alldata.forEach((row, index) => {
            if(!(row.empid && row.empid.toLowerCase().includes(searchTerm))){
                return; // skip rows that don't match the search term
            }

            let rowHtml = `<td>${index + 1}</td>`;
            headers.forEach(key => {
                rowHtml += `<td>${row[key]}</td>`;
            });
            document.getElementById("tableBody").innerHTML += `<tr>${rowHtml}</tr>`;
        });
    
}