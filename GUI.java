import org.jsoup.Jsoup;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class GUI {

    public static void main(String[] args) {
        Parser p = new Parser("https://en.wikipedia.org/wiki/Programming_Language");
        p.initialize();
        //Add elements to the GUI
        JFrame j = new JFrame("Wiki GUI");
        JTextField input = new JTextField();
        JButton enter = new JButton("Enter");
        JLabel label = new JLabel("Enter URL Here:");
        JLabel author = new JLabel("Project by Andrew Boyce");
        JTextArea results = new JTextArea();
        j.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        j.setResizable(false);
        //Listener for the enter button
        enter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!input.getText().isEmpty()) {
                    try {
                        //resets the results after enter is clicked
                        results.setText("");
                        ArrayList<Double> ar = new ArrayList();
                        WikiPage page = p.parseInput(Jsoup.connect(input.getText()));
                        for(WikiPage wiki : p.table) wiki.cosine = wiki.cosineSimilarity(page.words, wiki.words);
                        for(WikiPage wiki : p.table) ar.add(wiki.cosine);
                        Collections.sort(ar);
                        Collections.reverse(ar);
                        //Sorts the cosine similarities in an ArrayList, then looks up the page with the same score
                        // and appends to the results
                        for(int i = 0; i < 5; i++){
                            for(WikiPage wikiPage : p.table){
                            if(wikiPage.cosine == ar.get(i)) results.append((i+1) + ".) " +wikiPage.URL + "\n" + "Score: " + wikiPage.cosine + "\n");
                            }
                        }

                    } catch (Exception er) {
                        er.printStackTrace();
                    }
                }
            }
        });
        j.setBackground(Color.BLUE);
        author.setBounds(125, 460, 400, 20);
        results.setBounds(0, 60, 400, 400);
        label.setBounds(0, 0, 400, 20);
        input.setBounds(0, 20, 400, 20);
        enter.setBounds(0, 40, 400, 20);
        j.add(enter);
        j.add(input);
        j.add(label);
        j.add(results);
        j.add(author);
        j.setSize(400, 500);
        j.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        j.setResizable(false);
        j.setLayout(null);
        j.setVisible(true);
    }
}
