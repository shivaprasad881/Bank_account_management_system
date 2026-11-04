const token = sessionStorage.getItem("token")

console.log("dashboard token is : ",token)

function userdetails(){

    let url = `${API_BASE_URL}/user_details?token=${token}`;

    fetch(url, {
        method: 'GET'
    })
    .then(response => {
        if(response.status == 200) {
            return response.text();
        } else {
            showToast("Unauthorized request !!")
        }
    })
    .then(data => {

        let userdata = data.split(",");
        
        let uaccno = userdata[0];
        let uname = userdata[1];
        let age = userdata[2];
        let city = userdata[3];
        let phone = userdata[4];
        let image_url = userdata[5];
        
        document.getElementById("accno").innerHTML = uaccno;
        document.getElementById("uname").innerHTML = uname;
        document.getElementById("age").innerHTML = age;
        document.getElementById("city").innerHTML = city;
        document.getElementById("phonenumber").innerHTML = phone;

        //now i brought the current image url of the user , so by using that image url fetch the image from cloudinary and render it in the user profile , as the image tag itself is capable of perfomring the fetch rquest just pass the ulr to it 

        document.getElementById("rendering_user_photo").src = image_url;//now we defined the image url in the image tag now it would ftch that url fand render the iamge  as the url  beong thoe user it would rend rer eimag 
    })
    .catch(error => {
        showToast("error in fetching the user details !!");
    });

}

userdetails()


function upload_image(){


    let imagefile = document.getElementById('uploaded_image').files[0];
    let url = `${API_BASE_URL}/upload_image?token=${token}`;

    let formData = new FormData();
    formData.append('image', imagefile);

    fetch(url, {
        method: 'POST',
        body: formData
    })

    .then(res => {
        if (!res.ok) {
            throw new Error("Upload failed: " + res.status);
        }
        return res.text();
    })

    .then(data => {
        showToast(data);

        //upon updating the user profile , refetch for latest user details , so upon changes the user would see the instintct changes on the apge , something changed , then fetch the lastest data to obsxever those changes 

        userdetails();
    })
    
    .catch(err => {
        console.error(err);
        showToast("Error uploading image");
    });




}