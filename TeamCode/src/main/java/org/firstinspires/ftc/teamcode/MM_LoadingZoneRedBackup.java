package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.util.concurrent.TimeUnit;

@Disabled
@Autonomous(group =  "Red", name = "Loading Zone Red Backup")
public class MM_LoadingZoneRedBackup extends LinearOpMode {

    private Robot robot = new Robot();
    enum Skystone {LEFT, CENTER, RIGHT}
    private Skystone skystonePos = Skystone.LEFT;
    private enum ParkingPosition {FAR, CLOSE}// far or close to center
    private ParkingPosition parkingPosition = ParkingPosition.CLOSE;
    private double distanceToBuildZone; // distance to bridge tape from close edge of block
    private double distanceToFoundation = 33; // distance to skybridge from bridge tape
    private double speed = 0.55;
    private int stepNumber;

    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(this);
        robot.releaseBlock(this);
        // timer
        ElapsedTime timer = new ElapsedTime();
        // Detect skystone with camera
        int position;
        timer.reset();
        while (true) {
            position = robot.detectSkystone(this);
            if (timer.time(TimeUnit.SECONDS) > 3) {
                skystonePos = Skystone.CENTER;
                break;
            } else if (position == -1) {
                skystonePos = Skystone.LEFT;
                break;
            } else if (position == 0) {
                skystonePos = Skystone.CENTER;
                break;
            } else if (position == 1) {
                skystonePos = Skystone.RIGHT;
                break;
            }
            idle();
        }

        telemetry.addData("Distance", robot.frontDistance.getDistance(DistanceUnit.INCH));
        telemetry.update();

        // wait for start
        waitForStart();

        this.stepNumber = 1;
        try {
            while (opModeIsActive()) {
                this.linearOpmodeSteps();
                idle();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            robot.stopEverything();
        }
/*
        // put arm down
        robot.bringArmDown(this);
        robot.rotateGripper(1);

        // Drive to quarry
        robot.driveForwardDistance(17, speed, this);
        Thread.sleep(500);
        switch (skystonePos) {
            case LEFT:
                distanceToBuildZone = 48;
                // strafe to block
                robot.strafeTime(-speed, 1500);
                // correct for the strafe
                robot.turnRight(-0.25, 250);
                break;
            case CENTER:
                distanceToBuildZone = 36;
                robot.strafeTime(-speed, 250);
                break;
            case RIGHT:
                distanceToBuildZone = 24;
                // strafe to block
                robot.strafeTime(speed, 100);
                break;

        }

        Thread.sleep(500);
        // grab block
        robot.rotateGripper(0.5);
        Thread.sleep(500);
        robot.gripBlock();
        Thread.sleep(500);
        robot.rotateGripper(1);

        // back up
        robot.driveForwardDistance(8, -speed, this);
        // turn towards skybridge
        robot.turnRight(speed, 1075);
        // drive to skybridge
        robot.driveForwardDistance(distanceToFoundation + distanceToBuildZone, speed, this);

        Thread.sleep(500);
        // drop block
        robot.rotateGripper(0.5);
        Thread.sleep(500);
        robot.releaseBlock(this);
        Thread.sleep(500);
        robot.rotateGripper(1.0);

        Thread.sleep(500);
        // drive to second Skystone
        robot.driveForwardDistance(distanceToBuildZone + distanceToFoundation + 24, -speed, this);
        // turn
        robot.turnRight(-speed, 1075);

        // go to block
        Thread.sleep(500);
        robot.rotateGripper(1);
        robot.driveForwardDistance(10, speed, this);

        // grab block
        Thread.sleep(500);
        robot.rotateGripper(0.5);
        Thread.sleep(500);
        robot.gripBlock();
        Thread.sleep(500);
        robot.rotateGripper(1);

        // drive to foundation to drop the block off
        Thread.sleep(500);
        robot.driveForwardDistance(15, -speed, this);
        robot.turnRight(speed, 1075);
        robot.driveForwardDistance(distanceToBuildZone + distanceToFoundation + 24, speed, this);
        robot.releaseBlock(this);

        // park
        Thread.sleep(500);
        robot.driveForwardDistance(30, -speed, this);
        if (parkingPosition == parkingPosition.CLOSE) {
            robot.strafeTime(-speed, 2800);
        }


*/
    }

