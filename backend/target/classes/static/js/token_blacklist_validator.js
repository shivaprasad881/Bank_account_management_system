function check_token_in_blacklist(tokenn){// boolean

    let url = `${API_BASE_URL}/validate_user_token?token=${tokenn}`;

        return fetch(url, {
            method: 'GET'
        })
        .then(response => {
            if(response.status == 200) {
                return response.text();
            } else {
                //its an expired token
                //console.log("its not a valid token - caused for rejection")
                return "reject";
            }
        })
        .then(data => { 
            //it is live token - now we would look whether it is valid or not

            if(data=="reject"){

                //console.log("the token is present in the blacklist - caused for rejection")

                return false;


            }
            else{
                //only when

                return true;
            }
        
        })

        .catch(error => {
            //showToast("Error in validating the user_token with black_list !!");
            return false;
        });



}//check token