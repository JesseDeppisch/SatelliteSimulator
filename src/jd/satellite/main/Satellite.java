package jd.satellite.main;


public class Satellite {
	
	private double period;
	private double radiusOfOrbit;
	
	private double angle = 0; // Angle between starting point and current point, clockwise (in RADIANS)
	private double oldAngle = 0; // time-1 angle;
	
	private double speed; // m/s, positive
	
	public Satellite(double period, double radiusOfOrbit) {
		this.period = period;
		this.radiusOfOrbit = radiusOfOrbit;
		
		this.speed = (2*Math.PI*radiusOfOrbit) / (period*3600); // speed in m/s
	}

	public double getAngle() {
		return angle;
	}

	public double getAngleDeg() {
		return Math.toDegrees(angle);
	}
	
	public void setAngle(double angle) {
		this.angle = angle;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}
	
	public double getPeriod() {
		return period;
	}
	public void setPeriod(double period) {
		this.period = period;
	}
	public double getRadiusOfOrbit() {
		return radiusOfOrbit;
	}
	
	public void setRadiusOfOrbit(double radiusOfOrbit) {
		this.radiusOfOrbit = radiusOfOrbit;
	}

	public void updateAngle(double timeIncrement) {
		// Determine angle of change (in degrees)
		double changeAngle = (timeIncrement * speed) / radiusOfOrbit;
		// convert from radians to degrees
		changeAngle = Math.toDegrees(changeAngle);
		
		// Get current angle, in degrees
		double currentAngle = Math.toDegrees(angle);
		
		// Add together, but make sure to wrap around if needed, so that angle isn't weird
		// Note: not sure if this is needed, but it's 2 AM
		double newAngle = currentAngle + changeAngle;
		if (newAngle >= 360) {
			newAngle -= 360;
		}
		
		// Set old angle to be the old angle
		oldAngle = angle;
		
		// Convert back to radians
		angle = Math.toRadians(newAngle);
	}

	public double getOldAngleDeg() {
		return Math.toDegrees(oldAngle);
	}

}

