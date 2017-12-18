package project;
//총알 클래스
//총알의 종류(플레이어가 쏜 총알인지, 적군이 쏜 총알인지), 피해량, 이동속도, 현재 위치, 처음 위치 및 목적 위치에 대한 정보를 담고 있다.
public class Bullet extends ToDrawObject {
    double xf,yf;
    double v,vx=5,vy=5;
    double damage=10;
    boolean type=false;
    //Generade 클래스가 Bullet 클래스를 상속받기 때문에 기본생성자를 만들어두었다
    public Bullet(){
    }
    //총알의 특성을 입력받아 새로운 총알 객체를 만든다. Gunner 클래스의 fire 메소드에 의해 실행된다.
    public Bullet(boolean type, double v,double damage,double xi,double yi,double xf,double yf){
        this.type=type;
        this.w=this.h=10;
        this.v=v;this.damage=damage;
        this.x=xi;this.y=yi;
        this.xf=xf;this.yf=yf;
        double dx=xf-xi;double dy=yf-yi;
        this.vx=v*dx/Math.sqrt((double)dx*dx+dy*dy);
        this.vy=v*dy/Math.sqrt((double)dx*dx+dy*dy);
    }
    public void move() {
        x = x + vx;
        y = y + vy;
    }
}
