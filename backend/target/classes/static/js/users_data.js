
const emp_token = sessionStorage.getItem("emp_token")

console.log(emp_token)

function getusersdata(){

    //based on the emp role - render according user data

    let url = `${API_BASE_URL}/users_data_based_on_emp_role?emp_token=${emp_token}`;

    fetch(url, {
        method: 'GET'
    })
    .then(response => response.json())
    .then(data => {
        let users = data.users; // ← extract users array
        
        if(users.length == 0) {
            showToast("No users found !!");
            return;
        }

        let headers = Object.keys(users[0]); // ← use users not data
        
        let headerRow = "";
        headers.forEach(key => {
            headerRow += `<th>${key}</th>`;
        });
        document.getElementById("tableHead").innerHTML = `<tr>${headerRow}</tr>`;

        users.forEach(row => {
            let rowHtml = "";
            headers.forEach(key => {
                rowHtml += `<td>${row[key]}</td>`;
            });
            document.getElementById("tableBody").innerHTML += `<tr>${rowHtml}</tr>`;
        });
    })
    .catch(error => {
    console.log("exact error: ", error);
    showToast("Error in fetching users !!");
});

}

getusersdata()