    private void linearOpmodeSteps() throws InterruptedException {
        switch (stepNumber) {
            case 1:
                // put arm down
                robot.bringArmDown(this);
                robot.rotateGripper(0.8);
                Thread.sleep(250);
                this.stepNumber++;
                break;
            case 2:
                // Drive to quarry
                robot.driveForwardDistance(17, 0.4, this);
                /*robot.setDrivePower(speed);
                double distanceToBlock = robot.frontDistance.getDistance(DistanceUnit.INCH);
                while (distanceToBlock > 11.5) {
                    telemetry.addData("Distance", robot.frontDistance.getDistance(DistanceUnit.INCH));
                    telemetry.update();
                    distanceToBlock = robot.frontDistance.getDistance(DistanceUnit.INCH);
                }
                robot.stopDrive();
                 */
                Thread.sleep(500);
                switch (skystonePos) {
                    case LEFT:
                        distanceToBuildZone = 36;
                        // strafe to block
                        robot.strafeTime(-speed, 1500);
                        // correct for the strafe
                        //robot.turnRight(-0.25, 250);
                        break;
                    case CENTER:
                        distanceToBuildZone = 30;
                        robot.strafeTime(-speed, 250);
                        break;
                    case RIGHT:
                        distanceToBuildZone = 24;
                        // strafe to block
                        robot.strafeTime(speed, 1250);
                        // correct for the strafe
                        //robot.turnRight(-0.25, 250);
                        break;

                }
                this.stepNumber++;
                break;
            case 3:
                Thread.sleep(500);
                robot.grabBlockAuto();
                this.stepNumber++;
                break;
            case 4:
                // back up
                robot.driveForwardDistance(8, -speed, this);
                // turn towards skybridge
                robot.turnWithImu(0.3, -90, this);
                // drive to foundation
                robot.driveForwardDistance(distanceToFoundation + distanceToBuildZone, speed, this);
                this.stepNumber++;
                break;
            case 5:
                Thread.sleep(500);
                // drop block
                robot.rotateGripper(0.5);
                Thread.sleep(500);
                robot.releaseBlock(this);
                Thread.sleep(500);
                robot.rotateGripper(1.0);
                this.stepNumber++;
                break;
            case 6:
                Thread.sleep(500);
                // correct position
                robot.turnToGlobalPosition(0.25, -90, this);
                // drive to second Skystone
                robot.driveForwardDistance(distanceToBuildZone + distanceToFoundation + 24, -speed, this);
                // turn
                robot.turnToGlobalPosition(0.3, 0, this);
                this.stepNumber++;
                break;
            case 7:
                // go to block
                Thread.sleep(500);
                robot.driveForwardDistance(10, 0.4, this);
                this.stepNumber++;
                break;
            case 8:
                // grab block
                Thread.sleep(500);
                robot.grabBlockAuto();
                this.stepNumber++;
                break;
            case 9:
                // drive to foundation to drop the block off
                Thread.sleep(500);
                robot.driveForwardDistance(15, -speed, this);
                robot.turnWithImu(0.3, -90, this);
                robot.driveForwardDistance(distanceToBuildZone + distanceToFoundation, speed, this);
                robot.releaseBlock(this);
                this.stepNumber++;
                break;
            case 10:
                // park
                Thread.sleep(500);
                robot.driveForwardDistance(6, -speed, this);
                switch(parkingPosition) {
                    case FAR:
                        robot.strafeTime(speed, 2800);
                        break;
                    case CLOSE:
                        robot.strafeTime(-speed, 1250);
                        break;
                }
                this.stepNumber++;
                break;
            case 11: // everything is stopped
                onRobotStopOrInterrupt();
                break;
        }
    }

    void onRobotStopOrInterrupt() {
        robot.stopEverything();
        telemetry.addData("Opmode", "Stopped or Interrupted");
        telemetry.update();
    }

}