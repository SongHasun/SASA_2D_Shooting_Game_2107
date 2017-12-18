package project;

import javax.swing.*;

public class MachineGun extends Weapon {
    public MachineGun(){
        this.shootVel=5;
        this.damage=6;
        this.maxN=120;
        this.n=this.maxN;
        this.totN=this.maxN*2;
        this.fireTime=20;
        this.fireTimer=fireTime;
        this.moveVel=0.75;
        this.rebound=15;
        this.name="MachineGun";
        this.img=new ImageIcon("Resource/machineGun.png");
    }
}
