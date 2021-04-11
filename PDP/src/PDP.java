/**
*
* @author Yusuf Sonmez / yusuf.sonmez1@ogr.sakarya.edu.tr
* @since 09/04/2021
* @number B181210071
* 
* 
*/

import java.util.*;
import java.io.*;
public class PDP 
{
	public static void main(String[] args) throws FileNotFoundException 
	{
		File file = new File("src/Program.cpp");    
		
		Scanner scan_first = new Scanner(file);
		
		// read the file and adjust it to get a standard version
		
		String cppFile = "";
		while(scan_first.hasNext())
		{
			cppFile += scan_first.nextLine();
		}
		
		cppFile = cppFile.replaceAll("\s+", " ");
		cppFile = cppFile.replaceAll("\t+", " ");
		String new_cpp = "";
		int indexOfCpp = 0;
		int numberOfNewLine = 0;
		while(indexOfCpp != cppFile.length())
		{
			
			if(cppFile.charAt(indexOfCpp) == '{')
			{
				new_cpp += cppFile.charAt(indexOfCpp);
				new_cpp += "\n";
				numberOfNewLine++;
			}
			else if(cppFile.charAt(indexOfCpp) == '}')
			{
				new_cpp += cppFile.charAt(indexOfCpp);
				new_cpp += "\n";
				numberOfNewLine++;
			}
			else if(cppFile.charAt(indexOfCpp) == ';')
			{
				new_cpp += cppFile.charAt(indexOfCpp);
				new_cpp += "\n";
				numberOfNewLine++;
			}
			else if(cppFile.charAt(indexOfCpp) == ':')
			{
				new_cpp += cppFile.charAt(indexOfCpp);
				new_cpp += " ";
			}
			else
			{
				new_cpp += cppFile.charAt(indexOfCpp);
			}
			
			indexOfCpp++;
		}
		scan_first.close();
		
		String[] classes = new String[30];			// keeps classes in array as string
		
		int time = 0;	
		
		int ControlsInorOut = 0;			// this variable is to check class is ending or not
		
		int Line = 0;
		
		int textCounter = 0;
		
		String line = "";
		while(Line < numberOfNewLine)		// reads all the file 
		{
			line = "";
			while(new_cpp.charAt(textCounter) != '\n')
			{
				line += new_cpp.charAt(textCounter);
				textCounter++;
			}
			textCounter++;
			Line++;
			String control = line.trim();
			
			if(control.contains("class"))	// controls if line has "class" or not
			{
				String tmp_line = "";
				
				tmp_line = control;
				line = "";
				if(tmp_line.contains("{")) ControlsInorOut += 1;
				if(tmp_line.contains("}")) ControlsInorOut -= 1;
				
				while(new_cpp.charAt(textCounter) != '\n')
				{
					line += new_cpp.charAt(textCounter);
					textCounter++;
				}
				textCounter++;
				Line++;
				control = line.trim();
				
				if(control.contains("{")) ControlsInorOut += 1;
				if(control.contains("}")) ControlsInorOut -= 1;
				
				while(ControlsInorOut != 0)		// controls if line is at the end of class or not
				{
					tmp_line = tmp_line +"\n"+ control;
					line = "";
					while(new_cpp.charAt(textCounter) != '\n')
					{
						line += new_cpp.charAt(textCounter);
						textCounter++;
					}
					textCounter++;
					Line++;
					control = line.trim();
					if (control.contains("{")) ControlsInorOut++;
					if(control.contains("}")) ControlsInorOut--;
				}
				tmp_line = tmp_line + "\n};";
				// assigns temporary line to classes array
				classes[time] = tmp_line;
				time++;
			}
		}
		
		
		// storages of our data
		
		int n = 40;
		
		String[] ClassesNames = new String[classes.length];
		String[][] InheritanceNames = new String[classes.length][n];
		int[][] InheritanceTimes = new int[classes.length][n];
		String[][] FunctionNames = new String[classes.length][n*2];
		String[][] TypeOfFunction = new String[classes.length][n*2];
		int [][] ParametersTimes = new int[classes.length][n*2];
		String[][][] TypeOfParameters = new String[classes.length][n*2][n];
		
		int bool = 0;
		int FunctionNumber = 0;
		String allFunction = "";
		
		for(int i = 0; i < classes.length; i++)		// this "for" returns length of classes times and scan all the lines in it
		{
			if(classes[i] != null)
			{
				String temporary_class = classes[i];
				int numb_2 = 0;
				String temporary_line = "";
				
				for(;;)			// read all characters in the line
				{
					if(temporary_class.charAt(numb_2) != '\n') // for stops when next character equals to '\n' and we get the line
					{
						temporary_line += temporary_class.charAt(numb_2);
						numb_2++;
					}
					else
					{
						//separating all required data from array of classes
						
						if(temporary_line.contains("class")) 	// check whether this line is first line
						{
							if(temporary_line.contains(":"))	// check whether first line has inheritance class
							{
								temporary_line = temporary_line.replace(" ", "");
								temporary_line = temporary_line.replaceAll("class", "");
								temporary_line = temporary_line.replace("{", "");
								
								if(temporary_line.contains(","))	// checks whether the line has inheritance more than 1
								{
									temporary_line = temporary_line.replaceAll("public", "");
									temporary_line = temporary_line.replaceAll("private", "");
									temporary_line = temporary_line.replaceAll("protected", "");
									String temporaryClassName = "";
									int num = 0;
									for(;;)
									{
										if(temporary_line.charAt(num) == ':')	// we're going to assign inheritance classes
										{
											ClassesNames[i] = temporaryClassName;
											temporaryClassName = "";
											num++;
										}
										temporaryClassName += temporary_line.charAt(num);
										num++;
										if(num == temporary_line.length())
										{
											// checks Inheritance Name is already assigned this array
											String[] InheritNames = temporaryClassName.trim().split(",");
											int number = 0;
											boolean in = false;
											for(int counter = 0; counter < InheritNames.length; counter++)
											{
												// if we have already assigned this classes name in inheritance array then we increase the value
												for(int counter_2 = 0; counter_2 < InheritanceNames.length; counter_2++)
												{
													if(InheritanceNames[i][counter_2] != null && InheritNames[counter].equals(InheritanceNames[i][counter_2]))
													{
														InheritanceTimes[i][counter_2]++;
														in = true;
													}
												}
												// if we haven't, then create a new inheritance value
												if(in != true)
												{
													InheritanceNames[i][number] = InheritNames[counter];
													InheritanceTimes[i][number]++;
													number++;
												}
												in = false;
											}
											break;
										}
									}
								}
								else
								{
									temporary_line = temporary_line.replaceAll("public", "");
									temporary_line = temporary_line.replaceAll("protected", "");
									String temporaryClassName = "";
									int num = 0;
									for(;;)
									{
										if(temporary_line.charAt(num) == ':')
										{
											ClassesNames[i] = temporaryClassName;
											temporaryClassName = "";
											num++;
										}
										temporaryClassName += temporary_line.charAt(num);
										num++;
										if(num == temporary_line.length())
										{
											// checks Inheritance Name is already assigned this array
											int number = 0;
											boolean in = false;
											for(int annumb = 0; annumb < classes.length; annumb++)
											{
												for(int counter = 0; counter < n ;counter++)
												{
													if(InheritanceNames[annumb][counter] != null && InheritanceNames[annumb][counter].equals(temporaryClassName))
													{
														InheritanceTimes[annumb][counter]++;
														in = true;
													}
												}
											}
											if(in != true)
											{
												InheritanceNames[i][number] = temporaryClassName;
												InheritanceTimes[i][number]++;
												number++;
											}
											break;
										}
									}
								}
							}							
							else
							{
								temporary_line = temporary_line.replace(" ", "");
								temporary_line = temporary_line.replace("{", "");
								int ClassCounter = 0;
								String tmp = "";
								int ClassIndex = temporary_line.indexOf("class");
								while(ClassCounter < ClassIndex + 5)	// this while can eliminate all characters that comes behind class names
								{
									tmp += temporary_line.charAt(ClassCounter);
									ClassCounter++;
								}
								temporary_line = temporary_line.replaceAll(tmp, "");
								ClassesNames[i] = temporary_line;
							}
						}
						// checks this line is a line that build a function, or not
						
						if(temporary_line.contains("(") && !temporary_line.contains(";") && (!temporary_line.contains("if") 
																						  && !temporary_line.contains("while")
																						  && !temporary_line.contains("switch")
																						  && !temporary_line.contains("for")
																						  && !temporary_line.contains("else if")))	
						{
							if(bool != 1)		//this boolean controls that we are in the function or not
							bool++;
						}
						if(bool == 1)
						{
							allFunction += temporary_line;
							
							if(temporary_line.contains("}"))
							{
								bool = 0;
								
								allFunction = allFunction.replaceAll("private", "");
								allFunction = allFunction.replaceAll("public", "");
								allFunction = allFunction.replaceAll("static", "");
								allFunction = allFunction.replaceAll("friend", "");
								allFunction = allFunction.replaceAll(":", "");
								if(allFunction.indexOf("const") != -1)
								{
									int cons = allFunction.indexOf("const");
									int parantez = allFunction.indexOf("(");
									if(cons < parantez)
									{
										allFunction = allFunction.replaceFirst("const\s+", "");
									}
								}
								// these assignments are for standardisation of allFunction
								allFunction = allFunction.replaceAll("\s+", " ");
								allFunction = allFunction.replaceAll("\t+", " ");
								allFunction = allFunction.trim();
								
								int num_2 = 0;
								int bracket = 0;
								String tmpWord = "";
								String tmp = "";
								int ParametersCount = 0;
								boolean constructor = false;
								for(;;) 
								{
									// check function is constructor or not
									if(allFunction.charAt(num_2) == '(')
									{
										tmp = tmpWord;
										tmp = tmp.trim();
										if(tmp.contains(" "))
											constructor = false;
										else
											constructor = true;
										tmpWord = "";
										num_2 = 0;
										break;
									}
									tmpWord += allFunction.charAt(num_2);
									num_2++;
								}
								
								for(;;)
								{
									if(constructor == false && allFunction.charAt(num_2) == '(' && ParametersCount == 0)
									{
										tmpWord= tmpWord.trim();
										String[] TypeAndName = tmpWord.split(" ");
										TypeOfFunction[i][FunctionNumber] = TypeAndName[0];
										FunctionNames[i][FunctionNumber] = TypeAndName[1];
										tmpWord = "";
										ParametersCount++;
										num_2++;
										bracket++;
										
									}
									if(constructor == true && ParametersCount == 0)
									{
										if(allFunction.charAt(num_2) == '(') 
										{
											FunctionNames[i][FunctionNumber] = tmpWord;
											if(tmpWord.contains("~"))
											{
												TypeOfFunction[i][FunctionNumber] = "void";
											}
											else    TypeOfFunction[i][FunctionNumber] = "Nesne Adresi";
											tmpWord = "";
											num_2++;
											ParametersCount++;
											bracket++;
										}
									}
									
									if(ParametersCount != 0)
									{
										if(allFunction.charAt(num_2) == '(')
										{
											bracket++;
										}
										if(allFunction.charAt(num_2) == ')') 
										{bracket--;}
										if(allFunction.charAt(num_2) == ')' && bracket == 0)
										{
											int counter_2 = 0;
											if(tmpWord.contains(",")) // if there are parameters more than one
											{
												String[] ParametersAsAll = new String[n];
												ParametersAsAll = tmpWord.split(",");
												for(int j = 0; j < ParametersAsAll.length; j++)
												{
													if(ParametersAsAll[j] != "" && ParametersAsAll[j] != null)
													{
														if(ParametersAsAll[j].contains("const")) // if the parameter has const in line
														{
															if(ParametersAsAll[j].contains("*") || ParametersAsAll[j].contains("&")) 
															{
																int numPointer = 0;
																String tmpParameter = "";
																while(true) 
																{
																	tmpParameter += ParametersAsAll[j].charAt(numPointer);
																	if(ParametersAsAll[j].charAt(numPointer) == '*')
																	{
																		
																		if(ParametersAsAll[j].charAt(numPointer+1) == '&')
																		{
																			tmpParameter += ParametersAsAll[j].charAt(numPointer+1);
																			TypeOfParameters[i][FunctionNumber][j] = tmpParameter;
																			counter_2++;
																			break;
																		}
																		else
																		{
																			TypeOfParameters[i][FunctionNumber][j] = tmpParameter;
																			counter_2++;
																			break;
																		}
																	}
																	else if(ParametersAsAll[j].charAt(numPointer) == '&')
																	{
																		TypeOfParameters[i][FunctionNumber][j] = tmpParameter;
																		counter_2++;
																		break;
																	}
																	numPointer++;
																}
															}
															else
															{
																String[] temporary = ParametersAsAll[j].trim().split(" ");
																TypeOfParameters[i][FunctionNumber][j] = temporary[0] + " " + temporary[1];
																counter_2++;
															}
														}
														else	// if the parameter does not have const in line
														{
															if(ParametersAsAll[j].contains("*") || ParametersAsAll[j].contains("&"))
															{
																int numPointer = 0;
																String tmpParameter = "";
																while(true) 
																{
																	tmpParameter += ParametersAsAll[j].charAt(numPointer);
																	if(ParametersAsAll[j].charAt(numPointer) == '*')
																	{
																		
																		if(ParametersAsAll[j].charAt(numPointer+1) == '&')
																		{
																			tmpParameter += ParametersAsAll[j].charAt(numPointer+1);
																			TypeOfParameters[i][FunctionNumber][j] = tmpParameter;
																			counter_2++;
																			break;
																		}
																		else
																		{
																			TypeOfParameters[i][FunctionNumber][j] = tmpParameter;
																			counter_2++;
																			break;
																		}
																	}
																	else if(ParametersAsAll[j].charAt(numPointer) == '&')
																	{
																		TypeOfParameters[i][FunctionNumber][j] = tmpParameter;
																		counter_2++;
																		break;
																	}
																	numPointer++;
																}
															}
															else
															{
																String[] temporary = ParametersAsAll[j].trim().split(" ");
																TypeOfParameters[i][FunctionNumber][j] = temporary[0];
																counter_2++;
															}
														}
													}
												}
												ParametersTimes[i][FunctionNumber] = counter_2;
											}
											// this area is for single parameter
											else
											{
												String test = tmpWord.replaceAll(" ", "");
												if(test == "")
												{ParametersTimes[i][FunctionNumber] = 0;}
												else
												{
													if(tmpWord.contains("const"))
													{
														if(tmpWord.contains("*") || tmpWord.contains("&"))
														{
															int numPointer = 0;
															String tmpParameter = "";
															while(true) 
															{
																tmpParameter += tmpWord.charAt(numPointer);
																if(tmpWord.charAt(numPointer) == '*')
																{
																	
																	if(tmpWord.charAt(numPointer+1) == '&')
																	{
																		tmpParameter += tmpWord.charAt(numPointer+1);
																		TypeOfParameters[i][FunctionNumber][0] = tmpParameter;
																		ParametersTimes[i][FunctionNumber] = 1;
																		break;
																	}
																	else
																	{
																		TypeOfParameters[i][FunctionNumber][0] = tmpParameter;
																		ParametersTimes[i][FunctionNumber] = 1;
																		break;
																	}
																}
																else if(tmpWord.charAt(numPointer) == '&')
																{
																	TypeOfParameters[i][FunctionNumber][0] = tmpParameter;
																	ParametersTimes[i][FunctionNumber] = 1;
																	break;
																}
																numPointer++;
															}
														}
														else
														{
															String[] tmp_parameters = tmpWord.split(" ");
															TypeOfParameters[i][FunctionNumber][0] = tmp_parameters[0] + " " + tmp_parameters[1];
															ParametersTimes[i][FunctionNumber] = 1;
														}
													}
													else
													{
														if(tmpWord.contains("*") || tmpWord.contains("&"))
														{
															int numPointer = 0;
															String tmpParameter = "";
															while(true) 
															{
																tmpParameter += tmpWord.charAt(numPointer);
																if(tmpWord.charAt(numPointer) == '*')
																{
																	
																	if(tmpWord.charAt(numPointer+1) == '&')
																	{
																		tmpParameter += tmpWord.charAt(numPointer+1);
																		TypeOfParameters[i][FunctionNumber][0] = tmpParameter;
																		ParametersTimes[i][FunctionNumber] = 1;
																		break;
																	}
																	else
																	{
																		TypeOfParameters[i][FunctionNumber][0] = tmpParameter;
																		ParametersTimes[i][FunctionNumber] = 1;
																		break;
																	}
																}
																else if(tmpWord.charAt(numPointer) == '&')
																{
																	TypeOfParameters[i][FunctionNumber][0] = tmpParameter;
																	ParametersTimes[i][FunctionNumber] = 1;
																	break;
																}
																numPointer++;
															}
														}
														else
														{
															String[] tmp_parameters = tmpWord.split(" ");
															TypeOfParameters[i][FunctionNumber][0] = tmp_parameters[0];
															ParametersTimes[i][FunctionNumber] = 1;
														}
													}
												}
											}
											break;
										}
									}
									tmpWord += allFunction.charAt(num_2);
									num_2++;
								}
								FunctionNumber++;
								allFunction = "";
							}
						}
						
						// preparing parameters for other line
						numb_2++;
						temporary_line = "";
					}
					if (temporary_class.charAt(numb_2) == '}' && temporary_class.charAt(numb_2+1) == ';' && !temporary_line.contains("[]"))
					{
						break;
					}
				}
			}
		}
		// getting output ready here
		for(int i = 0; i<classes.length;i++)
		{
			if(ClassesNames[i] != null)
			{
				System.out.println("Sinif..: "+ClassesNames[i]);
				for(int j = 0; j < FunctionNumber;j++)
				{
					if(FunctionNames[i][j] != null && TypeOfFunction[i][j] != null)
					{
						System.out.println("	"+FunctionNames[i][j]);
						System.out.print("		"+"Parametre..: "+ ParametersTimes[i][j]);
						if(ParametersTimes[i][j] != 0)
						{
							System.out.print(" (");
							for(int k = 0; k < ParametersTimes[i][j]; k++)
							{
								if(k != ParametersTimes[i][j] - 1)
									System.out.print(TypeOfParameters[i][j][k]+", ");
								else
									System.out.print(TypeOfParameters[i][j][k]+")\n\n");
							}
						}
						else 
							System.out.println("\n");
						System.out.println("		"+"Donus Turu..: "+ TypeOfFunction[i][j]);
					}
				}
			}
		}
		for(int i = 0; i<classes.length;i++)
		{
			for(int j = 0; j < n;j++)
			{
				if(InheritanceNames[i][j] != null)
				{
					System.out.println("Super Sinif \n	"+ InheritanceNames[i][j]+"..: "+InheritanceTimes[i][j]);
				}
			}
		}
	}
}