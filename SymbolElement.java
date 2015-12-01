// KicadSymbolToGEDA - a utility for turning kicad modules to gEDA PCB footprints
// SymbolElement.java v1.0
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
* This class is an archetype for kicad symbol elements
*
*/

public class SymbolElement
{

  String elementDescriptor = "";  
  String output = "";
  
  public void SymbolPolyline()
  {
    output = "#Hmm, the no arg symbol polygon constructor didn't do much";
  }
  
  public void constructor(String arg)
  {
    elementDescriptor = arg; 
  } 

  public String toString() {
    return output;
  }

}
