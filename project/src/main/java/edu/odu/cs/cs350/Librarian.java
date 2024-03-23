package edu.odu.cs.cs350;

import java.io.IOException;
import java.util.Vector;
import java.util.ListIterator;

/**
 * The Librarian is responsible for determining what parts of the given input
 * need to evaluated and the actions that will be taken on these sections of
 * data in order to extract meta data. The Librarian will then reconstruct the
 * input back together and include the PER tags around personal names
 * that are found.
 * 
 * The input may be at most a page of text (4000 characters), or 50 lines of
 * 80 characters per line. Any input exceeding this amout will not be accepted,
 * and will throw an exception.
 * 
 * The Librarian takes looks for the NER tags ... (FILL THIS IN WHOEVER
 * HAS THIS STORY)
 */
public class Librarian {

    /**
     * Collection of each part of input string that is surrounded by
     * NER tags.
     */
    private Vector<Document> inputDocuments;

    private String inputPage;

    /**
     * 
     * @param inputPage is the string given by the user
     * 
     * @throws IOException if input is larger than 4000 characters
     */
    public Librarian(String inputPage) throws IOException {
        this.inputPage = inputPage;

        inputDocuments = new Vector<Document>();

        if (inputPage.length() > 4000)
            throw new IOException();
        else {
            if (inputPage != "") {
                Document document = new Document(inputPage);
                inputDocuments.add(document);
            }
        }
        // THIS MAY BE MODIFIED TO ONLY CREATE A DOCUMENT FROM TEXT WITH NER TAG
    }

    /**
     * 
     * @return true if one or more Documents have been added to collection
     */
    public boolean hasDocuments() {
        if (inputDocuments.size() > 0)
            return true;
        return false;
    }

    /**
     * 
     * @param index of the vector of Documents that is being retrived
     * @return the document object at that index
     */
    public Document getDocumentAt(int index) {
        return inputDocuments.elementAt(index);
    }

    /**
     * Returns the substring text between <NER> and </NER>.
     */
    public String getTextBetweenNERTags(String inputText) {

        String NER_TAG_START = "<NER>";
        String NER_TAG_END = "</NER>";

        int indexNERStart = 0;
        int indexNEREnd = 0;

        if (inputText.contains(NER_TAG_START) == true) {
            indexNERStart = getTagIndex(inputText, NER_TAG_START);
            indexNERStart = indexNERStart + NER_TAG_START.length();
        }

        if (inputText.contains(NER_TAG_END) == true) {
            indexNEREnd = getTagIndex(inputText, NER_TAG_END);
        }

        return inputText.substring(indexNERStart, indexNEREnd);
    }

    /*
     * Returns index of "tag" within "inputString".
     */
    public int getTagIndex(String inputString, String tag) {
        return inputString.indexOf(tag);
    }

    /**
     * @return The input document with any changes made
     */
    @Override
    public String toString() {
        StringBuilder markedUp = new StringBuilder();

        // Add any part of the inputString without NER as is
        // Any part with NER needs to come from the collection
        // that already has been marked up
        // All NER tags need to be preserved
        Document onlyDoc = inputDocuments.get(0);
        for (Token token : onlyDoc) {
            markedUp.append(token.getValue() + " ");
        }

        return markedUp.toString();
    }

    /**
     * 
     */
    public void markNames() throws IOException {
        for (Document document : inputDocuments) {
            ListIterator<Token> nextToken = document.iterator();
            while (nextToken.hasNext()) {
                ListIterator<Token> previous = nextToken;
                Token tokenValue = nextToken.next();
                if (needsTagBefore(tokenValue))
                    previous.add(new Token("<PER>"));
                else if (needsTagAfter(tokenValue))
                    nextToken.add(new Token("</PER>"));

            }
        }

    }

    private boolean needsTagBefore(Token nextToken) {
        return nextToken.getCommonFirstName();
    }

    private boolean needsTagAfter(Token nextToken) {
        return nextToken.getCommonLastName();
    }
}
