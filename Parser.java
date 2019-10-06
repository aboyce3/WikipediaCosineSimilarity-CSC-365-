import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;


public class Parser {
    String URL;
    Document document;
    Elements links;
    int fileCount;
    ArrayList<WikiPage> table;

    public Parser(String URL) {
        this.URL = URL;
        try {
            this.document = Jsoup.connect(URL).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        links = document.select("a[href]");
        fileCount = new File("/Users/andrewboyce/IdeaProjects/csc365/src").list().length;
        table = new ArrayList();
    }

    //if there are 24 files (txt files and java code) then check if the websites are updated or create files based off
    // of a wikipedia link
    public void initialize() {
        File[] f = new File("/Users/andrewboyce/IdeaProjects/csc365/src").listFiles();
        if (fileCount >= 24) {
            Arrays.stream(f)
                    .filter(e -> e.getName().contains(".txt"))
                    .forEach(e -> {
                        try {
                            update("https://en.wikipedia.org/wiki/" + e.getName().substring(0, e.getName().length() - 4), e);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });
        } else {
            parse(Jsoup.connect(this.URL), this.URL);
            this.links.stream()
                    .filter(e -> e.attr("abs:href")
                            .contains("en.wikipedia.org/wiki/") &&
                            !e.attr("abs:href").contains("#") &&
                            !e.attr("abs:href").substring(30).contains("/") &&
                            !e.attr("abs:href").substring(30).contains(":") && !e.attr("abs:href")
                            .contains(this.URL))
                    .distinct()
                    .limit(19)
                    .forEach(e -> parse(Jsoup.connect(e.attr("abs:href")), e.attr("abs:href")));
        }
    }
    //grabs the date and then checks if it's updated
    public void update(String s, File f) {
        try {
            HashTable temp = new HashTable(8);
            Document doc = Jsoup.parse(f, "UTF-8", s);
            String page = doc.text().toLowerCase();
            String[] arr = page.split("\\W+");
            Arrays.sort(arr);
            Arrays.stream(arr).forEach(e -> temp.put(e, e));
            WikiPage webPage = new WikiPage(s);
            webPage.setTable(temp);
            String lastMod = doc.body().text().substring(doc.body().text().indexOf("This page was last edited on "), doc.body().text().indexOf("(UTC)."));
            lastMod = lastMod.substring(29, lastMod.indexOf(",")) + lastMod.substring(lastMod.indexOf("at ") + 2);
            String[] date = lastMod.split("\\s+");
            switch (date[1]) {
                case "January":
                    date[1] = "01";
                    break;
                case "February":
                    date[1] = "02";
                    break;
                case "March":
                    date[1] = "03";
                    break;
                case "April":
                    date[1] = "04";
                    break;
                case "May":
                    date[1] = "05";
                    break;
                case "June":
                    date[1] = "06";
                    break;
                case "July":
                    date[1] = "07";
                    break;
                case "August":
                    date[1] = "08";
                    break;
                case "September":
                    date[1] = "09";
                    break;
                case "October":
                    date[1] = "10";
                    break;
                case "November":
                    date[1] = "11";
                    break;
                case "December":
                    date[1] = "12";
                    break;
            }
            switch (date[0]) {
                case "1":
                    date[0] = "01";
                    break;
                case "2":
                    date[0] = "02";
                    break;
                case "3":
                    date[0] = "03";
                    break;
                case "4":
                    date[0] = "04";
                    break;
                case "5":
                    date[0] = "05";
                    break;
                case "6":
                    date[0] = "06";
                    break;
                case "7":
                    date[0] = "07";
                    break;
                case "8":
                    date[0] = "08";
                    break;
                case "9":
                    date[0] = "09";
                    break;
            }
            String parseDate = date[2] + "-" + date[1] + "-" + date[0] + " " + date[3];
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm");
            Date d1 = sdf.parse(parseDate);
            webPage.date = d1;
            table.add(webPage);
            checkUpdated(d1, s, f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Parses the web page and adds to an ArrayList of web pages
    private void parse(Connection website, String s) {
        try {
            String link = "/Users/andrewboyce/IdeaProjects/csc365/src/" + s.substring(29) + ".txt";
            File f = new File(link);
            HashTable temp = new HashTable(8);
            Document doc = Jsoup.parse(website.get().html());
            String page = doc.text().toLowerCase();
            String lastMod = doc.body().text().substring(doc.body().text().indexOf("This page was last edited on "), doc.body().text().indexOf("(UTC)."));
            lastMod = lastMod.substring(29, lastMod.indexOf(",")) + lastMod.substring(lastMod.indexOf("at ") + 2);
            String[] date = lastMod.split("\\s+");
            switch (date[1]) {
                case "January":
                    date[1] = "01";
                    break;
                case "February":
                    date[1] = "02";
                    break;
                case "March":
                    date[1] = "03";
                    break;
                case "April":
                    date[1] = "04";
                    break;
                case "May":
                    date[1] = "05";
                    break;
                case "June":
                    date[1] = "06";
                    break;
                case "July":
                    date[1] = "07";
                    break;
                case "August":
                    date[1] = "08";
                    break;
                case "September":
                    date[1] = "09";
                    break;
                case "October":
                    date[1] = "10";
                    break;
                case "November":
                    date[1] = "11";
                    break;
                case "December":
                    date[1] = "12";
                    break;
            }
            switch (date[0]) {
                case "1":
                    date[0] = "01";
                    break;
                case "2":
                    date[0] = "02";
                    break;
                case "3":
                    date[0] = "03";
                    break;
                case "4":
                    date[0] = "04";
                    break;
                case "5":
                    date[0] = "05";
                    break;
                case "6":
                    date[0] = "06";
                    break;
                case "7":
                    date[0] = "07";
                    break;
                case "8":
                    date[0] = "08";
                    break;
                case "9":
                    date[0] = "09";
                    break;
            }
            String parseDate = date[2] + "-" + date[1] + "-" + date[0] + " " + date[3];
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm");
            Date d1 = sdf.parse(parseDate);
            String[] arr = page.split("\\W+");
            Arrays.sort(arr);
            Arrays.stream(arr).forEach(e -> temp.put(e, e));
            WikiPage webPage = new WikiPage(s);
            webPage.setTable(temp);
            webPage.date = d1;
            f.createNewFile();
            FileWriter writer = new FileWriter(link);
            writer.write(Jsoup.connect(s).get().text());
            writer.close();
            table.add(webPage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public WikiPage parseInput(Connection website) {
        try {
            HashTable temp = new HashTable(8);
            Document doc = Jsoup.parse(website.get().html());
            String page = doc.text().toLowerCase();
            String[] arr = page.split("\\W+");
            Arrays.sort(arr);
            Arrays.stream(arr).forEach(e -> temp.put(e, e));
            WikiPage webPage = new WikiPage(doc.attr("abs:href"));
            webPage.setTable(temp);
            return webPage;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //checks the date for each web page and updates
    public void checkUpdated(Date date, String URL, File f) {
        try {
            Document t = Jsoup.connect(URL).get();
            String temp = t.body().text().substring(t.body().text().indexOf("This page was last edited on "), t.body().text().indexOf("(UTC)."));
            temp = temp.substring(29, temp.indexOf(",")) + temp.substring(temp.indexOf("at ") + 2);
            String[] arr = temp.split("\\s+");
            switch (arr[1]) {
                case "January":
                    arr[1] = "01";
                    break;
                case "February":
                    arr[1] = "02";
                    break;
                case "March":
                    arr[1] = "03";
                    break;
                case "April":
                    arr[1] = "04";
                    break;
                case "May":
                    arr[1] = "05";
                    break;
                case "June":
                    arr[1] = "06";
                    break;
                case "July":
                    arr[1] = "07";
                    break;
                case "August":
                    arr[1] = "08";
                    break;
                case "September":
                    arr[1] = "09";
                    break;
                case "October":
                    arr[1] = "10";
                    break;
                case "November":
                    arr[1] = "11";
                    break;
                case "December":
                    arr[1] = "12";
                    break;
            }
            switch (arr[0]) {
                case "1":
                    arr[0] = "01";
                    break;
                case "2":
                    arr[0] = "02";
                    break;
                case "3":
                    arr[0] = "03";
                    break;
                case "4":
                    arr[0] = "04";
                    break;
                case "5":
                    arr[0] = "05";
                    break;
                case "6":
                    arr[0] = "06";
                    break;
                case "7":
                    arr[0] = "07";
                    break;
                case "8":
                    arr[0] = "08";
                    break;
                case "9":
                    arr[0] = "09";
                    break;
            }
            String parseDate = arr[2] + "-" + arr[1] + "-" + arr[0] + " " + arr[3];
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm");
            Date d1 = sdf.parse(parseDate);
            if (!d1.equals(date)) {
                File[] files = new File("/Users/andrewboyce/IdeaProjects/csc365/src").listFiles();
                for (File file : files) if (file.getName().contains(".txt")) file.delete();
                initialize();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

