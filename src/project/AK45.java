package project;

import javax.swing.*;

public class AK45 extends Weapon {
    public AK45(){
        this.shootVel=5;
        this.damage=17;
        this.maxN=25;
        this.n=this.maxN;
        this.totN=this.maxN*2;
        this.fireTime=100;
        this.fireTimer=this.fireTime;
        this.moveVel=1;
        this.rebound=10;
        this.name="AK45";
        this.img=new ImageIcon("Resource/AK45.png");
    }
}
