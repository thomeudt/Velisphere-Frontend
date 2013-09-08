import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;
import javax.swing.JSpinner;
import javax.swing.JSlider;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.AbstractAction;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.Action;
import javax.swing.JButton;


public class HomeController {

	private JFrame frmVelisphereHomeController;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		ServerParameters.my_queue_name="1003";
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HomeController window = new HomeController();
					window.frmVelisphereHomeController.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public HomeController() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmVelisphereHomeController = new JFrame();
		frmVelisphereHomeController.setTitle("VeliSphere Home Controller");
		frmVelisphereHomeController.setBounds(100, 100, 450, 300);
		frmVelisphereHomeController.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmVelisphereHomeController.getContentPane().setLayout(null);
		
		JLabel lblArbeitszimmer = new JLabel("Licht");
		lblArbeitszimmer.setBounds(10, 13, 99, 14);
		frmVelisphereHomeController.getContentPane().add(lblArbeitszimmer);
		
		final JToggleButton tglbtnLicht = new JToggleButton("Licht 1");
		tglbtnLicht.setBounds(10, 38, 121, 23);
		
		frmVelisphereHomeController.getContentPane().add(tglbtnLicht);
		
		JButton btnLichtAus = new JButton("Licht aus");
		btnLichtAus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnLichtAus.setBounds(163, 38, 121, 23);
		frmVelisphereHomeController.getContentPane().add(btnLichtAus);
		
		JButton btnRolladenRunter = new JButton("Rolladen runter");
		btnRolladenRunter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnRolladenRunter.setBounds(163, 72, 121, 23);
		frmVelisphereHomeController.getContentPane().add(btnRolladenRunter);
		
		final JToggleButton tglbtnLicht_2 = new JToggleButton("Licht 2");
		tglbtnLicht_2.setBounds(10, 72, 121, 23);
		frmVelisphereHomeController.getContentPane().add(tglbtnLicht_2);
		
		final JToggleButton tglbtnLicht_3 = new JToggleButton("Licht 3");
		tglbtnLicht_3.setBounds(10, 107, 121, 23);
		frmVelisphereHomeController.getContentPane().add(tglbtnLicht_3);
		
		final JToggleButton tglbtnLicht_4 = new JToggleButton("Licht 4");
		tglbtnLicht_4.setBounds(10, 138, 121, 23);
		frmVelisphereHomeController.getContentPane().add(tglbtnLicht_4);
		
		final JToggleButton tglbtnLicht_5 = new JToggleButton("Licht 5");
		tglbtnLicht_5.setBounds(10, 172, 121, 23);
		frmVelisphereHomeController.getContentPane().add(tglbtnLicht_5);
		
		JLabel lblFenster = new JLabel("Fenster");
		lblFenster.setBounds(163, 13, 99, 14);
		frmVelisphereHomeController.getContentPane().add(lblFenster);
						
		tglbtnLicht.addActionListener(new ActionListener( ) {
		   
			
			public void actionPerformed(ActionEvent ev) {
		
				String b1, b2, b3, b4, b5;
				
				if(tglbtnLicht.isSelected()){
					b1 = "1";
					}
				else{
					b1 = "0";
				}
				
				if(tglbtnLicht_2.isSelected()){
					b2 = "1";
					}
				else{
					b2 = "0";
				}
				
				if(tglbtnLicht_3.isSelected()){
					b3 = "1";
					}
				else{
					b3 = "0";
				}
				
				if(tglbtnLicht_4.isSelected()){
					b4 = "1";
					}
				else{
					b4 = "0";
				}
				
				if(tglbtnLicht_5.isSelected()){
					b5 = "1";
					}
				else{
					b5 = "0";
				}
				
				try {
					
		        	HashMap<String, String> messageHash = new HashMap<String, String>();
		        	messageHash.put("1000", b1);
		        	messageHash.put("1001", b2);
		        	messageHash.put("1004", b3);
		        	messageHash.put("1005", b4);
		        	messageHash.put("1006", b5);
		        	
		        	Send.sendHashTable(messageHash, "controller");

		        	// Send.main("lightsoff", "phi");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        JOptionPane.showMessageDialog(frmVelisphereHomeController,
                        "Licht umgeschaltet"
                        );
		      }
		    });

		
		
		
		
		
	}
}
