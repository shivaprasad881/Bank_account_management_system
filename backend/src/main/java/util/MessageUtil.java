package util;

import java.util.*;

public class MessageUtil {
    public static boolean issensitivedata(String s){

        s = s.toLowerCase();

        String[] words = s.split(" ");

        //for basic we would find the numbers 

        //pin - 4/6 digit
        //otp - 4/6
        // >10 accno

        String[] keywords = {"pin","atm-pin","atmpin","password","otp"};

        HashSet<String> set = new HashSet<>();


        for(int i=0;i<keywords.length;i++){
            set.add(keywords[i]);
        }
        

        for(String word:words){

            if(word.matches("^[0-9]+$")){

                //a number

                if(word.length()==4 || word.length()==6 || word.length()>10){
                    //suspicious number
                    return true;
                }
            }
            else{
                //its a word - check for existance of any keywords

                if(set.contains(word)){
                    //some sensitive keyword is present - reject
                    return true;
                }


            }
        }

        return false;
    }
}
