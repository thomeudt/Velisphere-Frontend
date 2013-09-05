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

import javax.swing.Action;


public class HomeController {

	private JFrame frmVelisphereHomeController;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
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
		
		JLabel lblArbeitszimmer = new JLabel("Arbeitszimmer");
		lblArbeitszimmer.setBounds(10, 13, 99, 14);
		frmVelisphereHomeController.getContentPane().add(lblArbeitszimmer);
		
		JToggleButton tglbtnLicht = new JToggleButton("Licht");
		tglbtnLicht.setBounds(10, 38, 121, 23);
		
		frmVelisphereHomeController.getContentPane().add(tglbtnLicht);
		
		tglbtnLicht.addActionListener(new ActionListener( ) {
		      public void actionPerformed(ActionEvent ev) {
		        try {
					Send.main("lightsoff", "phi");
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
