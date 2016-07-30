package com.empire.vince.crucom.login;

/**
 * Created by maditsha on 7/30/2016.
 */
public class Wins {
    private String name;
    private String winmeth;
    private String email;
    private String password;
    private String no;
    private String winner;

    public Wins(String name, String winmeth, String email, String password, String no, String winner) {
        this.name = name;
        this.winmeth = winmeth;
        this.email = email;
        this.password = password;
        this.no = no;
        this.winner = winner;
    }

    public String getName() {
        return name;
    }

    public String getWinmeth() {
        return winmeth;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getNo() {
        return no;
    }

    public String getWinner() {
        return winner;
    }
}
