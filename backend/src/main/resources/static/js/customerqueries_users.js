const emp_token = sessionStorage.getItem("emp_token")

console.log(emp_token)

//write a function to fetch all users accno's

function allusers(){
    let url = `${API_BASE_URL}/get_all_users?emp_token=${emp_token}`;

    fetch(url, {
        method: 'GET'
    })
    .then(response => {
        if (!response.ok) {
            showToast("Failed to fetch users");
            return null;
        }
        return response.json();
    })
    .then(data => {
        if (data) {
            renderUsers(data);
        }
    })
    .catch(error => {
        showToast("Error fetching users");
    });

}

allusers()


function renderUsers(users) {
    let list = document.getElementById("usersList");
    list.innerHTML = "";

    users.forEach(user => {
        list.innerHTML += `
            <div class="user-row">
                <span class="user-accno">${user.accno}</span>
                <button class="chat-btn" onclick="getChat('${user.accno}')">Get Chat</button>
            </div>
        `;
    });
}

function getChat(accno) {
    sessionStorage.setItem("chat_accno", accno);
    sessionStorage.setItem("source_page", "emp_dashboard");
    window.location.href = "chat.html";
}