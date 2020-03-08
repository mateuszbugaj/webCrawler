package com.mateuszbugaj.webCrawler;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FilesManager {


    public void clearFiles(String... fileNames){
        for(String fileName:fileNames){
            try {
                PrintWriter writer = new PrintWriter(fileName);
                writer.print("");
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void writeToFile(String fileName, String content){
        try {
            BufferedWriter writer = getWriter(fileName);
            //writer.write(content);
            writer.append("\n").append(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> readFile(String fileName){
        try {
            BufferedReader reader = getReader(fileName);
            List<String> linesOfContent = new ArrayList<>();
            String currentLine = "";
            while (currentLine!=null){
                currentLine = reader.readLine();
                linesOfContent.add(currentLine);
            }
            reader.close();
            return linesOfContent;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private BufferedReader getReader(String fileName) throws FileNotFoundException {
        return new BufferedReader(new FileReader(fileName));
    }

    private BufferedWriter getWriter(String fileName) throws IOException {
        return new BufferedWriter(new FileWriter(fileName, true));
    }
}
