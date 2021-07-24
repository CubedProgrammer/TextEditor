package cp.texteditor;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
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
import cp.texteditor.event.EnterHandler;
public class TextEditor extends JPanel
{
	private static final long serialVersionUID=-2941721218264436063L;
	public static final String NAME="/CubedProgrammer/TextEditor";
	private JTabbedPane editors;
	private JMenuBar menubar;
	private JMenu filemenu;
	private JMenu editmenu;
	private JMenu compilem;
	private JMenu linkm;
	private JMenu runm;
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
	private JMenuItem linkmi;
	private JMenuItem compileAndLink;
	private JMenuItem linkOptions;
	private JMenuItem runmi;
	private JMenuItem compileLinkRun;
	private JMenuItem runOptions;
	private HashMap<String,String>cmplops;
	private String linker;
	private String runcmd;
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
		this.linkm=new JMenu("Link");
		this.runm=new JMenu("Run");
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
		this.linkmi=new JMenuItem("Link");
		this.compileAndLink=new JMenuItem("Compile and Link");
		this.linkOptions=new JMenuItem("Link Options");
		this.runmi=new JMenuItem("Run");
		this.compileLinkRun=new JMenuItem("Compile, Link, and Run");
		this.runOptions=new JMenuItem("Run Options");
		this.cmplops=new HashMap<String,String>();
		this.cmplops.put("c","gcc -O3 -c");
		this.cmplops.put("cpp","g++ -O3 -std=c++20 -c");
		this.cmplops.put("cc","g++ -O3 -std=c++20 -c");
		this.cmplops.put("f95","gfortran -O3 -c");
		this.cmplops.put("cs","csc -optimize");
		this.cmplops.put("java","javac");
		this.linker="";
		this.runcmd="./a.out";
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
				this.linker=obj.get("link").toString();
				this.runcmd=obj.get("run").toString();
			}
			else
			{
				JSONObject obj=new JSONObject();
				obj.putAll(this.cmplops);
				JSONObject cpte=new JSONObject();
				cpte.put("compile",obj);
				cpte.put("link",this.linker);
				cpte.put("run",this.runcmd);
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
		this.linkmi.addActionListener(this::linkEventListener);
		this.compileAndLink.addActionListener(this::compileAndLinkEventListener);
		this.linkOptions.addActionListener(this::linkopEventListener);
		this.runmi.addActionListener(this::runEventListener);
		this.compileLinkRun.addActionListener(this::compileLinkRunEventListener);
		this.runOptions.addActionListener(this::runopEventListener);
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
		this.linkm.add(this.linkmi);
		this.linkm.add(this.compileAndLink);
		this.linkm.add(this.linkOptions);
		this.runm.add(this.runmi);
		this.runm.add(this.compileLinkRun);
		this.runm.add(this.runOptions);
		this.menubar.add(this.filemenu);
		this.menubar.add(this.editmenu);
		this.menubar.add(this.compilem);
		this.menubar.add(this.linkm);
		this.menubar.add(this.runm);
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
	public void link()
	{
		try
		{
			Process proc=Runtime.getRuntime().exec(this.linker);
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
				JFrame frame=new JFrame("Linker Output");
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
	@SuppressWarnings("unchecked")
	public void editLinkOptions()
	{
		JPanel panel=new JPanel();
		JButton save=new JButton("Save");
		JTextField field=new JTextField(this.linker,80);
		save.addActionListener
		(
			(evt)->
			{
				this.linker=field.getText();
				File f=new File(".cpte");
				JSONObject obj=new JSONObject();
				obj.putAll(this.cmplops);
				JSONObject cpte=new JSONObject();
				cpte.put("compile",obj);
				cpte.put("link",this.linker);
				cpte.put("run",this.runcmd);
				try
				{
					FileOutputStream fout=new FileOutputStream(f);
					fout.write(cpte.toJSONString().getBytes());
					fout.close();
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
		);
		panel.add(field);
		panel.add(save);
		{
			this.linker=field.getText();
			File f=new File(".cpte");
			JSONObject obj=new JSONObject();
			obj.putAll(this.cmplops);
			JSONObject cpte=new JSONObject();
			cpte.put("compile",obj);
			cpte.put("link",this.linker);
			try
			{
				FileOutputStream fout=new FileOutputStream(f);
				fout.write(cpte.toJSONString().getBytes());
				fout.close();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
		JFrame frame=new JFrame("Link Command");
		frame.add(panel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
	}
	public void run()
	{
		try
		{
			JFrame frame=new JFrame(this.runcmd+" 1");
			JFrame frame2=new JFrame(this.runcmd+" 0");
			JPanel panel=new JPanel();
			JPanel panel2=new JPanel();
			JTextArea stdout=new JTextArea(40,80);
			JTextArea stdin=new JTextArea(40,80);
			JScrollPane scout=new JScrollPane(stdout);
			JScrollPane scin=new JScrollPane(stdin);
			stdin.setEditable(true);
			stdout.setEditable(false);
			panel.add(scout);
			frame.add(panel);
			frame.pack();
			frame.setLocationRelativeTo(null);
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.setVisible(true);
			panel2.add(scin);
			frame2.add(panel2);
			frame2.pack();
			frame2.setLocationRelativeTo(null);
			frame2.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame2.setVisible(true);
			Process proc=Runtime.getRuntime().exec(this.runcmd);
			OutputStream pin=proc.getOutputStream();
			InputStream pout=proc.getInputStream();
			InputStream perr=proc.getErrorStream();
			stdin.addKeyListener(new EnterHandler(stdin,pin));
			BufferedReader reader=new BufferedReader(new InputStreamReader(pout));
			String ln=null;
			while(proc.isAlive())
			{
				ln=reader.readLine();
				stdout.append(ln+System.lineSeparator());
			}
			ln=reader.readLine();
			while(ln!=null)
			{
				stdout.append(ln+System.lineSeparator());
				ln=reader.readLine();
			}
			pin.close();
			reader.close();
			perr.close();
			stdout.append("Process exited with value "+proc.exitValue()+".");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	@SuppressWarnings("unchecked")
	public void editRunOptions()
	{
		JPanel panel=new JPanel();
		JButton save=new JButton("Save");
		JTextField field=new JTextField(this.runcmd,80);
		save.addActionListener
		(
			(evt)->
			{
				this.runcmd=field.getText();
				File f=new File(".cpte");
				JSONObject obj=new JSONObject();
				obj.putAll(this.cmplops);
				JSONObject cpte=new JSONObject();
				cpte.put("compile",obj);
				cpte.put("link",this.linker);
				cpte.put("run",this.runcmd);
				try
				{
					FileOutputStream fout=new FileOutputStream(f);
					fout.write(cpte.toJSONString().getBytes());
					fout.close();
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
		);
		panel.add(field);
		panel.add(save);
		JFrame frame=new JFrame("Run Command");
		frame.add(panel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
	public void linkEventListener(ActionEvent e)
	{
		this.link();
	}
	public void compileAndLinkEventListener(ActionEvent e)
	{
		this.compileAll();
		this.link();
	}
	public void linkopEventListener(ActionEvent e)
	{
		this.editLinkOptions();
	}
	public void runEventListener(ActionEvent e)
	{
		this.run();
	}
	public void compileLinkRunEventListener(ActionEvent e)
	{
		this.compileAll();
		this.link();
		this.run();
	}
	public void runopEventListener(ActionEvent e)
	{
		this.editRunOptions();
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