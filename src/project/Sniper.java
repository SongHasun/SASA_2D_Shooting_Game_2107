package project;

import javax.swing.*;

public class Sniper extends Weapon {
    public Sniper(){
        this.shootVel=20;
        this.damage=100;
        this.maxN=5;
        this.n=this.maxN;
        this.totN=this.maxN*2;
        this.fireTime=1000;
        this.fireTimer=fireTime;
        this.moveVel=0.7;
        this.rebound=1;
        this.name="Sniper";
        this.img=new ImageIcon("Resource/sniper.png");
    }
}
