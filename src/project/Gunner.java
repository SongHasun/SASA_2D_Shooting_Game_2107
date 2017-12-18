package project;
import javax.swing.*;
//군사 클래스이다.
//체력, 이동, 무기 사용 등의 기능을 수행한다.

public class Gunner extends ToDrawObject {
    double v;//이동속도
    boolean dead=false;boolean gaveWeapon=false;//생사 여부, 무기를 내놓았는지 여부
    int deadCount=0;//죽은 후 시체가 사라지기까지의 시간, 3초가 지나기 전에는 시체를 그린다.
    int genN=6;//갖고 있는 수류탄의 개수
    Weapon weapon=new Weapon();//사용하고 있는 무기
    int health=100;//체력
    private ImageIcon deadImg=new ImageIcon("Resource/blood.png");
    Gunner(){//Enemy 클래스가 Gunner 클래스를 상속받기 때문에 기본생성자를 만들어두었다.
        this.w=this.h=20;
        this.x=this.y=30;
        this.weapon=new Weapon();
    }
    public void timePassed(int t){
        this.weapon.timePassed(t);//무기에 시간변화를 준다.
        if(dead){//죽었다면 카운트다운을 시작한다. 3초 동안 혈흔이 남는다.
            deadCount+=t;
        }
    }
    public void setWeapon(Weapon w){//무기를 설정한다.
        if(w==null) return;//받은 무기가 null이라면 아무것도 하지 않는다.
        this.v=w.moveVel;//무기에 맞는 이동속도로 변경된다.
        if((this.weapon.name).equals(w.name)){//만약 같은 종류의 무기를 습득했다면, 그 무기의 총알만 빼서 추가한다.
            this.weapon.totN+=(w.totN+w.n);
        }else{//새로운 무기라면, 그 무기로 변경한다.
            this.weapon=w;
        }
    }
    public void gotDamaged(int damage){
        this.health-=damage;//피해량을 받는다.
        if(!dead){//아직 안 죽었다면 체력을 확인한다.
            if(health<=0){//체력이 0 이하면 죽인다. 이미지를 시체로 바꾼다.
                dead=true;
                this.img=deadImg;
            }
        }
    }
    public void move(double dx, double dy){
        if(this.weapon.reloading||dead){//재장전 중이거나 죽었다면 이동할 수 없다.
            return;
        }else{
            this.x+=dx;
            this.y+=dy;
        }
    }
    public void useItem(Item it){//아이템을 사용한다.
        setWeapon(it.weapon);//무기를 습득한다. 체력회복 아이템이라면 it.weapon이 null이므로 여기서 아무 일도 일어나지 않는다.
        this.health+=it.healthRecover;//체력을 회복한다. 무기 아이템이라면 healthRecover가 0이다.
        if(this.health>100) this.health=100;//체력은 최대 100이다.
    }
    //목표 지점을 향해 수류탄을 던진다.
    public Generade throwGenerade(double xf,double yf){
        if(dead||genN<=0) return null;//죽었거나, 남은 수류탄이 없으면 수류탄을 던질 수 없다.
        Generade g=new Generade(5,this.x,this.y,xf,yf);
        genN--;
        return g;
    }
    //목표 지점을 향해 발포한다. type에는 플레이어가 쐈는지, 적군이 쐈는지가 들어간다.
    public Bullet fire(double xf,double yf,boolean type){
        double rd;//총알의 데미지이다.
        if(type)rd=this.weapon.damage;
        else rd=(int)(this.weapon.damage/1.75);//적이 쏘는 총알은 데미지가 더 약하다(밸런스 조정)
        if(this.weapon.fireAble()&&!dead){
            Bullet b=new Bullet(type,this.weapon.shootVel,rd,this.x,this.y,xf,yf);
            return b;
        }
        else return null;
    }
}
