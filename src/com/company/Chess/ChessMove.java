package com.company.Chess;

import com.company.Data;
import com.company.GameMove;

public class ChessMove extends GameMove {

    private int from;
    private int to;
    private int eat;

    public ChessMove(int _from, int _to, int _eat) {
        from = _from;
        to = _to;
        eat = _eat;
    }

    public int getFrom() {
        return from;
    }

    public int getTo()
    {
        return to;
    }
    public int getEat()
    {
        return eat;
    }
    public ChessMove clone()
    {
        return new ChessMove(from,to,eat);
    }
    public void print()
    {
        System.out.println("move " + from + " -> " + to + " (" + eat + ")");
    }


    public void setFrom(int _from)
    {
        from = _from;
    }
    public void setTo(int _to)
    {
        to = _to;
    }
    public void setEat(int _eat)
    {
        eat = _eat;
    }

    public void opeLoc()
    {
        setFrom(Data.locOpisade(from));
        setTo(Data.locOpisade(to));
        setEat(-eat);
    }
    public String toString()
    {
        return from + " -> " + to + " (" + eat + ")";
    }
}
