//fetch the chat of this specific user 

const token = sessionStorage.getItem("token")

console.log("dashboard token is : ",token)

const emp_token = sessionStorage.getItem("emp_token")

console.log(emp_token)

let source = sessionStorage.getItem("source_page");
let user_accno = sessionStorage.getItem("chat_accno");

function fetch_chat(){

    //now check who is requesting - either customer or employee
    

    if(source=="customer_dashboard"){
        //customer is requesting - simple flow

        let url = `${API_BASE_URL}/fetch_chat_history?token=${token}`;
    

        return fetch(url, {
            method: 'GET'
        })
        .then(response => {
            if (!response.ok) {
                showToast("Invalid token");
                return null;
            }
            return response.json();
        })
        .then(data => {


            renderChat(data)
        })
        .catch(error => {
            showToast("Error fetching chat history");
            return null;
        });

    }
    else{
        //emp is requesting , so we would also have the user accno to get his data
        
        

        let url = `${API_BASE_URL}/fetch_chat_history2?emp_token=${emp_token}&user_accno=${user_accno}`;

        fetch(url, {
            method: 'GET'
        })
        .then(response => {
            if (!response.ok) {
                showToast("Failed to fetch chat history");
                return null;
            }
            return response.json();
        })
        .then(data => {
            if (data) {
                renderChat(data);
            }
        })
        .catch(error => {
            showToast("Error fetching chat history");
        });

    }
    
}




// call once immediately, then every 5 seconds
fetch_chat();
const pollId = setInterval(fetch_chat, 100);   // 5000 ms = 5 seconds


// You can also stop it automatically when the page closes:
window.addEventListener("beforeunload", () => clearInterval(pollId));




function renderChat(records) {
    let chatBox = document.getElementById("chatBox");
    chatBox.innerHTML = "";

    

    records.forEach(m => {
        let time = new Date(m.createdAt).toLocaleTimeString('en-IN', { hour: '2-digit', minute: '2-digit', hour12: true });

        let side;

        if (source === "customer_dashboard") {
            side = (m.type === "sender") ? "me" : "other";
        } else {
            side = (m.type === "sender") ? "other" : "me";
        }

        chatBox.innerHTML += `
            <div class="msg-row ${side}">
                <div class="msg-bubble">${m.message}</div>
                <div class="msg-time">${time}</div>
            </div>
        `;
    });
}


function sendMessage() {
    let message = document.getElementById("chatInput").value.trim();
    if (message === "") {
        showToast("Please type a message");
        return;
    }

    //check who is requesting , either cusomter or employee

    //separate endpoint for both because

    //in case of customer : we woudl have his token , from that token we woudl extract the accno 

    //incase of emp : we would have emp token and user accno , here we validate the emp token and use user accno 

    //as both scenarios are diff - then diff endpoints

    if(source=="customer_dashboard"){

        let url = `${API_BASE_URL}/send_message`;

        let userdata = {
            token: token,
            message: message
        };

        fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(userdata)
        })
        .then(response => {
            if (!response.ok) {
                
                showToast("Failed to send message");
            }
            else{
                document.getElementById("chatInput").value = "";//only empty the input field when the msg is sucessfully sent ,otherwise keep it the user would retry

                //also refetch the data to observer the latest changes , refetchthe data to boserve the changes 

                fetch_chat();
            }
        })
        .catch(error => {
            showToast("Error sending message");
        });

    }
    else{

        let url = `${API_BASE_URL}/send_message2`;

        let userdata = {
            emp_token: emp_token,
            user_accno: user_accno,
            message: message
        };

        fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(userdata)
        })
        .then(response => {
            if (!response.ok) {
                showToast("Failed to send message");
            }
            else {
                document.getElementById("chatInput").value = "";
                fetch_chat();
            }
        })
        .catch(error => {
            showToast("Error sending message");
        });

    }

    

    
}