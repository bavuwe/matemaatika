/*
 * This file is part of the Matemaatika Minileksikon.
 * https://github.com/bavuwe/matemaatika
 *
 * Copyright (c) 2019 Bavuwe Software and contributors.
 * See CONTRIBUTORS.txt for details.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bavuwe.matemaatika;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Alternative parser that uses regular expressions to fetch
 * and group HTML content.
 */
public class MataHTMLParser {
    private final static String MATA_STRUCTURE_FILENAME = "mata.structure";
    private final static String MATA_INDEX_FILENAME = "mata.index";
    private final static String MATA_HTML_FILENAME = "mata.html";

    // the HTML content
    private String content;
    // regex pattern for getting the titles
    private Pattern headerPat;

    private List<String> classTitles; // h1 headers
    private List<String> topicTitles; // h2 headers
    private List<String> subTopicTitles; // h3 headers

    // arraylists determing the start end end indices for subtopic content
    // in the HTML file.
    private List<Integer> subTopicStarts;
    private List<Integer> subTopicEnds;

    // the listView of a class
    private List<ArrayList<Integer>> classTopics;
    // subtopics of listView
    private List<ArrayList<Integer>> topics;

    /**
     * Constructor.
     *
     * @param is The input stream to the HTML file. NB! it will be closed by the class.
     * @throws IOException In case the parser is unable to find the HTML file.
     */
    MataHTMLParser(InputStream is) throws IOException {
        classTitles = new ArrayList<>();
        topicTitles = new ArrayList<>();
        subTopicTitles = new ArrayList<>();
        subTopicStarts = new ArrayList<>();
        subTopicEnds = new ArrayList<>();
        classTopics = new ArrayList<>();
        topics = new ArrayList<>();

        headerPat = Pattern.compile("(<h1.*?</h1>)|(<h2.*?</h2>)|(<h3.*?</h3>)");

        content = readFile(is);
        is.close();
        parse();
    }

    /**
     * Load the stuff from already preprocessed data.
     *
     * @param structIs
     * @param htmlIs
     * @throws IOException
     */
    public MataHTMLParser(InputStream structIs, InputStream htmlIs) throws IOException {
        DataInputStream dis = new DataInputStream(structIs);
        int n, m;
        n = dis.readInt();
        classTitles = new ArrayList<>(n);
        for (int i = 0; i < n; ++i) {
            classTitles.add(dis.readUTF());
        }
        n = dis.readInt();
        topicTitles = new ArrayList<>(n);
        for (int i = 0; i < n; ++i) {
            topicTitles.add(dis.readUTF());
        }
        n = dis.readInt();
        subTopicTitles = new ArrayList<>(n);
        for (int i = 0; i < n; ++i) {
            subTopicTitles.add(dis.readUTF());
        }
        n = dis.readInt();
        classTopics = new ArrayList<>(n);
        for (int i = 0; i < n; ++i) {
            m = dis.readInt();
            ArrayList<Integer> a = new ArrayList<>(m);
            for (int j = 0; j < m; ++j) {
                a.add(dis.readInt());
            }
            classTopics.add(a);
        }
        n = dis.readInt();
        topics = new ArrayList<>(n);
        for (int i = 0; i < n; ++i) {
            m = dis.readInt();
            ArrayList<Integer> a = new ArrayList<>(m);
            for (int j = 0; j < m; ++j) {
                a.add(dis.readInt());
            }
            topics.add(a);
        }
        n = dis.readInt();
        subTopicStarts = new ArrayList<>(n);
        for (int i = 0; i < n; ++i) {
            subTopicStarts.add(dis.readInt());
        }
        n = dis.readInt();
        subTopicEnds = new ArrayList<>(n);
        for (int i = 0; i < n; ++i) {
            subTopicEnds.add(dis.readInt());
        }
        structIs.close();

        // read the HTML line
        BufferedReader br = new BufferedReader(new InputStreamReader(htmlIs));
        content = br.readLine();
        assert (br.readLine() == null); // make sure we read it all
        br.close();
    }

