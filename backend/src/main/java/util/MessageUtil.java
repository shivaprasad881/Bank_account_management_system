package util;

import java.util.*;

public class MessageUtil {
    public static boolean issensitivedata(String s){

        String original = s.toLowerCase();

        

        //for basic we would find the numbers 

        //pin - 4/6 digit
        //otp - 4/6
        // >10 accno

        String[] keywords = {"pin","atmpin","atm-pin","password","pass-word","otp","accno"};
        HashSet<String> set = new HashSet<>();
        for(int i=0;i<keywords.length;i++){
            set.add(keywords[i]);
        }


        //1.original words matching
        String[] words = original.split(" ");
        //printwords(words);
        boolean res1 = wordmatching(words,set);
        

        

        

        //2.apply normalization

        // i.remove puntuation - then match

        String nopun = original.replaceAll("[^a-zA-Z0-9 ]", "");
        //printwords(nopun.split(" "));
        boolean res2 = wordmatching(nopun.split(" "),set);


        // ii.leetspace - replace common alternatives (@->a,$->s)
        // we should apply these changes only for words , so need to keep the original number unaltered

        String[] updated_words = applychangestowords(nopun.split(" "));
        //printwords(updated_words);
        boolean res3 = wordmatching(updated_words,set);



        return res1 || res2 || res3;


    }
    static String[] applychangestowords(String[] words){

        for(int i=0;i<words.length;i++){

            

            if(!words[i].matches("^[0-9]+$")){

                //its a word
                //System.out.println("\n before : "+words[i]+"  ");
                words[i] = words[i].replaceAll("@", "a");
                //words[i] = words[i].replaceAll("$", "s");
                words[i] = words[i].replaceAll("1", "i");
                words[i] = words[i].replaceAll("0", "o");
                //System.out.println("\n after : "+words[i]+"  ");
            }
            // else{
            //     System.out.println("\n pure number : "+words[i]+"  ");
            // }

            
        }

        return words;

    }
    static boolean wordmatching(String[] words,HashSet<String> set ){
        boolean keyword = false;
        boolean number = false;

        for(String word:words){

            if(word.matches("^[0-9]+$")){

                //a number

                if(word.length()==4 || word.length()==6 || word.length()>10){
                    //suspicious number
                    number = true;
                }
            }
            else{
                //its a word - check for existance of any keywords

                if(set.contains(word)){
                    //some sensitive keyword is present - reject
                    keyword = true;
                }


            }
        }

        return number && keyword;
    }

    public static void printwords(String[] words) {
        System.out.println();
        for (String word : words) {
            System.out.println(word);
        }
        System.out.println();
    }

    public static void autotesting(){

        HashMap<String, Boolean> testCases = new HashMap<>();

        // ── Keyword alone, no number → NOT harmful, nothing was actually leaked ──
        testCases.put("PIN", false);
        testCases.put("pin", false);
        testCases.put("P-I-N", false);
        testCases.put("p.i.n", false);
        testCases.put("p i n", false);
        testCases.put("P1N", false);
        testCases.put("password", false);
        testCases.put("PASSWORD", false);
        testCases.put("p@ssword", false);
        testCases.put("pass-word", false);
        testCases.put("what's your password?", false);
        testCases.put("don't share your pin with anyone", false);

        // ── Keyword + number together → confidently sensitive ──
        testCases.put("my pin is 552817", true);
        testCases.put("the customer's password is 4521", true);
        testCases.put("PIN: 7788", true);
        testCases.put("otp is 918273", true);
        testCases.put("share the accno 1234567890123", true);
        testCases.put("p@ssword is 9911", true);
        testCases.put("p i n 552817", true);

        // ── Bare suspicious numbers, no keyword → still risky (accidental leak case) ──
        testCases.put("552817", true);
        testCases.put("4521", true);
        testCases.put("7788 6699", true);
        testCases.put("customer said 918273 just now", true);
        testCases.put("1234567890123", true);
        testCases.put("5 5 2 8 1 7", true);
        testCases.put("1 2 3 4", true);

        // ── Safe / non-sensitive messages ──
        testCases.put("hey are you free for lunch", false);
        testCases.put("meeting at 3pm today", false);
        testCases.put("can you check ticket number 45", false);
        testCases.put("the branch is open till 6pm", false);
        testCases.put("call me back", false);
        testCases.put("how many employees work here", false);
        testCases.put("send me the report by tomorrow", false);

        // ── Numbers that don't match suspicious lengths (not 4/6/10+ digits) → safe ──
        testCases.put("room 302", false);
        testCases.put("extension 55", false);
        testCases.put("floor 12", false);
        testCases.put("id 789", false);

        int matched = 0;
        //now test one at a time,and log the response
        System.out.println("message             expected_output         current_output");
        System.out.println();
        for(String message:testCases.keySet()){
            boolean expected_output = testCases.get(message);

            boolean current_output = issensitivedata(message);

            if(expected_output==current_output){
                matched++;
            }

            //log the result
            System.out.println(message+"    "+expected_output+"    "+current_output);
            System.out.println();
        }
        System.out.println("matched : "+matched+"  unmatched : "+(testCases.size()-matched));
        System.out.println();
    }
}
