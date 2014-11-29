import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javax.swing.*;
import javax.swing.text.*;

/*
 * YAMLGen allows users to enter information about edges and nodes files to create edges.yaml,
 * nodes.yaml, create.sql and load_into_engine.sh to load information into engine.
 * 
 * YAMLGenMultiple allows users to enter multiple edges and nodes files into the system.
 * 
 * Application uses additional file to create grid:
 *   SpringUtilities.java
 *   
 * File is based off of TextInputDemo.java found on Oracle's Java Tutorial website.
 * (http://docs.oracle.com/javase/tutorial/uiswing/components/textfield.html)
 * 
 */

public class YAMLGenMultiple extends JPanel implements ActionListener, FocusListener {
	JLabel Display;
    final static int GAP = 10;
    File save; //variable to keep track of directory chosen to save all files created throughout program
    JTextArea filesindir= new JTextArea(10, 60); //shows files in save directory chosen on selection panel
    
    //variables to create edges.yaml files
	JTextField source, separator, attributes, vertices, buckets;
    JSpinner format, duplicate, directed, datatype;
    
    boolean edgesSet = false;
    Font regularFont, italicFont;
    
    ArrayList edge_yamls= new ArrayList(); //stores all edges.yaml files
    int edges=1; //keeps track of how many edges files exist
    
    JTextArea eattributes= new JTextArea(5, 5); //displays edge attributes entered by user on form
    ArrayList edgearray_attributes= new ArrayList(); //keeps track of edge attributes
    
    //variables to create nodes.yaml files
    JTextField sourceN, separatorN, verticesN, bucketsN;
    JSpinner formatN, duplicateN, directedN, datatypeN;
  
    boolean nodesSet = false;
    boolean nodes = false; //if false, this means no nodes files are needed and the create file will not have a line for nodes configuration
    //if user never clicks the "Add nodes file" button or clicks "No nodes file" button, the create file will have no line for nodes configuration
    //if user creates nodes files then clicks the "No nodes file" button, nodes yaml files will be created but will not show up in create file
    //creating a nodes yaml file sets nodes true
    
    JTextArea vattributes= new JTextArea(5, 5); //displays Vertex Attributes entered by user
    ArrayList nodearray_attributes= new ArrayList(); //keeps track of Vertex Attributes
    
    ArrayList node_yamls= new ArrayList();  //stores all nodes.yaml file
    int nodes_num=1; //keep track of how many nodes files exist
    
    //variables to create create.sql and load_into_engine.sh
    JTextField graph_name, author, comment, date;
    File pkg, graph; 
    boolean graphSet = false;
    
    File yourFile = new File ("pkgandgr.txt"); //keeps track of PKGDIR and GRAPHDIR for future use
    
    public YAMLGenMultiple() { //creates GUI display that allows user to select where to save files
    	JPanel panel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
    	
    	JButton button2= new JButton("Choose data directory");
        button2.addActionListener(this);
        button2.setActionCommand("save");
        panel.add(button2);
        
    	add(panel);
    	add(createDisplay());
    	
    }
    
    public void edgesPanel(){ //creates a panel to enter edge file information
    	removeAll(); //clears panel to allow edge input information to show
    	revalidate();
    	repaint();
    	
    	JPanel leftHalf = new JPanel();
    	leftHalf.setLayout(new BoxLayout(leftHalf, BoxLayout.Y_AXIS));
        leftHalf.add(createEntryFields());
        leftHalf.add(createButtonsEdges());
        eattributes.setText(null); //clears text area for new edge file attributes
        edgearray_attributes.clear(); //clears ArrayList for new attributes
        eattributes.setEditable(false);
        leftHalf.add(new JLabel ("Edge Attributes"));
        leftHalf.add(new JScrollPane(eattributes)); 
        
        JPanel panel= new JPanel();
    	ArrayList<File> files = new ArrayList<File>(Arrays.asList(save.listFiles())); //files in chosen data directory
    	filesindir.setText(null);
    	filesindir.setEditable(false);
    	JScrollPane scroll = new JScrollPane ( filesindir);
        scroll.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
    	for (int i = 0; i < files.size(); i++) 
          {
    		filesindir.append(files.get(i)+"\n");  
          }
          panel.add(new JLabel ("Files in selected data directory"));
          panel.add(scroll);
         
          JButton button = new JButton("Open a file"); //buttons allows user to open a file
          button.addActionListener(this);
          button.setActionCommand("open"); 
          panel.add(button);
          
        add(leftHalf);
        add(panel);
    }
    
