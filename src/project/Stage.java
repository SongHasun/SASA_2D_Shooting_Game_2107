package project;

import java.awt.*;

//특정 스테이지에 대한 정보를 담는다.
//이 정보를 바탕으로 실제 스테이지를 구성하는 메소드는 Main에 있다.
public class Stage {
    Rectangle goal;
    Weapon weaponOfPlayer;
    Weapon weaponOfEnemy;
    int playerX,playerY;
    int[] wallX;
    int[] wallY;
    int[] wallW;
    int[] wallH;
    int[] enemyX;
    int[] enemyY;
    int[] healthItemX;
    int[] healthItemY;
    public Stage(){}
    //레벨에 따른 스테이지 구성, 생성자를 이용했다.
    public Stage(int level){
        if(level==1){
            goal = new Rectangle(0, 0, 150, 150);
            //플레이어 무기 및 위치 설정
            weaponOfPlayer=new Pistol();
            weaponOfEnemy=new AK45();
            playerX = 500;
            playerY = 980;
            wallX = new int[]{70, 80, 220, 330, 200, 400, 500, 300, 170};
            wallY = new int[]{120, 548, 361, 427, 800, 900, 700, 800, 200};
            wallW = new int[]{50, 68, 120, 54, 37, 96, 45, 76, 401};
            wallH = new int[]{48, 13, 150, 248, 125, 43, 65, 285, 32};
            enemyX = new int[]{30, 50, 150, 250, 200, 190, 210};
            enemyY = new int[]{150, 50, 400, 700, 250, 300, 310};
            healthItemX = new int[]{100};
            healthItemY= new int[]{240};
        }
        else if(level==2){
            goal = new Rectangle(0, 0, 100, 100);
            weaponOfPlayer=new Sniper();
            weaponOfEnemy=new MachineGun();
            playerX = 30;
            playerY = 980;
            wallX=new int[] {150, 100, 50, 200, 100, 400, 200, 100, 350, 100, 180, 300};
            wallY=new int[] {50, 100, 150, 200, 300, 400, 550, 600, 650, 800, 800, 910};
            wallW = new int[]{65, 65, 65, 200, 100, 100, 200, 50, 50, 55, 135, 30};
            wallH = new int[]{50, 50, 50, 50, 150, 50, 50, 150, 200, 50, 50, 120};
            enemyX=new int[]{100, 30, 80, 150, 370, 460, 30, 330, 180, 180, 230, 450, 180, 230, 420, 430, 430};
            enemyY=new int[]{80, 120, 120, 180, 180, 380, 430, 480, 530, 680, 680, 680, 730, 730, 730, 830, 925, 975};
            healthItemX = new int[] {130, 270, 215};
            healthItemY = new int[] {892, 452, 123};
        }
        else if(level==3){
            goal = new Rectangle(230, 0, 90, 90);
            weaponOfPlayer=new MachineGun();
            weaponOfEnemy=new Pistol();
            playerX = 30;
            playerY = 980;
            wallX=new int[] {180,320,260,100};
            wallY=new int[] {0,0,430,700};
            wallW = new int[]{50, 50,30,20};
            wallH = new int[]{400,400,30,20};
            enemyX=new int[]{240,250,250,268,298,300,260,260,270,280,271,257,};
            enemyY=new int[]{20,89,300,351,250,198,32,154,320,350,270,240};
            healthItemX = new int[] {130, 350, 515};
            healthItemY = new int[] {792, 452, 123};
        }else if (level==4){
            goal = new Rectangle(0, 0, 150, 150);
            weaponOfPlayer=new AK45();
            weaponOfEnemy=new Sniper();
            playerX = 500;
            playerY = 980;
            wallX = new int[]{70, 80, 220, 330, 200, 400, 500, 300, 170};
            wallY = new int[]{120, 548, 361, 427, 800, 900, 700, 800, 200};
            wallW = new int[]{50, 68, 120, 54, 37, 96, 45, 76, 401};
            wallH = new int[]{48, 13, 150, 248, 125, 43, 65, 285, 32};
            enemyX = new int[]{30, 50, 150, 50, 200, 190, 210,585};
            enemyY = new int[]{150, 50, 400, 300, 250, 300, 310,200};
            healthItemX = new int[]{100};
            healthItemY= new int[]{240};
        }//이런 식으로 스테이지를 계속 구성해나간다.
    }
}
