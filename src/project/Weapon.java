package project;
import javax.swing.*;

public class Weapon implements Cloneable{//Stage에서 설정한 무기를 복제하여, 각 적들에게 나누어주기 위해 Cloneable을 구현했다.
    double shootVel;int damage;//총알의 속도, 피해량
    int n,maxN,totN;//현재 총알 수, 한 번에 장전할 수 있는 최대 총알 수, 현재 보유중인 여분의 총알 수
    int fireTime, fireTimer;//다음 발포를 위해 기다려야 하는 시간, 가장 최근의 발포 후 지난 시간
    int reloadTime=2000, reloadTimer=0;//장전을 위해 기다려야 하는 시간, 가장 최근의 장전 후 지난 시간
    double moveVel;//이 무기를 사용하는 군사의 이동속도
    boolean reloading=false;//재장전 중인지 여부
    int rebound;//총의 반동으로 인해 조준이 흔들리는 정도
    ImageIcon img;//이 무기가 그려질 이미지
    String name;//무기의 이름, 무기를 습득할 때 같은 종류의 무기인지 파악하기 위해 지정
    Weapon(){//Weapon을 상속받은 클래스가 있어서 기본 생성자를 만들어두었다.
        //nullPointer 에러를 막기 위해 이름과 이미지에 아무 것이나 넣어둔다.
        this.name="default";
        this.img=new ImageIcon("Resource/AK45.png");
    }
    public boolean fireAble(){//발포 가능한 상황인지 판단한다.
        if(fireTimer>=fireTime&&n>0&&!reloading){
            fireTimer=0;
            n--;
            return true;
        }
        return false;
    }
    public void reload(){//재장전한다. 여분의 총알을 이용하여 장전된 총알을 채운다.
        if(reloading)return;
        reloadTimer=0;
        reloading=true;
    }
    public void timePassed(int t){//발포와 발포 사이의 시간 간격, 재장전까지 남은 시간 등을 처리한다.
        fireTimer+=t;//마지막 발포 후 지난 시간을 체크한다.
        if(reloading){//재장전중이라면, 재장전을 시작한지 몇 초가 지났는지 확인한다.
            reloadTimer+=t;
            if(reloadTimer>=reloadTime){//2초가 지나면 재장전이 끝난다.
                reloading=false;
                reloadTimer=0;
                if(totN>=maxN-n){
                    totN-=(maxN-n);
                    n=maxN;
                }
                else{
                    n+=totN;
                    totN=0;
                }
            }
        }
    }
    protected Object clone() throws CloneNotSupportedException {//Stage 클래스의 Weapon들을 복제해서 각 Enemy 객체에 주어야 한다.
        return super.clone();
    }
}
