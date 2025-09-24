/*∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗*
  @file: Proj1.java
  @description: Entry point of the project (main) - expects a single command-line argument
  @How to run: In the terminal: ls -> cd src -> java Proj1.java playersData.csv input.txt
  @author: Calvin Malaney
  @date: September 23, 2025
∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗*/

import java.io.FileNotFoundException;
import java.io.IOException;

// runs through terminal
public class Proj1 {
    public static void main(String[] args) throws FileNotFoundException, IOException {
        if(args.length != 2){
            System.err.println("Argument count is invalid: " + args.length);
            System.exit(1);
        }
        new Parser(args[0], args[1]); //csv then commands
    }
}

