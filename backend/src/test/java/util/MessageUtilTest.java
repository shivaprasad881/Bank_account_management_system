package util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public class MessageUtilTest {

    @BeforeAll
    public static void setupOnce() {
        // runs once before all tests
        System.out.println("we successfully started testing our application....");
    }

    @BeforeEach
    public void setup() {
        // runs before every test
        System.out.println("we started our current test case...");
    }

    // ── Keyword alone, no number → NOT sensitive ──
    @Test public void test_keyword_pin_alone()          { assertFalse(MessageUtil.issensitivedata("PIN")); }
    @Test public void test_keyword_pin_lowercase()      { assertFalse(MessageUtil.issensitivedata("pin")); }
    @Test public void test_keyword_pin_dashes()         { assertFalse(MessageUtil.issensitivedata("P-I-N")); }
    @Test public void test_keyword_pin_dots()           { assertFalse(MessageUtil.issensitivedata("p.i.n")); }
    @Test public void test_keyword_pin_spaces()         { assertFalse(MessageUtil.issensitivedata("p i n")); }
    @Test public void test_keyword_pin_leet()           { assertFalse(MessageUtil.issensitivedata("P1N")); }
    @Test public void test_keyword_password_alone()     { assertFalse(MessageUtil.issensitivedata("password")); }
    @Test public void test_keyword_password_upper()     { assertFalse(MessageUtil.issensitivedata("PASSWORD")); }
    @Test public void test_keyword_password_leet()      { assertFalse(MessageUtil.issensitivedata("p@ssword")); }
    @Test public void test_keyword_password_dash()      { assertFalse(MessageUtil.issensitivedata("pass-word")); }
    @Test public void test_sentence_whats_password()    { assertFalse(MessageUtil.issensitivedata("what's your password?")); }
    @Test public void test_sentence_dont_share_pin()    { assertFalse(MessageUtil.issensitivedata("don't share your pin with anyone")); }

    // ── Keyword + number together → sensitive ──
    @Test public void test_pin_with_number()            { assertTrue(MessageUtil.issensitivedata("my pin is 552817")); }
    @Test public void test_password_with_number()       { assertTrue(MessageUtil.issensitivedata("the customer's password is 4521")); }
    @Test public void test_pin_colon_number()           { assertTrue(MessageUtil.issensitivedata("PIN: 7788")); }
    @Test public void test_otp_with_number()            { assertTrue(MessageUtil.issensitivedata("otp is 918273")); }
    @Test public void test_accno_with_number()          { assertTrue(MessageUtil.issensitivedata("share the accno 1234567890123")); }
    @Test public void test_leet_password_with_number()  { assertTrue(MessageUtil.issensitivedata("p@ssword is 9911")); }
    //@Test public void test_spaced_pin_with_number()     { assertTrue(MessageUtil.issensitivedata("p i n 552817")); }

    // ── Bare suspicious numbers → NOT sensitive (number alone not enough) ──
    @Test public void test_bare_6digit_number()         { assertFalse(MessageUtil.issensitivedata("552817")); }
    @Test public void test_bare_4digit_number()         { assertFalse(MessageUtil.issensitivedata("4521")); }
    @Test public void test_two_4digit_numbers()         { assertFalse(MessageUtil.issensitivedata("7788 6699")); }
    @Test public void test_number_in_sentence()         { assertFalse(MessageUtil.issensitivedata("customer said 918273 just now")); }
    @Test public void test_long_accno_number()          { assertFalse(MessageUtil.issensitivedata("1234567890123")); }

    // ── Safe messages ──
    @Test public void test_safe_lunch()                 { assertFalse(MessageUtil.issensitivedata("hey are you free for lunch")); }
    @Test public void test_safe_meeting()               { assertFalse(MessageUtil.issensitivedata("meeting at 3pm today")); }
    @Test public void test_safe_ticket()                { assertFalse(MessageUtil.issensitivedata("can you check ticket number 45")); }
    @Test public void test_safe_branch()                { assertFalse(MessageUtil.issensitivedata("the branch is open till 6pm")); }
    @Test public void test_safe_callme()                { assertFalse(MessageUtil.issensitivedata("call me back")); }
    @Test public void test_safe_employees()             { assertFalse(MessageUtil.issensitivedata("how many employees work here")); }
    @Test public void test_safe_report()                { assertFalse(MessageUtil.issensitivedata("send me the report by tomorrow")); }

    // ── Numbers with non-suspicious lengths → safe ──
    @Test public void test_safe_room_number()           { assertFalse(MessageUtil.issensitivedata("room 302")); }
    @Test public void test_safe_extension()             { assertFalse(MessageUtil.issensitivedata("extension 55")); }
    @Test public void test_safe_floor()                 { assertFalse(MessageUtil.issensitivedata("floor 12")); }
    @Test public void test_safe_id()                    { assertFalse(MessageUtil.issensitivedata("id 789")); }

    @AfterEach
    public void cleanup() {
        // runs after every test
        System.out.println("we ended our current test case...");
    }
    
    
    @AfterAll
    public static void endingmsg() {
        // runs once before all tests
        System.out.println("we successfully tested our application ...");
    }

}