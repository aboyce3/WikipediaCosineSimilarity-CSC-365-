import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class Algorithm {
    ArrayList<WikiPage> everyPage;
    HashMap<WikiPage, ArrayList<WikiPage>> sets;

    public Algorithm(ArrayList<WikiPage> p) {
        everyPage = p;
        sets = categories();
    }

    public HashMap<WikiPage, ArrayList<WikiPage>> getSets() {
        return sets;
    }

    public HashMap<WikiPage, ArrayList<WikiPage>> categories() {
        HashMap<WikiPage, ArrayList<WikiPage>> pages = new HashMap<>();
        WikiPage first, second, third, forth, fifth;

        //Starts with 5 random pages
        first = everyPage.get(0);
        second = everyPage.get(99);
        third = everyPage.get(199);
        forth = everyPage.get(299);
        fifth = everyPage.get(399);

        pages.put(first, new ArrayList<>());
        pages.put(second, new ArrayList<>());
        pages.put(third, new ArrayList<>());
        pages.put(forth, new ArrayList<>());
        pages.put(fifth, new ArrayList<>());

        //These five pages are considered "Medioids"
        first.medioid = true;
        second.medioid = true;
        third.medioid = true;
        forth.medioid = true;
        fifth.medioid = true;

        for (int j = 0; j < everyPage.size(); j++) {
            WikiPage page = everyPage.get(j);

            //The most similar page goes to its respective list
            double cosOne = page.cosineSimilarity(first.words, page.words);
            double cosTwo = page.cosineSimilarity(second.words, page.words);
            double cosThree = page.cosineSimilarity(third.words, page.words);
            double cosFour = page.cosineSimilarity(forth.words, page.words);
            double cosFive = page.cosineSimilarity(fifth.words, page.words);

            if (cosOne > cosTwo && cosOne > cosThree && cosOne > cosFour && cosOne > cosFive &&
                    !page.equals(first) && !page.equals(second) && !page.equals(third) && !page.equals(forth) && !page.equals(fifth))
                pages.get(first).add(page);
            else if (cosTwo > cosOne && cosTwo > cosThree && cosTwo > cosFour && cosTwo > cosFive && !second.equals(page) &&
                    !page.equals(first) && !page.equals(second) && !page.equals(third) && !page.equals(forth) && !page.equals(fifth))
                pages.get(second).add(page);
            else if (cosThree > cosOne && cosThree > cosTwo && cosThree > cosFour && cosThree > cosFive && !third.equals(page) &&
                    !page.equals(first) && !page.equals(second) && !page.equals(third) && !page.equals(forth) && !page.equals(fifth))
                pages.get(third).add(page);
            else if (cosFour > cosOne && cosFour > cosTwo && cosFour > cosThree && cosFour > cosFive && !forth.equals(page) &&
                    !page.equals(first) && !page.equals(second) && !page.equals(third) && !page.equals(forth) && !page.equals(fifth))
                pages.get(forth).add(page);
            else if (!fifth.equals(page) && !page.equals(first) && !page.equals(second) &&
                    !page.equals(third) && !page.equals(forth) && !page.equals(fifth))
                pages.get(fifth).add(page);
        }

        //Sorts in order of similarity
        Collections.sort(pages.get(first), Comparator.comparing(o -> o.cosineSimilarity(first.words, o.words)));
        Collections.sort(pages.get(second), Comparator.comparing(o -> o.cosineSimilarity(second.words, o.words)));
        Collections.sort(pages.get(third), Comparator.comparing(o -> o.cosineSimilarity(third.words, o.words)));
        Collections.sort(pages.get(forth), Comparator.comparing(o -> o.cosineSimilarity(forth.words, o.words)));
        Collections.sort(pages.get(fifth), Comparator.comparing(o -> o.cosineSimilarity(fifth.words, o.words)));

        //Collection of 5 lists that are disjoint from each other
        return pages;
    }
}