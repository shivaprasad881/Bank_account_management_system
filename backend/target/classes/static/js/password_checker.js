function password_strength(password) {
    let hasUpper = false;
    let hasLower = false;
    let hasNumber = false;
    let hasSpecial = false;
    
    for(let i = 0; i < password.length; i++) {
        let ch = password[i];
        
        if(ch >= 'A' && ch <= 'Z') hasUpper = true;
        else if(ch >= 'a' && ch <= 'z') hasLower = true;
        else if(ch >= '0' && ch <= '9') hasNumber = true;
        else hasSpecial = true;
    }
    
    return password.length >= 8 && hasUpper && hasLower && hasNumber && hasSpecial;
}