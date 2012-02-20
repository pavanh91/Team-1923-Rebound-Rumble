package org.usfirst.frc1923;

public class ShooterSteeringThread extends Thread {
	private Shooter shooter;
	private CameraDataPacket cdp;
	private boolean needsUpdate;
	private boolean isRunning;
	private boolean justRan;

	public ShooterSteeringThread(Shooter shooter) {
		this.shooter = shooter;
		this.cdp = null;
		this.needsUpdate = true;
		justRan = false;
	}

	public ShooterSteeringThread(Shooter shooter, CameraDataPacket cdp) {
		this.shooter = shooter;
		this.cdp = cdp;
		this.needsUpdate = false;
		justRan = false;
	}

	public boolean needsUpdate() {
		return needsUpdate;
	}

	public boolean isRunning() {
		return isRunning;
	}

	public boolean justRan() {
		return justRan;
	}

	public boolean isCentered() {
		return (!(this.cdp.getX() > 326) && !(this.cdp.getX() < 316));
	}

	public void update(CameraDataPacket cdp) {
		if (this.cdp != cdp) {
			this.cdp = cdp;
			needsUpdate = false;
		} else {
			needsUpdate = true;
		}
	}

	public void run() {
		while ((!this.isCentered())) {
			if (!this.needsUpdate()) {
				if (this.cdp.getX() > 326) {
					shooter.adjustRotation(-0.15);
					needsUpdate = true;
				} else if (this.cdp.getX() < 316) {
					shooter.adjustRotation(0.15);
					needsUpdate = true;
				} else {
					break;
				}
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
				}
				shooter.adjustRotation(0);
				Output.say("[SST] " + isRunning);
			}
		}
		if (this.isCentered()) {
			cdp = null;
			needsUpdate = true;
			isRunning = false;
			justRan = true;
		}
		Output.say("[SST] " + isRunning);
	}

	public CameraDataPacket getDataPacket() {
		return cdp;
	}

	public void start() {
		isRunning = true;
		super.start();
	}
}