    public void nodesPanel(){ //creates a panel to enter node file information
    	removeAll(); //clears panel to allow node input information to show
    	revalidate();
    	repaint();
    	
    	JPanel rightHalf = new JPanel(); 
        rightHalf.setLayout(new BoxLayout(rightHalf, BoxLayout.Y_AXIS));
        rightHalf.add(createEntryFieldsN());
        rightHalf.add(createButtonsNodes());
        vattributes.setText(null);  //clears text area for new node file attributes
        nodearray_attributes.clear(); //clears ArrayList for new attributes
        vattributes.setEditable(false);
        rightHalf.add(new JLabel ("Vertex Attributes"));
        rightHalf.add(new JScrollPane(vattributes));
        

        JPanel panel= new JPanel();
    	ArrayList<File> files = new ArrayList<File>(Arrays.asList(save.listFiles())); //files in chosen data directory
    	filesindir.setText(null);
    	filesindir.setEditable(false);
    	JScrollPane scroll = new JScrollPane ( filesindir);
        scroll.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
    	for (int i = 0; i < files.size(); i++) 
          {
    		filesindir.append(files.get(i)+"\n");  
          }
          panel.add(new JLabel ("Files in selected data directory"));
          panel.add(scroll);
         
          JButton button = new JButton("Open a file"); //buttons allows user to open a file
          button.addActionListener(this);
          button.setActionCommand("open"); 
          panel.add(button);
          
        add(rightHalf);
        add(panel);
        
    }
    
    public void graphPanel() throws IOException{ //creates a panel to enter create.sql and load_into_engine.sh file information
    	removeAll();
    	revalidate();
    	repaint();
    	
    	JPanel top = new JPanel(); 
    	top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
    	top.add(create_file());
    	top.add(createButtonsTop2());
        top.add(createButtonsTop());
        

        JPanel panel= new JPanel();
    	ArrayList<File> files = new ArrayList<File>(Arrays.asList(save.listFiles())); //files in chosen data directory
    	filesindir.setText(null);
    	filesindir.setEditable(false);
    	JScrollPane scroll = new JScrollPane ( filesindir);
        scroll.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
    	for (int i = 0; i < files.size(); i++) 
          {
    		filesindir.append(files.get(i)+"\n");  
          }
          panel.add(new JLabel ("Files in selected data directory"));
          panel.add(scroll);
         
          JButton button = new JButton("Open a file"); //buttons allows user to open a file
          button.addActionListener(this);
          button.setActionCommand("open"); 
          panel.add(button);
          
        add(top);
        add(panel);
    	
    	//top throughout program refers to section of GUI dealing with create and load into engine as this 
    	//part originally was at the top of the GUI
    }

    protected JComponent createButtonsFiles(){ //creates buttons for options after entering an edge or node file
    	//allows user to add additional edge and node files or to proceed to create and load file parts

    	JPanel panel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
          
    	 JButton button2= new JButton("Add edges file");
         button2.addActionListener(this);
         button2.setActionCommand("add_edge");
         panel.add(button2);
         
         button2= new JButton("Add nodes file");
         button2.addActionListener(this);
         button2.setActionCommand("add_node");
         panel.add(button2);
         
         button2= new JButton("No more nodes/edges files to enter");
         button2.addActionListener(this);
         button2.setActionCommand("finish");
         panel.add(button2);
       
         return panel;
    }
    
    protected JComponent createButtonsEdges() { //creates buttons to interact with edges information
    	
    	JPanel panel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        
    	JButton button = new JButton("Add Edge Attributes"); //click button to enter edge attributes
        button.addActionListener(this);
        button.setActionCommand("edgeattr"); 
        panel.add(button);
        
        button = new JButton("Set as defaults"); //clicking button will enter default information into fields
        button.addActionListener(this);
        button.setActionCommand("default");
        panel.add(button);

        button = new JButton("Clear form"); //user entered fields will be cleared
        button.addActionListener(this);
        button.setActionCommand("clear");
        panel.add(button);
        
        button = new JButton("Create edges YAML"); //edges.yaml will be created
        button.addActionListener(this);
        button.setActionCommand("create");
        panel.add(button);
        
        button = new JButton("Return to selection page"); //if user wishes not to create another edges file button should be pushed
        button.addActionListener(this);
        button.setActionCommand("cancel");
        panel.add(button);
        
        return panel;
    }

    protected JComponent createButtonsNodes() { //creates buttons to interact with nodes information
    	
    	JPanel panel = new JPanel(new FlowLayout(FlowLayout.TRAILING));

    	JButton button = new JButton("Add Vertex Attributes"); //click button to enter node attributes
        button.addActionListener(this);
        button.setActionCommand("vertattr");
        panel.add(button);
        
        button = new JButton("No nodes file"); //if no nodes files are needed, push this button
        button.addActionListener(this);
        button.setActionCommand("nonodes"); //clicking this button changes create.sql file to have no nodes information
        panel.add(button);
        
        button = new JButton("Set as defaults"); //default information will be entered into text fields
        button.addActionListener(this);
        button.setActionCommand("defaultn");
        panel.add(button);

        button = new JButton("Clear form"); //user entered fields will be cleared
        button.addActionListener(this);
        button.setActionCommand("clearn");
        panel.add(button);
        
        button = new JButton("Create nodes YAML"); //nodes.yaml will be created
        button.addActionListener(this);
        button.setActionCommand("createn");
        panel.add(button);
        
        button = new JButton("Return to selection page"); //if user wishes not to create another nodes file button should be pushed
        button.addActionListener(this);
        button.setActionCommand("cancel");
        panel.add(button);
        
        return panel;
    }

