import java.awt.Color; 
 
import ihs.apcs.spacebattle.*; 
import ihs.apcs.spacebattle.commands.*; 
 
public class ExampleShip extends BasicSpaceship { 
    private int worldWidth; 
    private int worldHeight; 
    private Point midpoint; 
     
    public static void main(String[] args) 
    { 
        TextClient.run("10.56.98.121", new ExampleShip()); 
    } 
 
    @Override 
    public RegistrationData registerShip(int numImages, int worldWidth, int worldHeight) 
    { 
        this.worldWidth = worldWidth/2; 
        this.worldHeight = worldHeight/2; 
        this.midpoint = new Point(this.worldWidth, this.worldHeight); 
        return new RegistrationData("Example Ship", new Color(255, 255, 255), 0); 
    } 
     
    public boolean isPointingAtMiddle(BasicEnvironment env) { 
      return Math.abs(getAngleToMidpoint(env)) <= 2; 
    } 
     
    public int getAngleToMidpoint(BasicEnvironment env) { 
      ObjectStatus ship = env.getShipStatus(); 
      Point currentPos = ship.getPosition(); 
      int angle = currentPos.getAngleTo(this.midpoint) - ship.getOrientation(); 
      while (angle > 180) { 
         angle -= 360; 
      } 
      while (angle < -180) { 
         angle += 360; 
      } 
      return angle; 
    } 
     
    @Override 
    public ShipCommand getNextCommand(BasicEnvironment env) 
    { 
      ObjectStatus ship = env.getShipStatus(); 
      Point currentPos = ship.getPosition(); 
      double speed = ship.getSpeed();
      
      RadarResults results = env.getRadar(); 
      if (results != null) {   
         for (ObjectStatus detectedObject : results) { 
            if (detectedObject.getType().equals("ship")) { 
               int angleToEnemy = currentPos.getAngleTo(detectedObject.getPosition()) - ship.getOrientation(); 
               if (Math.abs(angleToEnemy) < 40) { 
                  return new FireTorpedoCommand('F'); 
               } else { 
                  return new RotateCommand(angleToEnemy); 
               } 
            } 
         }
      } 

      if (currentPos.getDistanceTo(this.midpoint) > 100) { 
         if (!isPointingAtMiddle(env)) { 
            return new RotateCommand(getAngleToMidpoint(env)); 
         } 
         return new ThrustCommand('B', 0.5, 0.4); 
      } 
      if (speed > 0.5) { 
         return new BrakeCommand(0.5); 
      } 
      return new RadarCommand(10);
       
      
             
    } 
     
} 
 

 