
const emp_token = sessionStorage.getItem("emp_token")

console.log(emp_token)

function getusersdata(){

    //based on the emp role - render according user data

    let url = "http://localhost:8080/employees_data?emp_token=" + emp_token;

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

        // dynamically create headers from first object keys
        let headers = Object.keys(data[0]);

        // render headers
        let headerRow = "<th>S.no</th>"; 
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