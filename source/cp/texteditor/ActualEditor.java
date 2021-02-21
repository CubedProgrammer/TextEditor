package cp.texteditor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JTextArea;
import cp.texteditor.event.EditorHandler;
public class ActualEditor extends JTextArea
{
	private static final long serialVersionUID=-5129872073524556370L;
	private File file;
	private ArrayList<String>history;
	private int index;
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
	public void undo()
	{
		if(this.index>0)
		{
			this.setText(this.history.get(--this.index));
		}
	}
	public void redo()
	{
		if(this.index+1<this.history.size())
		{
			this.setText(this.history.get(++this.index));
		}
	}
	public void saveUndoHistory()
	{
		if(this.getName().charAt(0)!='*')
			this.setName("*"+this.getName());
		if(this.index+1<this.history.size())
			this.history=(ArrayList<String>)this.history.subList(0,index+1);
		this.history.add(this.getText());
		++this.index;
	}
	public void save()throws IOException
	{
		if(this.getName().charAt(0)=='*')
			this.setName(this.getName().substring(1));
		FileOutputStream out=new FileOutputStream(this.file);
		byte[]b=this.getText().getBytes();
		out.write(b);
		out.close();
	}
}