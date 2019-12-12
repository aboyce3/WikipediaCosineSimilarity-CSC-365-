import org.jsoup.Jsoup;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.*;

public class GUI {

    public static void main(String[] args) throws Exception {
        String s = "https://en.wikipedia.org/wiki/Music";
        File f = new File(System.getProperty("user.dir") + "/ParserOnDisk.ser");
        Parser parser;
        if (f.exists()) parser = readParser(f);
        else parser = new Parser(s);
        ArrayList<WikiPage> pages = parser.table;
        JFrame j = new JFrame("Wiki GUI");
        JTextField input = new JTextField();
        JTextField input2 = new JTextField();
        input.setText("Cosine/Cluster/Graph Input 1");
        input2.setText("Graph Input 2");
        JButton enter = new JButton("Cosine");
        JButton algorithm = new JButton("Cluster");
        JButton graph = new JButton("Graph");
        JLabel label = new JLabel("Please Enter a URL");
        JLabel author = new JLabel("Project by Andrew Boyce");
        JTabbedPane tab = new JTabbedPane();
        JTextArea results = new JTextArea();
        JScrollPane sp = new JScrollPane(results);
        Algorithm a = new Algorithm(parser.table);
        HashMap<WikiPage, ArrayList<WikiPage>> categories = a.getSets();
        WikiPage main = null;
        results.setText((new Graph(parser.table, parser.numberOfEdges,parser.edges,s).numDisjoints(s) != 1 ?
                        "There are more than one dijoint sets!" : "There is only one disjoint set!"));
        for(WikiPage p : parser.table)
            if(p.URL.equals(s)){
                main = p;
                break;
            }
        //Listener for the enter button
        enter.addActionListener(e -> {
            if (!input.getText().isEmpty()) {
                try {
                    results.setText("");
                    WikiPage page = parser.parseInput(Jsoup.connect(input.getText()));
                    Collections.sort(pages, Comparator.comparing(o -> o.cosineSimilarity(page.words, o.words)));
                    Collections.reverse(pages);
                    for(int i =0 ; i < 10; i++)
                        results.append((i+1)+ ".) "+ pages.get(i).URL + "\n" + "Cosine: " + pages.get(i).cosine+"\n");
                } catch (Exception er) {
                    results.setText("");
                    results.append("Invalid Link");
                }
            }
        });
        algorithm.addActionListener(e -> {
            results.setText("");
            int i = 1;
            for (WikiPage page : categories.keySet()) {
                results.append("[Cluster " + (i++) + "]\n");
                results.append("[Chosen Page : " + page.URL.substring(30) + "]\n");
                results.append("[Pages: ");
                for (int k = 0; k < categories.get(page).size(); k++)
                    results.append(categories.get(page).get(k).URL.substring(30) + "\n");
                results.append("]\n\n");
            }
        });
        graph.addActionListener(e -> {
            try{
                if(input.getText() != null && input2.getText() != null) {
                    results.setText("");
                    for(WikiPage p : parser.edges.keySet())
                        if(p.URL.equals(input.getText()))
                            for(WikiPage x : parser.table)
                                if(x.URL.equals(input2.getText())){
                                    results.append(new Graph(parser.table, parser.numberOfEdges,parser.edges,s).dijkstraShortestPath(p,x));
                                    parser.resetVisited();
                                }
                }
            }catch(Exception x){
                results.setText("");
                results.append("No Available Path");
                x.printStackTrace();
            }
        });
        author.setBounds(215, 480, 600, 20);
        sp.setBounds(0, 80, 620, 400);
        label.setBounds(235, 0, 600, 20);
        input.setBounds(0, 20, 600, 20);
        input2.setBounds(0, 40, 600, 20);
        enter.setBounds(0, 60, 200, 20);
        algorithm.setBounds(200, 60, 200, 20);
        graph.setBounds(400, 60, 200, 20);
        j.add(sp);
        j.add(input2);
        j.add(graph);
        j.add(enter);
        j.add(algorithm);
        j.add(input);
        j.add(label);
        j.add(author);
        j.add(tab);
        j.add(sp);
        j.setSize(600, 520);
        j.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        j.setResizable(false);
        j.setLayout(null);
        j.setVisible(true);
    }
    public static Parser readParser(File f) throws Exception {
        FileInputStream fis = new FileInputStream(f);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Parser s = (Parser) ois.readObject();
        ois.close();
        fis.close();
        return s;
    }
}