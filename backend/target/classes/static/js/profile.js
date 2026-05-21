const params = new URLSearchParams(window.location.search);
const token = params.get("token");


    url = "http://localhost:8080/user_details?token=" + token;

    fetch(url, {
        method: 'GET'
    })
    .then(response => {
        if(response.status == 200) {
            return response.text();
        } else {
            showToast("Invalid token !!");
        }
    })
    .then(data => {

        let userdata = data.split(",");
        
        let uaccno = userdata[0];
        let uname = userdata[1];
        let age = userdata[2];
        let city = userdata[3];
        let phone = userdata[4];
        
        document.getElementById("accno").innerHTML = uaccno;
        document.getElementById("uname").innerHTML = uname;
        document.getElementById("age").innerHTML = age;
        document.getElementById("city").innerHTML = city;
        document.getElementById("phonenumber").innerHTML = phone;
    })
    .catch(error => {
        showToast("error in fetching the user details !!");
    });
