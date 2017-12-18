package project;

import javax.swing.*;

public class Pistol extends Weapon {
    public Pistol(){
        this.shootVel=5;
        this.damage=23;
        this.maxN=10;
        this.n=this.maxN;
        this.totN=this.maxN*2;
        this.fireTime=200;
        this.fireTimer=fireTime;
        this.moveVel=1.25;
        this.rebound=10;
        this.name="Pistol";
        this.img= new ImageIcon("Resource/pistol.png");
    }
}
