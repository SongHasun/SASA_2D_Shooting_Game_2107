package project;

import javax.swing.*;

public class Enemy extends Gunner{
    public boolean alert=false;//플레이어 발견 여부. 플레이어를 발견한 뒤로는 플레이어를 향해 움직인다.
    public boolean canSee=false;//플레이어가 시야에 보이는지 여부. 플레이어가 시야에 보이면 발포한다.
    Enemy(double x, double y) {
        this.w=this.h=20;
        this.x = x;this.y = y;
        this.setWeapon(new Weapon());
        this.img= new ImageIcon("Resource/soldier22.png");
    }
    public Bullet fire(int x,int y){
        if(dead) return null;//죽은 적은 발포할 수 없다.
        if(weapon.n<=0&&weapon.totN>0) {
            //장전해둔 총알이 다 떨어졌고, 채울 총알이 있다면 장전한다.
            this.weapon.reload();
            return null;
        }else if(!canSee){
            //시야에 보이지 않으면 발포하지 않는다.
            return null;
        }else{
            return super.fire(x,y,false);
        }
    }
    public void move(double dx,double dy){
        if(alert)super.move(dx,dy);
    }
    public void ableToSee(boolean a){//플레이어를 발견했는지 여부를 입력받아, 행동에 변화를 준다.
        if(a){
            //처음 발견한 것이라면, 플레이어를 향해 움직이기 시작하며 발포한다.
            if(!alert)alert=true;
            canSee=true;
        }
        else{
            //시야에 보이지 않는다면 발포를 멈춘다.
            canSee=false;
        }
    }
}