    protected JComponent createButtonsTop() { //creates buttons to interact with create.sql file
    	
    	JPanel panel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
       
        JButton button = new JButton("Set as defaults"); //user entered fields will become defaults
        button.addActionListener(this);
        button.setActionCommand("defaultT");
        panel.add(button);

        button = new JButton("Clear form"); //user entered fields will cleared
        button.addActionListener(this);
        button.setActionCommand("clearT");
        panel.add(button);
        
        button = new JButton("Enter Information"); //create.sql and load_into_engine.sh will be created
        button.addActionListener(this);
        button.setActionCommand("createT");
        panel.add(button);
      
        return panel;
    }
 
    JTextField pkgselect; //displays previously selected PKGDIR if program has been used before
    String pkgname; //keeps track of PKGDIR for future use
    JTextField grselect; //displays previously selected GRAPHDIR if program has been used before
    String grname; //keeps track of GRAPHDIR for future use
    
    protected JComponent createButtonsTop2() throws IOException { //buttons to allow users to find directories for PKG and GRAPH through GUI
    	
    	//JPanel panel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
    	JPanel panel = new JPanel(new GridLayout(2, 0));
    	
        JButton button = new JButton("PKG_DIR");
        button.addActionListener(this);
        button.setActionCommand("pkg");
        panel.add(button);
        
        pkgselect = new JTextField(30);
	    pkgselect.addActionListener(this);
	    
	    grselect = new JTextField(30);
	    grselect.addActionListener(this);

        if(yourFile.exists()) { //saves selected directories for PKGDIR and GRAPHDIR in a text file
        	BufferedReader in = new BufferedReader(new FileReader("pkgandgr.txt"));
        	pkgname= in.readLine();
        	pkgselect.setText(pkgname);
        	grname= in.readLine();
        	grselect.setText(grname);
        }
             
	    panel.add(pkgselect); 
	    
        button = new JButton("GRAPH_DIR");
        button.addActionListener(this);
        button.setActionCommand("gr");
        panel.add(button);
	    
	    panel.add(grselect); 
	    
        return panel;
    }
    
    public void clearPanel(){ //clears panel after edge or node file information has been entered
    	//buttons to create another edge or node file or to proceed to create and load information panel will appear
    	removeAll();
    	revalidate();
    	repaint();
    	
    	JPanel panel= new JPanel();
    	ArrayList<File> files = new ArrayList<File>(Arrays.asList(save.listFiles())); //files in chosen data directory
    	filesindir.setText(null);
    	filesindir.setEditable(false);
    	JScrollPane scroll = new JScrollPane ( filesindir);
        scroll.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
    	for (int i = 0; i < files.size(); i++) 
          {
    		filesindir.append(files.get(i)+"\n");  
          }
          panel.add(new JLabel ("Files in selected data directory"));
          panel.add(scroll);
         
          JButton button = new JButton("Open a file"); //buttons allows user to open a file
          button.addActionListener(this);
          button.setActionCommand("open"); 
          panel.add(button);
          
    	JPanel leftHalf = new JPanel();
    	leftHalf.setLayout(new BoxLayout(leftHalf, BoxLayout.Y_AXIS));
        leftHalf.add(createButtonsFiles());

        add(leftHalf);
        add(panel);
        //add(createDisplay());
    }
    
