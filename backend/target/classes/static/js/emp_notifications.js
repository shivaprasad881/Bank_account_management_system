
const emp_token = sessionStorage.getItem("emp_token")

console.log(emp_token)

function fetchallnotifications() {
        let url = "http://localhost:8080/fetchallnotifications?emp_token=" + emp_token;

           fetch(url)
        .then(response => response.json())
        .then(data => {
            console.log("Notifications data:", data);
            console.log("Total notifications:", data.length);
            let html = "";
            data.forEach((n, index) => {
                console.log(`[${index+1}] Sender: ${n.sender}, Message: ${n.message}`);
                let time = new Date(n.createdAt);
                let timeStr = time.toLocaleTimeString('en-IN', { hour: '2-digit', minute: '2-digit', hour12: true });
                html += `<tr>
                            <td>${index + 1}</td>
                            <td>${n.sender}</td>
                            <td>${n.message}</td>
                            <td>${timeStr}</td>
                        </tr>`;
            });
            document.getElementById("notiTableBody").innerHTML = html || "<tr><td colspan='4'>No notifications</td></tr>";
        })

        .catch(() => showToast("Error fetching notifications"));
}

        

fetchallnotifications();