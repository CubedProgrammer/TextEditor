package cp.texteditor;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
public class TextEditor extends JPanel
{
	private static final long serialVersionUID=-2941721218264436063L;
	public static final String NAME="/CubedProgrammer/TextEditor";
	private JTabbedPane editors;
	private JMenuBar menubar;
	private JMenu filemenu;
	private JMenu editmenu;
	private JMenuItem save;
	private JMenuItem saveall;
	private JMenuItem open;
	private JMenuItem closefile;
	public TextEditor()
	{
		this.setSize(1024,576);
		this.setPreferredSize(this.getSize());
		this.editors=new JTabbedPane();
		this.menubar=new JMenuBar();
		this.filemenu=new JMenu("File");
		this.editmenu=new JMenu("Edit");
		this.save=new JMenuItem("Save");
		this.saveall=new JMenuItem("Save All");
		this.open=new JMenuItem("Open");
		this.closefile=new JMenuItem("Close Tab");
		this.editors.setPreferredSize(this.getSize());
		this.open.addActionListener(this::openEventListener);
		this.save.addActionListener(this::saveEventListener);
		this.saveall.addActionListener(this::saveallEventListener);
		this.closefile.addActionListener(this::closeEventListener);
		this.filemenu.add(this.save);
		this.filemenu.add(this.saveall);
		this.filemenu.add(this.open);
		this.filemenu.add(this.closefile);
		this.menubar.add(this.filemenu);
		this.menubar.add(this.editmenu);
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
		this.saveFile();
		this.editors.remove(this.editors.getSelectedComponent());
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
		this.closeTab();
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