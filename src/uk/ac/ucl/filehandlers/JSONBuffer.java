package uk.ac.ucl.filehandlers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class JSONBuffer {

    private String buffer;
    private Scanner scanner;
    private boolean EOF;

    JSONBuffer(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        scanner = new Scanner(file);
        buffer = "";
        EOF = false;
    }

    private boolean isBufferEmpty(){
        return buffer.equals("");
    }

    // loads in the character(s) in the buffer that are not separated by whitespace.
    void loadNextIntoBuffer(){
        EOF = !scanner.hasNext();
        if (!EOF){
            buffer = scanner.next();
        }
    }

    // adds the line into the buffer.
    void addNextLineIntoBuffer(){
        EOF = !scanner.hasNext();
        if (!EOF){
            buffer = buffer + scanner.nextLine();
        }
    }

    // checks if the string is closed with " marks.
    boolean stringClosureMarksAbsent(){
        return buffer.lastIndexOf('"') == 0 || buffer.lastIndexOf('\"') == buffer.lastIndexOf('"');
    }

    // if the buffer is empty, then load.
    void loadNextIfBufferEmpty(){
        if (isBufferEmpty()) loadNextIntoBuffer();
    }

    boolean startsWithChar(char character){
        return buffer.charAt(0) == character;
    }

    void discardFirstCharacter(){
        buffer = buffer.substring(1);
    }

    boolean reachedEOF(){
        return EOF;
    }

    void close(){
        scanner.close();
    }

    void trim(){
        buffer = buffer.trim();
    }

    boolean excludes(String sequence){
        return !buffer.contains(sequence);
    }

    // checks if there is a right brace that signals the end of file (EOF).
    boolean checkBrace() {
        if (startsWithChar('}')) {
            EOF = true;
            return true;
        }
        return false;
    }

    void setContents(String buffer){
        this.buffer = buffer;
    }

    String getContents(){
        return buffer;
    }

    String substringToCloseBracket(){
        return buffer.substring(0, buffer.indexOf(']'));
    }

    void shiftContentsToAfterCloseBracket(){
        buffer = buffer.substring(buffer.indexOf(']') + 1);
    }


}