    public void actionPerformed(ActionEvent e) { //controls actions for all buttons pressed on GUI
        if ("default".equals(e.getActionCommand())) { //default for edges button
            edgesSet = false;
            source.setText("edges.csv");
            vertices.setText("1");
            buckets.setText("1");
            separator.setText(",");
        } else  if ("clear".equals(e.getActionCommand())) { //clear for edges button
        	edgesSet = false;
            source.setText("");
            vertices.setText("");
            buckets.setText("");
            separator.setText("");
            edgearray_attributes.clear();
            eattributes.setText(null);
        } else if ("create".equals(e.getActionCommand())) { //create for edges button
        	edgesSet= true;
        	try {
				writeYAML();
			} catch (IOException e1) {
				e1.printStackTrace();
			}	
        	clearPanel(); //after edge information is entered buttons to create new files will appear
        } else if ("cancel".equals(e.getActionCommand())) { //creates GUI pop-up to enter Vertex Attributes
        	clearPanel();
        }else if ("vertattr".equals(e.getActionCommand())) { //creates GUI pop-up to enter Vertex Attributes
        	vertAttributesGUI();
        } else if ("entervattr".equals(e.getActionCommand())) { //enters Vertex Attribute information into ArrayList, displays information in text area, and closes pop-up
        	String attr = "- "+ textField.getText() +":"+ vattributesbox.getSelectedItem();
        	nodearray_attributes.add(attr); 
        	theGUI.dispose();
        	vattributes.append(attr+"\n");
        }else if ("exitv".equals(e.getActionCommand())) { //exits from pop-up GUI
        	theGUI.dispose();
        }else if ("edgeattr".equals(e.getActionCommand())) { //creates GUI pop-up to enter edge attributes
        	edgeAttributesGUI();
        } else if ("entereattr".equals(e.getActionCommand())) { //enters edge attribute information into ArrayList, displays information in text area, and closes pop-up
        	String attr = "- "+ textFielde.getText() +":"+ eattributesbox.getSelectedItem();
        	edgearray_attributes.add(attr); 
        	theGUIe.dispose();
        	eattributes.append(attr+"\n");
        }else if ("exite".equals(e.getActionCommand())) { //exits from pop-up GUI
        	theGUIe.dispose();
        }else if ("defaultn".equals(e.getActionCommand())) { //default for nodes button
        	edgesSet = false;
            sourceN.setText("nodes.csv");
            separatorN.setText(",");
            verticesN.setText("1");
            bucketsN.setText("1");  
        }else if ("nonodes".equals(e.getActionCommand())) { //button to indicate no nodes information
        	 nodesSet = false;
        	 nodes = false; 
        	 clearPanel();
        }else  if ("clearn".equals(e.getActionCommand())) { //clear for nodes button
            nodesSet = false;
            sourceN.setText("");
            separatorN.setText("");
            verticesN.setText("");
            bucketsN.setText("");
            nodearray_attributes.clear();
            vattributes.setText(null);
        } else if ("createn".equals(e.getActionCommand())) { //create for nodes button
        	 nodesSet= true;
        	 nodes = true;
        	try {
				writeYAMLnodes();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
        	clearPanel(); //after node information is entered buttons to create new files will appear
        }else if ("defaultT".equals(e.getActionCommand())) { //default for create file fields
        	//JTextField graph_name, author, comment, date;
        	graphSet = false;
            graph_name.setText("us_cities");
            author.setText("GraphSQL");
            comment.setText("City map dataset");
            date.setText("July 15, 2013");
        } else  if ("clearT".equals(e.getActionCommand())) { //clear for create file fields
        	graphSet = false;
            graph_name.setText("");
            author.setText("");
            comment.setText("");
            date.setText("");
            pkgselect.setText("");
            pkgname="";
            grselect.setText("");
            grname="";
        } else if ("open".equals(e.getActionCommand())) { 
        	 JFileChooser chooser = new JFileChooser(); 
             //chooser.setCurrentDirectory(new java.io.File("."));
        	 chooser.setCurrentDirectory(save);
             chooser.setDialogTitle("Open file");
             File file = chooser.getSelectedFile(); 
             if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) { 
             	file= chooser.getSelectedFile();
               }
             else {
               System.out.println("No Selection ");
               }

            Desktop dk=Desktop.getDesktop();
            try {
				dk.open(file);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        } else if ("createT".equals(e.getActionCommand())) { //create create.sql and load_into_engine.sh
        	graphSet= true;
        	try {
				writeCreate();
			} catch (IOException e1) {
				e1.printStackTrace();
			} 
        	removeAll(); //clears panel to print closing message
        	revalidate();
        	repaint();
        	
        	JPanel leftHalf = new JPanel();
        	leftHalf.setLayout(new BoxLayout(leftHalf, BoxLayout.Y_AXIS));
        	JLabel label= new JLabel("Done creating edges and nodes YAML files, create.sql, and load_into_engine.sh. They are saved in: \n" + save
        			+ " Please exit from GUI."); 
        	leftHalf.add(label);
            add(leftHalf);
            add(createDisplay());
            try {
				saveDirectory();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }else if ("pkg".equals(e.getActionCommand())) { //allows user to find directory of PKG for load file
        	graphSet = false;
         
            JFileChooser chooser = new JFileChooser(); 
            chooser.setCurrentDirectory(new java.io.File("."));
            chooser.setDialogTitle("");
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
           
            chooser.setAcceptAllFileFilterUsed(false);  
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) { 
            	pkg= chooser.getSelectedFile();
            	pkgname = pkg.toString();
            	pkgselect.setText(pkgname);
              }
            else {
              System.out.println("No Selection ");
              }
        } else if ("gr".equals(e.getActionCommand())) { //allows user to find directory of GRAPH for load file
        	graphSet = false;

            JFileChooser chooser = new JFileChooser(); 
            chooser.setCurrentDirectory(new java.io.File("."));
            chooser.setDialogTitle("");
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            
            chooser.setAcceptAllFileFilterUsed(false);
   
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) { 
            	graph= chooser.getSelectedFile();
            	grname = graph.toString();
            	grselect.setText(grname);
              }
            else {
              System.out.println("No Selection ");
              }
            
        } else  if ("add_edge".equals(e.getActionCommand())) { //allows user to enter information about another edge file
        	  edgesPanel();
        } else  if ("add_node".equals(e.getActionCommand())) { //allows user to enter information about another node file
      	  	  nodesPanel();
        } else  if ("finish".equals(e.getActionCommand())) { //allows user to enter information about create and load files
    	  	  try {
				graphPanel();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        } else if ("save".equals(e.getActionCommand())) { //allows user to choose where files are saved
        	graphSet = false;
         
            JFileChooser chooser = new JFileChooser(); 
            chooser.setCurrentDirectory(new java.io.File("."));
            chooser.setDialogTitle("");
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
           
            chooser.setAcceptAllFileFilterUsed(false);  
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) { 
            	save= chooser.getSelectedFile();
              }
            else {
              System.out.println("No Selection ");
              }
            clearPanel(); 
        }
    }
    
    
    protected void saveDirectory() throws IOException{ //writes selected directories for PKGDIR and GRAPHDIR to text file
    	 //File yourFile = new File ("pkgandgr.txt"); 
         if(!yourFile.exists()) {
             yourFile.createNewFile();
         } 
         FileWriter writer = new FileWriter(yourFile); 
         writer.write(pkgname+ "\n"+ grname); 
         writer.flush();
         writer.close();
    }
 
JTextField textField; //text field to allow user to enter name of vertex attribute
JComboBox vattributesbox; //combo box with options of type of vertex attribute
JFrame theGUI; //pop-up GUI to enter vertex attributes

