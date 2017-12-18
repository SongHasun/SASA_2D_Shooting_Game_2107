package project;
import javax.swing.*;
import java.awt.*;

public class Generade extends Bullet {
    boolean arrive=false;//수류탄이 목적지에 도달했는지 여부가 저장된다.
    boolean startExplode=false;//수류탄의 폭파가 시작됐는지 저장된다.
    boolean endExplode=false;//수류탄의 폭파가 끝났는지 저장된다.
    int gTimer=0,gTime=2000;//수류탄이 목적지에 도착하면 3초 후에 터진다.
    public Generade(double v,double xi,double yi,double xf,double yf){//이동속도, 출발지, 목적지에 대한 정보를 받는다.
        this.w=this.h=20;
        this.v=v;this.damage=400;
        this.x=xi;this.y=yi;
        this.xf=xf;this.yf=yf;
        //v의 속도로 목적지를 향해 갈 때의 속도의 x성분, y성분을 계산한다.
        double dx=xf-xi;double dy=yf-yi;
        this.vx=v*dx/Math.sqrt((double)dx*dx+dy*dy);
        this.vy=v*dy/Math.sqrt((double)dx*dx+dy*dy);
        this.img = new ImageIcon("Resource/wwwwwwwwwwwwwwwgenerade.png");
    }
    public void move() {
        if(arrive)return;//목적지에 도달했다면 멈춘다.
        Rectangle area=new Rectangle((int)xf-10,(int)yf-10,20,20);//목적지를 중심으로 한 사각형 영역을 만든다.
        if(area.contains(x,y)) arrive=true;//이 영역 안에 들어오면 도착한 것으로 한다.
        x = x + vx;
        y = y + vy;
    }
    public void timePassed(int t){
        if(!arrive)return;
        gTimer+=t;//도착했다면 카운트를 시작한다.
        if(gTimer>=gTime){//3초가 지나면 폭파한다.
            startExplode=true;
            this.img=new ImageIcon("Resource/explode.png");//폭파이미지로 바꾼다.
            if(this.w>=100){//폭파가 진행되어 크기가 100이상이 되면 폭파가 멈춘다. 이 수류탄은 Main에서 제거된다.
                endExplode=true;
                return;
            }
            this.w+=10;//이미지의 크기를 키운다.
            this.h+=10;
        }
    }
}
