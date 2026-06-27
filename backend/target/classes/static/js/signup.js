function user_signup() {
    let uname = document.getElementById('uname').value;
    let age = document.getElementById('age').value;
    let city = document.getElementById('city').value;
    let phonenumber = document.getElementById('phonenumber').value;
    
    let password = document.getElementById('password').value;

    //console.log("password being sent: " + password);

    if(uname == "" || age == ""  || city == "" || phonenumber == ""  || password == "") {
        showToast("please fill the  details !!");
    }
    else if( parseInt(age) <= 0 || parseInt(age) >= 150 ){
        showToast("please enter valid Age !!");
    }
    else if( phonenumber.length != 10  ){
        showToast("please enter valid Phonenumber !!");
    }
    else if( password_strength(password)==false ){
        showToast("please enter Strong Password !!",1500);

        setTimeout(() => {
            showToast("Strong password -:-\n\n * Atleast one small alphabet \n * Atleast one big alphabet \n * Atleast one digit \n * Atleast one special character \n * Atleast 8 length  ",8000)
        }, 1500);
    }
    else {

        sessionStorage.setItem('uname', uname);
        sessionStorage.setItem('age', age);
        sessionStorage.setItem('city', city);
        sessionStorage.setItem('phonenumber', phonenumber);
        
        sessionStorage.setItem('password', password);

        //email registration
        sessionStorage.setItem('purpose', 'registration')
        window.location.href = "emailvalidator.html"





        
    }
}