    protected void vertAttributesGUI(){ //create pop-up GUI to enter vertex attributes
    	 theGUI = new JFrame();
    	 theGUI.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    	 JPanel panel= new JPanel(); 
	     theGUI.setTitle("Add Vertex Attributes");
	     textField = new JTextField(20);
	     textField.addActionListener(this);
	      
	     String[] vattributes = { "CHARARRAY", "DOUBLE", "BOOLEAN", "INT" };
	     vattributesbox = new JComboBox(vattributes);
	     vattributesbox.addActionListener(this);
	     panel.add(new JLabel("Attribute Name", JLabel.TRAILING));
	     panel.add(textField);
	     panel.add(vattributesbox);
	     theGUI.add(panel);
	      
	    JButton button= new JButton("Enter");
  	    button.addActionListener(this);
  	    button.setActionCommand("entervattr");
  	    panel.add(button);
  	    
  	    button= new JButton("Exit");
	    button.addActionListener(this);
	    button.setActionCommand("exitv");
	    panel.add(button);
	        
	      //Display the window.
	      theGUI.pack();
	      theGUI.setVisible(true); 
	      theGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
    
    JTextField textFielde; //text field to allow user to enter name of vertex attribute
    JComboBox eattributesbox; //combo box with options of type of vertex attribute
    JFrame theGUIe; //pop-up GUI to enter vertex attributes

        protected void edgeAttributesGUI(){ //create pop-up GUI to enter vertex attributes
        	 theGUIe = new JFrame();
        	 theGUIe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        	 JPanel panel= new JPanel(); 
    	     theGUIe.setTitle("Add Edge Attributes");
    	     textFielde = new JTextField(20);
    	     textFielde.addActionListener(this);
    	      
    	     String[] eattributes = { "CHARARRAY", "DOUBLE", "BOOLEAN", "INT" };
    	     eattributesbox = new JComboBox(eattributes);
    	     eattributesbox.addActionListener(this);
    	     panel.add(new JLabel("Attribute Name", JLabel.TRAILING));
    	     panel.add(textFielde);
    	     panel.add(eattributesbox);
    	     theGUIe.add(panel);
    	      
    	    JButton button= new JButton("Enter");
      	    button.addActionListener(this);
      	    button.setActionCommand("entereattr");
      	    panel.add(button);
      	    
      	    button= new JButton("Exit");
    	    button.addActionListener(this);
    	    button.setActionCommand("exite");
    	    panel.add(button);
    	        
    	      //Display the window.
    	      theGUIe.pack();
    	      theGUIe.setVisible(true); 
    	      theGUIe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        }

    protected void writeYAML() throws IOException{ //creates edges.yaml files and writes information into file
    	String name= "edges" + edges + ".yaml";
    	String name_save= ""+ save+ "/edges" + edges + ".yaml"; //specific directory name to save edges file
    	//System.out.print(name_save); 
    	File outFile = new File (name_save);
        FileWriter fWriter = new FileWriter (outFile);
        PrintWriter pWriter = new PrintWriter (fWriter);
        pWriter.println (formatEdges());
        pWriter.close();
        
        edge_yamls.add(name); //add file name to list to put in create file
        edges++;
        
    }
    
    protected void writeYAMLnodes() throws IOException{ //creates nodes.yaml files and writes information into file
    	String name= "nodes" + nodes_num + ".yaml";
    	String name_save= ""+ save+ "/nodes" + nodes_num + ".yaml"; //specific directory name to save nodes file
    	File outFile = new File (name_save); 
        FileWriter fWriter = new FileWriter (outFile);
        PrintWriter pWriter = new PrintWriter (fWriter);
        pWriter.println (formatNodes());
        pWriter.close();
        
        node_yamls.add(name); //add file name to list to put in create file
    	nodes_num++;
    }
    
    protected void writeCreate() throws IOException{ //creates create.sql and load_into_engine.sh
    	//information is written into files
    	String name= ""+save+"/create.sql";
    	File outFile = new File (name);
        FileWriter fWriter = new FileWriter (outFile);
        PrintWriter pWriter = new PrintWriter (fWriter);
        pWriter.println (formatCreate());
        pWriter.close();
        
        String name2= ""+save+"/load_into_engine.sh";
        File loadFile = new File (name2);
        FileWriter f2Writer = new FileWriter (loadFile);
        PrintWriter p2Writer = new PrintWriter (f2Writer);
        p2Writer.println ("PKG_DIR="+pkgname+ "\nGRAPH_DIR=" + grname);
        p2Writer.println ("\n$PKG_DIR/graphsql_cse $GRAPH_DIR/ create.sql");

        p2Writer.close();
    	
    }

    protected JComponent createDisplay() { //formats GUI display for edges
        JPanel panel = new JPanel(new BorderLayout());
        Display = new JLabel();
        Display.setHorizontalAlignment(JLabel.CENTER);
        regularFont = Display.getFont().deriveFont(Font.PLAIN, 16.0f);
        italicFont = regularFont.deriveFont(Font.ITALIC);
        panel.add(Display, BorderLayout.CENTER);
        panel.setPreferredSize(new Dimension(100, 100));

        return panel;
    }

    protected String formatEdges() { //formats information from edges fields to be written into edges.yaml
        
        String source_name = source.getText();
        String form = (String) format.getValue();
        String sep = separator.getText();
        String vert= vertices.getText(); 
        String buck= buckets.getText();
        String dupl = (String) duplicate.getValue();
        String direct = (String) directed.getValue();
        String dataty = (String) datatype.getValue();
        //String attr = attributes.getText();
  
        String edgeattributes= "";
        for (int i = 0; i < edgearray_attributes.size(); i++) 
        {
        	edgeattributes+= "\t"+edgearray_attributes.get(i)+"\n";  
        	
        }
        
        String result= "Source: \""+ source_name + "\"\n"+ 
        "Format: "+ form + "\n" +
        "Separator: \""+sep + "\"\n" +
        "Number of Vertices (in million): "+ vert + "\n"+
        "Number of Buckets: "+ buck + "\n" + 
        "Allow Duplicate: "+ dupl + "\n" +
        "Directed: " + direct + "\n" +
        "Node datatypes: "+ dataty + "\n";
        if (!edgeattributes.equals(""))
            result+="Edge Attributes: \n" + edgeattributes;
        
        return result;
    }

    protected String formatNodes() { //formats information from nodes fields to be written into nodes.yaml
        String source_name = sourceN.getText();
        String form = (String) formatN.getValue();
        String sep = separatorN.getText();
        String vert= verticesN.getText(); 
        String buck= bucketsN.getText();
        String dupl = (String) duplicateN.getValue();
        String direct = (String) directedN.getValue();
        String dataty = (String) datatypeN.getValue();
        
        String nodeattributes= "";
        for (int i = 0; i < nodearray_attributes.size(); i++) 
        {
        	nodeattributes+= "\t"+nodearray_attributes.get(i)+"\n";  
        	
        }
        
        String result= "Source: \""+ source_name + "\"\n"+
                "Format: "+ form + "\n" +
                "Separator: \""+ sep + "\"\n" +
                "Number of Vertices (in million): " + vert + "\n"+
                "Number of Buckets: "+buck + "\n" +
                "Allow Duplicate: "+ dupl + "\n" +
                "Directed: "+ direct + "\n" +
                "Node datatypes: "+ dataty + "\n";
        if (!nodeattributes.equals(""))
                result+="Vertex Attributes: \n" + nodeattributes;
                
        return result;
    }
    
    protected String formatCreate() { //formats information for create.sql
        
        String gname = graph_name.getText();
        String authorT = author.getText();
        String commentT = comment.getText();
        String dateT = date.getText();
        String result= "";
        
        String edges="";
        String nodes_str="";
        
        for (int i = 0; i < edge_yamls.size(); i++) 
        {
        	edges+= edge_yamls.get(i);  
        	if(edge_yamls.size()>0 && i< edge_yamls.size()-1){
        		edges+=",";
        	}
        }
        
        for (int i = 0; i < node_yamls.size(); i++) 
        {
        	nodes_str+= node_yamls.get(i);  
        	if(node_yamls.size()>0 && i< node_yamls.size()-1){
        		nodes_str+=",";
        	}
        }
        
        if (nodes == true){ //format for if nodes information is entered
        	result= "drop graph "+ gname+ "_partition;\ndrop graph "+ gname+ 
        			";\n\ncreate graph "+ gname+ " -attributes author= '"+  authorT + 
        			"',\n\tcomment='"+ commentT + "',\n\tdate='" + dateT + "';\n\n"+ 
        			"load into graph "+gname+ " -nodeconfig " + nodes_str+ ";\nload into graph "+gname+
        			" -edgeconfig "+ edges+ "; \nshow graph "+ gname+ ";\n\n"+ 
        			"create graphview "+gname+ "_partition from graph "+ gname+ " as ( node_attributes(all) edge_attributes(all) );\n\n"+
        			"partition graph "+ gname+ "_partition -partNum 4 -partAlg streaming;";
        } else if (nodes == false){ //format for if no nodes button has been pressed
        	result= "drop graph "+ gname+ "_partition;\ndrop graph "+ gname+ 
        			";\n\ncreate graph "+ gname+ " -attributes author= '"+ authorT + 
        			"',\n\tcomment='"+ commentT + "',\n\tdate='" + dateT + "';\nload into graph " +gname+
        			" -edgeconfig "+ edges+ "; \nshow graph "+ gname+ ";\n\n"+ 
            		"create graphview "+gname+ "_partition from graph "+ gname+ " as ( node_attributes(all) edge_attributes(all) );\n\n"+
            		"partition graph "+ gname+ "_partition -partNum 4 -partAlg streaming;";
        }
        
        return result;
    }
   
    //A convenience method for creating a MaskFormatter.
    protected MaskFormatter createFormatter(String s) {
        MaskFormatter formatter = null;
        try {
            formatter = new MaskFormatter(s);
        } catch (java.text.ParseException exc) {
            System.err.println("formatter is bad: " + exc.getMessage());
            System.exit(-1);
        }
        return formatter;
    }

    /**
     * Called when one of the fields gets the focus so that
     * we can select the focused field.
     */
    public void focusGained(FocusEvent e) {
        Component c = e.getComponent();
        if (c instanceof JFormattedTextField) {
            selectItLater(c);
        } else if (c instanceof JTextField) {
            ((JTextField)c).selectAll();
        }
    }

    //Workaround for formatted text field focus side effects.
    protected void selectItLater(Component c) {
        if (c instanceof JFormattedTextField) {
            final JFormattedTextField ftf = (JFormattedTextField)c;
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    ftf.selectAll();
                }
            });
        }
    }

    //Needed for FocusListener interface.
    public void focusLost(FocusEvent e) { } //ignore

    protected JComponent createEntryFields() { //labels and creates fields on edges GUI panel
        JPanel panel = new JPanel(new SpringLayout());

        String[] labelStrings = {
            "Source (edges): ",
            "Format: ",
            "Separator: ",
            "Number of Vertices (in million): ",
            "Number of Buckets: ",
            "Allow Duplicate: ",
            "Directed: ",
            "Node datatypes: "
            //"Edge Attributes (distance): "
        };

        JLabel[] labels = new JLabel[labelStrings.length];
        JComponent[] fields = new JComponent[labelStrings.length];
        int fieldNum = 0;

        //Create the text field and set it up.
        source  = new JTextField();
        source.setColumns(20);
        fields[fieldNum++] = source;

        String[] fStrings = getFormatStrings();
        format = new JSpinner(new SpinnerListModel(fStrings));
        fields[fieldNum++] = format;

        separator = new JTextField();
        separator.setColumns(20);
        fields[fieldNum++] = separator;
        
        vertices = new JTextField("1"); 
        vertices.setColumns(20);
        fields[fieldNum++] = vertices;
        
        buckets = new JTextField("1");
        buckets.setColumns(20);
        fields[fieldNum++] = buckets;
        
        String[] duStrings = getDupStrings();
        duplicate = new JSpinner(new SpinnerListModel(duStrings));
        fields[fieldNum++] = duplicate;
        
        String[] diStrings = getDirStrings();
        directed = new JSpinner(new SpinnerListModel(diStrings));
        fields[fieldNum++] = directed;
        
        String[] daStrings = getDataStrings();
        datatype = new JSpinner(new SpinnerListModel(daStrings));
        fields[fieldNum++] = datatype;
        
        //attributes = new JTextField();
        //separator.setColumns(20);
        //fields[fieldNum++] = attributes;

        //Associate label/field pairs, add everything, and lay it out.
        for (int i = 0; i < labelStrings.length; i++) {
            labels[i] = new JLabel(labelStrings[i],
                                   JLabel.TRAILING);
            labels[i].setLabelFor(fields[i]);
            panel.add(labels[i]);
            panel.add(fields[i]);

            //Add listeners to each field.
            JTextField tf = null;
            if (fields[i] instanceof JSpinner) {
                tf = getTextField((JSpinner)fields[i]);
            } else {
                tf = (JTextField)fields[i];
            }
            tf.addActionListener(this);
            tf.addFocusListener(this);
        }
        SpringUtilities.makeCompactGrid(panel,
                                        labelStrings.length, 2,
                                        GAP, GAP, //init x,y
                                        GAP, GAP/2);//xpad, ypad 
        return panel;
    }
    
    protected JComponent create_file() {
        JPanel panel = new JPanel(new SpringLayout());

        String[] labelStrings = {
            "Graph name: ",
            "Attributes-Author: ",
            "Attributes-Comment: ",
            "Attributes-Date: "
        };

        JLabel[] labels = new JLabel[labelStrings.length];
        JComponent[] fields = new JComponent[labelStrings.length];
        int fieldNum = 0;

        //Create the text field and set it up.
        graph_name  = new JTextField();
        graph_name.setColumns(20);
        fields[fieldNum++] =  graph_name;

        author  = new JTextField();
        author.setColumns(20);
        fields[fieldNum++] =  author;
        
        comment  = new JTextField();
        comment.setColumns(20);
        fields[fieldNum++] =  comment;
        
        date  = new JTextField();
        date.setColumns(20);
        fields[fieldNum++] =  date;
       
        //Associate label/field pairs, add everything,
        //and lay it out.
        for (int i = 0; i < labelStrings.length; i++) {
            labels[i] = new JLabel(labelStrings[i],
                                   JLabel.TRAILING);
            labels[i].setLabelFor(fields[i]);
            panel.add(labels[i]);
            panel.add(fields[i]);

            //Add listeners to each field.
            JTextField tf = null;
            if (fields[i] instanceof JSpinner) {
                tf = getTextField((JSpinner)fields[i]);
            } else {
                tf = (JTextField)fields[i];
            }
            tf.addActionListener(this);
            tf.addFocusListener(this);
        }
        SpringUtilities.makeCompactGrid(panel,
                                        labelStrings.length, 2,
                                        10, GAP, //init x,y
                                        GAP, GAP/2);//xpad, ypad
        return panel;
    }
    
    protected JComponent createEntryFieldsN() { //labels and creates fields on nodes GUI panel
        JPanel panel = new JPanel(new SpringLayout());

        String[] labelStrings = {
            "Source (nodes): ",
            "Format: ",
            "Separator: ",
            "Number of Vertices (in million): ",
            "Number of Buckets: ",
            "Allow Duplicate: ",
            "Directed: ",
            "Node datatypes: "
        };

        JLabel[] labels = new JLabel[labelStrings.length];
        JComponent[] fields = new JComponent[labelStrings.length];
        int fieldNum = 0;

        //Create the text field and set it up.
        sourceN  = new JTextField();
        sourceN.setColumns(20);
        fields[fieldNum++] = sourceN;

        String[] fStrings = getFormatStringsN();
        formatN = new JSpinner(new SpinnerListModel(fStrings));
        fields[fieldNum++] = formatN;

        separatorN = new JTextField();
        separatorN.setColumns(20);
        fields[fieldNum++] = separatorN;
      
        verticesN = new JTextField("1"); 
        verticesN.setColumns(20);
        fields[fieldNum++] = verticesN;
        
        bucketsN = new JTextField("1");
        bucketsN.setColumns(20);
        fields[fieldNum++] = bucketsN;
        
        String[] duStrings = getDupStrings();
        duplicateN = new JSpinner(new SpinnerListModel(duStrings));
        fields[fieldNum++] = duplicateN;
        
        String[] diStrings = getDirStrings();
        directedN = new JSpinner(new SpinnerListModel(diStrings));
        fields[fieldNum++] = directedN;
        
        String[] daStrings = getDataStrings();
        datatypeN = new JSpinner(new SpinnerListModel(daStrings));
        fields[fieldNum++] = datatypeN;
        		
        //Associate label/field pairs, add everything, and lay it out.
        for (int i = 0; i < labelStrings.length; i++) {
            labels[i] = new JLabel(labelStrings[i],
                                   JLabel.TRAILING);
            labels[i].setLabelFor(fields[i]);
            panel.add(labels[i]);
            panel.add(fields[i]);

            //Add listeners to each field.
            JTextField tf = null;
            if (fields[i] instanceof JSpinner) {
                tf = getTextField((JSpinner)fields[i]);
            } else {
                tf = (JTextField)fields[i];
            }
            tf.addActionListener(this);
            tf.addFocusListener(this);
        }
        SpringUtilities.makeCompactGrid(panel,
                                        labelStrings.length, 2,
                                        GAP, GAP, //init x,y
                                        GAP, GAP/2);//xpad, ypad
        return panel;
    }
    
    

    public String[] getFormatStrings() { //shows options for edges Format JSpinner
        String[] FormatStrings = {
            "edge_with_properties",
            "edge_without_properties"
        };
        return FormatStrings;
    }
    
    public String[] getFormatStringsN() { //shows options for nodes Format JSpinner
        String[] FormatStrings = {
            "vertex_with_properties",
            "vertex_without_properties"
        };
        return FormatStrings;
    }
    
    public String[] getVertices() { //shows options for edges and nodes Number of Vertices JSpinner
        String[] VertStrings = {
            "1", "2", "3", "4", "5", "0"
        };
        return VertStrings;
    }

    public String[] getBuckets() { //shows options for edges and nodes Number of Buckets JSpinner
        String[] BuckStrings = {
            "1", "2", "3", "4", "5", "0"
        };
        return BuckStrings;
    }

    public String[] getDupStrings() { //shows options for edges and nodes Duplicate JSpinner
        String[] DupStrings = {
        		"false", "true"
            
        };
        return DupStrings;
    }
    
    public String[] getDirStrings() { //shows options for edges and nodes Directed JSpinner
        String[] DirStrings = {
        		"false", "true"
            
        };
        return DirStrings;
    }
    
    public String[] getDataStrings() { //shows options for edges and nodes Datatype JSpinner
        String[] DataStrings = {
            "long",
            "string"
        };
        return DataStrings;
    }
    
    public JFormattedTextField getTextField(JSpinner spinner) {
        JComponent editor = spinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            return ((JSpinner.DefaultEditor)editor).getTextField();
        } else {
            System.err.println("Unexpected editor type: "
                               + spinner.getEditor().getClass()
                               + " isn't a descendant of DefaultEditor");
            return null;
        }
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);

        //Create and set up the window.
        JFrame frame = new JFrame("Load YAML Information into Graph");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        JComponent newContentPane = new YAMLGenMultiple();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        //frame.setMinimumSize(new Dimension(900, 700));
        frame.setVisible(true);
    }
    

    public static void main(String[] args) throws IOException {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });

    }
}