    /**
     * Export the HTML structure to "mata.structure" and also saved the condensed
     * mata.html. NB! It overwrites it. The resulting file has no newlines, so it
     * can be used directly for accesesign the interesting parts of the content.
     */
    public void exportStructure() {
        try {
            FileOutputStream fos = new FileOutputStream(new File(MATA_STRUCTURE_FILENAME));
            DataOutputStream dos = new DataOutputStream(fos);
            dos.writeInt(classTitles.size());
            for (String title : classTitles) {
                dos.writeUTF(title);
            }
            dos.writeInt(topicTitles.size());
            for (String title : topicTitles) {
                dos.writeUTF(title);
            }
            dos.writeInt(subTopicTitles.size());
            for (String title : subTopicTitles) {
                dos.writeUTF(title);
            }
            dos.writeInt(classTopics.size());
            for (ArrayList<Integer> a : classTopics) {
                dos.writeInt(a.size());
                for (int i : a) {
                    dos.writeInt(i);
                }
            }
            dos.writeInt(topics.size());
            for (ArrayList<Integer> a : topics) {
                dos.writeInt(a.size());
                for (int i : a) {
                    dos.writeInt(i);
                }
            }
            dos.writeInt(subTopicStarts.size());
            for (int i : subTopicStarts) {
                dos.writeInt(i);
            }
            dos.writeInt(subTopicEnds.size());
            for (int i : subTopicEnds) {
                dos.writeInt(i);
            }
            dos.flush();
            dos.close();
            fos.close();

            fos = new FileOutputStream(MATA_HTML_FILENAME);
            PrintWriter pw = new PrintWriter(fos);
            pw.print(content);
            pw.flush();
            pw.close();
            fos.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Read the contents of the InputStream and replace newlines with space characters.
     *
     * @param is
     * @return
     * @throws IOException
     */
    protected String readFile(InputStream is) throws IOException {
        final char[] buffer = new char[0x10000];
        StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(is, "UTF-8");
        int read;
        do {
            read = in.read(buffer, 0, buffer.length);
            for (int i = 0; i < read; ++i) {
                if (buffer[i] != '\n') { // do not use newlines, as it makes regexes not work, if header is on several lines
                    out.append(buffer[i]);
                }
            }
        } while (read >= 0);
        return out.toString();
    }

    /**
     * Parse the HTML file and organize content.
     * h1 with name THE_END must be the last thing in the HTML file.
     */
    protected void parse() {
        Matcher matcher = headerPat.matcher(content);
        boolean inSubTopic = false;

        while (matcher.find()) {
            String title = matcher.group();
            String plainTitle = title.replaceAll("<(.|\n)*?>", "").trim(); // delete HTML tags

            if (inSubTopic) {
                subTopicEnds.add(matcher.start());
                inSubTopic = false;
            }

            // determine the header type
            if (title.startsWith("<h1")) {
                if (plainTitle.equals("THE_END")) {
                    break;
                } else {
                    classTitles.add(plainTitle);
                    classTopics.add(new ArrayList<Integer>());
                }
            } else if (title.startsWith("<h2")) {
                classTopics.get(classTopics.size() - 1).add(topicTitles.size());
                topicTitles.add(plainTitle);
                topics.add(new ArrayList<Integer>());
            } else if (title.startsWith("<h3")) {
                topics.get(topics.size() - 1).add(subTopicTitles.size());
                subTopicTitles.add(plainTitle);
                subTopicStarts.add(matcher.end()); // we know that a new subtopic starts here
                inSubTopic = true;
            }
        }
    }

    /**
     * @return The class titles as a String array.
     */
    public String[] getClassTitles() {
        String[] classTitles = new String[this.classTitles.size()];
        classTitles = this.classTitles.toArray(classTitles);
        return classTitles;
    }

    /**
     * @return The topic titles as a String array.
     */
    public String[] getTopicTitles() {
        String[] topicTitles = new String[this.topicTitles.size()];
        topicTitles = this.topicTitles.toArray(topicTitles);
        return topicTitles;
    }

    /**
     * @return The topic titles as a String array.
     */
    public String[] getSubTopicTitles() {
        String[] subTopicTitles = new String[this.subTopicTitles.size()];
        subTopicTitles = this.subTopicTitles.toArray(subTopicTitles);
        return subTopicTitles;
    }

    /**
     * @param classIndex The index of the class.
     * @return Return the array of integers of listView.
     */
    public int[] getClassTopics(int classIndex) {
        int[] topics = new int[this.classTopics.get(classIndex).size()];
        for (int index = 0; index < this.classTopics.get(classIndex).size(); ++index) {
            topics[index] = this.classTopics.get(classIndex).get(index);
        }
        return topics;
    }

    /**
     * @param topicIndex The index of the topic.
     * @return The array of integers of subtopics of topic with index 'topixIndex'.
     */
    public int[] getTopicSubTopics(int topicIndex) {
        int[] subTopics = new int[this.topics.get(topicIndex).size()];
        for (int index = 0; index < this.topics.get(topicIndex).size(); ++index) {
            subTopics[index] = this.topics.get(topicIndex).get(index);
        }
        return subTopics;
    }

    public String getSubTopicHTML(int subTopicIndex) {
        int start = subTopicStarts.get(subTopicIndex);
        int end = subTopicEnds.get(subTopicIndex);
        return String.format("%s%s%s", htmlHeader, content.subSequence(start, end), htmlFooter);
    }

    private final String htmlHeader =
                    "<html" +
                    "<head>" +
                    "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>" +
                    "<link rel=\"stylesheet\" href=\"file:///android_asset/css/style.css\" type=\"text/css\"/>" +
                    "</head>" +
                    "<body>" +
                    "<div id=\"globalWrapper\">";

    private final String htmlFooter = "</div></body></html>";

    /**
     * Main function for testing.
     */
    public static void main(String[] args) {
        File f = new File("assets/mata.html");
        InputStream is;
        try {
            System.err.println("Opening mata.html file");
            is = new FileInputStream(f);
            System.err.println("Parsing the HTML file");
            MataHTMLParser parser = new MataHTMLParser(is);
            ContentIndex index = new ContentIndex();
            System.err.println("Building the index");
            index.buildIndex(parser);
            System.err.println("Exporting the index to mata.index");
            index.save();
            System.err.println("Exporting the mata.structure and condensed mata.html");
            parser.exportStructure();
            System.err.println("Tesing and loading it!");
            parser = new MataHTMLParser(new FileInputStream(new File("mata.structure")), new FileInputStream(new File("mata.html")));
            System.err.println(parser.getSubTopicHTML(1));
            System.err.println("Finished! Congragulations!");

            FileInputStream fis = new FileInputStream(new File(MATA_INDEX_FILENAME));
            index.load(fis);
            fis.close();
            int[] indices = index.query("skalaarkorrut");
            String[] titles = parser.getSubTopicTitles();
            for (int i : indices) {
                System.out.println(titles[i]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
