package com.harry.cmd;

import com.harry.core.Transformer;
import com.harry.util.Execute;

import java.io.*;

/**
 * Tsmart-build-capture: The build capture component of Tsmart platform
 * Created by Han Wang.
 * Copyright (C) 2015-2017  Han Wang
 */
public class Crawler {

    public BufferedReader getReader(String filename){
        File inFile = new File(filename);
        BufferedReader result = null;
        try {
            result = new BufferedReader(new InputStreamReader(new FileInputStream(inFile)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    public BufferedWriter getWriter(String filename) {
        File outFile = new File(filename);
        BufferedWriter result = null;
        try {
            result = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void crawl(String filename) {
        String command = "./maude.linux64 real-time-maude.maude RMS.maude " + filename + " > temp";
        Execute.executeCommand(command);

        BufferedReader bufferedReader = getReader("temp");
        BufferedWriter bufferedWriter = getWriter("error.json");
        boolean writeLine = false;
        String line = null;
        try {
            while((line = bufferedReader.readLine()) != null){
                if (line.startsWith("Bye"))
                    writeLine = false;
                if (writeLine)
                    bufferedWriter.write(line);
                if (line.startsWith("Result ModelCheckResult"))
                    writeLine = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            try{
                bufferedReader.close();
                bufferedWriter.close();
            } catch(IOException e){
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) {
        new Crawler().crawl("example-false.maude");
        new Transformer().transform();
    }
}
