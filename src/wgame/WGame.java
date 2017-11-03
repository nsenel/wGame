
package wgame;

/**
 *
 * @author numan
 */
import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import java.util.HashSet;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.util.concurrent.TimeUnit;
import static javafx.application.Application.launch;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class WGame extends Application
{
  public static void main(String[] args)
    {
        launch(args);
    }
    static Scene mainScene;
    static GraphicsContext graphicsContext;
    static int WIDTH = 450;//450
    static int HEIGHT = 600;//600
    static Image player;
    //static Image canavar;
    static Image canavarG;
    static Image canavarM;
    static Image bos;
    static Image shop;
    static Image cloud,     PlayerVsGoblin,       PlayerVsMamoot,       goblinWon1,        goblinWon2,        mamootWon1,        mamootWon2;
           //bulutResmi,    PlayerVsGoblin,     PlayerVsMamoot ,     Savasi Goblin Kazandi 1,2 resimler ,   Savasi Mamoot Kazandi 1,2 Resimler
    static Image playerWon1, playerWon2;
           //  Savasi Player Kazandi 1,2 resimler
    
    static AnimationTimer animate,animate2;//animate oyunda ki mevcut haritayi ve hareketleri saglar animate 2 savas animasyonlarini gerceklestirir.
    public static int GAME_SIZE = 8 ,frameCount=0; //frameCount 2 animasyonda resmin anime edilmesinde kulaniliyor
    public static String savasilan ,kazanan;
    public static Entity[][] map = new Entity[GAME_SIZE][GAME_SIZE];
    public static Player Player ;
    public static shop Shop;
    public static int kill = 0;
    public static ArrayList<Entity> canavarlar = new ArrayList<>();
    static HashSet<String> currentlyActiveKeys;
    static boolean isMoved=false,oyunBitti=false;
    public void start(Stage mainStage)
    {
        mainStage.setTitle("WİSSEN GAME");

        Group root = new Group();
        mainScene = new Scene(root);
        mainStage.setScene(mainScene);

        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        root.getChildren().add(canvas);

        prepareActionHandlers();

        graphicsContext = canvas.getGraphicsContext2D();/*
        Font theFont = Font.font( "Helvetica", FontWeight.BOLD, 14 );
        graphicsContext.setFont( theFont );
        graphicsContext.setStroke( Color.BLACK );
        graphicsContext.setLineWidth(1);*/
        
        loadGraphics();
        baslangic();
        /**
         * savas animasyonlari
         */

        animate2 = new AnimationTimer(){
            @Override
            public void handle(long now) {
                    anii();
                        }
                    };

         /**
         * Main "game" loop
         */
        
        animate = new AnimationTimer() {
            @Override
            public void handle(long now)
            {
                tickAndRender();
                if (oyunBitti)this.stop();
            }
        };animate.start();


        mainStage.show();
    }
    /**
     *anii savas animasyonlarinin resimleri burada belirlenir
     */
        private static void anii()//savas animasyonlarinin resimleri burada belirlenir
        {
            if (savasilan.equals("Goblin")) {graphicsContext.drawImage( PlayerVsGoblin, 20, 75 );}
            else {graphicsContext.drawImage( PlayerVsMamoot, 20, 75 );}
            graphicsContext.drawImage( cloud, 20-(frameCount*3), 75 );
            graphicsContext.drawImage( cloud, 20+(frameCount*3), 75 );
            frameCount++;
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if(frameCount>=150){
                frameCount=0;
                int r = (int) (Math.random() * 2); //toplamda 6 kazanan resmi vardir 3 farkli kazanan olabilir bu yuzden herbirinin iki resmi vardir
                                                   // iki resimden birini secmek icin random bir sayi belirlenir.
                
                if (savasilan.equals("Goblin") && kazanan.equals("Canavar"))
                {
                    if (r==0){graphicsContext.drawImage( goblinWon1, 20, 75 );}
                    else {graphicsContext.drawImage( goblinWon2, 20, 75 );}
                }
                else if (savasilan.equals("Mamoot") && kazanan.equals("Canavar"))
                {
                    if (r==0){graphicsContext.drawImage( mamootWon1, 20, 75 );}
                    else {graphicsContext.drawImage( mamootWon2, 20, 75 );}
                }
                else
                {
                    if (savasilan.equals("Goblin")){graphicsContext.drawImage(playerWon1, 20, 75);}
                    else{graphicsContext.drawImage(playerWon2, 20, 75);}
                }
                try {
                Thread.sleep(2000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                animate2.stop();
                frameCount=0;
            }
        }
        /**
        * klavye ve mouse hareketlerini kontrol eder
        */
        private static void prepareActionHandlers()//klavye ve mouse hareketlerini kontrol eder
    {
        // iki kere tusa basildiginda islemin bitmesi beklensin listede iki adet tus olmasini onlemek icin hashset kulanildi
        currentlyActiveKeys = new HashSet<String>();
        mainScene.setOnKeyPressed(new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent event)
            {
                if (!currentlyActiveKeys.contains(event.getCode().toString()))
                {
                    currentlyActiveKeys.add(event.getCode().toString());
                }     
            }
        });
        mainScene.setOnKeyReleased(new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent event)
            {
                isMoved=true;
                
                try {
                        TimeUnit.MILLISECONDS.sleep(250);
                    } catch (Exception e) {
                    }
                currentlyActiveKeys.remove(event.getCode().toString());
            }
        });
        mainScene.setOnMouseClicked(new EventHandler<MouseEvent>()//herhangi bir fare tusuna basildigi zaman oyunu tekrar baslatir
        {
            @Override
            public void handle(MouseEvent event)
            {
                if (oyunBitti) {oyunBitti=false; animate.start();}//oyun bittiyse yeni tiklamayla oyunu yeniden baslatir.
                baslangic();
                
            }
        });
        
    }
        /**
        * loadGraphics oyunda kulanilan resim dosyalarini yukler
        */
        private static void loadGraphics()//oyunda kulanilan resim dosyalarini yukler
    {
        //canavar = new Image ("file:canavar.png");
        canavarG = new Image ("file:Goblin.png");
        canavarM = new Image ("file:Mamoot.png");
        player = new Image("file:player.png");
        bos = new Image("file:bos.png");
        shop = new Image("file:shop.png");
        cloud =new Image("file:cloud.png");
        PlayerVsGoblin =new Image("file:aVSs.png");
        PlayerVsMamoot =new Image("file:aVSl.png");
        goblinWon1 =new Image("file:samsungWon1.png");
        goblinWon2 =new Image("file:samsungWon2.png");
        mamootWon1 =new Image("file:lgWon1.png");
        mamootWon2 =new Image("file:lgWon2.png");
        playerWon1 =new Image("file:appleWon1.png");
        playerWon2 =new Image("file:appleWon2.png");
    }
        /**
         * baslangic oyun baslangic haritasini ayarlar
         */
        private static void baslangic()//oyun baslangic haritasini ayarlar
        {
            Player = new Player();
            Shop = new shop();
            canavarlar.clear();
            kill=0;
            for (int i = 0; i < GAME_SIZE; i++) 
            {
                for (int j = 0; j < GAME_SIZE; j++) 
                {
                    map[i][j] = new Entity();
                }
            }
        map[0][0] = Player;
        map[GAME_SIZE/2][GAME_SIZE/2]=Shop;
        for (int i = 1; i < GAME_SIZE; i++) 
        {
            int r = (int) (Math.random() * 2);
            int rX = (int) (Math.random() * GAME_SIZE);
            // Eğer r 0 İse, rX'e Goblin, r 1 İse, rX'e Mamoot
            // Yerleştir,
            Entity monster = (r == 0) ? new Goblin(i,rX) : new Mamoot(i,rX);
            /*
                Entity monster = null;
                if (r == 0) monster = new Goblin();
            else            monster = new Mamoot();     
             */
            if (rX/2==GAME_SIZE/2 && i/2==GAME_SIZE/2){rX++;}//shopun uzerinde canavar cikmamasi icin
            map[rX][i] = monster;
            canavarlar.add(monster);
        }
        }
        /**
         * drawmap oyun basladiginda ve her turda haritayi gunceller
         */
        private static void drawmap()//oyun basladiginda ve her turda haritayi gunceller
        {
            graphicsContext.setFill( Color.BLACK );
            String pointsText = " HP: " + Player.hp + " Level: " + Player.level + " Gold : " + Player.goldCnt + " Damage: "+ Player.dmg;
            graphicsContext.clearRect(0, 0, 600, 600); //yazi alanini siler
            Font theFont = Font.font( "Helvetica", FontWeight.BOLD, 22 );
            graphicsContext.setFont( theFont );
            graphicsContext.fillText( pointsText, 17, 50 );
            graphicsContext.strokeText( pointsText, 17, 50 );
            graphicsContext.fillText( "Click The Screen to Restart the Game", 25, 550 );
            
        int x=20,y=75;
        //System.out.println("\n\n");
        for (int i = 0; i < GAME_SIZE; i++) 
        {
            x=20;
            for (int j = 0; j < GAME_SIZE; j++) 
            {
                //System.out.print(map[i][j]);
                String ava = map[i][j].avatar;
                
                if (ava.equals(" "))
                {
                    graphicsContext.drawImage( bos, x, y );
                }
                else if (ava.equals("\u263A"))
                {
                    graphicsContext.drawImage( player, x, y );
                }
                else if (ava.equals("\u25b2"))
                {
                    graphicsContext.drawImage( canavarG, x, y );
                }
                else if (i==GAME_SIZE/2 && j==GAME_SIZE/2 )
                {
                    graphicsContext.drawImage( shop, x, y );
                }
                else
                {
                    graphicsContext.drawImage( canavarM, x, y );
                }
                x+=50; 
                
            }
            y+=50;
            //System.out.println("");
        }
        //System.out.println("\n\n");
        }
        /**
         * savas tum savas senaryolari burada gerceklesir
         * @param oyuncu entity
         * @param monster entity
         */
        private static void savas(Entity oyuncu, Entity monster) //tum savas senaryolari burada gerceklesir
        {
            if (monster instanceof Goblin){savasilan="Goblin";}
            else{savasilan="Mamoot";}       //hangi canavarin animasyonu olduguna karar vermek icin canavarin ismi kaydedilir
        while (oyuncu.isAlive() && monster.isAlive()) //oyuncu ve canavar sirasiyla vuruslarini gerceklestirir
        {
            int pVurus = 0, cVurus = 0;
            pVurus = Player.dmg - (int) (Math.random() * (Player.dmg / 2));
            monster.hp -= pVurus;
            if (monster.hp < 0) {
                break;
            }
            cVurus = monster.hp - (int) (Math.random() * (monster.dmg / 2));
            Player.hp -= cVurus;
            if (Player.hp < 0) {
                break;
            }
        }
        if (Player.isAlive()) {kill++; canavarlar.remove(monster); kazanan="Player";} //player canavari oldurduyse oldurdiklerine bir ekle oldurdugu canavari canavar listesinden sil. kazanan kismi 2.animasyonda gosterilecek resim icin belirlenir
        else //player savas sirasinda olduyse oyunu bitir
        {
            kazanan="Canavar";//ikinci animasyonda gosterilecek resim için belirlenir.
            System.out.println("Oyunu Kaybettiniz"); 
            map[Player.Py][Player.Px]=new Entity(); 
            drawmap();
            Font theFont = Font.font( "Helvetica", FontWeight.BOLD, 30 );
            graphicsContext.setFont( theFont );
            graphicsContext.setFill( Color.RED );
            graphicsContext.fillText( "Kaybettiniz", 130, 520 );
            oyunBitti=true; 
        }
        
        if (kill == 3) //player 3 tane canavar oldurdugunde level atlar.
        {
            System.out.println("Level Atladin !!!!");
            Player.lvlup();
            //System.out.println("Yeni Can : " + Player.hp);
            Player.level++;
            kill = 0;
        }
        animate2.start();
    }
        /**
         *pMove player ve canavarlar yeni bir alana hareket eder 
         * @param varlik entity
         * @param tmpY int
         * @param tmpX int
         */
        private static void pMove(Entity varlik,int tmpY, int tmpX) // player ve canavarlar yeni bir alana hareket eder 
        {
            if (varlik instanceof Player)
            {
                Player Ply = (Player) varlik;
                map[Ply.Py][Ply.Px] = new Entity();
                map[tmpY][tmpX] = Ply;
                Ply.Px = tmpX;
                Ply.Py = tmpY;
            }
            else
            {
                int rasgele = (int) (Math.random() * 4); //ragele 0 gelirse x++ 1 gelirse x-- 2 gelirse y++ 3 gelirse y--
                //System.out.println("rasgele :"+rasgele);
                if (rasgele==0)tmpX=tmpX+1;
                if (rasgele==1)tmpX=tmpX-1;
                if (rasgele==2)tmpY=tmpY+1;
                if (rasgele==3)tmpY=tmpY-1;
                if (tmpX > -1 && tmpY > -1 && tmpX <= GAME_SIZE-1 && tmpY <= GAME_SIZE-1 && map[tmpY][tmpX].avatar.equals(" ")) // eger haraket harita sinirlari icinde is ve hareket yerinde baska bir entity yoksa canavarin hareketini gerceklestir.
                {    
                    if (varlik instanceof Goblin)
                    {
                        Goblin gbn = (Goblin) varlik;
                        map[gbn.Py][gbn.Px] = new Entity();
                        //System.out.println("Islem G X"+tmpX+"Y :"+tmpY); 
                        map[tmpY][tmpX] = gbn;
                        gbn.Px = tmpX;
                        gbn.Py = tmpY;
                    }
                    else
                    {
                        Mamoot mmot = (Mamoot) varlik;
                        map[mmot.Py][mmot.Px] = new Entity();
                        //System.out.println("Islem M X"+tmpX+"Y :"+tmpY); 
                        map[tmpY][tmpX] = mmot;
                        mmot.Px = tmpX;
                        mmot.Py = tmpY;
                    }
                }
            }
        }
        /**
         * oyuncunun hareketlerini herceklesir ve hereken aksiyonu alir
         */
        private static void move()//oyuncunun hareketlerini herceklesir ve hereken aksiyonu alir
        {
            int tmpX = Player.Px;
            int tmpY = Player.Py;

            if (currentlyActiveKeys.contains("RIGHT")) 
            {
                    tmpX++;
            }
            if (currentlyActiveKeys.contains("LEFT")) 
            {
                    tmpX--;
            }
            if (currentlyActiveKeys.contains("DOWN")) 
            {
                    tmpY++;
            }
            if (currentlyActiveKeys.contains("UP")) 
            {
                    tmpY--;
            }
            if (tmpX < 0 || tmpY < 0 || tmpX >= GAME_SIZE || tmpY >= GAME_SIZE) {
                tmpX = Player.Px;
                tmpY = Player.Py;
            }
            Entity nextMove = map[tmpY][tmpX];
            // Bu Alan Boş, Oyuncu Hareket Edebilir
            if (nextMove.avatar.equals(" ") || nextMove.avatar.equals("s")) //player bos bir alana hareket etmis ise, canavar ve player in yeni alani atanir
            {
                if (nextMove.avatar.equals("s"))
                {
                    Player.hp= Player.hp + Shop.convert(Player.goldCnt);
                    Player.goldCnt=0;
                }
                else
                {
                pMove(Player, tmpY, tmpX); //player bos bir alana hareket eder
                }
                for (Entity monster : canavarlar) //canavarlar yeni bir alana hareket eder
                {
                    if (monster instanceof Goblin)
                    {
                        tmpX=((Goblin) monster).Px;
                        tmpY= ((Goblin) monster).Py;
                    }
                    else
                    {
                        tmpX =((Mamoot) monster).Px;
                        tmpY =((Mamoot) monster).Py;
                    }
                    pMove(monster, tmpY, tmpX);
                }
            }
            
            else // player canavar olan alana hareket etti ise 
            {
                System.out.println("SAVAŞ BAŞLIYOR");
                Entity canavar = map[tmpY][tmpX];
                //System.out.println(
                        //"Bir " + canavar.getClass().getName() + " İle Karşılaştın");

                if (canavar instanceof Goblin) 
                {

                    savas(Player, (Goblin) canavar);
                    if (Player.isAlive()) {
                        Player.goldCnt += ((Goblin) canavar).goldDrop;
                        //System.out.println("Can : " + Player.hp + "LVL : " + Player.level + "Gold : " + Player.goldCnt);
                    }
                }

                if (canavar instanceof Mamoot) 
                {

                    savas(Player, (Mamoot) canavar);
                    if (Player.isAlive()) 
                    {
                        Player.goldCnt += ((Mamoot) canavar).goldDrop;
                        //System.out.println("Can : " + Player.hp + "LVL : " + Player.level + "Gold : " + Player.goldCnt);
                    }
                }

                    map[Player.Py][Player.Px] = new Entity();
                    pMove(Player, tmpY, tmpX);//canavar oldu ise player canavarin yerine gecer

                }
        }
        /**
         * oyun sonlanana kadar oyunun devam etmesini saglar
         */
        private static void tickAndRender()//oyun sonlanana kadar oyunun devam etmesini saglar
    {
        drawmap();
        if (!currentlyActiveKeys.isEmpty() && isMoved)
        {
            move();
            isMoved=false;
            if (canavarlar.size()==0)//oyunda canavar kalmadiysa kendini sonlanririr
            {
                System.out.println("Oyun bitti Kazandin");
                drawmap();
                Font theFont = Font.font( "Helvetica", FontWeight.BOLD, 30 );
                graphicsContext.setFont( theFont );
                graphicsContext.setFill( Color.GREENYELLOW );
                graphicsContext.fillText( "Kazandiniz!!", 130, 520 );
                oyunBitti=true; 
            }
        }    
    }    
}
