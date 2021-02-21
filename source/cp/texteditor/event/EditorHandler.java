package cp.texteditor.event;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import cp.texteditor.ActualEditor;
public class EditorHandler implements DocumentListener
{
	private ActualEditor editor;
	public EditorHandler(ActualEditor editor)
	{
		this.editor=editor;
	}
	@Override
	public void insertUpdate(DocumentEvent e)
	{
		this.editor.saveUndoHistory();
	}
	@Override
	public void removeUpdate(DocumentEvent e)
	{
		this.editor.saveUndoHistory();
	}
	@Override
	public void changedUpdate(DocumentEvent e)
	{
		this.editor.saveUndoHistory();
	}
}