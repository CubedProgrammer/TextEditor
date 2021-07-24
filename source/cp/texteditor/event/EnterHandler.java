package cp.texteditor.event;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.OutputStream;
import javax.swing.JTextArea;
public class EnterHandler extends KeyAdapter
{
	private JTextArea area;
	private OutputStream stdin;
	private int index;
	public EnterHandler(JTextArea area,OutputStream stdin)
	{
		this.area=area;
		this.stdin=stdin;
	}
	public void keyTyped(KeyEvent e)
	{
		char typed = e.getKeyChar();
		if(!e.isShiftDown()&&typed=='\n')
		{
			try
			{
				this.stdin.write(this.area.getText(this.index,this.area.getText().length()-this.index).getBytes());
			}
			catch(Exception e1)
			{
				e1.printStackTrace();
			}
		}
	}
}