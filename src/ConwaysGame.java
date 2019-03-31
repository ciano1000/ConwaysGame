import com.sun.jndi.toolkit.url.Uri;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.io.*;
import java.util.Arrays;
import java.util.List;

public class ConwaysGame extends JFrame implements Runnable, MouseListener, MouseMotionListener {

    private boolean isInitialised =false;
    private boolean isGameRunning = false;
    private static final Dimension WindowSize = new Dimension(800,800);
    private BufferStrategy strategy;

    private boolean[][][] cells = new boolean[40][40][2];

    private int back = 0;
    private int front = 1;

    private int startX1 = 20;
    private int startY1 = 40;
    private int startH1 = 30;
    private int startW1 = 70;

    private int randX1 = 110;
    private int randY1 = 40;
    private int randW1 = 100;
    private int randH1 = 30;

    private int loadX1 = 230;
    private int loadY1 = 40;
    private int loadW1 = 70;
    private int loadH1 = 30;

    private int saveX1 = 320;
    private int saveY1 = 40;
    private int saveW1 = 70;
    private int saveH1 = 30;

    private int lastMouseX = 0;
    private int lastMouseY = 0;
    public ConwaysGame(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = screenSize.width/2-WindowSize.width/2;
        int y = screenSize.height/2-WindowSize.height/2;
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(x,y,WindowSize.width,WindowSize.height);
        setVisible(true);

        for(int i=0;i<cells.length;i++)
        {
            for(int j=0;j<cells[i].length;j++)
            {
                cells[i][j][front] = false;
            }
        }

        addMouseListener(this);
        addMouseMotionListener(this);


        createBufferStrategy(2);

        strategy = getBufferStrategy();
        isInitialised = true;

        Thread t = new Thread(this);
        t.start();
    }

    private void conwaysRules()
    {
        for(int x=0;x<40;x++){
            for(int y=0;y<40;y++){
                int startY = -1;
                int endY = 1;
                int startX = -1;
                int endX = 1;
                int count = 0;
                if(x ==0)
                {
                    startX = 0;
                    if(cells[y][39][front])
                        count++;
                }
                if(x==39){
                    endX =0;
                    if(cells[y][0][front])
                        count++;
                }
                if(y==0){
                    startY=0;
                    if(cells[39][x][front])
                        count++;
                }
                if(y==39){
                    endY=0;
                    if(cells[0][x][front])
                        count++;
                }

                for(int xx=startX;xx<=endX;xx++){
                    for(int yy=startY;yy<=endY;yy++){
                        if(xx!=0 || yy!=0){
                                if(cells[y+yy][x+xx][front])
                                    count++;
                        }
                    }
                }

                if(count<2)
                    cells[y][x][back] = false;
                if((count == 2 || count ==3) && cells[y][x][front])
                    cells[y][x][back] = true;
                if(count >3)
                    cells[y][x][back] = false;
                if(count == 3)
                    cells[y][x][back] = true;
            }
        }

        int tempBack = back;
        int tempFront = front;
        back = tempFront;
        front = tempBack;
    }

    private void randomise()
    {
        for(int i=0;i<cells.length;i++)
        {
            for(int j=0;j<cells[i].length;j++)
            {
                int rand = (int)Math.round(Math.random());
                if(rand == 0)
                    cells[i][j][front] = false;
                if(rand == 1)
                    cells[i][j][front] = true;
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

        Point point = e.getPoint();
        if(point.getX()>=0&&point.getY()>=0){
            int i = point.y / 20;
            int j = point.x/20;
            cells[i][j][front] = !cells[i][j][front];

            if(!isGameRunning)
            {
                if((point.y >startY1 && point.y<startY1+startH1)&&(point.x>startX1)&&point.x<startX1+startW1)
                {
                    isGameRunning = true;
                }
                else if((point.y >randY1 && point.y<randY1+randH1)&&(point.x>randX1)&&point.x<randX1+randW1)
                {
                    randomise();
                }
                else if((point.y >loadY1 && point.y<loadY1+loadH1)&&(point.x>loadX1)&&point.x<loadX1+loadW1){
                    loadState();
                }
                else if((point.y >saveY1 && point.y<saveY1+saveH1)&&(point.x>saveX1)&&point.x<saveX1+saveW1){
                    saveState();
                }
            }
        }

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
      mousePressed(e);

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void run() {
        int numFrames =0;
        while(true){
            numFrames++;
            try{
                if(isGameRunning){
                    Thread.sleep(200);
                }else {
                    Thread.sleep(20);
                }
            }catch(InterruptedException exceptpion){}
                if(isGameRunning)
                    conwaysRules();

            this.repaint();
        }
    }

    public void paint(Graphics g){
        if(!isInitialised)
            return;


        g=strategy.getDrawGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0,0,WindowSize.width,WindowSize.height);

        for(int i=0;i<cells.length;i++)
        {
            for(int j=0;j<cells[i].length;j++)
            {
                g.setColor(Color.WHITE);
                int x = j*20;
                int y = i*20;
                if(cells[i][j][front])
                {
                    g.fillRect(x,y,20,20);
                }
            }
        }

        if(!isGameRunning){
            g.setColor(Color.GREEN);
            g.fillRect(20,40,70,30);
            g.fillRect(110,40,100,30);
            g.fillRect(loadX1,loadY1,loadW1,loadH1);
            g.fillRect(saveX1,saveY1,saveW1,saveH1);

            g.setColor(Color.BLACK);
            Font courier = new Font("Courier",Font.BOLD,14);
            g.drawString("Start",40,60);
            g.drawString("Randomise",130,60);
            g.drawString("Load",250,60);
            g.drawString("Save",340,60);
        }

        strategy.show();
    }

    private void saveState(){
        String filename = "";
        String path = "";
        JFileChooser c = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES","txt","text");
        c.setFileFilter(filter);
        int rVal = c.showSaveDialog(this);
        if(rVal == JFileChooser.APPROVE_OPTION){
            path = c.getSelectedFile().getAbsolutePath();
            filename = c.getSelectedFile().getName();
            System.out.println(path);
        }

        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(path+".txt"));
            for(int i=0;i<cells.length;i++)
            {
                for(int j=0;j<cells[i].length;j++)
                {
                    writer.write(i+","+j+","+String.valueOf(cells[i][j][front]));
                    writer.newLine();
                }
            }
            writer.close();
        }catch (IOException e){

        }
    }

    private void loadState(){
        String line = null;
        String filename = "";
        String path = "";
        JFileChooser c = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES","txt","text");
        c.setFileFilter(filter);
        int rVal = c.showOpenDialog(this);
        if(rVal == JFileChooser.APPROVE_OPTION){
            path = c.getSelectedFile().getAbsolutePath();
            filename = c.getSelectedFile().getName();
            System.out.println(path);
        }
        try{
            BufferedReader reader = new BufferedReader(new FileReader(path));
            do{
                try{
                    line = reader.readLine();
                    if(line != null){
                        List<String> list =Arrays.asList(line.split(","));
                        int i = Integer.parseInt(list.get(0));
                        int j = Integer.parseInt(list.get(1));
                        boolean value;
                        if(list.get(2).equals("false")){
                            value = false;
                        }else{
                            value = true;
                        }
                        cells[i][j][front] = value;
                    }
                }catch (IOException e){

                }
            }while (line !=null);
        }catch (IOException e){

        }
    }

    public static void main(String[] args){
        ConwaysGame game = new ConwaysGame();
    }


}
