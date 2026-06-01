function user_signup() {
    let uname = document.getElementById('uname').value;
    let age = document.getElementById('age').value;
    let city = document.getElementById('city').value;
    let phonenumber = document.getElementById('phonenumber').value;
    let password = document.getElementById('password').value;

    console.log("password being sent: " + password);

    if(uname == "" || age == "" || parseInt(age) <= 0 || city == "" || phonenumber == "" || phonenumber.length != 10 || password == "") {
        showToast("please enter valid details !!");
    }
    else if( password_strength(password)==false ){
        showToast("please enter Strong Password !!",1500);

        setTimeout(() => {
            showToast("Strong password -:-\n\n * Atleast one small alphabet \n * Atleast one big alphabet \n * Atleast one digit \n * Atleast one special character \n * Atleast 8 length  ",8000)
        }, 1500);
    }
    else {
        let url = "http://localhost:8080/register";

        let userData = {
            uname: uname, 
            age: age,
            city: city,
            phonenumber: phonenumber,
            password : password
        };

        fetch(url, {
            method: 'post',
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(userData)
        })
        .then(response => response.text())
        .then(data => {
            showToast(data)

            setTimeout(() => {
                window.location.href = "signin.html";
            }, 3000)

        })
        .catch(error => {
            showToast("error in registration !!");
        });
    }
}