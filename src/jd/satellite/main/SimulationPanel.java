package jd.satellite.main;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class SimulationPanel  extends JPanel implements Runnable {
	App rootFrame;
	double time = 0; // Current time, in seconds, in the simulation

	// Defaults
	double dayLength = 13; // in hours
	double timeStep = 60;  // in seconds
	Satellite s1 = new Satellite(13.1902, 39700000); // Just default values
	Satellite s2 = new Satellite(6.9512, 25000000);
	Satellite new_s1 = new Satellite(13.1902, 39700000);
	Satellite new_s2 = new Satellite(6.9512, 25000000);
	int numPasses = 0;
	
	double scale = 0; // Used to determine where to draw the satellite paths
	DecimalFormat df, df1; // Bad naming, but who cares (described in
							// constructor)

	int sSize = 16; // Satellite size
	
	// Animation-handling stuff
	Thread animatorThread;
	boolean frozen = true;

	public SimulationPanel(App rootFrame) {
		// Get outer JFrame
		this.rootFrame = rootFrame;
		
		// Stuff for truncating numbers
		df = new DecimalFormat("####");	    // For velocities
		df1 = new DecimalFormat("##.####"); // For hours
	}

	public void paint(Graphics g) {
		// Clear old stuff
		g.clearRect(0, 0, getWidth(), getHeight());

		int xCenter = getHeight() / 2;
		int yCenter = getWidth() / 2;

		// Draw center planet (unnecessary, but whatever)
		g.fillOval(xCenter - 25, yCenter - 25, 50, 50);

		// Draw the satellite paths
		int rs1 = (int) (2 * scale * s1.getRadiusOfOrbit());
		int rs2 = (int) (2 * scale * s2.getRadiusOfOrbit());
		g.drawOval(xCenter - (rs1 / 2), yCenter - (rs1 / 2), rs1, rs1);
		g.drawOval(xCenter - (rs2 / 2), yCenter - (rs2 / 2), rs2, rs2);

		// Draw the satellites
		int x1 = (int) (scale * (s1.getRadiusOfOrbit()) * Math.cos(s1.getAngle()) + xCenter);
		int y1 = (int) (scale * (s1.getRadiusOfOrbit()) * Math.sin(s1.getAngle()) + yCenter);
		int x2 = (int) (scale * (s2.getRadiusOfOrbit()) * Math.cos(s2.getAngle()) + xCenter);
		int y2 = (int) (scale * (s2.getRadiusOfOrbit()) * Math.sin(s2.getAngle()) + yCenter);
		g.fillOval(x1 - sSize / 2, y1 - sSize / 2, sSize, sSize);
		g.fillOval(x2 - sSize / 2, y2 - sSize / 2, sSize, sSize);

		// Draw helping lines for satellites
		if (rootFrame.isDrawSatLines()) {
			g.drawLine(xCenter, yCenter, x1, y1);
			g.drawLine(xCenter, yCenter, x2, y2);
		}

		// Draw starting line
		if (rootFrame.isDrawStartingLine()) {
			g.drawLine(xCenter, yCenter, xCenter + 400, yCenter);
		}
		
		// Label satellites
		if (rootFrame.isDrawSatLabels()) {
			g.drawString("S1", x1 + 15, y1 + 10);
			g.drawString("S2", x2 + 15, y2 + 10);
		}

		// Display current time & other info
		g.drawString("Time (hours): " + df1.format(time / 3600), 1, 10);
		g.drawString("s1 angle in deg: " + Math.toDegrees(s1.getAngle()), 1, 25);
		g.drawString("s2 angle in deg: " + Math.toDegrees(s2.getAngle()), 1, 40);

		// Velocities
		g.drawString("V1: " + df.format(s1.getSpeed()) + " m/s", 1, 55);
		g.drawString("V2: " + df.format(s2.getSpeed()) + " m/s", 1, 70);
		
		// Number of passes
		g.drawString("# passes: " + numPasses, 1, 85);
	}

	public void startSimulation() {
		// Set time to zero and # passes to 0
		time = 0;
		numPasses = 0;
		
		// Reset simulation
		s1 = new Satellite(new_s1.getPeriod(), new_s1.getRadiusOfOrbit());
		s2 = new Satellite(new_s2.getPeriod(), new_s2.getRadiusOfOrbit());
		
		// Determine scale
		if (s1.getRadiusOfOrbit() > s2.getRadiusOfOrbit()) {
			scale = 300 / s1.getRadiusOfOrbit();
		} else {
			scale = 300 / s2.getRadiusOfOrbit();
		}
		
		// Actually start simulation
		if (frozen) {
          frozen = false;
          start();
        }
	}

	/** 
	 * Start the simulation
	 */
	private void start() {
		if (!frozen) {
			if (animatorThread == null) {
				animatorThread = new Thread(this);
			}
			animatorThread.start();
		}
	}
	
	/**
	 * Stop the simulation
	 */
	public void stop() {
	    animatorThread = null;
	    frozen = true;
	  }
	
	public void run() {
		Thread.currentThread().setPriority(Thread.MIN_PRIORITY);

		long startTime = System.currentTimeMillis();

		Thread currentThread = Thread.currentThread();

		while (currentThread == animatorThread) {
			updatePhysics();
			repaint();

			try {
				startTime += timeStep;
				Thread.sleep(10); // TODO - change delay maybe
			} catch (InterruptedException e) {
				break;
			}
		}
	}
	
	private void updatePhysics() {
		boolean dayStop = rootFrame.isStopAfterDay();
		if (!dayStop || (dayStop && time < (dayLength * 3600))) {
			// Update angle measures
			s1.updateAngle(timeStep);
			s2.updateAngle(timeStep);
			
			// Update time
			time += timeStep;
			
			// Check if passed ||  TODO - maybe do after repaint? not sure
			checkPassed();
		} 
	}
	
	/**
	 * Checks (experimentally, using angle values!) if the satellites have passed each other
	 * 
	 * Method: If the faster satellite's old angle was <= slower satellite's old angle,
	 * 			and the faster satellite's current angle is > slower satellite's angle,
	 * 			the faster satellite passed the slower one!
	 */
	private void checkPassed() {
		if (s1.getPeriod() < s2.getPeriod()) {
			if (s1.getOldAngleDeg() <= s2.getOldAngleDeg() && s1.getAngleDeg() > s2.getAngleDeg()) {
				// They passed!
				numPasses += 1;
			}
		} else {
			if (s2.getOldAngleDeg() <= s1.getOldAngleDeg() && s2.getAngleDeg() > s1.getAngleDeg()) {
				// They passed!
				numPasses += 1;
			}
		}
	}

	/**
	 * Update simulation
	 * @param t1 period of satellite 1 (hrs)
	 * @param r1 radius of satellite 1 (m)
	 * @param t2 period of satellite 2 (hrs)
	 * @param r2 radius of satellite 2 (m)
	 * @param dL day length (hrs)
	 * @param tS time step (sec)
	 */
	public void updateValues(double t1, double r1, double t2, double r2, double dL, double tS) {
		new_s1 = new Satellite(t1, r1);
		new_s2 = new Satellite(t2, r2);
		dayLength = dL;
		timeStep = tS;
	}
}
