package jd.satellite.main;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JSeparator;
import java.awt.Color;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class App extends JFrame {

	private JPanel contentPane;
	private JTextField s1Period;
	private JTextField s1Radius;
	private JTextField s2Period;
	private JTextField s2Radius;
	private JTextField dayLength;
	private JTextField timeStep;
	
	private boolean drawStartingLine = false;
	private boolean drawSatLines = true;
	private boolean drawSatLabels = true;
	private boolean stopAfterDay = true;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					App frame = new App();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public App() {
		setTitle("Satellite Orbits Simulator v0.1 by Jesse Deppisch");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setBounds(100, 100, 1120, 860);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		SimulationPanel panel = new SimulationPanel(this);
		panel.setBounds(12, 13, 800, 800);
		contentPane.add(panel);
		
		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setForeground(Color.BLACK);
		separator.setBounds(826, 13, 2, 800);
		contentPane.add(separator);
		
		JButton btnStop = new JButton("Stop simulation");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Stop simulation
				panel.stop();
			}
		});
		btnStop.setBounds(840, 414, 253, 25);
		contentPane.add(btnStop);
		
		JButton btnApplyChanges = new JButton("Apply changes");
		btnApplyChanges.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Trying to apply changes
				
				// Convert values first
				double t1 = Double.parseDouble(s1Period.getText());
				double r1 = Double.parseDouble(s1Radius.getText());
				double t2 = Double.parseDouble(s2Period.getText());
				double r2 = Double.parseDouble(s2Radius.getText());
				double dL = Double.parseDouble(dayLength.getText());
				double tS = Double.parseDouble(timeStep.getText());
				
				// Send values to simulation panel
				panel.updateValues(t1, r1, t2, r2, dL, tS);
			}
		});
		btnApplyChanges.setBounds(840, 452, 253, 25);
		contentPane.add(btnApplyChanges);
		
		JButton btnStartSimulation = new JButton("Start/Restart simulation");
		btnStartSimulation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Trying to start simulation
				panel.startSimulation();
			}
		});
		btnStartSimulation.setBounds(840, 490, 253, 25);
		contentPane.add(btnStartSimulation);
		
		JCheckBox startingLine = new JCheckBox("Starting Line");
		startingLine.setBounds(836, 261, 113, 25);
		startingLine.setSize(startingLine.getPreferredSize());
		startingLine.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (startingLine.isSelected()) {
					drawStartingLine = true;
				} else {
					drawStartingLine = false;
				}
			}
		});
		contentPane.add(startingLine);
		
		JCheckBox labelSatellites = new JCheckBox("Satellite Labels");
		labelSatellites.setBounds(836, 321, 125, 25);
		labelSatellites.setSize(labelSatellites.getPreferredSize());
		labelSatellites.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (labelSatellites.isSelected()) {
					drawSatLabels = true;
				} else {
					drawSatLabels = false;
				}
			}
		});
		labelSatellites.setSelected(true);
		contentPane.add(labelSatellites);
		
		JCheckBox satelliteLines = new JCheckBox("Satellite Lines");
		satelliteLines.setBounds(836, 291, 113, 25);
		satelliteLines.setSize(satelliteLines.getPreferredSize());
		satelliteLines.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (satelliteLines.isSelected()) {
					drawSatLines = true;
				} else {
					drawSatLines = false;
				}
			}
		});
		satelliteLines.setSelected(true);
		contentPane.add(satelliteLines);
		
//		// For reference, in case new thing doesn't work
//		JCheckBox satelliteLines = new JCheckBox("Satellite Lines");
//		satelliteLines.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				if (satelliteLines.isSelected()) {
//					drawSatLines = true;
//				} else {
//					drawSatLines = false;
//				}
//			}
//		});
//		satelliteLines.setSelected(true);
//		satelliteLines.setBounds(836, 291, 113, 25);
//		contentPane.add(satelliteLines);
		
		JLabel lblS1Period = new JLabel("Satellite 1 Period (hrs):");
		lblS1Period.setBounds(840, 13, 137, 25);
		contentPane.add(lblS1Period);
		
		s1Period = new JTextField();
		s1Period.setText("13.1902");
		s1Period.setBounds(977, 13, 116, 22);
		contentPane.add(s1Period);
		s1Period.setColumns(10);
		
		s1Radius = new JTextField();
		s1Radius.setText("39700000");
		s1Radius.setColumns(10);
		s1Radius.setBounds(977, 52, 116, 22);
		contentPane.add(s1Radius);
		
		s2Period = new JTextField();
		s2Period.setText("6.9512");
		s2Period.setColumns(10);
		s2Period.setBounds(977, 92, 116, 22);
		contentPane.add(s2Period);
		
		s2Radius = new JTextField();
		s2Radius.setText("25000000");
		s2Radius.setColumns(10);
		s2Radius.setBounds(977, 130, 116, 22);
		contentPane.add(s2Radius);
		
		dayLength = new JTextField();
		dayLength.setText("13");
		dayLength.setColumns(10);
		dayLength.setBounds(977, 168, 116, 22);
		contentPane.add(dayLength);
		
		JLabel lblS1Radius = new JLabel("Satellite 1 Radius (m):");
		lblS1Radius.setBounds(840, 51, 137, 25);
		contentPane.add(lblS1Radius);
		
		JLabel lblS2Period = new JLabel("Satellite 2 Period (hrs):");
		lblS2Period.setBounds(840, 87, 137, 25);
		contentPane.add(lblS2Period);
		
		JLabel lblS2Radius = new JLabel("Satellite 2 Radius (m):");
		lblS2Radius.setBounds(840, 127, 137, 25);
		contentPane.add(lblS2Radius);
		
		JLabel lblDayLength = new JLabel("Day length (hrs):");
		lblDayLength.setBounds(840, 165, 137, 25);
		contentPane.add(lblDayLength);
		
		JLabel lblTimeStep = new JLabel("Time step (sec):");
		lblTimeStep.setBounds(840, 203, 137, 25);
		contentPane.add(lblTimeStep);
		
		timeStep = new JTextField();
		timeStep.setText("60");
		timeStep.setColumns(10);
		timeStep.setBounds(977, 206, 116, 22);
		contentPane.add(timeStep);
		
		JCheckBox autoStopSim = new JCheckBox("Stop simulation @ day end");
		autoStopSim.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (autoStopSim.isSelected()) {
					stopAfterDay = true;
				} else {
					stopAfterDay = false;
				}
			}
		});
		autoStopSim.setSelected(true);
		autoStopSim.setBounds(836, 351, 215, 25);
		contentPane.add(autoStopSim);
	}

	public boolean isDrawStartingLine() {
		return drawStartingLine;
	}

	public boolean isDrawSatLines() {
		return drawSatLines;
	}

	public boolean isDrawSatLabels() {
		return drawSatLabels;
	}

	public boolean isStopAfterDay() {
		return stopAfterDay;
	}

}
