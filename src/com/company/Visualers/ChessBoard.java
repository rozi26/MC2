package com.company.Visualers;

import com.company.Chess.ChessMove;
import com.company.Data;
import com.company.Game;
import com.company.GameMC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChessBoard {

    GameMC model;
    boolean sigmoid;
    int prevLoc = -1;
    char[] tools;
    char[] _colors;
    JButton[] blocks;
    JLabel text2;
    public ChessBoard(GameMC _model, boolean _sigmoid) {
        model = _model;
        sigmoid = _sigmoid;
        JFrame frame = new JFrame();
        frame.setBackground(Color.GRAY);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1070, 1232);
        frame.enableInputMethods(false);
        frame.setVisible(true);
        JLabel text = new JLabel("sigmoid = " + sigmoid + "    model simple acrracy = " + _model.runSimulation(1000,1,_sigmoid));
        text.setSize(300,20);
        text.setVisible(true);
        frame.add(text);
        text2 = new JLabel("hello");
        text2.setLocation(0,20);
        text2.setSize(200,132);
        text2.setVisible(true);
        frame.add(text2);
        final String order = "jhiklihjggggggggnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnaaaaaaaadbcefcbd";
        tools = new char[64];
        _colors = new char[64];
        for (int i = 0; i < 64; i++) {
            tools[i] = order.charAt(i);
        }
        blocks = new JButton[65];
        for (int i = 0; i < 65; i++) {
            blocks[i] = new JButton();
            blocks[i].setSize(132, 132);
            blocks[i].setLocation((i % 8) * 132, (i / 8 + 1) * 132);
            blocks[i].setVisible(true);
            blocks[i].setCursor(Cursor.getDefaultCursor());
            blocks[i].setContentAreaFilled(false);
            blocks[i].addActionListener(a);
            blocks[i].setOpaque(false);
            blocks[i].setContentAreaFilled(false);
            blocks[i].setBorderPainted(false);
            frame.add(blocks[i]);

        }
        updateBoard(tools, clearColor());
    }

    ActionListener a = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            boolean clear = true;
            int loc = findClick(e.getSource());
            if (_colors[loc] == 'y' || _colors[loc] == 'g') {
                playMove(new ChessMove(prevLoc,loc,0));
                gameOver();
                loc = -1;
                playModelMove();
                gameOver();
            }
            else if(prevLoc == loc)
                loc = -1;
            else if (Data.isWhite(tools[loc])) {
                updateBoard(clearColor());
                char[] move = Data.doMove(tools, loc);
                for (int i = 0; i < 64; i++) {
                    if (move[i] != 'n')
                    {
                        blocks[i].setIcon(Data.getImage(tools[i], move[i]));
                        _colors[i] = move[i];
                    }
                }
                clear = false;
            }
            prevLoc = loc;
            if(clear)
                updateBoard(clearColor());
        }
    };

    private int findClick(Object o) {
        for (int i = 0; i < 64; i++) {
            if (o.equals(blocks[i]))
                return i;
        }
        return -1;
    }

    private char[] clearColor() {
        char[] color = new char[64];
        for (int i = 0; i < 64; i++) {
            if ((i / 8) % 2 == 0)
                color[i] = ((i % 2 == 0) ? 'w' : 'b');
            else
                color[i] = ((i % 2 == 1) ? 'w' : 'b');
        }
        return color;
    }

    public void updateBoard(char[] board, char[] color) {
        for (int i = 0; i < 64; i++) {
            _colors[i] = color[i];
            blocks[i].setIcon(Data.getImage(board[i], color[i]));
        }
    }

    public void updateBoard(char[] color) {
        for (int i = 0; i < 64; i++) {
            _colors[i] = color[i];
            blocks[i].setIcon(Data.getImage(tools[i], color[i]));
        }
    }

    private void playModelMove()
    {
        Data.toolReverser(tools);
        ChessMove a = (ChessMove)model.getModelMove(new Game(Data.charToolsToInt(tools),1));
        // text2.setText("model move " + a.getFrom() + " -> " + a.getTo() + " (" + a.getEat() + ")");
        playMove(a);
        Data.toolReverser(tools);
        updateBoard(tools,clearColor());
        //text2.setText(model.getMoveCalculaiton(model.charToIntBoard(tools),sigmoid));
    }
    private void playMove(ChessMove move)
    {
        blocks[1].setIcon(Data.getImage('a','w'));
        tools[move.getTo()] = tools[move.getFrom()];
        tools[move.getFrom()] = 'n';
    }
    private void gameOver()
    {
        boolean thereWhite = false;
        boolean thereBlack = false;
        for(int i = 0; i < 64; i++)
        {
            if(tools[i] == 'f')
                thereWhite = true;
            if(tools[i] == 'l')
                thereBlack = true;
        }
        if (!(thereBlack && thereWhite))
        {
            final String order = "jhiklihjggggggggnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnaaaaaaaadbcefcbd";
            for(int i = 0; i < 64; i++)
            {
                tools[i] = order.charAt(i);
            }
            updateBoard(tools,clearColor());
        }
    }
}
