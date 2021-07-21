package cp.texteditor;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
public class TextEditor extends JPanel
{
	private static final long serialVersionUID=-2941721218264436063L;
	public static final String NAME="/CubedProgrammer/TextEditor";
	private JTabbedPane editors;
	private JMenuBar menubar;
	private JMenu filemenu;
	private JMenu editmenu;
	private JMenu compilem;
	private JMenuItem save;
	private JMenuItem saveall;
	private JMenuItem open;
	private JMenuItem closefile;
	private JMenuItem closenosave;
	private JMenuItem closeall;
	private JMenuItem undob;
	private JMenuItem redob;
	private JMenuItem compilemi;
	private JMenuItem compileall;
	private JMenuItem compileOptions;
	private HashMap<String,String>cmplops;
	@SuppressWarnings("unchecked")
	public TextEditor()
	{
		this.setSize(1024,576);
		this.setPreferredSize(this.getSize());
		this.editors=new JTabbedPane();
		this.menubar=new JMenuBar();
		this.filemenu=new JMenu("File");
		this.editmenu=new JMenu("Edit");
		this.compilem=new JMenu("Compile");
		this.save=new JMenuItem("Save");
		this.saveall=new JMenuItem("Save All");
		this.open=new JMenuItem("Open");
		this.closefile=new JMenuItem("Close Tab");
		this.closenosave=new JMenuItem("Close Without Saving");
		this.closeall=new JMenuItem("Close All");
		this.undob=new JMenuItem("Undo");
		this.redob=new JMenuItem("Redo");
		this.compilemi=new JMenuItem("Compile Current");
		this.compileall=new JMenuItem("Compile All");
		this.compileOptions=new JMenuItem("Compile Options");
		this.cmplops=new HashMap<String,String>();
		this.cmplops.put("c","gcc -O3 -c");
		this.cmplops.put("cpp","g++ -O3 -std=c++20 -c");
		this.cmplops.put("cc","g++ -O3 -std=c++20 -c");
		this.cmplops.put("f95","gfortran -O3 -c");
		this.cmplops.put("cs","csc -optimize");
		this.cmplops.put("java","javac");
		try
		{
			File f=new File(".cpte");
			if(f.exists())
			{
				this.cmplops.clear();
				FileReader reader=new FileReader(f);
				JSONObject obj=(JSONObject)new JSONParser().parse(reader);
				reader.close();
				this.cmplops.putAll((JSONObject)obj.get("compile"));
			}
			else
			{
				JSONObject obj=new JSONObject();
				obj.putAll(this.cmplops);
				JSONObject cpte=new JSONObject();
				cpte.put("compile",obj);
				FileOutputStream fout=new FileOutputStream(f);
				fout.write(cpte.toJSONString().getBytes());
				fout.close();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		this.editors.setPreferredSize(this.getSize());
		this.open.addActionListener(this::openEventListener);
		this.save.addActionListener(this::saveEventListener);
		this.saveall.addActionListener(this::saveallEventListener);
		this.closefile.addActionListener(this::closeEventListener);
		this.closenosave.addActionListener(this::cnsEventListener);
		this.closeall.addActionListener(this::closeallEventListener);
		this.undob.addActionListener(this::undoEventListener);
		this.redob.addActionListener(this::redoEventListener);
		this.compilemi.addActionListener(this::compileEventListener);
		this.compileall.addActionListener(this::compileallEventListener);
		this.compileOptions.addActionListener(this::compileopEventListener);
		this.filemenu.add(this.save);
		this.filemenu.add(this.saveall);
		this.filemenu.add(this.open);
		this.filemenu.add(this.closefile);
		this.filemenu.add(this.closenosave);
		this.filemenu.add(this.closeall);
		this.editmenu.add(this.undob);
		this.editmenu.add(this.redob);
		this.compilem.add(this.compilemi);
		this.compilem.add(this.compileall);
		this.compilem.add(this.compileOptions);
		this.menubar.add(this.filemenu);
		this.menubar.add(this.editmenu);
		this.menubar.add(this.compilem);
		this.add(this.menubar);
		this.add(this.editors);
	}
	public void openFiles()
	{
		JFrame frame=new JFrame("Open");
		JFileChooser chooser=new JFileChooser(".");
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setMultiSelectionEnabled(true);
		chooser.showOpenDialog(frame);
		File[]files=chooser.getSelectedFiles();
		for(int i=0;i<files.length;i++)
		{
			this.editors.add(new ActualEditor(files[i]));
		}
		frame.dispose();
	}
	public void saveFile()
	{
		var comp=this.editors.getSelectedComponent();
		if(comp.getClass()==ActualEditor.class)
		{
			ActualEditor editor=(ActualEditor)comp;
			try
			{
				editor.save();
			}
			catch(IOException e)
			{
				System.out.println("File could not be saved for some reason.");
				e.printStackTrace();
			}
		}
	}
	public void saveFiles()
	{
		Component[]components=this.editors.getComponents();
		ActualEditor editor=null;
		for(int i=0;i<components.length;i++)
		{
			if(components[i].getClass()==ActualEditor.class)
			{
				editor=(ActualEditor)components[i];
				try
				{
					System.out.println("saving");
					editor.save();
				}
				catch(IOException e)
				{
					System.out.println("File could not be saved for some reason.");
					e.printStackTrace();
				}
			}
		}
	}
	public void closeTab()
	{
		this.editors.remove(this.editors.getSelectedComponent());
	}
	public void closeAllTabs()
	{
		this.editors.removeAll();
	}
	public void undo()
	{
		var comp=this.editors.getSelectedComponent();
		if(comp.getClass()==ActualEditor.class)
		{
			ActualEditor editor=(ActualEditor)comp;
			editor.undo();
		}
	}
	public void redo()
	{
		var comp=this.editors.getSelectedComponent();
		if(comp.getClass()==ActualEditor.class)
		{
			ActualEditor editor=(ActualEditor)comp;
			editor.redo();
		}
	}
	public void compileFile()
	{
		var comp=this.editors.getSelectedComponent();
		if(comp.getClass()==ActualEditor.class)
		{
			ActualEditor editor=(ActualEditor)comp;
			File f=editor.getFile();
			String fname=f.getName();
			int period=fname.lastIndexOf('.');
			String extension="";
			if(period>0)
				extension=fname.substring(period+1);
			System.out.println("compiling " + f.getAbsolutePath());
			System.out.println(extension);
			System.out.println(this.cmplops);
			if(this.cmplops.containsKey(extension))
			{
				try
				{
					Process proc=Runtime.getRuntime().exec(this.cmplops.get(extension)+" "+f.getPath());
					InputStream perr=proc.getErrorStream();
					proc.waitFor();
					if(perr.available()>0)
					{
						byte[]b=new byte[perr.available()];
						perr.read(b);
						JTextArea area=new JTextArea(new String(b));
						area.setEditable(false);
						JPanel panel=new JPanel();
						panel.add(area);
						JFrame frame=new JFrame("Compiler Output");
						frame.add(panel);
						frame.pack();
						frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
						frame.setLocationRelativeTo(null);
						frame.setResizable(false);
						frame.setVisible(true);
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	public void compileAll()
	{
		Component[]components=this.editors.getComponents();
		ActualEditor editor=null;
		File f=null;
		String fname=null;
		String extension="";
		Process proc=null;
		InputStream perr=null;
		JPanel panel=new JPanel();
		JFrame frame=new JFrame("Compiler Output");
		String err="";
		for(int i=0;i<components.length;i++)
		{
			if(components[i].getClass()==ActualEditor.class)
			{
				editor=(ActualEditor)components[i];
				f=editor.getFile();
				fname=f.getName();
				int period=fname.lastIndexOf('.');
				extension="";
				if(period>0)
					extension=fname.substring(period+1);
				if(this.cmplops.containsKey(extension))
				{
					try
					{
						proc=Runtime.getRuntime().exec(this.cmplops.get(extension)+" "+f.getPath());
						perr=proc.getErrorStream();
						proc.waitFor();
						if(perr.available()>0)
						{
							byte[]b=new byte[perr.available()];
							perr.read(b);
							err+=new String(b);
						}
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}
			}
		}
		if(err.length()>0)
		{
			JTextArea area=new JTextArea(err);
			area.setEditable(false);
			panel.add(area);
			frame.add(panel);
			frame.pack();
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.setLocationRelativeTo(null);
			frame.setResizable(false);
			frame.setVisible(true);
		}
		else
		{
			frame.dispose();
		}
	}
	@SuppressWarnings("unchecked")
	public void editCompilerOptions()
	{
		String[]extensions=this.cmplops.keySet().toArray(new String[this.cmplops.size()]);
		JPanel panel=new JPanel();
		JScrollPane sp = new JScrollPane();
		String ext = null;
		//panel.add(new JLabel("File Extension"));
		sp.setSize(1024,576);
		sp.setPreferredSize(sp.getSize());
		ArrayList<JTextField>extensionTextFields=new ArrayList<>();
		ArrayList<JTextField>commandTextFields=new ArrayList<>();
		JTextField field=null;
		int height=0;
		for(int i=0;i<extensions.length;i++)
		{
			ext=extensions[i];
			field = new JTextField(ext,8);
			field.setLocation(0,height);
			extensionTextFields.add(field);
			panel.add(field);
			field = new JTextField(this.cmplops.get(ext), 32);
			commandTextFields.add(field);
			panel.add(field);
			height+=32;
		}
		JButton add=new JButton("Add");
		JButton save=new JButton("Save");
		/*add.addActionListener
		(
			(e)->
			{
				extensionTextFields.add(new JTextField(8));
				panel.add(extensionTextFields.get(extensionTextFields.size()-1));
				commandTextFields.add(new JTextField(32));
				panel.add(commandTextFields.get(commandTextFields.size()-1));
			}
		);*/
		save.addActionListener
		(
			(evt)->
			{
				for(int i=0;i<extensionTextFields.size();i++)
				{
					this.cmplops.put(extensionTextFields.get(i).getText(),commandTextFields.get(i).getText());
				}
				try
				{
					File f=new File(".cpte");
					JSONObject obj=new JSONObject();
					obj.putAll(this.cmplops);
					JSONObject cpte=new JSONObject();
					cpte.put("compile",obj);
					FileOutputStream fout=new FileOutputStream(f);
					fout.write(cpte.toJSONString().getBytes());
					fout.close();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		);
		panel.add(add);
		panel.add(save);
		panel.setSize(512,576);
		panel.setPreferredSize(panel.getSize());
		JFrame frame=new JFrame("Compiler Options");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.add(panel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	public void openEventListener(ActionEvent e)
	{
		this.openFiles();
	}
	public void saveEventListener(ActionEvent e)
	{
		this.saveFile();
	}
	public void saveallEventListener(ActionEvent e)
	{
		this.saveFiles();
	}
	public void closeEventListener(ActionEvent e)
	{
		this.saveFile();
		this.closeTab();
	}
	public void cnsEventListener(ActionEvent e)
	{
		this.closeTab();
	}
	public void closeallEventListener(ActionEvent e)
	{
		this.closeAllTabs();
	}
	public void undoEventListener(ActionEvent e)
	{
		this.undo();
	}
	public void redoEventListener(ActionEvent e)
	{
		this.redo();
	}
	public void compileEventListener(ActionEvent e)
	{
		this.compileFile();
	}
	public void compileallEventListener(ActionEvent e)
	{
		this.compileAll();
	}
	public void compileopEventListener(ActionEvent e)
	{
		this.editCompilerOptions();
	}
	public static final void main(String[]args)
	{
		System.out.println(NAME);
		JFrame frame=new JFrame(NAME);
		TextEditor editor=new TextEditor();
		frame.add(editor);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}