package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.awt.geom.Line2D;

/*도움을 주고받은 친구*/
//김지훈에게 도움을 줌. 배열과 for문을 이용해서 코드 중복 해결.
//권순현에게 도움을 받음. 자바 프로그램에 이미지 파일 저장 및 불러와서 쓰기.

/*목차*/
//시작 Main 구현, 이니셜라이저, run 메소드
//150 메소드 구현; 이동, 발포, 공격 처리 등
//400 그림 그리기 구현
//500 리스너 구현

//전체적인 골격은 수업시간에 받은 'Shooting Game' 코드 참고
//이 코드를 골격삼아 원하는 대로 코드를 수정해서 개발함.
public class Main extends JFrame implements Runnable, KeyListener, MouseMotionListener, MouseListener {
    private BufferedImage bi = null;            //화면을 그려놓을 버퍼공간
    private Player player = null;
    private ArrayList<Bullet> bulletList = null;
    private ArrayList<Generade> generadeList = null;
    private ArrayList<Enemy> enemyList = null;
    private ArrayList<Rectangle> wallList = null;
    private ArrayList<Item> itemList = null;
    private Rectangle goal = null;
    private int level;
    private int timer = 10;
    private boolean left = false, right = false, up = false, down = false, gThrow = false;//키 입력을 받기 위한 변수들
    private boolean fire = false, pressedKnife = false, drawKnife = false;
    private double tx = 450, ty = 50;//현재 마우스가 가리키고 있는 위치 저장
    private boolean start = false, end = false,clear=false;//게임 시작, 실패, 클리어 여부를 넣는 변수
    private int w = 600, h = 1000; //게임 화면의 폭과 높이
    private ImageIcon startImage, deco, wallIcon, grassField ; //리소스 이미지를 담을 공간
    private Stage currentStage;
    private int maxLevel=3;
    //생성자로 초기화
    private Main() {
        start = false;end = false;
        player = new Player(100, 100); //플레이어 객체
        bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);  // 이미지를 그려놓을 버퍼공간
        bulletList = new ArrayList<>();
        generadeList = new ArrayList<>();
        enemyList = new ArrayList<>();
        wallList = new ArrayList<>();
        itemList = new ArrayList<>();
        this.level = 1;
        this.addKeyListener(this);      //Key 입력을 받기 위한 것
        this.addMouseListener(this);    //마우스 클릭, 위치를 받기 위한 것
        this.addMouseMotionListener(this);
        this.setSize(w, h);             //윈도우의 크기를 저장
        this.setTitle("War Survival Game"); //타이틀 지정
        this.setResizable(false);       //윈도우 크기조정(마우스 등)을 금지
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);          //윈도우 보여주기
        //리소스 불러오기
        startImage = new ImageIcon("Resource/startImage.jpg");
        deco = new ImageIcon("Resource/aloneSoldier.jpg");
        grassField = new ImageIcon("Resource/grassField.jpg");
        wallIcon = new ImageIcon("Resource/wall.jpg");
    }

    //main 스레드
    public static void main(String[] args) {
        Thread t = new Thread(new Main());
        t.start();
    }

    private void init() {//이전 스테이지의 정보를 초기화한다.
        currentStage=new Stage(1);//스테이지를 1로 돌린다. nullPointerException을 막기 위한 것이다. init을 실행한 후 맞는 level로 다시 StageSetter가 이루어진다.
        player.health = 100;//플레이어의 체력을 회복시키고, 살아있는 상태로 되돌린다. 무기도 원래대로 되돌린다.
        player.dead = false;
        player.img=new ImageIcon("C:\\Users\\송하선\\Desktop\\java_resource\\soldier11.png");
        player.genN = 6;
        player.weapon.n=player.weapon.maxN;
        player.weapon.totN=player.weapon.maxN*2;
        //이전 스테이지의 적, 총알을 모두 제거한다.
        enemyList.clear();
        bulletList.clear();
        generadeList.clear();
        wallList.clear();
        itemList.clear();
    }

    private void stageSetter() {
        init();//먼저 초기화를 한다.
        currentStage=new Stage(level);//level에 해당하는 Stage 객체를 생성한다.
        goal=currentStage.goal;//골인 지점을 설정한다.
        player.setWeapon(currentStage.weaponOfPlayer);//플레이어의 무기, 처음 위치를 지정한다.
        player.x=currentStage.playerX;
        player.y=currentStage.playerY;
        for(int i=0;i<currentStage.wallX.length;i++){//벽을 배치한다.
            wallList.add(new Rectangle(currentStage.wallX[i],currentStage.wallY[i],currentStage.wallW[i],currentStage.wallH[i]));
        }
        for(int i=0;i<currentStage.enemyX.length;i++){//적을 배치한다.
            Enemy en=new Enemy(currentStage.enemyX[i],currentStage.enemyY[i]);
            try{//모든 적이 하나의 무기 객체를 쓰는 것이 아니라, 각각 하나의 무기 객체를 가져야 하므로 Clone을 사용했다.
                en.setWeapon((Weapon)currentStage.weaponOfEnemy.clone());
            }catch(CloneNotSupportedException e){
                System.out.println("Clone Not Supported Exception During Creating New Enemy");
            }
            enemyList.add(en);
        }
        for (int i = 0; i < currentStage.healthItemX.length; i++) {//아이템을 배치한다.
            itemList.add(new Item(currentStage.healthItemX[i], currentStage.healthItemY[i], 50));
        }
    }

    //run 메소드. 실제 프로그램을 실행시킨다.
    public void run() {
        try {
            if (!start && !end) drawStart();//시작하기 전에는 시작화면을 띄운다.
            int gCnt = 2000;//수류탄을 2초마다 던질 수 있도록 하기 위한 변수이다.
            int kCnt = 2000;//칼을 2초마다 휘두룰 수 있도록 하기 위한 변수이다.
            for(;;){
                Thread.sleep(timer); //timer 간격으로 작업을 수행
                if (start) {
                    //게임화면 그리기
                    if (!end) {
                        drawGame();
                        //drawDebug();
                    }
                    if (gCnt > 2000) {//2초가 지나고, 플레이어가 장전 중이 아니며, 우클릭을 했다면 던지는 것을 시도한다.
                        if (gThrow && !player.weapon.reloading) {
                            Generade g = player.throwGenerade(tx, ty);
                            if (g != null) generadeList.add(g);
                            gCnt = 0;
                        }
                    }
                    if (kCnt > 1000) {//1초가 지나고, 플레이어가 F를 눌렀으면 칼을 휘두를 수 있다.
                        if (pressedKnife) {
                            useKnife();
                            kCnt = 0;
                        }
                    }
                    if (kCnt > 500) {//칼은 0.5초 동안 그려지고 사라진다.
                        drawKnife = false;
                    }
                    goalInChk();
                    moveObject();
                    checkIfAbleToSee();
                    getItem();
                    gunnerFire();
                    attackChk();
                    removeChk();
                    timePassed();
                    gCnt += timer;
                    kCnt += timer;
                }
                if (!start && end){
                    if(clear) drawClear();//게임 클리어 화면을 띄운다.
                    else drawEnd();//게임 오버 화면을 띄운다.
                }
            }
        } catch (Exception e) {
            e.printStackTrace();        //sleep을 사용하려면 try-cath문을 사용해야 한다.
        }
    }
    ///* 메소드 구현 *///

    /*스테이지 클리어 확인*/
    //플레이어가 골인 지점에 도달했는지 확인한다.
    private void goalInChk() {
        if (goal.contains(player.x, player.y)) {
            level++;//다음 레벨로 넘어간다.
            if(level>maxLevel){//만약 최고 레벨을 클리어했다면 클리어화면으로 넘어간다.
                clear=true;
                end=true;
                start=false;
                level=1;
            }
            else stageSetter();//아직 스테이지가 남았다면 다음 스테이지를 준비한다.
        }
    }

    /*플레이어와 적의 무기, 수류탄에 시간 흐름 주기*/
    //다음 발포까지의 시간, 장전 완료까지 남은 시간, 폭파까지의 카운트다운을 처리하기 위해 timer마다 시간을 흘려보낸다.
    private void timePassed() {
        player.timePassed(timer);
        for (Enemy e : enemyList) { //
            e.timePassed(timer);
        }
        for (Generade g : generadeList) {
            g.timePassed(timer);
        }
    }

    /*공격*/
    //군사들 발포
    private void gunnerFire() {
        if (fire) {  //좌클릭을 하고 있다면
            Bullet b = player.fire(tx+bound(player), ty+bound(player));
            if (b != null) bulletList.add(b);//player.fire의 결과로 null을 받는다면, 즉 발포가 불가능한 상황이라면 아무 일도 일어나지 않는다.
        }
        for (Enemy e : enemyList) {
            Bullet b = e.fire((int)player.x+bound(e),(int)player.y+bound(e));
            if (b != null) bulletList.add(b);//플레이어의 발포와 같다.
        }
    }
    //총의 반동으로 인해 조준이 흔들리는 효과
    private int bound(Gunner g){
        return (int)(Math.random()*g.weapon.rebound*2+g.weapon.rebound);
    }
    //플레이어 칼 휘두르기
    private void useKnife() {
        drawKnife = true;//칼을 그려야 한다.
        for (Enemy e : enemyList) {//모든 적에 대해서 칼에 맞았는지 확인한다.
            if (dis(player.x, player.y, e.x, e.y) < 25) {
                e.gotDamaged(70);
            }
        }
    }

    /*공격 처리*/
    //군사가 총을 맞았는지 확인하는 메소드이다.
    private boolean isGunnerGotShooted(Gunner obj, Bullet b) {//총알의 위치와 크기에 해당하는 Polygon을 만들어 군사 객체와 교차하는지 확인한다.
        int[] xpoints = {(int) b.x, (int) (b.x + b.w), (int) (b.x + b.w), (int) b.x};
        int[] ypoints = {(int) b.y, (int) b.y, (int) (b.y + b.h), (int) (b.y + b.h)};
        Polygon p = new Polygon(xpoints, ypoints, 4);
        return (p.intersects(obj.x, obj.y, (double) obj.w, (double) obj.h));
    }
    //특정한 두 지점 사이의 거리를 계산하는 메소드이다.
    private double dis(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }
    //군사가 공격을 받았는지 확인하고, 그에 맞는 처리를 한다.
    private void attackChk() {
        for (int i = 0; i < bulletList.size(); i++) {
            Bullet b = bulletList.get(i);
            if (b.type) {//플레이어가 쏜 총알이라면 적군이 맞았는지 확인
                for (Enemy en : enemyList) {    //이중 for문을 이용해서 미사일과 적 객체 1:1 대응
                    if (en.dead) continue;
                    if (isGunnerGotShooted(en, b)) {
                        bulletList.remove(i);//총알은 사라진다.
                        en.gotDamaged((int)b.damage);//플레이어가 총에 맞는다.
                        break;
                    }
                }
            } else {//적군이 쏜 총알이라면 플레이어가 맞았는지 확인
                if (isGunnerGotShooted(player, b)) {
                    bulletList.remove(i);
                    player.gotDamaged((int)b.damage);
                }
            }
        }
        //수류탄 폭파 범위 내에 있는지 확인
        for (Generade ge : generadeList) {
            if (!ge.startExplode) continue;
            double dis;
            for (Enemy e : enemyList) {
                dis = dis(e.x, e.y, ge.x, ge.y);
                if (dis < ge.w / 2) {
                    e.gotDamaged((int) (ge.damage / dis));
                }
            }
            dis = dis(player.x, player.y, ge.x, ge.y);
            if (dis <= ge.w / 2) {
                player.gotDamaged((int) (ge.damage / dis));
            }
        }
    }

    /*이동*/
    /* 이동 */
    private void moveObject() {
        playerMove();
        enMove();
        for(Bullet b:bulletList){b.move();}
        for(Generade g:generadeList){g.move();}
    }
    //군사가 이동하고자 하는 지점이 갈 수 있는 곳인지 확인한다.
    private boolean moveable(Gunner g, double dx, double dy) {//이동하고자 하는 군사와, 어떻게 움직이려 하는지 받는다.
        if (g.x - g.w / 2 + dx >= 0 && g.x + g.w / 2 + dx <= w && g.y - g.h / 2 + dy >= 10 && g.y + g.h / 2 + dy <= h) {  //스테이지 밖으로 나가지 않는다면
            for (Rectangle w : wallList) {
                if (g.x + g.w / 2 + dx >= w.x && g.x - g.w / 2 + dx <= w.x + w.width && g.y + g.h / 2 + dy >= w.y && g.y - g.h / 2 + dy <= w.y + w.height) { //단 하나의 벽과 부딪히더라도 이동할 수 없다.
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }
    //플레이어 이동
    private void playerMove() {
        if (left) {
            if (moveable(player, -player.v, 0)) {
                player.move(-player.v, 0);
            }
        }
        if (right) {
            if (moveable(player, player.v, 0)) {
                player.move(player.v, 0);
            }
        }
        if (up) {
            if (moveable(player, 0, -player.v)) {
                player.move(0, -player.v);
            }
        }
        if (down) {
            if (moveable(player, 0, player.v)) {
                player.move(0, player.v);
            }
        }
    }
    //적군 이동
    private void enMove() {
        for (Enemy e : enemyList) {
            //적군의 이동속도를 고려하여, 플레이어를 향해 가려면 x축과 y축으로 각각 얼마씩 이동해야 하는지 계산한다.
            double dx = player.x - e.x;
            double dy = player.y - e.y;
            double mx = e.v * dx / Math.sqrt(dx * dx + dy * dy);
            double my = e.v * dy / Math.sqrt(dx * dx + dy * dy);
            if (moveable(e, mx, 0)) e.move(mx, 0);
            if (moveable(e, 0, my)) e.move(0, my);
        }
    }
    //플레이어가 적의 시야에 보이는지 여부를 확인하는 메소드이다. 적군은 플레이어가 시야에 보일 때 발포한다.
    private void checkIfAbleToSee() {
        //선을 하나만 사용하면 아주 작은 틈으로도 발각된다.
        //이를 막기 위해, 군사의 중심뿐만 아니라 대각선으로 8개의 점을 더 찍고, 이들을 잇는다.
        //이들 직선이 모두 벽과 교차하지 않는지 확인한다.
        int dx[] = {0, 3, 3, -3, -3, 5, 5, -5, -5};
        int dy[] = {0, 3, -3, 3, -3, 5, -5, 5, -5};
        for (Enemy e : enemyList) {
            boolean flag = true; //기본적으로 발각을 가정한다.
            for (int j = 0; j < dx.length; j++) {
                int[] xpoints = {(int) e.x + dx[j], (int) player.x + dx[j]};
                int[] ypoints = {(int) e.y + dy[j], (int) player.y + dy[j]};
                Polygon p = new Polygon(xpoints, ypoints, 2);//Polygon 클래스를 이용하여 직선 객체를 만들었다.
                for (Rectangle w : wallList) {//모든 벽에 대해, 벽이 시야를 막는지 확인한다.
                    if (p.intersects(w)) {//Polygon 직선과 Rect가 교차하는지 확인한다. 간혹 에러가 발생한다. 원인은 파악하지 못했다.
                        e.ableToSee(false); //이 적(e)은 플레이어를 발견했다.
                        flag = false;//하나의 벽이라도 막고 있으면 발각된 것이 아니다.
                    }
                }
            }
            if (flag) e.ableToSee(true);
        }
    }

    /*아이템 습득*/
    //두 객체 사이의 거리가 특정 값 이하인지 확인하는 메소드이다.
    private boolean isInRange(ToDrawObject obj1, ToDrawObject obj2, double dis){
        return (dis(obj1.x,obj1.y,obj2.x,obj2.y)<dis);
    }
    private void getItem() {
        for (int i = 0; i < itemList.size(); i++) {
            Item it = itemList.get(i);
            if (isInRange(player, it, 20)) { //아이템이 일정 반경 안에 있으면,
                player.useItem(it);
                itemList.remove(i);
            }
        }
    }

    /*제거 대상 확인*/
    //죽은 군사, 혹은 총알 확인
    private void removeChk() {
        //적이 죽었는지 확인
        for (int i = 0; i < enemyList.size(); i++) {
            Enemy e = enemyList.get(i);
            if(e.dead){
                if(!e.gaveWeapon){
                    itemList.add(new Item(e));
                    e.gaveWeapon=true;
                }
                if(e.deadCount>=3000){
                    enemyList.remove(i);
                }
            }
        }
        if (player.dead) {
            end = true;
            start = false;
            clear=false;
        }
        //총알 또는 수류탄이 죽었는지 확인
        removeBulletOutOfStage(bulletList);
        removeBulletOutOfStage(generadeList);
        for(int i=0;i<bulletList.size();i++){//총알이 벽과 부딪히면 사라진다.
            Bullet b=bulletList.get(i);
            if(isBulletInWall(b)) bulletList.remove(i);
        }
        for(int i=0;i<generadeList.size();i++){//수류탄이 벽과 부딪히면 멈춘다.
            Generade g=generadeList.get(i);
            if(isBulletInWall(g)) g.arrive=true;
            if (g.endExplode) {//폭파가 끝났다면 화면에서 제거한다.
                generadeList.remove(i);
            }
        }
    }
    //화면 밖으로 나간 총알을 제거한다.
    private void removeBulletOutOfStage(ArrayList<?extends Bullet> arr){
        for(int i=0;i<arr.size();i++){
            Bullet b=arr.get(i);
            if (b.y < 0 || b.y > h || b.x < 0 || b.x > w) {
                arr.remove(i);
            }
        }
    }
    //총알이 벽과 부딪혔는지 확인한다.
    private boolean isBulletInWall(Bullet b){
        for(Rectangle w:wallList){
            if(w.contains(b.x,b.y)) return true;
        }
        return false;
    }

    /*화면 그리기*/
    //게임 시작 화면 그리기
    private void drawStart() {
        Graphics gs = bi.getGraphics();
        gs.setColor(Color.white);
        gs.fillRect(0, 0, w, h);
        gs.drawImage(startImage.getImage(), 0, 500, 600, 500, null);
        gs.drawImage(deco.getImage(), 405, 300, 200, 200, null);
        gs.setColor(Color.black);
        gs.setFont(new Font("굴림체", Font.BOLD, 25));
        gs.drawString("Java Project_2107 송하선", 150, 190);
        gs.setFont(new Font("바탕체", Font.BOLD, 20));
        gs.drawString("게임시작 : Enter", 220, 280); //게임시작 하는 Enter 키 안내
        gs.setFont(new Font("바탕체", Font.BOLD, 15));
        gs.drawString("이동: WASD", 260, 330);
        gs.drawString("발포: 좌클릭    수류탄: 우클릭", 190, 360);
        gs.drawString("장전: R 버튼    칼: 스페이스바", 190, 390);
        Graphics ge = this.getGraphics();               //실제공간 불러오기
        ge.drawImage(bi, 0, 0, w, h, this);
    }
    //게임 종료 화면 그리기
    private void drawEnd() {
        Graphics gs = bi.getGraphics();
        gs.setColor(Color.black);
        gs.fillRect(0, 0, w, h);
        gs.setColor(Color.white);
        gs.drawString("G A M E     O V E R", 250, 500);
        gs.drawString("Try Again?", 270, 540);
        level = 1;
        Graphics ge = this.getGraphics();               //실제공간 불러오기
        ge.drawImage(bi, 0, 0, w, h, this);
    }
    //게임 클리어 화면 그리기
    private void drawClear(){
        Graphics gs = bi.getGraphics();
        gs.setColor(Color.white);
        gs.fillRect(0, 0, w, h);
        gs.setColor(Color.black);
        gs.setFont(new Font("굴림체",Font.BOLD,20));
        gs.drawString("G A M E     C L E A R", 200, 500);
        gs.drawString("Congratulations!", 200, 540);
        level = 1;
        Graphics ge = this.getGraphics();               //실제공간 불러오기
        ge.drawImage(bi, 0, 0, w, h, this);
    }

    //개발 중 프로그램이 제대로 작동하는지 확인하기 위한 그림그리기
    private void drawDebug() {
        Graphics gs = bi.getGraphics();                          //버퍼공간 불러오기
        gs.setFont(new Font("굴림체",Font.BOLD,10));
        gs.setColor(Color.white);
        gs.drawString("적의 수 :" + enemyList.size(), 450, 50);
        gs.drawString("1번 적의 탄창 : " + (enemyList.size() > 0 ? enemyList.get(0).weapon.n : false), 450, 70);
        gs.drawString("1번 적의 체력 : " + (enemyList.size() > 0 ? enemyList.get(0).health : false), 450, 30);
        gs.drawString("1번 적의 재장전 여부 : " + (enemyList.size() > 0 ? enemyList.get(0).weapon.reloading : false), 450, 90);
        gs.drawString("1번 적의 발포 시간 : " + (enemyList.size() > 0 ? enemyList.get(0).weapon.fireTimer : false), 450, 110);
        gs.drawString("1번 적의 재장전 시간 : " + (enemyList.size() > 0 ? enemyList.get(0).weapon.reloadTimer : false), 450, 130);
        gs.drawString("1번 적의 경계 여부 : " + (enemyList.size() > 0 ? enemyList.get(0).alert : false), 450, 150);
        gs.drawString("1번 적의 시야 안? : " + (enemyList.size() > 0 ? enemyList.get(0).canSee : false), 450, 170);
        gs.drawString("나의 탄창 :" + player.weapon.n, 450, 190);
        gs.drawString("나의 체력 :" + player.health, 450, 210);
        gs.drawString("나의 발포시간:" + player.weapon.fireTimer, 450, 230);
        gs.drawString("1번 적의 속도 :" + (enemyList.size() > 0 ? enemyList.get(0).v : false), 450, 250);
        gs.drawString("나의 재장전 여부?:" + player.weapon.reloading, 450, 270);
        gs.drawString("1번 총알의 특성 : " + (bulletList.size() > 0 ? bulletList.get(0).type : -1), 450, 290);
        gs.drawString("1번 수류탄 도착? : " + (generadeList.size() > 0 ? generadeList.get(0).arrive : false), 450, 310);
        gs.drawString("스테이지 :" + level, 450, 330);
        gs.drawString("당신의 무기:" + player.weapon.name, 450, 350);
        gs.drawString("좌표:" + tx + " " + ty, 450, 370);
        gs.drawString("남은 수류탄:" + player.genN, 450, 390);
        for (int i = 0; i < enemyList.size(); i++) {
            Enemy e = enemyList.get(i);
            if (!e.dead) {
                gs.drawString("reloading?" + e.weapon.reloading + " location:" + e.x + " " + e.y + " " + e.v, 450, (410 + 20 * i));
                gs.drawString("" + e.health, (int) (e.x - e.w / 2), (int) (e.y - e.h / 2));
                gs.drawString("tot: " + e.weapon.totN + " cur:" + e.weapon.n, (int) (e.x - e.w / 2), (int) (e.y - e.h / 2) + 25);
            }

        }
        for (Generade g:generadeList) {
            gs.drawString("" + ((g.arrive) ? (int) (2 - g.gTimer / 1000) : 3), (int) (g.x), (int) (g.y));
        }
        gs.drawString(start + " " + end, 450, 800);
        Graphics ge = this.getGraphics();
        ge.drawImage(bi, 0, 0, w, h, this);
    }

    //게임 실행 중 그림 그리기
    //ToDrawObject를 상속받은 클래스들을 그린다.
    private void drawAll(ArrayList<?extends ToDrawObject> arr){
        for(ToDrawObject obj:arr) {
            Graphics gs = bi.getGraphics();
            gs.drawImage(obj.img.getImage(), (int)obj.x-(obj.w/2), (int)obj.y-(obj.h/2), obj.w, obj.h, null);
        }
    }
    private void drawGame() {
        Graphics gs = bi.getGraphics();
        gs.drawImage(grassField.getImage(), 0, 0, w, h, null); //풀숲 그리기
        gs.setColor(Color.GRAY);
        gs.fillRect(goal.x, goal.y, goal.width, goal.height);  //골인 지점 그리기
        //플레이어 스탯 표시
        gs.setColor(Color.cyan);
        gs.fillRect(500,870,100,130);
        gs.setColor(Color.black);
        gs.drawString("Health: "+player.health,510,890);
        gs.drawString("Bullet Now: "+player.weapon.n,510,910);
        gs.drawString("Bullet Left: "+player.weapon.totN,510,930);
        gs.drawString("Generade: "+player.genN,510,950);
        gs.drawString("Reloading?:"+(player.weapon.reloading?"Yes":"No"),510,970);
        gs.drawString("Weapon: "+player.weapon.name,510,990);
        for (Rectangle w : wallList) {  //벽 그리기
            gs.drawImage(wallIcon.getImage(), w.x, w.y, w.width, w.height, null);
        }
        //플레이어 그리기
        //리소스로 쓴 그림의 상하좌우에 약간의 여백이 있다. 부자연스러움을 극복하기 위해서 그림을 그릴 때는 실제 w,h에서 5씩 더 크게 그린다.
        gs.drawImage(player.img.getImage(), (int) (player.x - (player.w + 5) / 2), (int) (player.y - (player.h + 5) / 2), player.w + 5, player.h + 5, null);
        //플레이어 정보 그리기
        gs.setColor(Color.white);
        gs.drawString("" + player.health, (int) (player.x), (int) (player.y - 10));
        gs.drawString("" + player.weapon.n, (int) (player.x - 20), (int) (player.y - 10));
        gs.drawString("" + player.weapon.totN, (int) (player.x - 20), (int) (player.y + 20));
        gs.drawString((player.weapon.reloading ? "reloading" : ""), (int) (player.x), (int) (player.y + 20));
        drawAll(enemyList);//적 그리기
        drawAll(generadeList);//수류탄 그리기
        drawAll(itemList);//아이템 그리기
        //총알 그리기, 폭을 주기 위해 아래의 코드를 참고함
        //https://stackoverflow.com/questions/2839508/java2d-increase-the-line-width 코드 참고
        gs.setColor(Color.yellow);
        for (Bullet b : bulletList) {
            Graphics2D gs2 = (Graphics2D) gs;
            gs2.setStroke(new BasicStroke(2));
            gs2.draw(new Line2D.Float((int) (b.x - 20 * b.vx / b.v), (int) (b.y - 20 * b.vy / b.v), (int) b.x, (int) b.y));
        }
        //칼 그리기
        if (drawKnife) {
            gs.setColor(Color.WHITE);
            gs.drawLine((int) player.x + 5 + 5, (int) player.y - 10 - 10, (int) player.x + 20, (int) player.y - 40);
        }
        Graphics ge = this.getGraphics();//실제공간 불러오기
        ge.drawImage(bi, 0, 0, w, h, this);             //실제공간에 그리기
    }

    /*리스너 구현*/
    //KeyListener 구현
    public void keyPressed(KeyEvent ke) {
        switch (ke.getKeyCode()) {
            case KeyEvent.VK_A:
                left = true;
                break;
            case KeyEvent.VK_D:
                right = true;
                break;
            case KeyEvent.VK_W:
                up = true;
                break;
            case KeyEvent.VK_S:
                down = true;
                break;
            case KeyEvent.VK_R:
                player.weapon.reload();
                break;
            case KeyEvent.VK_SPACE:
                pressedKnife = true;
                break;
            case KeyEvent.VK_ENTER:
                if(!(start&&!end)){//게임 플레이 중에 누르는 엔터는 무시한다.
                    level=1;
                    stageSetter();//엔터를 누르면 stage1을 구성하고 시작된다.
                    start = true;
                    end = false;
                    break;
                }
        }
    }

    public void keyReleased(KeyEvent ke) {
        switch (ke.getKeyCode()) {
            case KeyEvent.VK_A:
                left = false;
                break;
            case KeyEvent.VK_D:
                right = false;
                break;
            case KeyEvent.VK_W:
                up = false;
                break;
            case KeyEvent.VK_S:
                down = false;
                break;
            case KeyEvent.VK_SPACE:
                pressedKnife = false;
                break;
        }
    }

    public void keyTyped(KeyEvent ke) {
    }

    //MouseListener 구현
    public void mousePressed(MouseEvent e) {
        if (e.isMetaDown()) {//우클릭을 확인하는 방법이다.
            gThrow = true;
        } else {
            fire = true;
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (e.isMetaDown()) {
            gThrow = false;
        } else {
            fire = false;
        }
    }

    public void mouseClicked(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }

    public void mouseDragged(MouseEvent e) {
        tx = e.getX();
        ty = e.getY();
    }

    public void mouseMoved(MouseEvent e) {
        tx = e.getX();
        ty = e.getY();
    }
}