package view;
import javax.swing.JOptionPane;

public class ErrorMessage {
	public static void errorBox(String infoMessage, String titleBar) {
		JOptionPane.showMessageDialog(null,infoMessage,"ErrorBox: " + titleBar, JOptionPane.ERROR_MESSAGE);
	}
}
