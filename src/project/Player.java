package project;

import javax.swing.*;

public class Player extends Gunner{
    Player(double x, double y) {
        this.w=this.h=20;
        this.x = x;this.y = y;
        this.img = new ImageIcon("Resource/soldier11.png");
    }
    public Bullet fire(double xf,double yf){
        return super.fire(xf,yf,true);
    }
}
