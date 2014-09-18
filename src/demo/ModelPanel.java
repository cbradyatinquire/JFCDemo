package demo;

import java.awt.Dimension;
import java.awt.Graphics;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.nlogo.api.CompilerException;
import org.nlogo.api.LogoException;
import org.nlogo.headless.HeadlessWorkspace;

public class ModelPanel extends JPanel {

	HeadlessWorkspace myWS;

	/**
	 * 
	 */
	private static final long serialVersionUID = -5698493279961927038L;
	public int width;
	public int height;

	public ModelPanel( int w, int h) {
		myWS = HeadlessWorkspace.newInstance();
		width = w;
		height = h;
		this.setPreferredSize(new Dimension(width, height));
		this.setSize(width, height);
	}

	public void openModel(String model) {
		try {
			myWS.open(model);
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (CompilerException e1) {
			e1.printStackTrace();
		} catch (LogoException e1) {
			e1.printStackTrace();
		}

	}

	@SuppressWarnings("unused")
	private void openModel() {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"NetLogo models", "nlogo");
		chooser.setFileFilter(filter);
		int returnVal = chooser.showOpenDialog(getParent());
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			try {
				myWS.open(chooser.getSelectedFile().getPath());
			} catch (IOException e) {
				e.printStackTrace();
			} catch (CompilerException e) {
				e.printStackTrace();
			} catch (LogoException e) {
				e.printStackTrace();
			}
		}

	}
	public void runCommand(String command){

		try {
			myWS.command(command);
		} catch (CompilerException e) {
			e.printStackTrace();
		} catch (LogoException e) {
			e.printStackTrace();
		}
	}
	
	public double runReporter(String reporter){

		try {
			Object o = myWS.report(reporter);
			return (Double)o;
			
		} catch (CompilerException e) {
			e.printStackTrace();
		} catch (LogoException e) {
			e.printStackTrace();
		}
		return -1.0;
	}

	public ArrayList<String> getGlobals(){
		ArrayList<String> theList = new ArrayList<String>();
		for (int i = 0; i < myWS.world().observer().getVariableCount(); i++){
			theList.add(myWS.world().observer().variableName(i));
		}
		return theList;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(myWS.exportView(), 0, 0, null);
	}

}
