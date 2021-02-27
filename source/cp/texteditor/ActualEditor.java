package cp.texteditor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import cp.texteditor.event.EditorHandler;
public class ActualEditor extends JTextArea
{
	private static final long serialVersionUID=-5129872073524556370L;
	private File file;
	private ArrayList<String>history;
	private int index;
	private int undoed;
	public ActualEditor(File file)
	{
		super();
		this.setName(file.getName());
		this.history=new ArrayList<String>();
		try
		{
			FileInputStream in=new FileInputStream(this.file=file);
			byte[]b=new byte[in.available()];
			in.read(b);
			this.setText(new String(b));
			in.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		this.history.add(this.getText());
		this.getDocument().addDocumentListener(new EditorHandler(this));
	}
	public ActualEditor()
	{
		this(new File("."));
	}
	public File getFile()
	{
		return this.file;
	}
	public void undo()
	{
		if(this.index>0)
		{
			this.undoed=2;
			this.setText(this.history.get(--this.index));
		}
	}
	public void redo()
	{
		if(this.index+1<this.history.size())
		{
			this.undoed=2;
			this.setText(this.history.get(++this.index));
		}
	}
	public void saveUndoHistory()
	{
		if(this.undoed==0)
		{
			if(this.getName().charAt(0)!='*')
			{
				this.setName("*"+this.getName());
				JTabbedPane parent=(JTabbedPane)this.getParent();
				parent.setTitleAt(parent.indexOfComponent(this),this.getName());
			}
			if(this.index+1<this.history.size())
				this.history=new ArrayList<String>(this.history.subList(0,this.index+1));
			this.history.add(this.getText());
			++this.index;
		}
		else
		{
			--this.undoed;
		}
	}
	public void save()throws IOException
	{
		if(this.getName().charAt(0)=='*')
		{
			this.setName(this.getName().substring(1));
			JTabbedPane parent=(JTabbedPane)this.getParent();
			parent.setTitleAt(parent.indexOfComponent(this),this.getName());
		}
		System.out.println("name "+this.getName());
		FileOutputStream out=new FileOutputStream(this.file);
		byte[]b=this.getText().getBytes();
		out.write(b);
		out.close();
	}
}