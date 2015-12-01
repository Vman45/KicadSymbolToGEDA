// KicadSymbolToGEDA - a utility for turning kicad modules to gEDA PCB footprints
// Symbol.java v1.0
// Copyright (C) 2015 Erich S. Heinzle, a1039181@gmail.com

//    see LICENSE-gpl-v2.txt for software license
//    see README.txt
//    
//    This program is free software; you can redistribute it and/or
//    modify it under the terms of the GNU General Public License
//    as published by the Free Software Foundation; either version 2
//    of the License, or (at your option) any later version.
//    
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//    
//    You should have received a copy of the GNU General Public License
//    along with this program; if not, write to the Free Software
//    Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
//    
//    KicadSymbolToGEDA Copyright (C) 2015 Erich S. Heinzle a1039181@gmail.com



/**
 *
 * This object coordinates the header, text descriptors, drawn lines, drawn arcs, drawn circles
 * and also connections for a given Kicad symbol, passed as a string array of the form (String[] args)
 * and is then able to produce a gEDA gschema symbol
 *
 */

import java.util.Scanner;

public class Symbol
{
  String footprintName = "newSymbol";
  String assembledGEDAelement = "";
  String passedString = "";

  SymbolElement symbolElements[] = new SymbolElement[200];

  int moduleLineCountTotal = 0;	
  int padDefinitionLineCount = 0;

  String padDefinitionLines;

  int symFeatureCount = 0;

  int lineCount = 0;

  int pinCount = 0;
  int drawPolylineCount = 0;
  int drawArcCount = 0;
  int drawCircleCount = 0;
  int textDescriptorCount = 0;

  String reconstructedKicadSymbolAsString = ""; // we'll return this from the toString() 

  String symbolName = "";

  String referencePrefix = "";
  long pinNameOffset = 0;
  String drawPinNumber = "";
  String drawPinName = "";
  long unitCount = 0;
  String unitsLocked = "";
  String optionFlag = "";

  boolean metricSystem = false; //not really needed

  public Symbol(String args)
  {

    boolean symbolFinished = false;

    Scanner symbolDefinition = new Scanner(args);

    String parseString = "";
    String trimmedString = "";
    String[] tokens;

    //		System.out.println(args);

    while (symbolDefinition.hasNext() && !symbolFinished)
      {			
        parseString = symbolDefinition.nextLine();
        trimmedString = parseString.trim();
        tokens = trimmedString.split(" ");

        // we now move into the main parsing section
        // which decides what each line is all about and then deploys
        // it to construct the relevant symbol element object 

        if (tokens[0].startsWith("EESchema"))
          {
            //	System.out.println(footprintName); // we don't care about the header
          }
        else if (tokens[0].startsWith("DEF"))
          {       // it all starts here, with the symbol header

            symbolName = tokens[1];
            // now we get rid of characters ![a-zA-Z0-9.-] which
            // may be unacceptable for a filename
            symbolName = symbolName.replaceAll("[^a-zA-Z0-9.-]", "_");

            referencePrefix = tokens[2];
            pinNameOffset = Long.parseLong(tokens[4]);
            drawPinNumber = tokens[5];
            drawPinName = tokens[6];
            unitCount = Long.parseLong(tokens[7]);
            unitsLocked = tokens[8];
            optionFlag = tokens[9];

            // we now step through the symbol definition line by line
            while (symbolDefinition.hasNext() && !symbolFinished)
              {
                parseString = symbolDefinition.nextLine();
                trimmedString = parseString.trim();
                //	System.out.println("Current symbol def line:" + 
                //			trimmedString);

                // we tokenize the line
                tokens = trimmedString.split(" ");
					
                // and we then decide what to do with the tokenized lines
                // we start with F descriptor fields and plain text fields
                // System.out.println(tokens[0]);
                if (tokens[0].startsWith("F") || tokens[0].startsWith("T"))
                  {
                    symbolElements[symFeatureCount] = new SymbolText();
                    symbolElements[symFeatureCount].constructor(trimmedString);
                    textDescriptorCount++;
                    symFeatureCount++;
                  }
                else if (tokens[0].startsWith("S"))
                  {
                    symbolElements[symFeatureCount] = new SymbolRectangle();
                    symbolElements[symFeatureCount].constructor(trimmedString);
                    symFeatureCount++;
                  }
                else if (tokens[0].startsWith("C"))
                  {
                    symbolElements[symFeatureCount] = new SymbolCircle();
                    symbolElements[symFeatureCount].constructor(trimmedString);
                    symFeatureCount++;
                    drawCircleCount++;
                  }
                else if (tokens[0].startsWith("A"))
                  {
                    symbolElements[symFeatureCount] = new SymbolArc();
                    symbolElements[symFeatureCount].constructor(trimmedString);
                    symFeatureCount++;
                    drawArcCount++;
                  }
                else if (tokens[0].startsWith("P"))
                  {
                    symbolElements[symFeatureCount] = new SymbolPolyline();
                    symbolElements[symFeatureCount].constructor(trimmedString);
                    symFeatureCount++;
                    drawPolylineCount++;
                  }
                else if (tokens[0].startsWith("X"))
                  {  // we have identified a pin definition in the symbol
                    symbolElements[symFeatureCount] = new SymbolPin();
                    symbolElements[symFeatureCount].constructor(trimmedString);
                    symFeatureCount++;
                    pinCount++;
                  }

              }

          }

      }

    // we also create a single string version of the module for
    // use by the toString() method
    reconstructedKicadSymbolAsString = args;

  }

  public String generateGEDAsymbolFilename()
  {
    return symbolName + ".sch";
  }

  public String generateGEDAsymbol()
  {
    String output = "";
    System.out.println("Have identified this many symbol features: " + symFeatureCount);
    for (int index = 0; index < symFeatureCount; index++) {
      output = output + symbolElements[index].toString();
      if (index < (symFeatureCount - 1)) {
        output = output + "\n";
      }
    }
    return output;
  }

  public String getKicadSymbolName()
  {
    return symbolName;
  }

  public String toString()
  {
    return reconstructedKicadSymbolAsString;
  }

  private long convertToNanometres(float rawValue, boolean metricSystem)
  {
    if (metricSystem) // metricSystem = units mm
      {
        return (long)(1000000 * rawValue);
        // multiply mm by 1000000 to turn into nm
      }
    else
      {
        return (long)(2540 * rawValue);
        // multiply 0.1 mil units by 2540 to turn into nm
      }
  }



}