package project;

import javax.swing.*;

//아이템 클래스
//무기 아이템과 체력 회복 아이템 두 가지가 있다.
//종류가 적고 비교적 간단한 코드라 상속을 활용하는 것보다는
//생성자를 이용해서 두 가지를 다르게 만들어주는 것이 더 좋을 것 같다.
public class Item extends ToDrawObject{
    int healthRecover=0;
    Weapon weapon=null;
    public Item(Enemy e){//무기 아이템을 만드는 생성자, 죽은 적의 무기를 입력받는다.
        this.w=40;
        this.h=20;
        this.x=(int)e.x+12;this.y=(int)e.y+12;
        this.weapon=e.weapon;
        this.weapon.reloading=false;//적이 총을 장전하다가 죽은 경우, 그 총의 장전을 취소한다.
        this.weapon.reloadTimer=0;
        this.healthRecover=0;
        this.img=this.weapon.img;
    }
    public Item(int x,int y,int health){//체력회복 아이템을 만드는 생성자, 회복량을 입력받는다.
        this.w=this.h=20;
        this.x=x;this.y=y;
        this.healthRecover=health;
        weapon=null;
        this.img=new ImageIcon("Resource/medicine.png");
    }
}
